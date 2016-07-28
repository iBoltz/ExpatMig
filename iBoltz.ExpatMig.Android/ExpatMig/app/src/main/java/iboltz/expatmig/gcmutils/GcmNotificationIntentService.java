package iboltz.expatmig.gcmutils;

import iboltz.expatmig.R;
import iboltz.expatmig.models.NotificationModel;
import iboltz.expatmig.utils.LogHelper;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.UiUtils;

public class GcmNotificationIntentService extends IntentService {


    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmNotificationIntentService() {
        super("GcmIntentService");

    }

    public static final String TAG = "GcmNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            Bundle extras = intent.getExtras();
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

            String messageType = gcm.getMessageType(intent);

            if (!extras.isEmpty()) {
                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                        .equals(messageType)) {
                    sendNotification("Send error: " + extras.toString());
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                        .equals(messageType)) {
                    sendNotification("Deleted messages on server: "
                            + extras.toString());
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                        .equals(messageType)) {
                    Object Message = extras.get(AppConstants.MessageKey);
                    if (Message != null) {
                        String MyMessage = Message.toString();
                        sendNotification(MyMessage);
                    }
                }
            }

        } catch (Exception ex) {

            LogHelper.HandleException(ex);

        }
    }

    private void PlayRingTone() {
        try {


            MediaPlayer mMediaPlayer = new MediaPlayer();
            mMediaPlayer = MediaPlayer.create(this, R.raw.old_phone_ringtone);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.start();



        } catch (Exception ex) {
                      LogHelper.HandleException(ex);
        }

    }


    public void sendNotification(String msg) {
        try {


            Log.d("MyApp", "Notification - " + msg);

//            java.lang.reflect.Type DataType = (java.lang.reflect.Type) (new TypeToken<NotificationModel>() {
//            }).getType();




//            NotificationModel Recvd = new Gson().fromJson(msg,
//                    (java.lang.reflect.Type) DataType);
//
//            Log.d("MyApp", "NotificationType" + Recvd.NotificationType);

//            switch (Recvd.NotificationType) {
//                case "Promotions":
//                    SendPromotionNotification(Recvd.NotificationData);
//                    break;
//                case "Orders":
//                    SendOrderNotification(Recvd.NotificationData);
//                    break;
//                case "Ack":
//                    SendDinnerNotification(Recvd.NotificationData);
//                    break;
//                case "Kitchen":
//                    SendKitchenNotification(Recvd.NotificationData);
//                    break;
//                case "RTS":
//                    SendRTSNotification(Recvd.NotificationData);
//                    break;
//                case "Bill":
//                    SendBillNotification(Recvd.NotificationData);
//                    break;
//                case "HandShake":
//                    HandShakeGCM(Recvd.NotificationData);
//                    break;
//            }

            PlayRingTone();
        } catch (Exception ex) {

            LogHelper.HandleException(ex);

        }
    }

}
