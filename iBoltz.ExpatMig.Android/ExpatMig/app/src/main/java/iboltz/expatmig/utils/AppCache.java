package iboltz.expatmig.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import iboltz.expatmig.models.GroupsModel;
import iboltz.expatmig.models.ImageLoaderModel;
import iboltz.expatmig.models.LocalCache;
import iboltz.expatmig.models.ThreadsModel;
import iboltz.expatmig.models.TopicsModel;
import iboltz.expatmig.models.UserRolesModel;
import iboltz.expatmig.models.UsersModel;
import iboltz.expatmig.utils.DateUtils;
import iboltz.expatmig.utils.LogHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppCache {

    public static String MessageKey = "";
    public static long SleepValue = 0;
    public static String AccessToken;
    public static List<LocalCache> Cached;
    public static ArrayList<TopicsModel> CachedTopics=null;
    public static ArrayList<ThreadsModel> CachedThreads;
    public static ArrayList<Integer> CachedAllowedThreads =null;
    public static ArrayList<GroupsModel> CachedGroups;
    public static ThreadsModel SelectedThread=null;
    public static GroupsModel SelectedGroup=null;
    public static int CurrentItemPosition=0;
    public static ArrayList<ImageLoaderModel> CachedImages;

    public static Typeface LinearIcons = null;
    public static Typeface IonIcons = null;
    public static Typeface FontQuickLight = null;
    public static Typeface FontQuickRegular = null;
    public static Typeface FontRuppee = null;

    public static Boolean IsEnableToast=false;
    public static Boolean IsEnableErrMsg = true;
    public static Boolean IsGCM = false;
    public static Boolean IsVerifiedByDeviceId = false;
    public static Boolean IsVerified = false;


    public static boolean AlreadySentToPlayStoreForUpdates = false;
    public static Context CurrentActivity;
    public static String AppId;
    public static String AppCurrentVersion = "0.0";
    public static String DeviceId = "";
    public static UsersModel CurrentUser;
    public static ArrayList<UserRolesModel> HisRoles;
    public static String RegistrationID = "";
    public static Integer HisUserID = -1;

    public static String UserName = "";
    public static Integer ScreenWidth;
    public static Integer ScreenHeight;



    /*****************************  Start App Specific Cache    **********************************/








    /*****************************  End App Specific Cache    **********************************/

    public static String GetUTCTime(){
        try{
            String format = "yyyy-MM-dd HH:mm:ss";
            final SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String utcTime = sdf.format(new Date());
            return utcTime;
        }
        catch (Exception ex){
            LogHelper.HandleException(ex);
        }
return null;
    }

    public static String GetLocalTime(String UtcDate){
        try{
            UtcDate =UtcDate.replace('T',' ');
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(UtcDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            return dateFormatter.format(value);
        }
        catch (Exception ex){
            LogHelper.HandleException(ex);
        }
        return null;
    }


   public static String EncodeUrl(String ImagePath) {
       try {
           final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()~'%";
           String urlEncoded = Uri.encode(ImagePath, ALLOWED_URI_CHARS);
           urlEncoded = WebServiceUrls.ImageServiceUrl + urlEncoded;
           Log.d("MyApp", "Image URl" + urlEncoded);
           return urlEncoded.trim();
       } catch (Exception ex) {
           LogHelper.HandleException(ex);
       }
       return null;
   }

    public static void SaveToStore(Activity ctx) {
        try {
            Gson MyConverter = new Gson();
            String Storable = MyConverter.toJson(AppCache.Cached);

            StorageManager.Put(ctx, "ObjectStore", Storable);
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public static String round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public static void LoadStore(Activity ctx) {
        try {
            Gson MyConverter = new Gson();

            String Storable = StorageManager.Get(ctx, "ObjectStore");

            java.lang.reflect.Type collectionType = (java.lang.reflect.Type) (new TypeToken<List<LocalCache>>() {
            }).getType();

            AppCache.Cached = MyConverter.fromJson(Storable, collectionType);

            if (AppCache.Cached == null) {
                AppCache.Cached = new ArrayList<>();
            }

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

    }

    public static void PushToLocalStore(String Msg, String CacheName, Context CurrentContext) {
        try {
            LocalCache NewCache = new LocalCache();

            NewCache.CacheName = CacheName;
            NewCache.CacheValue = Msg;
            NewCache.UpdatedOn = new Date();
            NewCache.ExpiresOn = DateUtils.AddDays(new Date(), 1);

            AppCache.PutIntoLocalCache(NewCache);

            AppCache.SaveToStore((Activity) CurrentContext);// store to local disk
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public static Date GetDateByString(String InputDateString) {
        try {
            String dtStart = InputDateString;
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format.parse(dtStart);
            return date;
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
        return null;
    }

    public static LocalCache GetLocalCacheByName(String Name) {
        try {
            for (LocalCache EachItem : AppCache.Cached) {
                if (EachItem.CacheName.equals(Name)) {
                    return EachItem;
                }
            }
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

        return null;
    }

    public static void PutIntoLocalCache(LocalCache ThisItem) {
        try {
            int ThisIndex = 0;
            boolean IsSet = false;
            for (LocalCache EachItem : AppCache.Cached) {
                if (EachItem.CacheName.equals(ThisItem.CacheName)) {
                    AppCache.Cached.set(ThisIndex, ThisItem);
                    IsSet = true;
                }
                ThisIndex++;
            }
            if (!IsSet) AppCache.Cached.add(ThisItem);
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }

    }
}
