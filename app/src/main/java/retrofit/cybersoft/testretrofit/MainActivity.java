package retrofit.cybersoft.testretrofit;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kobakei.ratethisapp.RateThisApp;

import org.jsoup.Jsoup;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.tatarka.support.job.JobInfo;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<StackQuestions> {

    @BindView(R.id.question_list)
    ListView questionList;
    ProgressDialog pd;

    ComponentName myServiceComponent;
    MyJobService myService;
    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            myService = (MyJobService) msg.obj;
            myService.setUICallback(MainActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //questionList = (ListView)findViewById(R.id.question_list);
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Question q = (Question)parent.getAdapter().getItem(position);
                Intent i = new Intent(MainActivity.this, WebActivity.class);
                i.putExtra("URL",q.link);
                startActivity(i);
            }
        });
        myServiceComponent = new ComponentName(this, MyJobService.class);
        Intent myServiceIntent = new Intent(this, MyJobService.class);
        myServiceIntent.putExtra("messenger", new Messenger(myHandler));
        startService(myServiceIntent);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void configRateApp(){
        RateThisApp.Config config = new RateThisApp.Config(3, 5);
        config.setTitle(R.string.rate_title);
        config.setMessage(R.string.rate_message);

        RateThisApp.init(config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           callRetrofitAPI();
            return true;
        } else if( id == R.id.action_second) {
            startActivity(new Intent(this,SecondActivity.class));
        } else if( id == R.id.action_user) {
            startActivity(new Intent(this,UserListActivity.class));
        } else if(id == R.id.action_signout) {
            signOutUser();
        } else if(id == R.id.action_rate) {
            configRateApp();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

    private void callRetrofitAPI() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo info =manager.getActiveNetworkInfo();
        if(info!=null && info.isConnected()) {
            getFeeds();
           // new GetVersionCode().execute();
        } else {
            Toast.makeText(this,"No active connection. Please try later",Toast.LENGTH_SHORT).show();
            //setJobScheduler();
        }
    }

    public void getFeeds(){
        pd = ProgressDialog.show(this,"Loading...",null,true,false);
//        Progress progress = new Progress(context);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // prepare call in Retrofit 2.0
        StackAPI stackOverflowAPI = retrofit.create(StackAPI.class);

        Call<StackQuestions> call = stackOverflowAPI.loadQuestions("android");
        //asynchronous call
        call.enqueue(this);
        Log.d("Test UUID", getUniquePhoneIdentity());
    }


    public String getUniquePhoneIdentity() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }
        return "android-" + new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    @Override
    public void onResponse(Response<StackQuestions> response) {
        if(pd!=null) {
            pd.cancel();
        }
        questionList.setAdapter(new QuestionsAdapter(this,response.body().items));
        //RateThisApp.showRateDialog(MainActivity.this);
    }

    @Override
    public void onFailure(Throwable t) {
        if(pd!=null) {
            pd.cancel();
        }
        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }


    public void setJobScheduler(){
        JobInfo.Builder builder = new JobInfo.Builder(0, myServiceComponent);
        //builder.setRequiresCharging(true);
        //builder.setRequiresDeviceIdle(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        myService.scheduleJob(builder.build());
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=net.cybersoft.yottatenant&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            String currentVersion = "1.4.0" ;
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (currentVersion.equalsIgnoreCase(onlineVersion)) {
                    Log.d("Update Not Needed", "Current version " + currentVersion + "playstore version " + onlineVersion);
                } else {
                    Log.d("Update Needed", "Current version " + currentVersion + "playstore version " + onlineVersion);
                }
            }
            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
        }
    };
}
