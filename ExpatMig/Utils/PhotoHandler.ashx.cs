using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Microsoft.VisualBasic;
using System.Collections;
using System.Data;
using System.Diagnostics;
using System.Data.SqlClient;
using System.IO;
using System.Drawing.Imaging;
using System.Drawing;

using System.Drawing.Drawing2D;
using System.Web.SessionState;

namespace ExpatMig.Utils
{
    /// <summary>
    /// Summary description for PhotoHandler
    /// </summary>
    public class PhotoHandler : IHttpHandler, IReadOnlySessionState
    {
         
        private int? ImageWidth;
        private int? ImageHeight;
        private bool IsThubmNail;
        private double? ScaleRatio;
        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
         
    
        public void ProcessRequest(HttpContext Context)
        {
            Context.Response.Clear();
            Context.Response.ContentType = "image/png";


            HttpCachePolicy c = Context.Response.Cache;
            c.SetCacheability(HttpCacheability.Public);
            c.SetMaxAge(new TimeSpan(10, 0, 0, 0));

            var FromPath = Context.Request.Params["FromPath"];
            var IsThumb = Context.Request.Params["IsThumb"];


            if ((IsThumb == null || string.IsNullOrEmpty(IsThumb)))
            {
                IsThubmNail = false;
            }
            else
            {
                IsThubmNail = true;
            }

            ImageWidth = RequestHelper.GetIntParam("Width");
            ImageHeight = RequestHelper.GetIntParam("Height");

            //Threading.Thread.Sleep(1000)'for debugging loading panel


            if (string.IsNullOrEmpty(FromPath))
                return;

            dynamic PhotoPath = Context.Server.MapPath(FromPath);
            if ((!File.Exists(PhotoPath)))
            {
                FromPath = "~/Images/NoPhotos.png";


                //Return
            }

            string rawIfModifiedSince = Context.Request.Headers.Get("If-Modified-Since");

            dynamic fullSizeImg = System.Drawing.Image.FromFile(PhotoPath);
            dynamic ActualWidth = fullSizeImg.Width;
            dynamic ActualHeight = fullSizeImg.Height;


            if ((ImageWidth == null & ImageHeight == null))
            {
                if ((IsThubmNail))
                {
                    ImageWidth = 240;
                    ImageHeight = 200;
                }
                else
                {
                    ImageWidth = ActualWidth;
                    ImageHeight = ActualHeight;
                }

            }

            dynamic HorizontalResizeRatio = ((double)ImageWidth / ActualWidth);

            dynamic VerticalResizeRatio = HorizontalResizeRatio;
            if ((ImageHeight != null))
            {
                VerticalResizeRatio = ((double)ImageHeight / ActualHeight);
            }


            if ((VerticalResizeRatio == HorizontalResizeRatio))
            {
                //no croping necessary
                ScaleRatio = VerticalResizeRatio;

            }
            else
            {
                //width is longer than height
                if ((HorizontalResizeRatio > VerticalResizeRatio))
                {
                    ScaleRatio = HorizontalResizeRatio;
                    //crop vertically, leave horizontal as it is
                    dynamic NewHeight = ((ActualHeight * VerticalResizeRatio) / HorizontalResizeRatio);
                    dynamic vOffset = (ActualHeight - NewHeight) / 2;
                    fullSizeImg = ImageHelper.CropImage(fullSizeImg, new Point(0, vOffset), new Point(ActualWidth, vOffset + NewHeight));
                }
                else
                {
                    ScaleRatio = VerticalResizeRatio;
                    //crop vertically, leave horizontal as it is
                    dynamic NewWidth = ((ActualWidth * HorizontalResizeRatio) / VerticalResizeRatio);
                    dynamic hOffset = (ActualWidth - NewWidth) / 2;
                    fullSizeImg = ImageHelper.CropImage(fullSizeImg, new Point(hOffset, 0), new Point(hOffset + NewWidth, ActualHeight));

                }
            }
            //--


            if ((IsThubmNail == true))
            {
                RenderThumb(fullSizeImg, Context, PhotoPath);

            }
            else
            {
                dynamic Resized = fullSizeImg;
                if ((ScaleRatio != null))
                {
                    Resized = ImageHelper.ResizeImage(ScaleRatio.Value, fullSizeImg);
                }
                SaveCached(Resized, PhotoPath);
                RenderFull(Resized, Context);
            }


        }


