package iboltz.expatmig.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import iboltz.expatmig.utils.EndlessScrollListener;
import iboltz.expatmig.utils.StorageManager;

public class ChatActivity extends BaseActivity implements iboltz.expatmig.gcmutils.iPostStatus {
    ListView lvChat;
    EditText txtMsg;
    Button btnSend;
    //    Button btnRefresh;
    ChatMessageAdapter chatMessageAdapter;
    ArrayList<TopicsModel> topic = new ArrayList<TopicsModel>();
    public static Activity CurrentInstance;
    public static boolean IsRunningNow = false;
    public static String NotificationMessage = "";

    private int pageCount = 0;

    @Override
    public void PostStatusToOrder(TopicsModel Message) {
        AppCache.CachedTopics.add(Message);
        AppCache.CurrentItemPosition = AppCache.CachedTopics.size();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadTopics();
            }
        });

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
        SetBackButtonAction();
        InitControls();
        ButtonListener();
        FetchTopicsFromServer(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_chat, menu);
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

            AppCache.CachedTopics = new ArrayList<TopicsModel>();

            lvChat = (ListView) findViewById(R.id.lvChat);
            btnSend = (Button) findViewById(R.id.btnSend);
//            btnRefresh=(Button) findViewById(R.id.btnRefresh);

            btnSend.setTypeface(AppCache.IonIcons);
//            btnRefresh.setTypeface(AppCache.LinearIcons);

            txtMsg = (EditText) findViewById(R.id.txtMsg);
            txtMsg.setTypeface(AppCache.FontQuickRegular);
            if (AppCache.CachedTopics != null) {
                LoadTopics();
            }
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ps_neutral);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            LinearLayout layout = (LinearLayout) findViewById(R.id.lnrbg);
            layout.setBackgroundDrawable(bitmapDrawable);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void ButtonListener() {
        try {
            lvChat.setOnScrollListener(onScrollListener());


            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(txtMsg.getText())) {
                        return;
                    }

                    String Inputtxt = txtMsg.getText().toString();
                    TopicsModel InputTopic = FillTopic(Inputtxt);
                    AppCache.CachedTopics.add(InputTopic);
                    LoadTopics();
                    SaveTopic(InputTopic);
                    txtMsg.setText("");
                    AppCache.CurrentItemPosition = AppCache.CachedTopics.size();

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ScrollToRecentPosition() {

        lvChat.setSelection(AppCache.CurrentItemPosition - 1);
    }

    private void LoadTopics() {
        try {
            ListView lvChat = (ListView) findViewById(R.id.lvChat);
            ChatMessageAdapter chatMessageAdapter;

            chatMessageAdapter = new ChatMessageAdapter(getApplicationContext(), AppCache.CachedTopics);
            lvChat.setAdapter(chatMessageAdapter);
            ScrollToRecentPosition();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private TopicsModel FillTopic(String TopicsMessage) {
        try {
            TopicsModel item = new TopicsModel();
            item.ThreadID = AppCache.SelectedThread.ThreadID;
            item.Description = TopicsMessage;
            item.Slug = "";
            item.IsActive = true;
            item.SeqNo = 1;
            item.UserName = AppCache.UserName;
            item.CreatedBy = AppCache.HisUserID;
            item.CreatedDate = AppConstants.StandardDateFormat
                    .format(new Date());
            item.ModifiedBy = 0;
            item.ModifiedDate = AppConstants.StandardDateFormat
                    .format(new Date());
            String UsderDeviceID= StorageManager.Get(this, "gcmdeviceid");
            item.UserDeviceID=Integer.valueOf(UsderDeviceID);
            return item;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void FetchTopicsFromServer(int PageIndex) {
        try {
            if (AppCache.SelectedThread == null) {
                return;
            }
            TopicsFacade tf = new TopicsFacade(CurrentContext);
            tf.setOnFinishedEventListener(new OnLoadedListener() {
                @Override
                public void OnLoaded(EventObject e) {
                    LoadTopics();
                }
            });
            tf.GetTopicsByThreadID(AppCache.SelectedThread.ThreadID, PageIndex);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void SetBackButtonAction() {
        try {
            View v = getActionBar().getCustomView();
            TextView txtHeader = (TextView) v.findViewById(R.id.txtHeader);
            Button btnGoBack = (Button) v.findViewById(R.id.btnGoBack);
            txtHeader.setText(AppCache.SelectedThread.Description);
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
                GroupsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_move_left_open,
                R.anim.activity_move_left_close);
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

    private boolean listIsAtTop() {
        //   if(lvChat.getChildCount() == 0) return true;
        int CurrentChildCount = lvChat.getChildCount();

        return lvChat.getFirstVisiblePosition() == 0 && (lvChat.getChildCount() == 0 || lvChat.getChildAt(0).getTop() == 0);
        //  return lvChat.getChildAt(0).getTop() == 0;
    }

    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // int threshold = 1;
                // int count = lvChat.getCount();
                Boolean IsAllow = listIsAtTop();
                if (IsAllow) {
                    //  if (lvChat.getLastVisiblePosition() >= count - threshold) {
                    pageCount = pageCount + 1;
                    FetchTopicsFromServer(pageCount);
                    //  }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }

        };
    }
}
