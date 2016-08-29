package iboltz.expatmig.gcmutils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.UsersFacadeListeners;
import iboltz.expatmig.facades.UsersFacade;
import iboltz.expatmig.utils.LogHelper;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.StorageManager;


public class GcmManager
{
	public static final String GcmRegID = "REG_ID";
	GoogleCloudMessaging gcm;
	Context context;
	Activity CurrentActivity;
	String regId;
	// Loghelper log=new Loghelper();
	public static final String REG_ID = "regId";

	static final String TAG = "Register Activity";

	public GcmManager(Activity CurrentActivity)
	{

		this.context = CurrentActivity;
		this.CurrentActivity = CurrentActivity;
		setGcmRegisterHandler(CurrentActivity);
	}

	public interface IGcmRegisterCallback
	{
		void HasRegistered(Boolean IsDone);
	}

	private IGcmRegisterCallback callerActivity;

	public void setGcmRegisterHandler(Activity activity)
	{
		callerActivity = (IGcmRegisterCallback) activity;
	}

	public String registerGCM()
	{
		try
		{
			gcm = GoogleCloudMessaging.getInstance(context);
			regId = getRegistrationId(context);

			Log.d("MyApp", "regId :" + regId);

			if (TextUtils.isEmpty(regId))
			{
				registerInBackground();
			} else
			{

				String gcmdeviceid = StorageManager.Get(context, "gcmdeviceid");
				Log.d("MyApp", "gcmdeviceid :" + gcmdeviceid);

				if (gcmdeviceid.isEmpty() == true)
				{
					registerInBackground();
				} else
				{
					/*
					 * Toast.makeText(context,
					 * "Already Registered for push notifications ",
					 * Toast.LENGTH_LONG).show();
					 */
					callerActivity.HasRegistered(true);
					AppCache.RegistrationID = regId;
				}
			}
			return regId;
		} catch (Exception ex)
		{
			LogHelper.HandleException(ex);
			return "";
		}

	}

	private void UpdateRegistrationToUcf(String RegID,Boolean IsVerifiedByDeviceId)
	{
		try
		{
			UsersFacade UserDeviceManager=new UsersFacade(context);
			UserDeviceManager.setUserProcessFinishedListener(new UsersFacadeListeners() {
				@Override
				public void OnProcesFinished(EventObject e) {

				}
			});
			UserDeviceManager.UpdateRegistrationID(RegID, IsVerifiedByDeviceId);

		} catch (Exception ex)
		{
			LogHelper.HandleException(ex);

		}
	}

	private String getRegistrationId(Context context)
	{
		try
		{

			String registrationId = StorageManager.Get(context, GcmRegID);
			if (registrationId.isEmpty())
			{
				Log.i(TAG, "Registration not found.");
				return "";
			}

			String registeredVersion = StorageManager.Get(context,
					"APP_VERSION");

			String currentVersion = AppCache.AppCurrentVersion;
			if (registeredVersion.equals(currentVersion) != true)
			{
				Log.i(TAG, "App version changed");
				return "";
			}
			AppCache.RegistrationID = registrationId;
			return registrationId;
		} catch (Exception ex)
		{
			LogHelper.HandleException(ex);
			return "";
		}
	}

	private void registerInBackground()
	{
		try
		{
			new AsyncTask<Void, Void, String>()
			{
				@Override
				protected String doInBackground(Void... params)
				{
					String msg = "";
					try
					{
						if (gcm == null && context != null)
						{
							gcm = GoogleCloudMessaging.getInstance(context);
						}
						if (gcm != null)
						{
							try
							{
								regId = gcm.register(AppConstants.GcmProjectID);
								msg = regId;
								storeRegistrationId(context, regId);
							} catch (IOException ex)
							{
								// skip bcoz of gcm unavailble
								// or
								// due to network problems


								/**************************************/


								// Put a gcm register in background again to retry the notificatioms


								/**************************************/




							}

						}

					} catch (Exception ex)
					{
						LogHelper.HandleException(ex);

						msg = "Error :" + ex.getMessage();
					}
					return msg;
				}

				@Override
				protected void onPostExecute(String msg)
				{
					try
					{
						UpdateRegistrationToUcf(msg,
								AppCache.IsVerifiedByDeviceId);
						// Toast.makeText(context, "Registered with GCM Server."
						// + msg,
						// Toast.LENGTH_LONG).show();
						callerActivity.HasRegistered(true);
					} catch (Exception ex)
					{
						LogHelper.HandleException(ex);
					}
				}
			}.execute(null, null, null);
		} catch (Exception ex)
		{
			LogHelper.HandleException(ex);
		}
	}

	private void storeRegistrationId(Context context, String regId)
	{
		try
		{
			AppCache.RegistrationID = regId;

			StorageManager.Put(context, "REG_ID", regId);
			StorageManager.Put(context, "APP_VERSION", AppCache.AppCurrentVersion);

			StorageManager.Put(context, "LastRegisteredDate", AppConstants.JsonDateFormat.format(new Date()));
		}
		catch(Exception ex)
		{
			LogHelper.HandleException(ex);
		}

	}
}