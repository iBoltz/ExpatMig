using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Web;

namespace ExpatMig.Utils.PushNotifications.Notifiers
{
    public class AndroidNotifier : INotifier
    {
        public void SendNotifications(string ApiRegistrationID, string SenderID, string Message)
        {
            //var SENDER_ID = "1036409930929";
            try
            {
                var value = Message;
                var tRequest = WebRequest.Create("https://android.googleapis.com/gcm/send");
                tRequest.Method = "post";
                tRequest.ContentType = " application/x-www-form-urlencoded;charset=UTF-8";
                tRequest.Headers.Add(String.Format("Authorization: key={0}", "AIzaSyCZ0J-1Kq2uQYBFejXkQnC-x2bc05WSdsg"));

                tRequest.Headers.Add(String.Format("Sender: id={0}", SenderID));
                var postData = "collapse_key=score_update&time_to_live=0&delay_while_idle=1&data.message=" +
                 value + "&data.time=" + System.DateTime.Now.ToString() + "&registration_id=" + ApiRegistrationID + "";

                Console.WriteLine(postData);
                var byteArray = Encoding.UTF8.GetBytes(postData);
                tRequest.ContentLength = byteArray.Length;

                var dataStream = tRequest.GetRequestStream();
                dataStream.Write(byteArray, 0, byteArray.Length);
                dataStream.Close();

                var tResponse = tRequest.GetResponse();

                dataStream = tResponse.GetResponseStream();

                var tReader = new StreamReader(dataStream);

                var sResponseFromServer = tReader.ReadToEnd();

                tReader.Close();
                dataStream.Close();
                tResponse.Close();
            }
            catch (Exception)
            {

                //Loghelper.Hand
            }

        }
    }
}