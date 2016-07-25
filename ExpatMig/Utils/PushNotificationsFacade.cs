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
        public static string SendGcmBrowsers(String ApiRegistrationID)
        {
            var sResponseFromServer = "";

            var tRequest = WebRequest.Create("https://android.googleapis.com/gcm/send");
            tRequest.Method = "post";
            tRequest.ContentType = "application/json";
            tRequest.Headers.Add(String.Format("Authorization: key={0}", "AIzaSyA3D9VafNkYg7SWaX-T0-R34ciBfFsT4WU"));

            var postData = "{\"registration_ids\":[\"" + ApiRegistrationID + "\"]}";
            var byteArray = Encoding.UTF8.GetBytes(postData);
            tRequest.ContentLength = byteArray.Length;

            var dataStream = tRequest.GetRequestStream();
            dataStream.Write(byteArray, 0, byteArray.Length);
            dataStream.Close();

            var tResponse = tRequest.GetResponse();
            dataStream = tResponse.GetResponseStream();

            var tReader = new StreamReader(dataStream);
            sResponseFromServer = tReader.ReadToEnd();

            tReader.Close();
            dataStream.Close();
            tResponse.Close();

            return sResponseFromServer;
        }
    }
}