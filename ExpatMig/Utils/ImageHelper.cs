

using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Drawing2D;

public class ImageHelper
{


    public ImageHelper() : base()
    {
        // This constructor is used when an object is loaded from a persistent storage.
        // Do not place any code here.			
    }

    public static Image ResizeImage(double scaleFactor, Image SourceImage)
    {
        //scaleFactor = scaleFactor / 100
        dynamic newWidth = Convert.ToInt32(SourceImage.Width * scaleFactor);
        dynamic newHeight = Convert.ToInt32(SourceImage.Height * scaleFactor);

        dynamic thumbnailBitmap = new Bitmap(newWidth, newHeight);
        dynamic thumbnailGraph = Graphics.FromImage(thumbnailBitmap);
        thumbnailGraph.CompositingQuality = CompositingQuality.HighQuality;
        thumbnailGraph.SmoothingMode = SmoothingMode.HighQuality;
        thumbnailGraph.InterpolationMode = InterpolationMode.HighQualityBicubic;

        dynamic imageRectangle = new Rectangle(0, 0, newWidth, newHeight);

        thumbnailGraph.DrawImage(SourceImage, imageRectangle);
        thumbnailGraph.Dispose();
        //thumbnailBitmap.Dispose()
        return thumbnailBitmap;
    }

    public static Bitmap CropImage(Bitmap OriginalImage, Point TopLeft, Point BottomRight)
    {

        Bitmap btmCropped = new Bitmap((BottomRight.X - TopLeft.X), (BottomRight.Y - TopLeft.Y));
        Graphics grpOriginal = Graphics.FromImage(btmCropped);

        grpOriginal.DrawImage(OriginalImage, new Rectangle(0, 0, btmCropped.Width, btmCropped.Height), TopLeft.X, TopLeft.Y, btmCropped.Width, btmCropped.Height, GraphicsUnit.Pixel);
        grpOriginal.Dispose();
        return (btmCropped);
    }


}