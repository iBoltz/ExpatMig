package iboltz.expatmig.utils;

/**
 * Created by pons on 29-Nov-15.
 */

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import org.w3c.dom.Text;

public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;
    private int TextColor = -1;

    public CustomTypefaceSpan(String family, Typeface type) {
        super(family);
        newType = type;
        TextColor = -1;
    }

    public CustomTypefaceSpan(String family, Typeface type, int TextColor) {
        super(family);
        newType = type;
        this.TextColor = TextColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }

    private void applyCustomTypeFace(Paint paint, Typeface tf) {
        try
        {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }


            paint.setColor(TextColor);


            paint.setTypeface(tf);
        }
        catch(Exception ex)
        {
            LogHelper.HandleException(ex);
        }

    }
}