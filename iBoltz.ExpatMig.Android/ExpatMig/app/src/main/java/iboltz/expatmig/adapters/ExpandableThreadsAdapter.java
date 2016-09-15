package iboltz.expatmig.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import iboltz.expatmig.ListenerInterfaces.OnCreatedListener;
import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.R;
import iboltz.expatmig.controls.NewThreadControl;
import iboltz.expatmig.facades.ThreadSubscriptionFacade;
import iboltz.expatmig.models.GroupsModel;
import iboltz.expatmig.models.ThreadSubscriptionsModel;
import iboltz.expatmig.models.ThreadsModel;
import iboltz.expatmig.screens.ChatActivity;
import iboltz.expatmig.screens.GroupsActivity;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.LogHelper;


/**
 * Created by dev on 11-01-2016.
 */
public class ExpandableThreadsAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<GroupsModel> GroupsModelList;
    private OnCreatedListener Watcher;

    public ExpandableThreadsAdapter(Context context, List<GroupsModel> GroupsModelList) {
        this.context = context;
        this.GroupsModelList = new ArrayList<GroupsModel>();
        this.GroupsModelList.addAll(GroupsModelList);
    }

    public void setEventListener(OnCreatedListener listener) {
        Watcher = listener;
    }
    public void RaiseOnCreated() {
        EventObject eObj = new EventObject(new Object());

        if (Watcher != null)
            Watcher.OnCreated(eObj); // event object :)
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ThreadsModel> ThreadsModelList = GroupsModelList.get(groupPosition).AllThreads;
        return ThreadsModelList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ThreadsModel Item = (ThreadsModel) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.thread_items, null);
        }
        TextView tvThreadName = (TextView) convertView.findViewById(R.id.tvThreadName);
        TextView SendReqToAccess=(TextView)convertView.findViewById(R.id.SendReqToAccess);

        if(Item.IsAccessible){
            tvThreadName.setClickable(true);
            SendReqToAccess.setVisibility(View.GONE);
            tvThreadName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppCache.SelectedThread = (ThreadsModel) v.getTag();

                    Intent myIntent = new Intent(context,
                            ChatActivity.class);

                    context.startActivity(myIntent);
                }
            });
        }
        else {
            tvThreadName.setClickable(false);
            tvThreadName.setOnClickListener(null);
            SendReqToAccess.setVisibility(View.VISIBLE);
        }


        SendReqToAccess.setTypeface(AppCache.IonIcons);
        SendReqToAccess.setTag(Item);
        SendReqToAccess.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCache.SelectedThread = (ThreadsModel) v.getTag();
                SaveThreadSub();

            }
        });


        tvThreadName.setText(Item.Description);
        tvThreadName.setTypeface(AppCache.FontQuickRegular);
        tvThreadName.setTag(Item);


        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<ThreadsModel> ThreadsModelList = GroupsModelList.get(groupPosition).AllThreads;
        return ThreadsModelList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return GroupsModelList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return GroupsModelList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        GroupsModel ThisGroup = (GroupsModel) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_item, null);
        }

        TextView item = (TextView) view.findViewById(R.id.lblGroupName);
        TextView AddNewThread=(TextView) view.findViewById(R.id.NewThread);
        AddNewThread.setTypeface(AppCache.IonIcons);
        AddNewThread.setTag(ThisGroup);
        AddNewThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCache.SelectedGroup =(GroupsModel)v.getTag();
                
                final Dialog dialog = new Dialog(context);
                NewThreadControl Threadpop = new NewThreadControl(context, dialog);
                Threadpop.setEventListener(new OnCreatedListener() {
                    @Override
                    public void OnCreated(EventObject e) {
                        RaiseOnCreated();
                      //Raise Event to Reload Menu

                    }
                });
                dialog.setContentView(Threadpop);
                dialog.setTitle("New Thread");
                dialog.show();
            }
        });

        item.setTypeface(null, Typeface.BOLD);
        item.setTypeface(AppCache.FontQuickRegular);
        item.setText(ThisGroup.Description);
        return view;
    }
    private ThreadSubscriptionsModel GetNewThreadSub(){
        ThreadSubscriptionsModel item=new ThreadSubscriptionsModel();
        item.ThreadID=AppCache.SelectedThread.ThreadID;
        item.UserID=AppCache.HisUserID;
        item.IsActive = true;
        item.SeqNo = 1;
        item.CreatedBy = AppCache.HisUserID;
        item.CreatedDate = AppConstants.StandardDateFormat
                .format(new Date());
        item.ModifiedBy = 0;
        item.ModifiedDate = AppConstants.StandardDateFormat
                .format(new Date());
        return item;
    }
    private void SaveThreadSub(){

        ThreadSubscriptionFacade tf = new ThreadSubscriptionFacade(context);
        tf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {
                // skip now
                Toast.makeText(context, "your request send successfully", Toast.LENGTH_SHORT).show();
                RaiseOnCreated();
                //GetHisAllowedThreads();
            }
        });
        tf.SaveThreadSubscriptions(GetNewThreadSub());
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }





}