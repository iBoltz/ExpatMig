package iboltz.expatmig.screens;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.R;
import iboltz.expatmig.adapters.ChatMessageAdapter;
import iboltz.expatmig.facades.GroupsFacade;
import iboltz.expatmig.facades.ThreadsFacade;
import iboltz.expatmig.facades.TopicsFacade;
import iboltz.expatmig.models.GroupsModel;
import iboltz.expatmig.models.ThreadsModel;
import iboltz.expatmig.models.TopicsModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.BaseActivity;

public class GroupsActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
   Spinner ddlGroups;
    Spinner ddlThreads;
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
        CurrentInstance=this;
        InitControls();
        LoadGroups();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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

    private void LoadGroups(){
        GroupsFacade gf = new GroupsFacade(CurrentContext);
        gf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {
                ArrayList<GroupsModel> GroupsList=(ArrayList<GroupsModel>) e.getSource();
                List<String> AllGroups = new ArrayList<String>();
                AllGroups.add("Select Group");
                for(GroupsModel EachGroup: GroupsList){
                    AllGroups.add(EachGroup.Description);
                }
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CurrentInstance, android.R.layout.simple_spinner_item, AllGroups);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                ddlGroups.setAdapter(dataAdapter);
            }
        });
        gf.LoadAllGroups();
    }
    private void LoadThreads(int GroupID){

        ThreadsFacade tf = new ThreadsFacade(CurrentContext);
        tf.setOnFinishedEventListener(new OnLoadedListener() {
            @Override
            public void OnLoaded(EventObject e) {
                ArrayList<ThreadsModel> BindableThreads = (ArrayList<ThreadsModel>) e.getSource();

                ArrayList<String> AllThreads = new ArrayList<String>();
                AllThreads.add("Select Thread");
                for(ThreadsModel EachThread : BindableThreads){
                    AllThreads.add(EachThread.Description);
                }

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CurrentInstance,android.R.layout.simple_spinner_item, AllThreads);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                ddlThreads.setAdapter(dataAdapter);

            }
        });
        tf.GetThreadsByGroupID(GroupID);
    }
    private void InitControls() {
        try {

            ddlGroups = (Spinner) findViewById(R.id.ddlGroups);
            ddlThreads=(Spinner)findViewById(R.id.ddlThreads);

            ddlGroups.setOnItemSelectedListener(this);
            ddlThreads.setOnItemSelectedListener(this);



        }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    private void ButtonListener()
    {
        try
        {
//            btnRefresh.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    FetchTopicsFromServer();
//                    LoadTopics();
//                }
//            });

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private void LoadTopics()
    {
        try {

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void scrollMyListViewToBottom() {

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.ddlGroups)
        {
            //do this
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getId() == R.id.ddlThreads)
        {
            //do this
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
