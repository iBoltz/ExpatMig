package iboltz.expatmig.utils;

public class WebServiceUrls {
    public static final String WebServiceUrl = "192.168.1.118:5001";
    public static final String WebSiteUrl = "food.iboltz.com";

    //Verify Update - But not used
    public static final String AppVersionSyncService = "http://192.168.1.118:5001/apps/syncversion/";


    //Start Up
    public static final String UserLoginService = "http://192.168.1.118:5001/api/useraccess/login";
    public static final String UserCreationByDeviceService = "http://192.168.1.118:5001/usersbyDeviceID/getorcreateuser";
    public static final String GetAccessTokenWithUser = "http://192.168.1.118:5001/users/getaccesstokenwithuser/";
    public static final String GetRestaurantByUserID = "http://192.168.1.118:5001/restaurants/getRestaurantByUserID/";
    public static final String GcmDeviceRegistration = "http://192.168.1.118:5001/api/userdevices";

   //Chat Page
   public static final String CreateTopics = "http://192.168.1.118:5001/api/Topics";
    public static final String GetTopicsByThreadID = "http://192.168.1.118:5001/api/Topics/AllTopicsForThisThread/";

    //My Profile
    public static final String UserUpdationService = "http://192.168.1.118:5001/users/updateuser/";
    public static final String UpdateUserDetails = "http://192.168.1.118:5001/users/UpdateuserDetail/";

    //Settings Page
    public static final String HandShakeGCM = "http://192.168.1.118:5001/orders/handshakegcm/";
    public static final String GetTestService = "http://192.168.1.118:5001/users/testservice/";

    //Log Helper
    public static final String SendErrorEmail = "http://192.168.1.118:5001/apps/senderrormail/";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
