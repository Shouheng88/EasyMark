package me.shouheng.sil;

import android.content.Context;
import android.content.res.Resources;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: Utils, v 0.1 2018/11/26 22:21 shouh Exp$
 */
public class Utils {

    final static String SHARED_PREFERENCES_NAME = "__soft_input_layout_preferences";

    final static String PREFERENCES_KEY_KEYBOARD_HEIGHT = "__pref_keyboard_height";

    /**
     * Get the navigation bar height.
     *
     * @param context the context
     * @return the navigation bar height
     */
    static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        try {
            Resources rs = context.getResources();
            int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
            if (id > 0) {
                navigationBarHeight = rs.getDimensionPixelSize(id);
            }
        } catch (Exception e) {
            // default 0
        }
        return navigationBarHeight;
    }
}