        public void SaveCached(Image SrcImage, string PhotoPath)
        {
            dynamic ThisFolder = string.Empty;

            dynamic ThisFileName = string.Empty;

            dynamic LocalPath = PhotoPath;


            if ((LocalPath.LastIndexOf('\\') > 0))
            {
                ThisFileName = LocalPath.Substring(LocalPath.LastIndexOf('\\') + 1, LocalPath.Length - LocalPath.LastIndexOf('\\') - 1);
                ThisFolder = LocalPath.Remove(LocalPath.LastIndexOf('\\'), LocalPath.Length - LocalPath.LastIndexOf('\\'));
                SrcImage.Save(ThisFolder + "\\" + ImageWidth + "_" + ImageHeight + "_" + ThisFileName);
            }

        }

        public void RenderFull(Image FinalImage, HttpContext Context)
        {
            if ((FinalImage != null))
            {
                dynamic PhotoData = GetBmpBytes(FinalImage);

                Context.Response.OutputStream.Write(PhotoData, 0, PhotoData.Length);
                Context.Response.End();
            }
        }
        private void RenderThumb(Image fullSizeImg, HttpContext Context, string PhotoPath)
        {
            //Create the delegate
            dynamic dummyCallBack = new System.Drawing.Image.GetThumbnailImageAbort(ThumbnailCallback);


            System.Drawing.Image thumbNailImg = default(System.Drawing.Image);
            thumbNailImg = fullSizeImg.GetThumbnailImage(ImageWidth.Value,
                ImageHeight.Value, dummyCallBack, IntPtr.Zero);

            SaveCached(thumbNailImg, PhotoPath);
            dynamic bmpBytes = GetBmpBytes(thumbNailImg);
            Context.Response.OutputStream.Write(bmpBytes, 0, bmpBytes.Length);


            //Context.Response.End()
        }
        private static byte[] GetBmpBytes(System.Drawing.Image thumbNailImg)
        {
            using (var ms = new MemoryStream())
            {


                ImageCodecInfo jpgEncoder = GetEncoder(ImageFormat.Jpeg);

                System.Drawing.Imaging.Encoder myEncoder = System.Drawing.Imaging.Encoder.Quality;
                EncoderParameters myEncoderParameters = new EncoderParameters(1);

                EncoderParameter myEncoderParameter = new EncoderParameter(myEncoder, 60L);
                myEncoderParameters.Param[0] = myEncoderParameter;

                //thumbnailBitmap.Save(ImagePath, jpgEncoder, myEncoderParameters)

                thumbNailImg.Save(ms, jpgEncoder, myEncoderParameters);
                dynamic bmpBytes = ms.GetBuffer();
                thumbNailImg.Dispose();
                ms.Close();
                return bmpBytes;
            }
        }

        private static ImageCodecInfo GetEncoder(ImageFormat format)
        {
            ImageCodecInfo[] codecs = ImageCodecInfo.GetImageDecoders();

            //ImageCodecInfo codec = default(ImageCodecInfo);
            foreach (var codec in codecs)
            {
                if (codec.FormatID == format.Guid)
                {
                    return codec;
                }
            }
            return null;

        }
        public bool ThumbnailCallback()
        {
            return false;
        }
        

    }


     
public class RequestHelper
    {
        public static int? GetIntParam(string ParamName)
        {
            if (string.IsNullOrEmpty(GetStringParam(ParamName)))
            {
                return null;
            }
            else
            {
                return int.Parse(GetStringParam(ParamName));
            }


        }
        public static string GetStringParam(string ParamName)
        {
            return HttpContext.Current.Request.QueryString[ParamName];
        }


    } 









}