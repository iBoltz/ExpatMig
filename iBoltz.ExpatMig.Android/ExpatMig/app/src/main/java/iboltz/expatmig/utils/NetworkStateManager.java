package iboltz.expatmig.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import iboltz.expatmig.ListenerInterfaces.NetworkListener;
import iboltz.expatmig.ListenerInterfaces.WebClientEventObject;
import iboltz.expatmig.ListenerInterfaces.WebClientListeners;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.WebServiceUrls;

public class NetworkStateManager {
	private static NetworkListener Watcher;
	public static void setEventListener(NetworkListener listener) {
		Watcher = listener;
	}

	public static void RaiseOnServerResponse(String ResponseData,InputStream ResponseStream) {

		WebClientEventObject eObj = new WebClientEventObject(new Object());
           eObj.ResponseData=ResponseData;
		eObj.ResponseStream=ResponseStream;
		if (Watcher != null)
			Watcher.OnServerStatusReceived(eObj); // event object :)
	}
	public static  void RaiseOnServerResponse(String ResponseData) {
		RaiseOnServerResponse(ResponseData, null);
	}

	public static boolean isNetworkAvailable(Activity activity) {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) activity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//LogHelper.HandleException(e);
			return false;
		}
		return status;
	}
	public  static boolean isServerReachable(String ServiceUrl) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {



				ConnectivityManager cm = (ConnectivityManager) AppCache.CurrentActivity
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo netInfo = cm.getActiveNetworkInfo();
				if (netInfo != null && netInfo.isConnected()) {
					try {
						String WebService = "http://" + params[0];
						URL url = new URL(WebService);   // Change to "http://google.com" for www  test.
						HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
						urlc.setConnectTimeout(10 * 1000);          // 10 s.
						urlc.connect();

						int NetWorkStatusCode=urlc.getResponseCode();
						return  String.valueOf(NetWorkStatusCode);
						//RaiseOnServerResponse();

					} catch (MalformedURLException ex) {
						LogHelper.HandleException(ex);
					} catch (IOException ex) {
						ex.printStackTrace();
						LogHelper.HandleException(ex);
						return String.valueOf("");
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(String msg) {
				try {
					RaiseOnServerResponse(msg);
				} catch (Exception ex) {
					LogHelper.HandleException(ex);
				}
			}
		}.execute(ServiceUrl, null, null);
		return false;
	}
}




