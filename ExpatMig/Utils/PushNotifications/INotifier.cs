using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ExpatMig.Utils.PushNotifications
{
    public interface INotifier
    {
        void SendNotifications(string ApiRegistrationID, string SenderID, string Message);
    }
}