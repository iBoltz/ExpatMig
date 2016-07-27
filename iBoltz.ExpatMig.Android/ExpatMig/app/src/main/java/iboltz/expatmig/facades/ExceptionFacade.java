package iboltz.expatmig.facades;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.ListenerInterfaces.WebClientEventObject;
import iboltz.expatmig.ListenerInterfaces.WebClientListeners;
import iboltz.expatmig.utils.WebServiceUrls;
import iboltz.expatmig.utils.LogHelper;
import iboltz.expatmig.utils.WebClient;

/**
 * Created by dev on 22-12-2015.
 */
public class ExceptionFacade {
    private OnLoadedListener LoadedWatcher;
    Context CurrentContext;

    public ExceptionFacade() {
        /*this.CurrentContext = CurrentContext;*/
    }

    public void setOnFinishedEventListener(OnLoadedListener listener) {
        LoadedWatcher = listener;
    }

    public void OnFinished() {
        try
        {
            EventObject eObj = new EventObject(new Object());

            if (LoadedWatcher != null)
                LoadedWatcher.OnLoaded(eObj); // event object :)
        }
        catch(Exception ex)
        {
            LogHelper.HandleException(ex);
        }

    }
    public void SendErrorMail(String Exception){
        Gson gson = new Gson();
        String PostData = gson.toJson(Exception);

        WebClient Wc = new WebClient();
        Wc.PostData(WebServiceUrls.SendErrorEmail, PostData);
        Wc.setOnResponseReceivedListener(new WebClientListeners() {
            @Override
            public void OnResponseReceived(WebClientEventObject e) {

                OnFinished();
            }
        });
    }
}
