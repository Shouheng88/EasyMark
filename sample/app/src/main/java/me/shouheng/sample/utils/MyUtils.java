package me.shouheng.sample.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import me.shouheng.easymark.tools.Utils;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: MyUtils, v 0.1 2018/11/24 19:32 shouh Exp$
 */
public class MyUtils {

    public static void themeMenu(Menu menu, boolean isDarkMode) {
        int size = menu.size();
        for (int i=0; i<size; i++) {
            MenuItem menuItem = menu.getItem(i);
            Drawable icon = menuItem.getIcon();
            if (icon != null) {
                menuItem.setIcon(Utils.tintDrawable(icon, isDarkMode ? Color.WHITE : Color.BLACK));
            }
        }
    }
}
