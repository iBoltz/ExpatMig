package iboltz.expatmig.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import iboltz.expatmig.ListenerInterfaces.OnCreatedListener;
import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.R;
import iboltz.expatmig.adapters.ExpandableThreadsAdapter;
import iboltz.expatmig.controls.NewGroupControl;
import iboltz.expatmig.controls.NewThreadControl;
import iboltz.expatmig.facades.GroupsFacade;
import iboltz.expatmig.facades.ThreadSubscriptionFacade;
import iboltz.expatmig.facades.ThreadsFacade;
import iboltz.expatmig.models.GroupsModel;
import iboltz.expatmig.models.ThreadSubscriptionsModel;
import iboltz.expatmig.models.ThreadsModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.BaseActivity;

public class GroupsActivity extends BaseActivity implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {
   /* Spinner ddlGroups;
    Spinner ddlThreads;*/
    ExpandableListView Group_list;
  //  RelativeLayout progress_panel;
    private ExpandableThreadsAdapter ThreadAdapter;

    Button btnChat;
    Button btnReqToAccess;
    Button btnNewGroup;
    Button btnNewThread;
    public static Activity CurrentInstance;
    public static boolean IsRunningNow = false;

    @Override
    public void onStart() {
        super.onStart();
        IsRunningNow = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        IsRunningNow = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        InitControls();
        GetHisAllowedThreads();
        CurrentInstance = this;
        SetBackButtonAction();

    }

    private void SetBackButtonAction() {
        try {
            View v = getActionBar().getCustomView();
            TextView txtHeader = (TextView) v.findViewById(R.id.txtHeader);
            Button btnGoBack = (Button) v.findViewById(R.id.btnGoBack);
            txtHeader.setText("Groups");
            btnGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GotToPreviousPage();
                }
            });
        } catch (Exception ex) {
            //  LogHelper.HandleException(ex);
        }
    }

    private void GotToPreviousPage() {
        Intent intent = new Intent(getApplicationContext(),
                SplashActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_move_left_open,
                R.anim.activity_move_left_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void BindGroups() {

        for (GroupsModel EachGroup : AppCache.CachedGroups) {
            for(ThreadsModel EachThread :EachGroup.AllThreads){
                for (Integer EachThreadID : AppCache.CachedAllowedThreads) {
                if(EachThread.ThreadID == EachThreadID){
                    EachThread.IsAccessible=true;
                }
                    else{
                    EachThread.IsAccessible=false;
                }
                }
            }
        }

        ThreadAdapter = new ExpandableThreadsAdapter(CurrentContext, AppCache.CachedGroups);
        ThreadAdapter.setEventListener(new OnCreatedListener() {
            @Override
            public void OnCreated(EventObject e) {
                GetHisAllowedThreads();
            }
        });
        Group_list.setAdapter(ThreadAdapter);


        Group_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return true;
            }
        });


    }

    private void LoadGroups() {
       /* progress_panel.setVisibility(View.VISIBLE);*/
        GroupsFacade gf = new GroupsFacade(CurrentContext);
        gf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {
                BindGroups();
            }
        });
        gf.LoadAllGroups();
    }


    private void GetHisAllowedThreads(){
     //   progress_panel.setVisibility(View.VISIBLE);
        ThreadsFacade tf = new ThreadsFacade(CurrentContext);

        tf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {
                LoadGroups();
            }
        });
        tf.GetHisPermittedThreads(AppCache.HisUserID);
      //  progress_panel.setVisibility(View.GONE);
    }
    private void expandAll() {
        int count = ThreadAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            Group_list.expandGroup(i);
        }
    }
    private void CollapseAll() {
        int count = ThreadAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            Group_list.collapseGroup(i);
        }
    }
    @Override
    public boolean onQueryTextChange(String query) {
       // ThreadAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
       // ThreadAdapter.filterData(query);
        expandAll();
        return false;
    }


    private void InitControls() {
        try {

            Group_list=(ExpandableListView)findViewById(R.id.Group_list);
            btnNewGroup=(Button)findViewById(R.id.btnAddNewGroup);
            btnNewGroup.setTypeface(AppCache.IonIcons);
            btnNewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(CurrentContext);
                    NewGroupControl voidorder = new NewGroupControl(CurrentContext, dialog);
                    voidorder.setEventListener(new OnCreatedListener() {
                        @Override
                        public void OnCreated(EventObject e) {

                            LoadGroups();


                        }
                    });
                    dialog.setContentView(voidorder);
                    dialog.setTitle("New Group");
                    dialog.show();

                }

            });
           // progress_panel=(RelativeLayout)findViewById(R.id.progress_panel);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public boolean onClose() {
        return false;
    }
}
