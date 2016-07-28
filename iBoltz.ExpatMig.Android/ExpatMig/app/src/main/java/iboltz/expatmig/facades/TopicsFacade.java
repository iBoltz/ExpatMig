package iboltz.expatmig.facades;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.EventObject;
import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.ListenerInterfaces.WebClientEventObject;
import iboltz.expatmig.ListenerInterfaces.WebClientListeners;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.WebServiceUrls;
import iboltz.expatmig.models.TopicsModel;
import iboltz.expatmig.utils.WebClient;

/**
 * Created by ucfpriya on 26-07-2016.
 */
public class TopicsFacade {
    Context CurrentContext;
    private OnLoadedListener LoadedWatcher;
    public TopicsFacade(Context CurrentContext) {
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
    public void GetTopicsByThreadID(int ThreadID){
        try{
            String UpdateUrl = WebServiceUrls.GetTopicsByThreadID + ThreadID;

            WebClient Wc = new WebClient(CurrentContext);
            Wc.GetData(UpdateUrl);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {
                    java.lang.reflect.Type collectionType = (java.lang.reflect.Type) (new TypeToken<ArrayList<TopicsModel>>() {
                    }).getType();

                    ArrayList<TopicsModel> GetMyTopics = new Gson()
                            .fromJson(
                                    e.ResponseData,
                                    (java.lang.reflect.Type) collectionType);

                    AppCache.CachedTopics = GetMyTopics;
                    OnFinished();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void SaveTopics(TopicsModel CurrentTopics) {
        try {

            Gson gson = new Gson();
            String PostData = gson.toJson(CurrentTopics);
            WebClient Wc = new WebClient(CurrentContext);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {

                    OnFinished();
                }
            });
            Wc.PostData(WebServiceUrls.CreateTopics, PostData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
