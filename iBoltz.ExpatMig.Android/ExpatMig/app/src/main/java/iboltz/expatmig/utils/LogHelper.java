package iboltz.expatmig.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.EventObject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import iboltz.expatmig.ListenerInterfaces.OnLoadedListener;
import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.utils.AppConstants;
import iboltz.expatmig.utils.WebServiceUrls;
import iboltz.expatmig.facades.ExceptionFacade;

public class LogHelper {
    public static String VersionName;
    private static String Tag;

    protected void onCreate(Bundle savedInstanceState) {

    }

    public static void SendExceptionMail(final String Ex) {
        try {
            String Message = " {\"StackTrace\":\" "
                    + Ex
                    + " \",\"CreatedDate\":\" "
                    + AppConstants.StandardDateFormat
                    .format(new Date())
                    + " \",\"DeviceID\":\" " + AppCache.DeviceId
                    + " \",\"AppCurrentVersion\":\" "
                    + AppCache.AppCurrentVersion + " \"}";

            ExceptionFacade AppEf = new ExceptionFacade();
            AppEf.setOnFinishedEventListener(new OnLoadedListener() {
                @Override
                public void OnLoaded(EventObject e) {
                    //skip result
                }
            });
            AppEf.SendErrorMail(Message);

        } catch (Exception ex) {

        }
    }

    public static void HandleException(Exception ex) {
        try {
            Log.e("MyAppErr", "Error", ex);

            OutputStream buf = new ByteArrayOutputStream();
            PrintStream p = new PrintStream(buf);

            ex.printStackTrace(p);

//            SendExceptionMail(buf.toString());

            buf.close();
            p.close();
        } catch (Exception e) {
            // Log.e(Tag, "Error In Handler", e);
        }
    }

}