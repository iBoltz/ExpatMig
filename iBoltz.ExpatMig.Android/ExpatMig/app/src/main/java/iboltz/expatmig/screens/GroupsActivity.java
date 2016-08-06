package iboltz.expatmig.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import iboltz.expatmig.ListenerInterfaces.OnCreatedListener;
import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.R;
import iboltz.expatmig.controls.NewGroupControl;
import iboltz.expatmig.controls.NewThreadControl;
import iboltz.expatmig.facades.GroupsFacade;
import iboltz.expatmig.facades.ThreadsFacade;
import iboltz.expatmig.models.GroupsModel;
import iboltz.expatmig.models.ThreadsModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.BaseActivity;

public class GroupsActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    Spinner ddlGroups;
    Spinner ddlThreads;
    Button btnChat;
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
        CurrentInstance = this;
        SetBackButtonAction();
        InitControls();
        ButtonListener();
        LoadGroups();
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

    private void BindThreads() {
        ArrayList<String> AllThreads = new ArrayList<String>();
        AllThreads.add("Select Thread");
        for (ThreadsModel EachThread : AppCache.CachedThreads) {
            AllThreads.add(EachThread.Description);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CurrentInstance, R.layout.spinner_text, AllThreads);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        ddlThreads.setAdapter(dataAdapter);
        if (AppCache.CachedThreads.size() != 0) {

            if (AppCache.SelectedThread == null) {

                AppCache.SelectedThread = AppCache.CachedThreads.get(0);
            }
            String compareValue = AppCache.SelectedThread.Description;
            int spinnerPosition = dataAdapter.getPosition(compareValue);
            ddlThreads.setSelection(spinnerPosition);

        }
    }

    private void BindGroups() {
        List<String> AllGroups = new ArrayList<String>();

        AllGroups.add("Select Group");
        for (GroupsModel EachGroup : AppCache.CachedGroups) {
            AllGroups.add(EachGroup.Description);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CurrentInstance, R.layout.spinner_text, AllGroups);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        // attaching data adapter to spinner
        ddlGroups.setAdapter(dataAdapter);
        if (AppCache.CachedGroups.size() != 0) {

            if (AppCache.SelectedGroup == null) {

                AppCache.SelectedGroup = AppCache.CachedGroups.get(0);
            }
            String compareValue = AppCache.SelectedGroup.Description;
            int spinnerPosition = dataAdapter.getPosition(compareValue);
            ddlGroups.setSelection(spinnerPosition);
            //Load Their Topics
            LoadThreads(AppCache.SelectedGroup.GroupID);
        }


    }

    private void LoadGroups() {
        GroupsFacade gf = new GroupsFacade(CurrentContext);
        gf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {
                BindGroups();
            }
        });
        gf.LoadAllGroups();
    }

    private void LoadThreads(int GroupID) {

        ThreadsFacade tf = new ThreadsFacade(CurrentContext);
        tf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {

                BindThreads();

            }
        });
        tf.GetThreadsByGroupID(GroupID);
    }

    private void InitControls() {
        try {

            ddlGroups = (Spinner) findViewById(R.id.ddlGroups);
            ddlThreads = (Spinner) findViewById(R.id.ddlThreads);
            btnChat = (Button) findViewById(R.id.btnChat);
            btnNewGroup = (Button) findViewById(R.id.btnNewGroup);
            btnNewThread = (Button) findViewById(R.id.btnNewThread);
            ddlGroups.setOnItemSelectedListener(this);
            ddlThreads.setOnItemSelectedListener(this);

            btnChat.setTypeface(AppCache.IonIcons);
            btnNewGroup.setTypeface(AppCache.IonIcons);
            btnNewThread.setTypeface(AppCache.IonIcons);
            if (AppCache.SelectedGroup == null) {
                btnNewThread.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ButtonListener() {
        try {
            btnNewThread.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(CurrentContext);
                    NewThreadControl Threadpop = new NewThreadControl(CurrentContext, dialog);
                    Threadpop.setEventListener(new OnCreatedListener() {
                        @Override
                        public void OnCreated(EventObject e) {

                            LoadThreads(AppCache.SelectedGroup.GroupID);

                        }
                    });
                    dialog.setContentView(Threadpop);
                    dialog.setTitle("New Thread");
                    dialog.show();
                }
            });
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


            btnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AppCache.SelectedThread == null || AppCache.SelectedGroup==null){
                        Toast.makeText(GroupsActivity.this, "Please select group & thread !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent myIntent = new Intent(getApplicationContext(),
                            ChatActivity.class);

                    startActivity(myIntent);
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.ddlGroups) {
            if (parent.getItemAtPosition(position).toString() == "Select Group") {
                AppCache.CachedThreads=new ArrayList<ThreadsModel>();
                AppCache.SelectedGroup = null;
                AppCache.SelectedThread =null;
                BindThreads();
                btnNewThread.setVisibility(View.INVISIBLE);
               return;
            }
            for (GroupsModel EachModel : AppCache.CachedGroups) {
                if (EachModel.Description == parent.getItemAtPosition(position).toString()) {
                    AppCache.SelectedGroup = EachModel;
                    btnNewThread.setVisibility(View.VISIBLE);
                    AppCache.SelectedThread =null;
                    LoadThreads(EachModel.GroupID);
                    break;
                }
            }
            //do this

        } else if (spinner.getId() == R.id.ddlThreads) {
            if (parent.getItemAtPosition(position).toString() == "Select Thread") {
                AppCache.SelectedThread = null;
                return;
            }
            for (ThreadsModel EachThread : AppCache.CachedThreads) {
                if (EachThread.Description == parent.getItemAtPosition(position).toString()) {
                    AppCache.SelectedThread = EachThread;
                    break;
                }
            }

            //do this

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
