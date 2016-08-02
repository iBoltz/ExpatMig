package iboltz.expatmig.utils;

public class WebServiceUrls {
    public static final String WebServiceUrl = "192.168.1.103:4004";
    public static final String WebSiteUrl = "food.iboltz.com";

    //Verify Update - But not used
    public static final String AppVersionSyncService = "http://192.168.1.103:4004/apps/syncversion/";


    //Start Up
    public static final String UserLoginService = "http://192.168.1.103:4004/api/useraccess/login";
    public static final String UserCreationByDeviceService = "http://192.168.1.103:4004/usersbyDeviceID/getorcreateuser";
    public static final String GetAccessTokenWithUser = "http://192.168.1.103:4004/users/getaccesstokenwithuser/";
    public static final String GetRestaurantByUserID = "http://192.168.1.103:4004/restaurants/getRestaurantByUserID/";
    public static final String GcmDeviceRegistration = "http://192.168.1.103:4004/api/userdevices";

   //Chat Page
   public static final String CreateTopics = "http://192.168.1.103:4004/api/Topics";
    public static final String GetTopicsByThreadID = "http://192.168.1.103:4004/api/Topics/AllTopicsForThisThread/";

    //Group Page
    public static final String ListGroups="http://192.168.1.103:4004/api/Threads/GetThreadsByGroupID/";
    public static final String GetThreadsByGroupID="http://192.168.1.103:4004/api/Threads/GetThreadsByGroupID/";

    //My Profile
    public static final String UserUpdationService = "http://192.168.1.103:4004/users/updateuser/";
    public static final String UpdateUserDetails = "http://192.168.1.103:4004/users/UpdateuserDetail/";

    //Settings Page
    public static final String HandShakeGCM = "http://192.168.1.103:4004/orders/handshakegcm/";
    public static final String GetTestService = "http://192.168.1.103:4004/users/testservice/";

    //Log Helper
    public static final String SendErrorEmail = "http://192.168.1.103:4004/apps/senderrormail/";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
