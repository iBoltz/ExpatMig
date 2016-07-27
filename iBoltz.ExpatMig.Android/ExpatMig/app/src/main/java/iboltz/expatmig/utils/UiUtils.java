package iboltz.expatmig.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.Button;
import android.widget.Toast;

import iboltz.expatmig.utils.AppCache;
import iboltz.expatmig.R;

public class UiUtils {
    public static void ShowToast(Activity CurrentActivity, String Message) {
        Toast.makeText(CurrentActivity.getApplicationContext(), Message, Toast.LENGTH_LONG).show();
    }

    public static void SetIboltzButton(Context CurrentContext, Button btnInput) {
        String InputText = (String) btnInput.getText();

        int Color1 = CurrentContext.getResources().getColor(R.color.iBoltzOrange);
        int Color2 = CurrentContext.getResources().getColor(R.color.iBoltzWhite);

        SpannableStringBuilder RichText = new SpannableStringBuilder(InputText);
        RichText.setSpan(new CustomTypefaceSpan("", AppCache.IonIcons, Color1), 0, 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        RichText.setSpan(new CustomTypefaceSpan("", AppCache.FontQuickRegular, Color2), 1, InputText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        btnInput.setText(RichText);
    }

    public static void ShowDialog(Activity CurrentActivity, String Message) {
        AlertDialog alertDialog = new AlertDialog.Builder(CurrentActivity).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(Message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static class DisplayInfo {
        public int Width;
        public int Height;
        public float WidthDp;
        public float HeightDp;
        public int Orientation ;

    }

public static DisplayInfo GetDisplayInfo(Context CurrentContext)
{
    DisplayInfo ThisDisplay = new DisplayInfo();
    DisplayMetrics displayMetrics = CurrentContext.getResources().getDisplayMetrics();

    ThisDisplay.Height = displayMetrics.heightPixels ;
    ThisDisplay.Width= displayMetrics.widthPixels ;
    ThisDisplay.HeightDp = displayMetrics.heightPixels / displayMetrics.density;
    ThisDisplay.WidthDp= displayMetrics.widthPixels / displayMetrics.density;
    ThisDisplay.Orientation =CurrentContext.getResources().getConfiguration().orientation;
    return ThisDisplay;
}

}
