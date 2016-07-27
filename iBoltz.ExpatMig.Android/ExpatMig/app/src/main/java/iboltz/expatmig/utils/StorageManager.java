package iboltz.expatmig.utils;

import iboltz.expatmig.utils.LogHelper;
import android.content.Context;
import android.content.SharedPreferences;

public class StorageManager {
	private static final String PrefNameSpace = "iBoltz.Food";

	public static void Put(Context Ctx, String Key, String Value) {
		try {
			SharedPreferences sharedpreferences = Ctx.getSharedPreferences(
					PrefNameSpace, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putString(Key, Value);
			editor.commit();
		} catch (Exception ex) {
			LogHelper.HandleException(ex);
		}
	}

	public static String Get(Context Ctx, String Key) {
		try {
			SharedPreferences sharedpreferences = Ctx.getSharedPreferences(
					PrefNameSpace, Context.MODE_PRIVATE);


			return sharedpreferences.getString(Key, "");
		} catch (Exception ex) {
			LogHelper.HandleException(ex);
		}
		return "";
	}
	public static void ClearData(Context Ctx) {
		try
		{
			SharedPreferences sharedpreferences = Ctx.getSharedPreferences(
					PrefNameSpace, Context.MODE_PRIVATE);

			sharedpreferences.edit().clear().commit();
		}
		catch (Exception ex)
		{
			LogHelper.HandleException(ex);
		}

	}

}
