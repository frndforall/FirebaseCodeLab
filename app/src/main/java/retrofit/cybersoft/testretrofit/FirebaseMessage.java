package retrofit.cybersoft.testretrofit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by nagendra on 05/12/16.
 */

public class FirebaseMessage extends FirebaseMessagingService {

    private static final String TAG = "Resident-Message-Receiver";
    private static final String PUSH_PAYLOAD = "json_payload";
    private static final String PUSH_TITLE="title";
    private static final String PUSH_MESSAGE="message";
    private static final String PUSH_NOTIFICATION_TYPE = "notification_type";
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //RemoteMessage.Notification n= remoteMessage.getNotification();
        Map<String,String> remoteData=remoteMessage.getData();
        Log.d("Recieved message",remoteData.toString());
        sendNotification(remoteData.get("title"),remoteData.get("body"),"");
    }

    private void sendNotification(String title,String message,String customData) {
        PendingIntent resultPendingIntent = null;
//        int notificationId = YottaConstants.notificationId;
        mNotifyManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        if(title!=null && title.length()<15){
            mBuilder.setContentTitle(title);
        } else {
            mBuilder.setContentTitle(getString(R.string.app_name));
        }
        mBuilder.setSmallIcon(android.R.drawable.btn_star_big_off);
        mBuilder.setContentText(message);
        Intent resultIntent = new Intent(this, SMSActivity.class);

//        if(customData!=null){           // This is json payload. Need to act on this to proceed further
//            YLog.d(TAG," data "+customData);
//            resultIntent.putExtra(YottaConstants.PUSH_TYPE, Integer.parseInt(customData));
//            notificationId = getNotificationId(Integer.parseInt(customData));
//        }


        resultPendingIntent = PendingIntent.getActivity(this,0 , resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyManager.notify(0, mBuilder.build());
    }

}
