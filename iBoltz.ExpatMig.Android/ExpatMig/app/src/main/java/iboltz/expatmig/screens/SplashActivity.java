package iboltz.expatmig.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.UsersFacadeListeners;
import iboltz.expatmig.R;
import iboltz.expatmig.facades.UsersFacade;
import iboltz.expatmig.gcmutils.GcmManager;
import iboltz.expatmig.models.LocalCache;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.BaseActivity;
import iboltz.expatmig.utils.DateUtils;
import iboltz.expatmig.utils.LogHelper;
import iboltz.expatmig.utils.StorageManager;
import iboltz.expatmig.utils.UiUtils;

public class SplashActivity extends BaseActivity
        implements GcmManager.IGcmRegisterCallback {
    Context CurrentContext;

    private Button btnLogin;
    private EditText txtUserName;
    private EditText txtPassword;
    private LinearLayout pnlLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        CurrentContext = this;
        InitCtrls();
        CheckLoginAndRegisterGcm();

    }

    private void CheckLoginAndRegisterGcm() {
        if (CheckAlreadyLoggedIn()) {
            CheckAndRefreshGcmCache();


            GcmManager gcmManager = new GcmManager((Activity) CurrentContext);
            gcmManager.registerGCM();
            RedirectToChat();
        }
    }

    private void CheckAndRefreshGcmCache() {
        //if the reg check at gcm server is more than 24 hrs old then
        //clear the local store to refetch the rgistration id
        //if registration id is same in the gcm server
        // it will return the same gcm. wont fetch new one
        try {
            String LastUpdatedOnString = StorageManager.Get(CurrentContext, "LastRegisteredDate");
            if (LastUpdatedOnString.trim() == "") return;
            Date LastUpdatedOn = AppConstants.JsonDateFormat.parse(LastUpdatedOnString);

            if (DateUtils.AddDays(LastUpdatedOn, 1).getTime() >= (new Date()).getTime()) {
                //clear pref edit
                StorageManager.Put(CurrentContext, "REG_ID", "");

            }

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    private void RedirectToChat() {
        try {
            Intent myIntent = new Intent(getApplicationContext(),
                    GroupsActivity.class);

            startActivity(myIntent);
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

    }

    private boolean CheckAlreadyLoggedIn() {
        LocalCache ThatItemFromCache = AppCache.GetLocalCacheByName("HasLoggedIn");
        LocalCache UserNameFromCache = AppCache.GetLocalCacheByName("UserName");
        if(UserNameFromCache != null){
            AppCache.UserName= String.valueOf(UserNameFromCache.CacheValue);
        }
        if (ThatItemFromCache == null || ThatItemFromCache.CacheValue == null) return false;
        int UserID = Integer.parseInt(ThatItemFromCache.CacheValue);
        if (UserID >= 0) {

            AppCache.HisUserID = UserID;
            pnlLogin.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private void InitCtrls() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        pnlLogin = (LinearLayout) findViewById(R.id.pnlLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TryLogin();


            }
        });
    }

    private void TryLogin() {
        UsersFacade Manager = new UsersFacade(CurrentContext);
        Manager.setUserProcessFinishedListener(new UsersFacadeListeners() {
            @Override
            public void OnProcesFinished(EventObject e) {
//                        UiUtils.ShowToast((Activity) CurrentContext, "You Loged in " + e.toString());
                LocalCache PassedValue = (LocalCache) e.getSource();
                LocalCache UserNameFromCache = AppCache.GetLocalCacheByName("UserName");
                if(UserNameFromCache != null){
                    AppCache.UserName= String.valueOf( UserNameFromCache.CacheValue);
                }
                int UserID = Integer.parseInt(PassedValue.CacheValue);

                if (UserID >= 0) {
                    AppCache.HisUserID = UserID;
                    pnlLogin.setVisibility(View.GONE);
                    CheckLoginAndRegisterGcm();
                } else {
                    UiUtils.ShowDialog((Activity) CurrentContext, "Login Failed");

                }
            }
        });
        Manager.Login(txtUserName.getText().toString(),
                txtPassword.getText().toString());
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

    @Override
    public void HasRegistered(Boolean IsDone) {
        try {
            if (IsDone) {

                AppCache.IsGCM = true;
                UiUtils.ShowDialog((Activity) CurrentContext, "GCM done");
            }

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }
}
