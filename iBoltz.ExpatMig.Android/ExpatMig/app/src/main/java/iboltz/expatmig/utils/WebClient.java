package iboltz.expatmig.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EventObject;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import iboltz.expatmig.ListenerInterfaces.NetworkListener;
import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.ListenerInterfaces.WebClientEventObject;
import iboltz.expatmig.ListenerInterfaces.WebClientListeners;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.WebServiceUrls;

public class WebClient {
    private static String Tag = "WebClient";
    private Context CurrentContext;
    private WebClientListeners WebListener;


    public WebClient() {

    }

    public WebClient(Context CurrentContext) {
        this.CurrentContext = CurrentContext;
    }

    public void PostData(final String UrlToPost, final String PostDataString) {
        try {
// if current context null
            if (CurrentContext == null) {
                PostMethod(UrlToPost, PostDataString);
            }

            Boolean IsNetworkAvailable = NetworkStateManager.isNetworkAvailable((Activity) CurrentContext);

            if (IsNetworkAvailable) {
                AppCache.CurrentActivity = CurrentContext;
                PostMethod(UrlToPost, PostDataString);

            } else {
                if (CurrentContext != null) {
                    UiUtils.ShowToast((Activity) CurrentContext, "Unable to connect to the internet");
                }
            }

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public void AlertMessage() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CurrentContext);

        // set title
        alertDialogBuilder.setTitle("Food-iBoltz");

        // set dialog message
        alertDialogBuilder
                .setMessage("Server Isn't Responding")
                .setCancelable(false)

                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing

                                dialog.cancel();
                            }
                        }

                );

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void PostMethod(final String UrlToPost, String PostDataString) {
        final Long StartTime = System.currentTimeMillis();

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    //  Log.d(Tag, "Started APi Post ");
                    String UrlToPost = params[0];
                    String PostDataString = params[1];

                    HttpPost Post = new HttpPost(UrlToPost);
                    Post.addHeader("Accept", "application/json");
                    Post.addHeader("Content-Type", "application/json");
                    if (AppCache.AccessToken != null) {

                        String authorizationString = "iBoltzKey " + AppCache.AccessToken.toString(); //Base64.NO_WRAP flag
                        Post.addHeader("Authorization", authorizationString);

                    }
                    StringEntity stringEntity = new StringEntity(PostDataString);
                    Post.setEntity(stringEntity);
                    HttpClient httpClient1 = new DefaultHttpClient();
                    HttpResponse response = httpClient1.execute(Post);

                    msg = EntityUtils.toString(response.getEntity());

                } catch (Exception ex) {
                    LogHelper.HandleException(ex);
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String ResponseText) {
                try {
                    Long StopTime = System.currentTimeMillis();
                    if ((CurrentContext != null) && (AppCache.IsEnableToast == true)) {
                        UiUtils.ShowToast((Activity) CurrentContext, " Time elapsed is " + (StopTime - StartTime) + " ms in " + UrlToPost);
                    }
                    if (ResponseText != null && ResponseText != "") {
                        OnResponseReceived(ResponseText);
                    }
                } catch (Exception ex) {
                    LogHelper.HandleException(ex);
                }
            }
        }.execute(UrlToPost, PostDataString, null);


    }

    public void GetData(final String ResourceUrl) {
        try {
            if (NetworkStateManager.isNetworkAvailable((Activity) CurrentContext)) {
                AppCache.CurrentActivity = CurrentContext;

                NetworkStateManager.isServerReachable(WebServiceUrls.WebServiceUrl);
                NetworkStateManager.setEventListener(new NetworkListener() {

                    @Override
                    public void OnServerStatusReceived(WebClientEventObject e) {
                        if (e.ResponseData.equals("200")) {
                            GetMethod(ResourceUrl);
                        } else {
                            AlertMessage();
                        }
                    }
                });

            } else {
                UiUtils.ShowToast((Activity) CurrentContext, "Unable to connect to the internet");
            }


        } catch (Exception ex) {

            LogHelper.HandleException(ex);
        }
    }

    public void GetMethod(final String ResourceUrl) {
        final Long StartTime = System.currentTimeMillis();

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    HttpClient httpclient = new DefaultHttpClient();

                    String url = params[0];
                    HttpGet Get = new HttpGet(url);

                    Get.addHeader("Accept", "application/json");
                    if (AppCache.AccessToken != null) {

                        String authorizationString = "iBoltzKey " + AppCache.AccessToken.toString(); //Base64.NO_WRAP flag
                        Get.addHeader("Authorization", authorizationString);

                    }

                    HttpResponse response = httpclient.execute(Get);

                    int StatusCode = response.getStatusLine().getStatusCode();
                    msg = EntityUtils.toString(response.getEntity());

                    if (msg == null || msg == "") msg = "Did not work!";
                    if (StatusCode != 200) {
                        LogHelper.SendExceptionMail("ExecuteUrl from webClient : "
                                + url + " and StatusCode :" + StatusCode);
                        msg = null;
                        if (StatusCode == 500) {
                            msg = String.valueOf(StatusCode);
                        }
                    }

                    Log.d("StatusCode", "" + StatusCode);

                } catch (Exception ex) {


                    LogHelper.HandleException(ex);
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String ResponseText) {
                try {
                    Long StopTime = System.currentTimeMillis();
                    if ((CurrentContext != null) && (AppCache.IsEnableToast == true)) {
                        UiUtils.ShowToast((Activity) CurrentContext, " Time elapsed is " + (StopTime - StartTime) + " ms in " + ResourceUrl);
                    }
                    if (ResponseText.equals("500") && AppCache.IsEnableErrMsg == true) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CurrentContext);
                        // set title
                        alertDialogBuilder.setTitle("Error -" + "StatusCode : " + ResponseText);

                        // set dialog message
                        alertDialogBuilder
                                .setMessage(ResourceUrl)
                                .setCancelable(false)

                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // if this button is clicked, just close
                                                // the dialog box and do nothing

                                                dialog.cancel();
                                            }
                                        }

                                );

                        // create alert dialog
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                        ResponseText = null;
                    }

                    if (ResponseText != null && ResponseText != "") {
                        OnResponseReceived(ResponseText);
                    }
                } catch (Exception ex) {
                    LogHelper.HandleException(ex);
                }
            }
        }.execute(ResourceUrl, null, null);
    }

    public void GetDataStream(final String ResourceUrl) {
        try {
            if (NetworkStateManager.isNetworkAvailable((Activity) CurrentContext)) {
                AppCache.CurrentActivity = CurrentContext;
      /*          if((NetworkStateManager.isServerReachable(WebServiceUrls.WebServiceUrl))) {*/
                final Long StartTime = System.currentTimeMillis();

                new AsyncTask<String, Void, InputStream>() {
                    @Override
                    protected InputStream doInBackground(String... params) {
                        InputStream ResponseStream = null;

                        try {
                            HttpClient httpclient = new DefaultHttpClient();

                            String url = params[0];
                            HttpGet Get = new HttpGet(url);

                            Get.addHeader("Accept", "application/json");
                            if (AppCache.AccessToken != null) {

                                String authorizationString = "iBoltzKey " + AppCache.AccessToken.toString(); //Base64.NO_WRAP flag
                                Get.addHeader("Authorization", authorizationString);

                            }

                            HttpResponse response = httpclient.execute(Get);

                            HttpEntity entity = response.getEntity();
                            if (entity != null) {
                                ResponseStream = entity.getContent();
                            }
                            //ResponseStream.close();


                        } catch (IOException ex) {
                            LogHelper.HandleException(ex);
                        } catch (RuntimeException ex) {
                            LogHelper.HandleException(ex);
                        } catch (Exception ex) {
                            LogHelper.HandleException(ex);
                        } finally {


                        }
                        return ResponseStream;
                    }

                    @Override
                    protected void onPostExecute(InputStream InputData) {
                        try {
                            Long StopTime = System.currentTimeMillis();
                       /*         UiUtils.ShowToast((Activity) CurrentContext, " Time elapsed is " + (StopTime - StartTime) + " ms in " + ResourceUrl);*/
                            if (InputData != null) {
                                OnResponseReceived("", InputData);
                            }
                        } catch (Exception ex) {
                            LogHelper.HandleException(ex);
                        }
                    }
                }.execute(ResourceUrl, null, null);

             /*   }
                else
                {
                    UiUtils.ShowToast((Activity) CurrentContext, "Server isn't responding");
                }*/
            } else {
                UiUtils.ShowToast((Activity) CurrentContext, "Unable to connect to the internet");
            }

        } catch (Exception ex) {

            LogHelper.HandleException(ex);
        }
    }

    public static String ExecuteUrl(String url) {
        String result = "";

        try {
            Log.d(Tag, "Going to execute " + url);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet Get = new HttpGet(url);

            Get.addHeader("Accept", "application/json");
            if (AppCache.AccessToken != null) {

                String authorizationString = "iBoltzKey " + AppCache.AccessToken.toString(); //Base64.NO_WRAP flag
                Get.addHeader("Authorization", authorizationString);

            }

            HttpResponse response = httpclient.execute(Get);

            int StatusCode = response.getStatusLine().getStatusCode();
            result = EntityUtils.toString(response.getEntity());

            if (result == null || result == "") result = "Did not work!";
            if (StatusCode != 200) {
                LogHelper.SendExceptionMail("ExecuteUrl from webClient : "
                        + url + " and StatusCode :" + StatusCode);
                result = null;
            }
            Log.d("StatusCode", "" + StatusCode);
        } catch (Exception e) {
            Log.e(Tag, "Errored", e);
            LogHelper.HandleException(e);
        }
        return result;
    }


    public void setOnResponseReceivedListener(WebClientListeners listener) {
        WebListener = listener;
    }

    public void OnResponseReceived(String ResponseData, InputStream ResponseStream) {
        WebClientEventObject eObj = new WebClientEventObject(new Object());
        eObj.ResponseData = ResponseData;
        eObj.ResponseStream = ResponseStream;

        if (WebListener != null)
            WebListener.OnResponseReceived(eObj); // event object :)
    }

    public void OnResponseReceived(String ResponseData) {
        OnResponseReceived(ResponseData, null);
    }

}
