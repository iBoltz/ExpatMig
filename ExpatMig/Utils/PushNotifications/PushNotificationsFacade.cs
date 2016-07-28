using ExpatMig.Models;
using ExpatMig.Utils.PushNotifications;
using ExpatMig.Utils.PushNotifications.Notifiers;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web;

namespace ExpatMig.Utils
{
    public class PushNotificationsFacade
    {
        public static void SendNotification(UserDevice ThatDevice, String Message)
        {
            var SenderID = "1036409930929";
            INotifier Notififier;
            switch(ThatDevice.DeviceTypeID)
            {
                case (int)AppConstants.DeviceTypes.AndroidMobile:
                    Notififier = new AndroidNotifier();

                    break;
                case (int)AppConstants.DeviceTypes.ChromeBrowser:
                    Notififier = new BrowserNotifier();

                    break;
                default:
                    Notififier = new BrowserNotifier();
                    break;
                    

            }
            Notififier.SendNotifications(ThatDevice.ApiRegistrationID, SenderID, Message);

        }
    }
}