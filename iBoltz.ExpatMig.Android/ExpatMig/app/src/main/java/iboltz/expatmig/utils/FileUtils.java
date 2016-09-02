package iboltz.expatmig.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils
{
	public FileUtils()
	{
		// TODO Auto-generated constructor stub
	}

	public static void Put(String FileName, String InputText)
	{
		try
		{
			File AppFolder = GetDir();
			if (AppFolder != null)
			{
				File OrderFile = new File(AppFolder, FileName);
				FileOutputStream OrderFileStream = new FileOutputStream(
						OrderFile);
				OrderFileStream.write(InputText.getBytes());
				OrderFileStream.close();

			}
		} catch (FileNotFoundException e)
		{
			LogHelper.HandleException(e);
		} catch (IOException e)
		{
			LogHelper.HandleException(e);
		} catch (Exception e)
		{
			LogHelper.HandleException(e);
		}
	}

	public static String Get(String FileName)
	{
		try
		{
			File AppFolder = GetDir();
			if (AppFolder != null)
			{
				File OrderFile = new File(AppFolder, FileName);

				StringBuilder OutputBuilder = new StringBuilder();
				BufferedReader OrderReader = new BufferedReader(new FileReader(
						OrderFile));
				String EachLine;
				while ((EachLine = OrderReader.readLine()) != null)
				{
					OutputBuilder.append(EachLine);
				}

				OrderReader.close();
				return OutputBuilder.toString();

			}
		} catch (FileNotFoundException e)
		{
			LogHelper.HandleException(e);
		} catch (IOException e)
		{
			LogHelper.HandleException(e);
		} catch (Exception e)
		{
			LogHelper.HandleException(e);
		}
		return "";
	}

	private static File GetDir()
	{
		File MySdCard = Environment.getExternalStorageDirectory();
		File AppFolder = new File(MySdCard.getAbsolutePath() + "/FoodiBoltz");

		if (!AppFolder.exists())
		{
			AppFolder.mkdir();
		}
		return AppFolder;
	}
	public static Bitmap GetBitmapFromFile(File f)
	{
		try
		{
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 480;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true)
			{
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bmTest = BitmapFactory.decodeStream(new FileInputStream(f),
					null, o2);

			return bmTest;
		} catch (FileNotFoundException e)
		{

		} catch (Exception e)
		{
			LogHelper.HandleException(e);
		}
		return null;
	}
}
