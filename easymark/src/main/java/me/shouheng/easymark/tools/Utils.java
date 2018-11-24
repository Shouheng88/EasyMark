package me.shouheng.easymark.tools;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.EditText;

import me.shouheng.easymark.Constants;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: Utils, v 0.1 2018/11/23 18:59 shouh Exp$
 */
public class Utils {

    /** The space char */
    private final static char CHAR_SPACE = ' ';

    /**
     * Change the dp value to pixel
     *
     * @param context the context
     * @param dp the dp value
     * @return the pixel value
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * Get the start spaces of given string, for example '  xxx' will return '  '
     *
     * @param target the target string
     * @return the start spaces
     */
    public static String getStartSpace(String target) {
        StringBuilder sb = new StringBuilder();
        char[] chars = target.toCharArray();
        for (char c : chars) {
            if (c == CHAR_SPACE) {
                sb.append(CHAR_SPACE);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Tint the drawable in code.
     *
     * @param drawable the drawable to tint
     * @param color the color to use
     * @return the tinted drawable
     */
    public static Drawable tintDrawable(Drawable drawable, @ColorInt int color) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
        return wrappedDrawable;
    }

    /**
     * Tint the drawable in code.
     *
     * @param context the context
     * @param drawableRes the drawable to tint
     * @param color the color to use
     * @return the tinted drawable
     */
    public static Drawable tintDrawable(Context context, @DrawableRes int drawableRes, @ColorInt int color) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        return tintDrawable(drawable, color);
    }

    /**
     * Is the source single line
     *
     * @param source the source string
     * @param selectionStart the selection start
     * @return is single line
     */
    public static boolean isSingleLine(String source, int selectionStart) {
        if (source.isEmpty()) return true;
        source = source.substring(0, selectionStart);
        return source.length() == 0 || source.charAt(source.length() - 1) == Constants.CHAR_ENTER;
    }

    /**
     * Is the source two single lines
     *
     * @param source the source string
     * @param selectionStart the selection start
     * @return is two single lines
     */
    public static boolean isTwoSingleLines(String source, int selectionStart) {
        source = source.substring(0, selectionStart);
        return source.length() >= 2
                && source.charAt(source.length() - 1) == Constants.CHAR_ENTER
                && source.charAt(source.length() - 2) == Constants.CHAR_ENTER;
    }

    /**
     * Insert list symbol
     *
     * @param symbol the symbol of list
     * @param source the source
     * @param selectionStart the start selection
     * @param selectionEnd the end selection
     * @param editor the editor
     */
    public static void insertList(
            String symbol, String source, int selectionStart, int selectionEnd, EditText editor) {
        String substring = source.substring(0, selectionStart);
        int line = substring.lastIndexOf(Constants.CHAR_ENTER);

        selectionStart = line == -1 ? 0 : line + 1;

        substring = source.substring(selectionStart, selectionEnd);

        String[] split = substring.split(Constants.STRING_ENTER);
        StringBuilder sb = new StringBuilder();

        if (split.length > 0) {
            for (String s : split) {
                if (s.length() == 0 && sb.length() != 0) {
                    sb.append(Constants.CHAR_ENTER);
                    continue;
                }
                if (!s.trim().startsWith(symbol)) {
                    if (sb.length() > 0) sb.append(Constants.CHAR_ENTER);
                    sb.append(symbol).append(s);
                } else {
                    if (sb.length() > 0) sb.append(Constants.CHAR_ENTER);
                    sb.append(s);
                }
            }
        }

        if (sb.length() == 0) sb.append(symbol);
        editor.getText().replace(selectionStart, selectionEnd, sb.toString());
        editor.setSelection(sb.length() + selectionStart);
    }
}
