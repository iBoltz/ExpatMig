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
    public void GetTopicsByThreadID(int ThreadID,int PageIndex){
        try{
            String UpdateUrl = WebServiceUrls.GetTopicsByThreadID + ThreadID +"/" + PageIndex;

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

                                    if (GetMyTopics.size()==0){
                                        Toast.makeText(CurrentContext, "No more messages", Toast.LENGTH_SHORT).show();

                                        return;
                                    }
                                    if(AppCache.CachedTopics.size()==0){
                                        AppCache.CachedTopics = GetMyTopics;

                                    }else{
                                        ArrayList<TopicsModel> SwapToBottom=AppCache.CachedTopics;
                                        AppCache.CachedTopics = new ArrayList<TopicsModel>();
                                        AppCache.CachedTopics.addAll(GetMyTopics);
                                        AppCache.CachedTopics.addAll(SwapToBottom);

                                    }
                    AppCache.CurrentItemPosition=GetMyTopics.size();
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
