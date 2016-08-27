
using Microsoft.VisualBasic;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.Web;
using System.Web.Services;
using System.IO;
using System.Text;
using System.Drawing.Imaging;

namespace ExpatMig.Utils
{
    public class MultipleImageHandler : System.Web.IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            StringBuilder OutputWriter = new StringBuilder();


            if ((context.Request.Files.Count > 0))
            {

                dynamic UploadedFiles = context.Request.Files;
                foreach (var UploadedImageName_loopVariable in UploadedFiles)
                {
                    var UploadedImageName = UploadedImageName_loopVariable;
                    HttpPostedFile PhotoFile = UploadedFiles[UploadedImageName];

                    dynamic SavedPath = SavePhoto(PhotoFile, context);
                    OutputWriter.AppendLine(SavedPath);
                }
                context.Response.ContentType = "text/plain";
                context.Response.Write(OutputWriter.ToString());
            }

        }

        public bool IsReusable
        {
            get { return false; }
        }

        private string SavePhoto(HttpPostedFile PhotoFile, HttpContext context)
        {
            dynamic FolderPath = "~/UploadedImages/" + DateTime.Now.Year + "/" +
                DateTime.Now.Month + "/" + DateTime.Now.Day + "/";

            if ((!Directory.Exists(FolderPath)))
                Directory.CreateDirectory(context.Server.MapPath(FolderPath));
            dynamic FilePath = FolderPath + DateTime.Now.Ticks + PhotoFile.FileName.Remove(0, PhotoFile.FileName.LastIndexOf("."));

            using (var Resized = ResizePhoto(PhotoFile))
            {

                var jpgEncoder = GetEncoder(ImageFormat.Jpeg);

                var myEncoder = System.Drawing.Imaging.Encoder.Quality;
                var myEncoderParameters = new EncoderParameters(1);

                var myEncoderParameter = new EncoderParameter(myEncoder, 60L);
                myEncoderParameters.Param[0] = myEncoderParameter;


                Resized.Save(context.Server.MapPath(FilePath), jpgEncoder, myEncoderParameters);
                Resized.Dispose();

                //Resized.Save(context.Server.MapPath(FilePath));
            }


            return FilePath;
        }
        private ImageCodecInfo GetEncoder(ImageFormat format)
        {
            var codecs = ImageCodecInfo.GetImageDecoders();


            foreach (var codec in codecs)
            {
                if (codec.FormatID == format.Guid)
                {
                    return codec;
                }
            }
            return null;


        }
        private System.Drawing.Image ResizePhoto(HttpPostedFile fluPhoto)
        {
            if (fluPhoto == null)
                return null;
            dynamic MaximumResolution = 1024;
            //Dim ImageMemStream = New System.IO.MemoryStream.r()
            dynamic UploadedImage = System.Drawing.Image.FromStream(fluPhoto.InputStream);
            if ((UploadedImage == null))
                return null;
            dynamic ActualWidth = UploadedImage.Width;
            if ((ActualWidth <= MaximumResolution))
                return UploadedImage;

            dynamic HorizontalResizeRatio = ((double)MaximumResolution / ActualWidth);



            return ImageHelper.ResizeImage(HorizontalResizeRatio, UploadedImage);


        }
    }
}