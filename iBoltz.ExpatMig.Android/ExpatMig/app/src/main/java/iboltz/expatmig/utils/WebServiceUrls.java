package iboltz.expatmig.utils;

public class WebServiceUrls {
    public static final String WebServiceUrl = "expatmig.iboltz.com";
    public static final String WebSiteUrl = "food.iboltz.com";

    //Verify Update - But not used
    public static final String AppVersionSyncService = "https://expatmig.iboltz.com/apps/syncversion/";


    //Start Up
    public static final String UserLoginService = "https://expatmig.iboltz.com/api/useraccess/login";
    public static final String UserCreationByDeviceService = "https://expatmig.iboltz.com/usersbyDeviceID/getorcreateuser";
    public static final String GetAccessTokenWithUser = "https://expatmig.iboltz.com/users/getaccesstokenwithuser/";
    public static final String GetRestaurantByUserID = "https://expatmig.iboltz.com/restaurants/getRestaurantByUserID/";
    public static final String GcmDeviceRegistration = "https://expatmig.iboltz.com/api/userdevices";

   //Chat Page
   public static final String CreateTopics = "https://expatmig.iboltz.com/api/Topics";
    public static final String GetTopicsByThreadID = "https://expatmig.iboltz.com/api/Topics/AllTopicsForThisThread/";

    //Group Page
    public static final String ListGroups="https://expatmig.iboltz.com/api/Groups";
    public static final String GetThreadsByGroupID="https://expatmig.iboltz.com/api/Threads/GetThreadsByGroupID/";
    public static final String SaveNewGroup="https://expatmig.iboltz.com/api/Groups";
    public static final String SaveNewThread="https://expatmig.iboltz.com/api/Threads";


    //My Profile
    public static final String UserUpdationService = "https://expatmig.iboltz.com/users/updateuser/";
    public static final String UpdateUserDetails = "https://expatmig.iboltz.com/users/UpdateuserDetail/";

    //Settings Page
    public static final String HandShakeGCM = "https://expatmig.iboltz.com/orders/handshakegcm/";
    public static final String GetTestService = "https://expatmig.iboltz.com/users/testservice/";

    //Log Helper
     public static final String SendErrorEmail = "https://expatmig.iboltz.com/apps/senderrormail/";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
