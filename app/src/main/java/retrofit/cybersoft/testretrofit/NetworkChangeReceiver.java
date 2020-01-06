package retrofit.cybersoft.testretrofit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "NETWORK TAG";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.v(LOG_TAG, "Receieved notification about network status");
        if(isNetworkAvailable(context))
            EventBus.getDefault().post(new NetworkEvent(true));
        else
            EventBus.getDefault().post(new NetworkEvent(false));

    }


    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                            Log.v(LOG_TAG, "Now you are connected to Internet!");


                        return true;
                    }
                }
            }
        }
        return false;
    }
}