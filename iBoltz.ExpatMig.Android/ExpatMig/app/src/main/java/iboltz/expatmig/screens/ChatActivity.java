package iboltz.expatmig.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.R;
import iboltz.expatmig.adapters.ChatMessageAdapter;
import iboltz.expatmig.emojicon.EmojiconEditText;
import iboltz.expatmig.emojicon.EmojiconGridView;
import iboltz.expatmig.emojicon.EmojiconsPopup;
import iboltz.expatmig.emojicon.emoji.Emojicon;
import iboltz.expatmig.models.UserDevicesModel;
import iboltz.expatmig.models.UsersModel;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.facades.TopicsFacade;
import iboltz.expatmig.models.TopicsModel;
import iboltz.expatmig.utils.BaseActivity;
import iboltz.expatmig.utils.EndlessScrollListener;
import iboltz.expatmig.utils.LogHelper;
import iboltz.expatmig.utils.StorageManager;

public class ChatActivity extends BaseActivity implements iboltz.expatmig.gcmutils.iPostStatus {
    private static int IMG_RESULT = 1;
    ListView lvChat;
    Intent intent;
    String ImageDecode;
    ImageView submit_btn;
    TextView attach_img;
    EmojiconEditText emojiconEditText;
    ImageView emojiButton;
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
        LoadEmojiEvents();
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
            submit_btn = (ImageView) findViewById(R.id.submit_btn);
            emojiconEditText=(EmojiconEditText) findViewById(R.id.emojicon_edit_text);
            emojiButton = (ImageView) findViewById(R.id.emoji_btn);
            attach_img=(TextView) findViewById(R.id.attach_img);
            attach_img.setTypeface(AppCache.IonIcons);
            attach_img.setVisibility(View.GONE);
            attach_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, IMG_RESULT);
                }
            });

            emojiconEditText.setTypeface(AppCache.FontQuickRegular);
            if (AppCache.CachedTopics != null) {
                LoadTopics();
            }

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ps_neutral);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
            bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            LinearLayout layout = (LinearLayout) findViewById(R.id.root_view);
            layout.setBackgroundDrawable(bitmapDrawable);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();
                String[] FILE = { MediaStore.Images.Media.DATA };


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();

               /* imageViewLoad.setImageBitmap(BitmapFactory
                        .decodeFile(ImageDecode));*/

            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }

private void LoadEmojiEvents(){
    final View rootView = findViewById(R.id.root_view);
    final EmojiconsPopup popup = new EmojiconsPopup(rootView, this);

    //Will automatically set size according to the soft keyboard size
    popup.setSizeForSoftKeyboard();

    //If the emoji popup is dismissed, change emojiButton to smiley icon
    popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

        @Override
        public void onDismiss() {
            changeEmojiKeyboardIcon(emojiButton, R.drawable.smiley);
        }
    });

    //If the text keyboard closes, also dismiss the emoji popup
    popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

        @Override
        public void onKeyboardOpen(int keyBoardHeight) {

        }

        @Override
        public void onKeyboardClose() {
            if (popup.isShowing())
                popup.dismiss();
        }
    });
//On emoji clicked, add it to edittext
    popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

        @Override
        public void onEmojiconClicked(Emojicon emojicon) {
            if (emojiconEditText == null || emojicon == null) {
                return;
            }
            int start = emojiconEditText.getSelectionStart();
            int end = emojiconEditText.getSelectionEnd();
            if (start < 0) {
                emojiconEditText.append(emojicon.getEmoji());
            } else {
                emojiconEditText.getText().replace(Math.min(start, end),
                        Math.max(start, end), emojicon.getEmoji(), 0,
                        emojicon.getEmoji().length());
            }
        }
    });
            //On backspace clicked, emulate the KEYCODE_DEL key event
            popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

                @Override
                public void onEmojiconBackspaceClicked(View v) {
                    KeyEvent event = new KeyEvent(
                            0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                    emojiconEditText.dispatchKeyEvent(event);
                }
            });
    // To toggle between text keyboard and emoji keyboard keyboard(Popup)
    emojiButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            //If popup is not showing => emoji keyboard is not visible, we need to show it
            if (!popup.isShowing()) {

                //If keyboard is visible, simply show the emoji popup
                if (popup.isKeyBoardOpen()) {
                    popup.showAtBottom();
                    changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                }
                //else, open the text keyboard first and immediately after that show the emoji popup
                else {
                    emojiconEditText.setFocusableInTouchMode(true);
                    emojiconEditText.requestFocus();
                    popup.showAtBottomPending();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(emojiconEditText, InputMethodManager.SHOW_IMPLICIT);
                    changeEmojiKeyboardIcon(emojiButton, R.drawable.ic_action_keyboard);
                }
            }

            //If popup is showing, simply dismiss it to show the undelying text keyboard
            else {
                popup.dismiss();
            }
        }
    });


}
    private void changeEmojiKeyboardIcon(ImageView iconToBeChanged, int drawableResourceId){
        Resources resources = getResources();
        iconToBeChanged.setImageDrawable(resources.getDrawable(drawableResourceId));
    }

    private void ButtonListener() {
        try {
            lvChat.setOnScrollListener(onScrollListener());


            submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(emojiconEditText.getText())) {
                        return;
                    }

                    String Inputtxt = emojiconEditText.getText().toString();
                    TopicsModel InputTopic = FillTopic(Inputtxt);
                    AppCache.CachedTopics.add(InputTopic);
                    LoadTopics();
                    SaveTopic(FillTopic(Inputtxt));
                    emojiconEditText.setText("");
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
            item.IsAndroid=true;
            item.UserDeviceID=GetUserDeviceID();
            return item;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
public int GetUserDeviceID(){
    String ThisUserDevicejson= StorageManager.Get(this, "gcmdeviceid");
    Type collectionType = (Type) (new TypeToken<UserDevicesModel>() {
    }).getType();
    UserDevicesModel ThisUserDevice = new Gson().fromJson(ThisUserDevicejson, (Type) collectionType);
    return ThisUserDevice.UserDeviceID;
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
public String EncodeMsg(){
    try{
        byte[] data = emojiconEditText.getText().toString().getBytes("UTF-8");
        String baseString = Base64.encodeToString(data, Base64.DEFAULT);

return baseString;
    }
    catch(Exception ex){
        LogHelper.HandleException(ex);
    }
    return null;

}
    public void SaveTopic(TopicsModel InputTopic) {
        try {
            TopicsModel ConvertedTopics= new TopicsModel();
            ConvertedTopics=InputTopic;
            ConvertedTopics.Description=EncodeMsg();

            TopicsFacade tf = new TopicsFacade(CurrentContext);

            tf.setOnFinishedEventListener(new OnLoadedListener() {
                @Override
                public void OnLoaded(EventObject e) {

                }
            });
            tf.SaveTopics(ConvertedTopics);
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
