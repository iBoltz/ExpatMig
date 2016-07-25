using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace iBoltz.ExpatMig.TestApp
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            //https://android.googleapis.com/gcm/send/cbB0mpWWwWQ:APA91bEgEjqatDW0TJaI6m_xkHmBJ5Truvpx7YtMmthiLv19iij9ugvGFyvUp0EmZX2urX_a3Vu4Z5euzx09Sv2aZa4-rPJ3atKm1G9pAmhyXVLp-GdQlwpoMSAFBlKCZMyS-HdOuheg
            
            var DeviceID = "f4f0ZGKMDsk:APA91bF1Aug_hdyBobTTMdp-YnIR2Uyvw0FnO3LL-HWAvVgAgZEtBuYtomMuKd4FhYFbjAlluKSyum9FyDlCFr8g8hUV7gtR9ElaXNXTgDHvCSheeu6isxU1KUpH-KEHHrULGUqVkASt";
            var Response = SendGcm(DeviceID, "Hello");
            MessageBox.Show(Response);

        }
        public string SendGcm(String DeviceId, String Message)
        {
            //'Dim GoogleAppID As String = "google application id"
            var sResponseFromServer = "";
            //1036409930929
            //expatmigchat
            var SENDER_ID = "1036409930929";
            var value = Message;
            var tRequest = WebRequest.Create("https://android.googleapis.com/gcm/send");
            tRequest.Method = "post";
            tRequest.ContentType = "application/json";
            tRequest.Headers.Add(String.Format("Authorization: key={0}", "AIzaSyA3D9VafNkYg7SWaX-T0-R34ciBfFsT4WU"));

            //tRequest.Headers.Add(String.Format("Sender: id={0}", SENDER_ID));
            var postData = "{\"registration_ids\":[\""+ DeviceId +"\"]}";

            Console.WriteLine(postData);
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
