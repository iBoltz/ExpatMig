package iboltz.expatmig.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

import iboltz.expatmig.utils.AppConstants;

public class DateUtils {

    public static String ConvertToLocalDate(String SourceDate,
                                            String SourceTimeZone) {
        try {

            AppConstants.StandardDateFormat.setTimeZone(TimeZone
                    .getTimeZone(SourceTimeZone));
            Date date = null;
            try {
                date = AppConstants.StandardDateFormat.parse(SourceDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TimeZone destTz = TimeZone.getDefault();
            Log.d("Test", "" + destTz);
            AppConstants.StandardDateFormat.setTimeZone(destTz);
            String result = AppConstants.StandardDateFormat.format(date);
            return result;
        } catch (Exception e) {
            LogHelper.HandleException(e);
        }
        return "";
    }

    public static String ConvertToServerDate(String SourceDate,
                                             String DestinationTimeZone) {
        try {
            TimeZone SourceTimeZone = TimeZone.getDefault();
            AppConstants.StandardDateFormat.setTimeZone(SourceTimeZone);
            Date SourceDateFormated = AppConstants.StandardDateFormat
                    .parse(SourceDate);

            AppConstants.StandardDateFormat.setTimeZone(TimeZone
                    .getTimeZone(DestinationTimeZone));

            String result = AppConstants.StandardDateFormat
                    .format(SourceDateFormated);

            return result;
        } catch (Exception e) {
            LogHelper.HandleException(e);
        }
        return "";
    }

    public static String DateConversion(String dateString)
            throws ParseException {
        try
        {
// "dd MMM yyyy  HH:mm"
            Date ParsedDate = AppConstants.JsonDateFormat.parse(dateString.trim());

            String LocalClockTime = AppConstants.StandardDateFormat
                    .format(ParsedDate);

            String LocalTime = ConvertToLocalDate(LocalClockTime,
                    AppConstants.ServerTimeZone);

            Log.d("Test", "LocalTime :" + LocalTime);
            return LocalTime;
        }
        catch(Exception ex)
        {
            LogHelper.HandleException(ex);
        }
        return null;
    }


    public static Date AddDays(Date date, int days) {
        try
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, days); //minus number would decrement the days
            return cal.getTime();
        }
        catch(Exception ex)
        {
            LogHelper.HandleException(ex);
        }
        return null;
    }

    public static String DisplayDate(Date InputDate) {
        try
        {
            return AppConstants.DisplayDateFormat
                    .format(InputDate);
        }
        catch(Exception ex)
        {
            LogHelper.HandleException(ex);
        }
        return null;
    }

    public static String DisplayDate(String InputDate) {
        Date ParsedDate = null;
        try {
            ParsedDate = AppConstants.JsonDateFormat.parse(InputDate.trim());
        } catch (ParseException e) {
            LogHelper.HandleException(e);
            e.printStackTrace();
        }
        if (ParsedDate == null) return "";

        return AppConstants.DisplayDateFormat
                .format(ParsedDate);
    }
}
