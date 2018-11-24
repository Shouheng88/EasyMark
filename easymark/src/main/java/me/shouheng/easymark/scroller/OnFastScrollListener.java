package me.shouheng.easymark.scroller;

import android.view.View;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: OnFastScrollListener, v 0.1 2018/11/23 19:06 shouh Exp$
 */
public interface OnFastScrollListener {

    void onFastScrollStart(View view, FastScrollDelegate delegate);

    void onFastScrolled(View view, FastScrollDelegate delegate, int touchDeltaY, int viewScrollDeltaY, float scrollPercent);

    void onFastScrollEnd(View view, FastScrollDelegate delegate);
}