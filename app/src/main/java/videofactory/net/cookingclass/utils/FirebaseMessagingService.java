package videofactory.net.cookingclass.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import net.videofactory.planb.R;
import net.videofactory.planb.ui.screens.MainScreen;

/**
 * Created by ethanc on 4/8/17.
 */
public class FirebaseMessagingService  extends com.google.firebase.messaging.FirebaseMessagingService  {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        sendPushNotification(remoteMessage);
    }

    private void sendPushNotification(RemoteMessage remoteMessage) {
        LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + remoteMessage.getNotification().getBody());
        Intent intent = new Intent(this, MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_arrow_black)
                .setLargeIcon(BitmapFactory.decodeResource(getResources() ,R.mipmap.ic_launcher) )
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setTicker(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
                .setLights(000000255,500,2000)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);

//        new android.app.AlertDialog.Builder(new IntroScreen())
//                .setTitle(remoteMessage.getNotification().getTitle())
//                .setMessage(remoteMessage.getNotification().getBody())
//                .setPositiveButton("Cool", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).create().show();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
