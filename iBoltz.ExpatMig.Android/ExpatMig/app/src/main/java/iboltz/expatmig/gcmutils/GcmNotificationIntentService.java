package iboltz.expatmig.gcmutils;

import iboltz.expatmig.R;
import iboltz.expatmig.models.NotificationModel;
import iboltz.expatmig.models.TopicsModel;
import iboltz.expatmig.screens.ChatActivity;
import iboltz.expatmig.utils.LogHelper;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;

import iboltz.expatmig.utils.AppConstants;

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
                    GetNotification("Send error: " + extras.toString());
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                        .equals(messageType)) {
                    GetNotification("Deleted messages on server: "
                            + extras.toString());
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                        .equals(messageType)) {
                    Object Message = extras.get(AppConstants.MessageKey);
                    if (Message != null) {
                        String MyMessage = Message.toString();
                        GetNotification(MyMessage);
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


    public void GetNotification(String msg) {
        try {


            Log.d("MyApp", "Notification - " + msg);

           java.lang.reflect.Type DataType = (java.lang.reflect.Type) (new TypeToken<NotificationModel>() {

           }).getType();


            NotificationModel Recvd = new Gson().fromJson(msg,
                   (java.lang.reflect.Type) DataType);
//
            Log.d("MyApp", "NotificationType" + Recvd.NotificationType);

            switch (Recvd.NotificationType) {
                case "topics":

                    ChatNotification(Recvd.NotificationData);
                    break;

            }

            PlayRingTone();
        } catch (Exception ex) {

            LogHelper.HandleException(ex);

        }
    }
    private String DecodeText(String base64)
    {
        base64=base64.replace(" ","+");
        byte[] data1 = Base64.decode(base64, Base64.DEFAULT);
        String text1 = null;
        try {
            text1 = new String(data1, "UTF-8");
            return text1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  null;
    }
    private TopicsModel ConvertToTopic(String Message){

     //   Message = DecodeText(Message);
        java.lang.reflect.Type collectionType = (java.lang.reflect.Type) (new TypeToken<TopicsModel>() {
        }).getType();

        TopicsModel LatestTopic = new Gson()
                .fromJson(Message,
                        (java.lang.reflect.Type) collectionType);

        LatestTopic.Description=DecodeText(LatestTopic.Description);
        return LatestTopic;
    }
    private void ChatNotification(String Message) {
        try {

            TopicsModel RecentTopic=ConvertToTopic(Message);


            mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent NotificationIndent = new Intent(this, ChatActivity.class);
            NotificationIndent.putExtra("Chat", Message);


            if (ChatActivity.IsRunningNow) {
                iNotifyTopic Poster = (iNotifyTopic) ChatActivity.CurrentInstance;
                if (Poster != null)
                    ChatActivity.NotificationMessage = Message;
                Poster.AddRecentTopic(RecentTopic);
            } else {
                /// Skip when other screens open
            }

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                    NotificationIndent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this)
                    .setSmallIcon(R.drawable.iboltzlogo)
                    .setContentTitle(""+ RecentTopic.ThreadName)
                    .setStyle(
                            new NotificationCompat.BigTextStyle().bigText(RecentTopic.UserName + " : " + RecentTopic.Description))
                    .setContentText(RecentTopic.UserName + " : " + RecentTopic.Description);

            mBuilder.setContentIntent(contentIntent);
            Notification ThatNotification = mBuilder.build();
//            ThatNotification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
            ThatNotification.flags |= Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(NOTIFICATION_ID, ThatNotification);

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

    }


}
