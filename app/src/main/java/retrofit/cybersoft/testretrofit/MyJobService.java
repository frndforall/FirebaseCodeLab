package retrofit.cybersoft.testretrofit;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobScheduler;
import me.tatarka.support.job.JobService;

/**
 * Created by nagendra on 25/10/16.
 */

public class MyJobService extends JobService {
    // This method is called when the service instance
    // is created
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyService", "myService created");
    }

    // This method is called when the service instance
    // is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MyService", "myService destroyed");
    }

    // This method is called when the scheduled job
    // is started
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("MyService", "on start job");
        if(myMainActivity!=null)
        myMainActivity.getFeeds();
        return true;
    }

    // This method is called when the scheduled job
    // is stopped
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("MyService", "on stop job");
        return true;
    }

    MainActivity myMainActivity;

    public void setUICallback(MainActivity activity) {
        myMainActivity = activity;
    }


    // This method is called when the start command
    // is fired
    @Override
    public int onStartCommand(Intent intent, int flags,
                              int startId) {
        Messenger callback = intent.getParcelableExtra("messenger");
        Message m = Message.obtain();
        m.what = 2;
        m.obj = this;
        try {
            callback.send(m);
        } catch (RemoteException e) {
            Log.e("MyService", "Error passing service object ßback to activity.");
        }
        return START_NOT_STICKY;
    }

    // Method that schedules the job
    public void scheduleJob(JobInfo build) {
        Log.i("MyService","Scheduling job");
        JobScheduler jobScheduler = JobScheduler.getInstance(this);
        jobScheduler.schedule(build);
    }
}


