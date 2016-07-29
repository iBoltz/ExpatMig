package iboltz.expatmig.screens;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.R;
import iboltz.expatmig.adapters.ChatMessageAdapter;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.facades.TopicsFacade;
import iboltz.expatmig.models.TopicsModel;
import iboltz.expatmig.utils.BaseActivity;

public class ChatActivity extends BaseActivity implements iboltz.expatmig.gcmutils.iPostStatus {
    ListView lvChat;
    EditText txtMsg;
    Button btnSend;
//    Button btnRefresh;
    ChatMessageAdapter chatMessageAdapter;
    ArrayList<TopicsModel> topic=new ArrayList<TopicsModel>();
    public static Activity CurrentInstance;
    public static boolean IsRunningNow = false;
    public static String NotificationMessage = "";
    @Override
    public void PostStatusToOrder(Integer OrderID, Integer OrderDetailID, Integer StatusID) {

        FetchTopicsFromServer();

    }
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
        setContentView(R.layout.activity_chat);
        CurrentInstance = this;
        InitControls();
        ButtonListener();
        FetchTopicsFromServer();
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
    private void InitControls() {
        try {

            AppCache.CachedTopics= new ArrayList<TopicsModel>();


            lvChat = (ListView) findViewById(R.id.lvChat);
            btnSend = (Button) findViewById(R.id.btnSend);
//            btnRefresh=(Button) findViewById(R.id.btnRefresh);

            btnSend.setTypeface(AppCache.LinearIcons);
//            btnRefresh.setTypeface(AppCache.LinearIcons);

            txtMsg = (EditText) findViewById(R.id.txtMsg);
            if(AppCache.CachedTopics!=null)
            {
            LoadTopics();
            }
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
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(txtMsg.getText())){return;}

                    String Inputtxt = txtMsg.getText().toString();
                    TopicsModel InputTopic = FillTopic(Inputtxt);
                    AppCache.CachedTopics.add(InputTopic);
                    LoadTopics();
                    SaveTopic(InputTopic);
                    txtMsg.setText("");

                }
            });
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private void LoadTopics()
    {
        try {
            chatMessageAdapter = new ChatMessageAdapter(this, AppCache.CachedTopics);
            lvChat.setAdapter(chatMessageAdapter);
            scrollMyListViewToBottom();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void scrollMyListViewToBottom() {
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lvChat.setSelection(chatMessageAdapter.getCount() - 1);
            }
        });
    }
    private  TopicsModel FillTopic(String TopicsMessage)
    {
        try {
            TopicsModel item =new TopicsModel();
                item.ThreadID = 1;
                item.Description = TopicsMessage;
                item.Slug = "";
                item.IsActive = true;
                item.SeqNo = 1;
                item.CreatedBy = 1;
                item.CreatedDate = AppConstants.StandardDateFormat
                        .format(new Date());
                item.ModifiedBy = 0;
                item.ModifiedDate = AppConstants.StandardDateFormat
                        .format(new Date());
return item;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
    public void FetchTopicsFromServer(){
        try{
            TopicsFacade tf = new TopicsFacade(CurrentContext);
            tf.setOnFinishedEventListener(new OnLoadedListener() {
                @Override
                public void OnLoaded(EventObject e) {
                    LoadTopics();
                }
            });
tf.GetTopicsByThreadID(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void SaveTopic(TopicsModel InputTopic) {
        try {
           TopicsFacade tf = new TopicsFacade(CurrentContext);

            tf.setOnFinishedEventListener(new OnLoadedListener() {
                @Override
                public void OnLoaded(EventObject e) {

                }
            });
            tf.SaveTopics(InputTopic);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
