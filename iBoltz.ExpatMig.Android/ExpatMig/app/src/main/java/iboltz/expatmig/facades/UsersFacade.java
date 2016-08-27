package iboltz.expatmig.facades;

import android.app.Activity;
import android.content.Context;
import android.util.Property;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;

import iboltz.expatmig.ListenerInterfaces.UsersFacadeListeners;
import iboltz.expatmig.ListenerInterfaces.WebClientEventObject;
import iboltz.expatmig.ListenerInterfaces.WebClientListeners;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.StorageManager;
import iboltz.expatmig.utils.WebServiceUrls;
import iboltz.expatmig.models.LocalCache;
import iboltz.expatmig.models.UserDevicesModel;
import iboltz.expatmig.models.UsersModel;
import iboltz.expatmig.utils.DateUtils;
import iboltz.expatmig.utils.LogHelper;
import iboltz.expatmig.utils.WebClient;

/**
 * Created by PonSaravanan on 12/18/2015.
 */
public class UsersFacade {
    private UsersFacadeListeners FacadeListener;
    Context CurrentContext;
    private String Tag = "UsersFacade";
    public String TestResult;
    public String CurrentVersion;

    public UsersFacade(Context CurrentContext) {
        this.CurrentContext = CurrentContext;
    }

    public void DownloadAccessToken() {
        try {
            Gson gson = new Gson();
            String PostData = gson.toJson(AppCache.CurrentUser);

            WebClient Wc = new WebClient(CurrentContext);
            Wc.PostData(WebServiceUrls.GetAccessTokenWithUser, PostData);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {
                    AppCache.AccessToken = e.ResponseData;
                    if (AppCache.AccessToken != "") {
                        AppCache.AccessToken = AppCache.AccessToken.trim();
                        if (AppCache.AccessToken.startsWith("\""))
                            AppCache.AccessToken = AppCache.AccessToken.substring(1, AppCache.AccessToken.length() - 1);
                        if (AppCache.AccessToken.endsWith("\""))
                            AppCache.AccessToken = AppCache.AccessToken.substring(0, AppCache.AccessToken.length() - 1);

                    }
                    RaiseOnProcesFinished();
                }
            });

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public LocalCache PushToLocalStore(String UserData, String CacheName) {
        try {
            LocalCache NewCache = new LocalCache();

            NewCache.CacheName = CacheName;
            NewCache.CacheValue = UserData;
            NewCache.UpdatedOn = new Date();
            NewCache.ExpiresOn = DateUtils.AddDays(new Date(), 7);

            AppCache.PutIntoLocalCache(NewCache);

            AppCache.SaveToStore((Activity) CurrentContext);// store to local disk
            return NewCache;
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
        return null;
    }


    public void LoadUser(final String DeviceId) {
        try {
            LocalCache ThatItemFromCache = AppCache.GetLocalCacheByName(AppConstants.LocalCacheType.CurrentUser.name());
            if (ThatItemFromCache == null || ThatItemFromCache.CacheValue == null) {
                Gson gson = new Gson();
                String PostData = gson.toJson(DeviceId);

                WebClient Wc = new WebClient(CurrentContext);
                Wc.PostData(WebServiceUrls.UserCreationByDeviceService, PostData);
                Wc.setOnResponseReceivedListener(new WebClientListeners() {
                    @Override
                    public void OnResponseReceived(WebClientEventObject e) {
                        ConvertToUserModel(e.ResponseData);
                        PushToLocalStore(e.ResponseData, AppConstants.LocalCacheType.CurrentUser.name());
                        RaiseOnProcesFinished();
                    }
                });
            } else {
                //get from store
                ConvertToUserModel(ThatItemFromCache.CacheValue);
                RaiseOnProcesFinished();
            }
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }


    public void Login(final String UserName, final String Password) {
        try {
            LocalCache ThatItemFromCache = AppCache.GetLocalCacheByName("HasLoggedIn");
            if (ThatItemFromCache == null || ThatItemFromCache.CacheValue == null) {
                String PostData = "{UserName:'" + UserName + "',Password:'" + Password + "'}";

                WebClient Wc = new WebClient(CurrentContext);
                Wc.PostData(WebServiceUrls.UserLoginService, PostData);
                Wc.setOnResponseReceivedListener(new WebClientListeners() {
                    @Override
                    public void OnResponseReceived(WebClientEventObject e) {
                        LocalCache ThatItemFromCache = PushToLocalStore(e.ResponseData, "HasLoggedIn");
                        RaiseOnProcesFinished(ThatItemFromCache);
                    }
                });
            } else {
                RaiseOnProcesFinished(ThatItemFromCache);
            }
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }


    private void ConvertToUserModel(String inputjson) {
        try {
            Type collectionType = (Type) (new TypeToken<UsersModel>() {
            }).getType();
            AppCache.CurrentUser = new Gson().fromJson(inputjson, (Type) collectionType);
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public String CheckHisVisitsResult;

//    public void CheckHisVisits(String RestaurantID, String UserID) {
//try {
//    String UpdateUrl = WebServiceUrls.CheckUserVisitService
//            + RestaurantID + "/" + UserID;
//
//    WebClient Wc = new WebClient(CurrentContext);
//    Wc.GetData(UpdateUrl);
//    Wc.setOnResponseReceivedListener(new WebClientListeners() {
//        @Override
//        public void OnResponseReceived(WebClientEventObject e) {
//            CheckHisVisitsResult = e.ResponseData;
//            RaiseOnProcesFinished();
//        }
//    });
//}catch (Exception ex) {
//    LogHelper.HandleException(ex);
//}
//    }

//    public void LogMyVisit(String RestaurantID, String UserID) {
//        try {
//            String UpdateUrl = WebServiceUrls.UserLogService
//                    + RestaurantID + "/" + UserID;
//
//            WebClient Wc = new WebClient(CurrentContext);
//            Wc.GetData(UpdateUrl);
//            Wc.setOnResponseReceivedListener(new WebClientListeners() {
//                @Override
//                public void OnResponseReceived(WebClientEventObject e) {
//                    RaiseOnProcesFinished();
//                }
//            });
//        } catch (Exception ex) {
//            LogHelper.HandleException(ex);
//        }
//    }


    public void UpdateUser(UsersModel UserDetails) {
        try {
            Gson gson = new Gson();
            String PostData = gson.toJson(UserDetails);

            WebClient Wc = new WebClient(CurrentContext);
            Wc.PostData(WebServiceUrls.UpdateUserDetails, PostData);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {

                    RaiseOnProcesFinished();
                }
            });

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public void UpdateUserByPhoneNo() {
        try {
            String UpdateUrl = WebServiceUrls.UserUpdationService
                    + AppCache.HisUserID + "/" + AppCache.HostPhoneNo;
            WebClient Wc = new WebClient(CurrentContext);
            Wc.GetData(UpdateUrl);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {
                    RaiseOnProcesFinished();
                }
            });
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public void TestService(String InputText) {
        try {
            String UpdateUrl = WebServiceUrls.GetTestService
                    + InputText;

            WebClient Wc = new WebClient(CurrentContext);
            Wc.GetData(UpdateUrl);
            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {
                    TestResult = e.ResponseData;
                    RaiseOnProcesFinished();
                }
            });
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public void VerifyPendingUpdates() {
        String UpdateUrl = WebServiceUrls.AppVersionSyncService
                + AppCache.AppCurrentVersion;
        WebClient Wc = new WebClient(CurrentContext);
        Wc.GetData(UpdateUrl);
        Wc.setOnResponseReceivedListener(new WebClientListeners() {
            @Override
            public void OnResponseReceived(WebClientEventObject e) {
                CurrentVersion = e.ResponseData;
                RaiseOnProcesFinished();
            }
        });
    }

    public void UpdateRegistrationID(String RegID, Boolean IsVerifiedByDeviceId) {
        try {
            String UpdateUrl = WebServiceUrls.GcmDeviceRegistration;

            UserDevicesModel CurrentUserDevice = new UserDevicesModel();
            CurrentUserDevice.UserID=AppCache.HisUserID;
            CurrentUserDevice.DeviceID = AppCache.DeviceId;
            CurrentUserDevice.DeviceTypeID=1;//1:android
            CurrentUserDevice.ApiRegistrationID = RegID;
            CurrentUserDevice.AppVersion = AppCache.AppCurrentVersion;
            CurrentUserDevice.CreatedBy = AppCache.HisUserID;

            Gson gson = new Gson();
            String PostData = gson.toJson(CurrentUserDevice);

            WebClient Wc = new WebClient(CurrentContext);
            Wc.PostData(UpdateUrl, PostData);

            Wc.setOnResponseReceivedListener(new WebClientListeners() {
                @Override
                public void OnResponseReceived(WebClientEventObject e) {
                    if (e.ResponseData != null) {
                        StorageManager.Put(CurrentContext, "gcmdeviceid", e.ResponseData);
                        RaiseOnProcesFinished();
                    }
                }
            });
        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
    }

    public void setUserProcessFinishedListener(UsersFacadeListeners listener) {
        FacadeListener = listener;
    }

    public void RaiseOnProcesFinished() {
        RaiseOnProcesFinished(new Object());
    }

    public void RaiseOnProcesFinished(Object Obj) {
        EventObject eObj = new EventObject(Obj);

        if (FacadeListener != null)
            FacadeListener.OnProcesFinished(eObj); // event object :)
    }

}
