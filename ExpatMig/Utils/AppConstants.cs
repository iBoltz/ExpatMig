using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ExpatMig.Utils
{
    public class AppConstants
    {
        public enum DeviceTypes
        {
            AndroidMobile=1,
            ChromeBrowser=2

        }
        public class NotificationTypes
        {
            public static string Groups = "groups";
            public static string Threads = "threads";
            public static string Topics = "topics";

        }

        public class AttachmentTypes
        {
            public static string Images = "image/jpeg";

        }
    }
}