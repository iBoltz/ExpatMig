package iboltz.expatmig.facades;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.ListenerInterfaces.WebClientEventObject;
import iboltz.expatmig.ListenerInterfaces.WebClientListeners;
import iboltz.expatmig.models.ThreadSubscriptionsModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.WebClient;
import iboltz.expatmig.utils.WebServiceUrls;

/**
 * Created by ucfpriya on 26-07-2016.
 */
public class ThreadSubscriptionFacade {
    Context CurrentContext;
    private OnLoadedListener LoadedWatcher;
    public ThreadSubscriptionFacade(Context CurrentContext) {
        this.CurrentContext = CurrentContext;
    }
    public void setOnFinishedEventListener(OnLoadedListener listener) {
        LoadedWatcher = listener;
    }

    public void OnFinished() {
        try {
            EventObject eObj = new EventObject(new Object());

            if (LoadedWatcher != null)
                LoadedWatcher.OnLoaded(eObj); // event object :)
        } catch (Exception ex) {
         /*   LogHelper.HandleException(ex);*/
        }

    }
  
    public void SaveThreadSubscriptions(ThreadSubscriptionsModel CurrentTopics) {
        try {

            Gson gson = new Gson();
            String PostData = gson.toJson(CurrentTopics);
            WebClient Wc = new WebClient(CurrentContext);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {
                   // Toast.makeText(CurrentContext, " " +e.ResponseData, Toast.LENGTH_SHORT).show();
                    OnFinished();
                }
            });
            Wc.PostData(WebServiceUrls.SaveThreadSubRequests, PostData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
