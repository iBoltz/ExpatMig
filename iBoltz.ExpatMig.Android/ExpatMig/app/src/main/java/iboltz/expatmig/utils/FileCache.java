package iboltz.expatmig.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCache {

    private File cacheDir;
    private Context CurrentContext;

    public FileCache(Context context) {
        cacheDir = GetCacheDir(context);
    }

    public File GetCacheDir(Context context) {
        try {
// Find the dir to save cached images
            if (android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)) {
                cacheDir = new File(
                        android.os.Environment.getExternalStorageDirectory(),
                        "iBoltzLazyList");

                CurrentContext = context;

            } else {
                cacheDir = context.getCacheDir();
            }

            if (!cacheDir.exists()) {
                cacheDir.mkdirs();

            } else {

            }

        } catch (Exception ex) {
            LogHelper.HandleException(ex);
        }
        return cacheDir;
    }

    public void PutImage(String url, Bitmap ToCache) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.

        String filename = url.replaceAll("\\W+", "");

        // Another possible solution (thanks to grantland)
        // String filename = URLEncoder.encode(url);
//        File f = new File(cacheDir, filename);
//        if (!f.exists()) {
        try {
            File f = new File(cacheDir, filename);
            //     f.createNewFile();
            FileOutputStream outputStream;

            outputStream = new FileOutputStream(f);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ToCache.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();


            outputStream.write(byteArray);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.HandleException(e);
        }
//        }

//        return f;

    }

    public File getFile(String url) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.

        String filename = url.replaceAll("\\W+", "");

        // Another possible solution (thanks to grantland)
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        if (!f.exists()) {
            try {
                f.createNewFile();

            } catch (IOException e) {

                LogHelper.HandleException(e);

            }
        }

        return f;

    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}