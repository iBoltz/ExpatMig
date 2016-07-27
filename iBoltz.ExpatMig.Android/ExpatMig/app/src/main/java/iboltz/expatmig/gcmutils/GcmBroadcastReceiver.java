package iboltz.expatmig.gcmutils;

import iboltz.expatmig.utils.LogHelper;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

//import pon.usedcarsportal.android.usedcarfleet.utils.Loghelper;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{

		try
		{

			Toast.makeText(context.getApplicationContext(), "GCM",
					Toast.LENGTH_LONG).show();
			ComponentName comp = new ComponentName(context.getPackageName(),
					GcmNotificationIntentService.class.getName());
			startWakefulService(context, (intent.setComponent(comp)));
			setResultCode(Activity.RESULT_OK);

			Log.d("GCM", "GCM Hit");
		} catch (Exception ex)
		{
			Log.e("GCM", "error... ", ex);

			LogHelper.HandleException(ex);

		}
	}
}
