package iboltz.expatmig.utils;

public class WebServiceUrls {
    public static final String WebServiceUrl = "192.168.1.105:125";
    public static final String WebSiteUrl = "food.iboltz.com";

    //Verify Update - But not used
    public static final String AppVersionSyncService = "http://192.168.1.105:125/apps/syncversion/";


    //Start Up
    public static final String UserLoginService = "http://192.168.1.105:125/api/useraccess/login";
    public static final String UserCreationByDeviceService = "http://192.168.1.105:125/usersbyDeviceID/getorcreateuser";
    public static final String GetAccessTokenWithUser = "http://192.168.1.105:125/users/getaccesstokenwithuser/";
    public static final String GetRestaurantByUserID = "http://192.168.1.105:125/restaurants/getRestaurantByUserID/";
    public static final String GcmDeviceRegistration = "http://192.168.1.105:125/api/userdevices";

   //Chat Page
   public static final String CreateTopics = "http://192.168.1.105:125/api/Topics";
    public static final String GetTopicsByThreadID = "http://192.168.1.105:125/api/Topics/AllTopicsForThisThread/";
    public static final String ImageServiceUrl = "http://192.168.1.105:125/utils/photohandler.ashx?Width=150&frompath=";
    public static final String PostImagesUrl = "http://192.168.1.105:125/api/Topics/AttachPhoto/";

    //Group Page
    public static final String ListGroups="http://192.168.1.105:125/api/Groups/Full";
    public static final String GetThreadsByGroupID="http://192.168.1.105:125/api/Threads/GetThreadsByGroupID/";
    public static final String SaveNewGroup="http://192.168.1.105:125/api/Groups";
    public static final String SaveNewThread="http://192.168.1.105:125/api/Threads";
    public static final String GetThreadsByUser = "http://192.168.1.105:125/api/ThreadSubscriptions/GetHisThreads/";
    public static final String SaveThreadSubRequests = "http://192.168.1.105:125/api/ThreadSubscriptions/PostThreadSubscriptions/";

    //My Profile
    public static final String UserUpdationService = "http://192.168.1.105:125/users/updateuser/";
    public static final String UpdateUserDetails = "http://192.168.1.105:125/users/UpdateuserDetail/";

    //Settings Page
    public static final String HandShakeGCM = "http://192.168.1.105:125/orders/handshakegcm/";
    public static final String GetTestService = "http://192.168.1.105:125/users/testservice/";

    //Log Helper
     public static final String SendErrorEmail = "http://192.168.1.105:125/apps/senderrormail/";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
