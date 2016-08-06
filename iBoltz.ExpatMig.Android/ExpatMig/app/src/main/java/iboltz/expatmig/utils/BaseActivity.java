/**
 *
 */
package iboltz.expatmig.utils;

import iboltz.expatmig.ListenerInterfaces.UsersFacadeListeners;
import iboltz.expatmig.facades.UsersFacade;
import iboltz.expatmig.R;

import java.util.EventObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author pons This should be the base activity for all screens
 */
public class BaseActivity extends Activity implements
        LocationListener {

    public Context CurrentContext;
    protected String Tag = "Test";
    boolean doubleBackToExitPressedOnce;

    /**
     * constructor
     */
    public BaseActivity() {
        Tag = "MyApp";
        // TODO Auto-generated constructor stub
    }


    protected void TargetPortrait() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void TargetLandscape(Activity ThisActivity) {
        ThisActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            CurrentContext = this;
            InitApp();
            InitAppCache();
            InflateActionBar();
             if (NetworkStateManager.isNetworkAvailable(this)) {

            }
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    private void InitApp() {
        try {

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

    }

    private void InflateActionBar() {
        try {

            AppCache.LoadStore(this);


            ActionBar actionBar = getActionBar();
            if (actionBar == null)
                return;
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);

            ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            View customNav = LayoutInflater.from(this).inflate(
                    R.layout.menu_actionbar, null);

            actionBar.setCustomView(customNav, lp1);
            Button btnGoBack = (Button) findViewById(R.id.btnGoBack);
            TextView txtHeader=(TextView) findViewById(R.id.txtHeader);
            txtHeader.setTypeface(AppCache.FontQuickRegular);
            btnGoBack.setTypeface(AppCache.IonIcons);

//            txtHeader.setEllipsize(TruncateAt.END);


            btnGoBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("MyApp", "Clicked Back" + CurrentContext.toString());

                        // TODO Auto-generated method stub
                        OnBackPressed();
                    } catch (Exception ex) {
                        LogHelper.HandleException(ex);
                    }

                }

                private void OnBackPressed() {

                }


            });

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }


    public void ExitApp() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit",
                Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    boolean toscan;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_bar, menu);
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context,
                                     AttributeSet attrs) {

                if (name.equalsIgnoreCase(
                        "TextView")) {
                    try {
                        LayoutInflater li = LayoutInflater.from(context);
                        final View view = li.createView(name, null, attrs);
                        new Handler().post(new Runnable() {
                            public void run() {

                                ((TextView) view).setTypeface(AppCache.FontQuickRegular);
                            }
                        });
                        return view;
                    } catch (InflateException e) {
                        //Handle any inflation exception here
                    } catch (ClassNotFoundException e) {
                        //Handle any ClassNotFoundException here
                    }
                }
                return null;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            Log.d("MyApp", "onOptionsItemSelected" + item.getItemId());
            switch (item.getItemId()) {
                // Respond to the action bar's Up/Home button
                case android.R.id.home:
                    NavUtils.navigateUpFromSameTask(this);
                    return true;

            }
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.clear();

            getMenuInflater().inflate(R.menu.main_activity_bar, menu);
            MenuSplit(menu);


        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

        return super.onPrepareOptionsMenu(menu);

    }

    public void MenuSplit(Menu menu) {

        try {

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

    }


    private void InitAppCache() {
        try {
            if (AppCache.DeviceId != "")
                return;
            DisplayMetrics displaymetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay()
                    .getMetrics(displaymetrics);

            AppCache.ScreenWidth = displaymetrics.widthPixels;
            AppCache.ScreenHeight = displaymetrics.heightPixels;

            AppCache.AppCurrentVersion = GetInstalledVersion();

            AppCache.DeviceId = GetDeviceUid();


            AppCache.LinearIcons = Typeface.createFromAsset(
                    CurrentContext.getAssets(), "fonts/Linearicons-Free.ttf");
            AppCache.FontQuickLight = Typeface.createFromAsset(
                    CurrentContext.getAssets(), "fonts/Quicksand-Light.ttf");
            AppCache.FontQuickRegular = Typeface.createFromAsset(
                    CurrentContext.getAssets(), "fonts/Quicksand-Regular.ttf");

            AppCache.IonIcons = Typeface.createFromAsset(
                    CurrentContext.getAssets(), "fonts/ionicons.ttf");

            AppCache.FontRuppee = Typeface.createFromAsset(
                    CurrentContext.getAssets(), "fonts/Rupee_Foradian.ttf");

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

    }

    private String GetDeviceUid() {

        String FoundDeviceUid = "";
        final TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        FoundDeviceUid = tm.getDeviceId();// keep this as last one
        if (FoundDeviceUid == null || FoundDeviceUid == "") {
            FoundDeviceUid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        }

        return FoundDeviceUid;
    }

    public static void UpdateUser(Context CurrentContext) {
        try {
            UsersFacade HisUser = new UsersFacade(CurrentContext);
            HisUser.UpdateUserByPhoneNo();
            HisUser.setUserProcessFinishedListener(new UsersFacadeListeners() {
                @Override
                public void OnProcesFinished(EventObject e) {

                }
            });

        } catch (Exception ex) {
            LogHelper.HandleException(ex);

        }
    }

    private void SyncUpdates() {
        try {

//            Log.d("SmartUpdates", "AppCache.LastUpdateChecked  "
//                    + AppCache.LastUpdateChecked);
//            if (AppCache.LastUpdateChecked == "") {
//                AppCache.LastUpdateChecked = StorageManager.Get(this,
//                        "LastUpdateChecked");
//            }
//
//            Boolean CheckDurationExpired = true;
//
//            if (AppCache.LastUpdateChecked != null
//                    && AppCache.LastUpdateChecked != "") {
//                Long HowLong = Calendar.getInstance().getTimeInMillis()
//                        - Long.parseLong(AppCache.LastUpdateChecked);
//                CheckDurationExpired = HowLong > (6 * 60 * 60 * 1000);
//                // don't ask for updates within 6 hrs time
//            }
//            Log.d("SmartUpdates", "CheckDurationExpired "
//                    + CheckDurationExpired);
//
//            if (CheckDurationExpired) {// check iBoltz services to see new version available or not.
//                VerifyPendingUpdates();
//            }
//
//            if (AppCache.IsUpdateAvailable) {
//                if (!AppCache.AlreadySentToPlayStoreForUpdates)
//                    GoToUpdates();
//            }
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    private void GoToUpdates() {
        try {
            UiUtils.ShowToast((Activity) CurrentContext,
                    "Update to New Version");

            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=iboltz.smartdinein")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=iboltz.smartdinein")));
                LogHelper.HandleException(anfe);
            }

            AppCache.AlreadySentToPlayStoreForUpdates = true;
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

    }

    private void VerifyPendingUpdates() {

        try {
//
//            final UsersFacade HisUser = new UsersFacade(CurrentContext);
//
//            HisUser.setUserProcessFinishedListener(new UsersFacadeListeners() {
//                @Override
//                public void OnProcesFinished(EventObject e) {
//
//                    if (HisUser.CurrentVersion != null) {
//                        String Version = HisUser.CurrentVersion.replace('"', ' ');
//
//                        Version = Version.trim();
//
//                        if (Version == "")
//                            return;
//
//                        if (VersionComparer.IsNewVersion(
//                                AppCache.AppCurrentVersion, Version)) {
//                            AppCache.IsUpdateAvailable = true;
//                        }
//                        Log.d("SmartUpdates", "Version services responded as "
//                                + Version + "; so there is a new version yes? "
//                                + AppCache.IsUpdateAvailable);
//
//                        AppCache.LastUpdateChecked = Long.toString(Calendar
//                                .getInstance().getTimeInMillis());
//
//                        StorageManager.Put(CurrentContext, "LastUpdateChecked",
//                                AppCache.LastUpdateChecked);
//                    }
//                }
//            });
//            //    HisUser.VerifyPendingUpdates();

        } catch (Exception ex) {
            LogHelper.HandleException(ex);

        }
    }

    private String GetInstalledVersion() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            // UiUtils.ShowToast(this, pInfo.versionName);
            return pInfo.versionName;
        } catch (NameNotFoundException e) {
            LogHelper.HandleException(e);
        }
        return null;
    }

    protected void LogMe(String Msg) {
        Log.d(Tag, Msg);
    }

    @Override
    public void onLocationChanged(Location location) {
//        AppCache.Latitude = (float) location.getLatitude();
//        AppCache.Longitude = (float) location.getLongitude();

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


}
