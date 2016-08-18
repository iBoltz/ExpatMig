package iboltz.expatmig.utils;

public class    WebServiceUrls {
    public static final String WebServiceUrl = "192.168.1.105:125";

    //Verify Update - But not used
    public static final String AppVersionSyncService = "https://192.168.1.105:125/apps/syncversion/";


    //Start Up
    public static final String UserLoginService = "https://192.168.1.105:125/api/useraccess/login";
    public static final String UserCreationByDeviceService = "https://192.168.1.105:125/usersbyDeviceID/getorcreateuser";
    public static final String GetAccessTokenWithUser = "https://192.168.1.105:125/users/getaccesstokenwithuser/";
    public static final String GetRestaurantByUserID = "https://192.168.1.105:125/restaurants/getRestaurantByUserID/";
    public static final String GcmDeviceRegistration = "https://192.168.1.105:125/api/userdevices";

   //Chat Page
   public static final String CreateTopics = "https://192.168.1.105:125/api/Topics";
    public static final String GetTopicsByThreadID = "https://192.168.1.105:125/api/Topics/AllTopicsForThisThread/";

    //Group Page
    public static final String ListGroups="https://192.168.1.105:125/api/Groups";
    public static final String GetThreadsByGroupID="https://192.168.1.105:125/api/Threads/GetThreadsByGroupID/";
    public static final String SaveNewGroup="https://192.168.1.105:125/api/Groups";
    public static final String SaveNewThread="https://192.168.1.105:125/api/Threads";


    //My Profile
    public static final String UserUpdationService = "https://192.168.1.105:125/users/updateuser/";
    public static final String UpdateUserDetails = "https://192.168.1.105:125/users/UpdateuserDetail/";

    //Settings Page
    public static final String HandShakeGCM = "https://192.168.1.105:125/orders/handshakegcm/";
    public static final String GetTestService = "https://192.168.1.105:125/users/testservice/";

    //Log Helper
     public static final String SendErrorEmail = "https://192.168.1.105:125/apps/senderrormail/";


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
