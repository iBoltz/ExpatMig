package iboltz.expatmig.facades;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.ListenerInterfaces.WebClientEventObject;
import iboltz.expatmig.ListenerInterfaces.WebClientListeners;
import iboltz.expatmig.models.GroupMenuModel;
import iboltz.expatmig.models.GroupsModel;
import iboltz.expatmig.models.ThreadsModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.LogHelper;
import iboltz.expatmig.utils.WebClient;
import iboltz.expatmig.utils.WebServiceUrls;

/**
 * Created by ucfpriya on 26-07-2016.
 */
public class GroupsFacade {
    Context CurrentContext;
    private OnLoadedListener LoadedWatcher;
    public GroupsFacade(Context CurrentContext) {
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
    public void SaveNewGroup(GroupsModel ThisGroup) {
        try {
            Gson gson = new Gson();
            String PostData = gson.toJson(ThisGroup);

            String UpdateUrl = WebServiceUrls.SaveNewGroup;

            WebClient Wc = new WebClient(CurrentContext);
            Wc.PostData(UpdateUrl, PostData);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {
                    java.lang.reflect.Type collectionType = (java.lang.reflect.Type) (new TypeToken<GroupsModel>() {
                    }).getType();

                   GroupsModel NewModel = new Gson()
                            .fromJson(
                                    e.ResponseData,
                                    (java.lang.reflect.Type) collectionType);

                   AppCache.SelectedGroup=NewModel;
                   OnFinished();
                }
            });
        }
        catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public void LoadAllGroups(){
        try{
            String UpdateUrl = WebServiceUrls.ListGroups ;

            WebClient Wc = new WebClient(CurrentContext);
            Wc.GetData(UpdateUrl);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {
                    try{

                    java.lang.reflect.Type collectionType = (java.lang.reflect.Type) (new TypeToken<ArrayList<GroupMenuModel>>() {
                    }).getType();

                    ArrayList<GroupMenuModel> ListAllModels = new Gson()
                            .fromJson(
                                    e.ResponseData,
                                    (java.lang.reflect.Type) collectionType);

                        AppCache.CachedGroups=new ArrayList<GroupsModel>();

                    for (GroupMenuModel item:  ListAllModels ) {
                    item.ParentGroup.AllThreads=new ArrayList<ThreadsModel>();
                       item.ParentGroup.AllThreads.addAll(item.ChildThreads);

                        AppCache.CachedGroups.add(item.ParentGroup);
                    }
                    OnFinished();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

}
