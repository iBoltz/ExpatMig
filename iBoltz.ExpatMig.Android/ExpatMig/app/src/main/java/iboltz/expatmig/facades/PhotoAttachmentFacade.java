package iboltz.expatmig.facades;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.OnCreatedListener;
import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.ListenerInterfaces.OnPhotoSavedListener;
import iboltz.expatmig.ListenerInterfaces.WebClientEventObject;
import iboltz.expatmig.ListenerInterfaces.WebClientListeners;
import iboltz.expatmig.models.GroupsModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.LogHelper;
import iboltz.expatmig.utils.WebClient;
import iboltz.expatmig.utils.WebServiceUrls;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Created by PonSaravanan on 02-Sep-16.
 */
public class PhotoAttachmentFacade {
    Context CurrentContext;
    private OnPhotoSavedListener LoadedWatcher;
    public PhotoAttachmentFacade(Context CurrentContext) {
        this.CurrentContext = CurrentContext;
    }
    public void setOnFinishedEventListener(OnPhotoSavedListener listener) {
        LoadedWatcher = listener;
    }

    public void OnFinished(String SavedPath) {
        try {
            EventObject eObj = new EventObject(new Object());

            if (LoadedWatcher != null)
                LoadedWatcher.OnPhotoPosted(eObj, SavedPath); // event object :)
        } catch (Exception ex) {
         /*   LogHelper.HandleException(ex);*/
        }

    }
    public void AttachNewPhoto(final  byte[] PostData) {
        try {



            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    String msg = "";
                    try {

                        Log.d("MyApp", "UpdateUrl:" + WebServiceUrls.PostImagesUrl);
                        HttpPost request = new HttpPost(WebServiceUrls.PostImagesUrl);
                        request.addHeader("Accept", "application/json");
                        request.addHeader("Content-Type", "application/json");
                        request.setEntity(new ByteArrayEntity(PostData));

                        HttpClient httpClient = new DefaultHttpClient();
                        HttpResponse response = httpClient.execute(request);

                        msg = EntityUtils.toString(response.getEntity());
                        Log.d("MyApp", "Img Upload Response is: " + msg);

                    } catch (Exception ex) {
                        msg = "Error :" + ex.getMessage();
                        Log.d("MyApp", "Exception: " + msg);
                        LogHelper.HandleException(ex);
                    }

                    return msg;
                }

                @Override
                protected void onPostExecute(String msg) {
                    OnFinished(msg);

                }
            }.execute(null, null, null);


        }
        catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }



}
