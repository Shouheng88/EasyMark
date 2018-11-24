package me.shouheng.easymark.scroller;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: FastScrollable, v 0.1 2018/11/23 19:05 shouh Exp$
 */
public interface FastScrollable {

    void superOnTouchEvent(MotionEvent event);

    int superComputeVerticalScrollExtent();

    int superComputeVerticalScrollOffset();

    int superComputeVerticalScrollRange();

    View getFastScrollableView();

    FastScrollDelegate getFastScrollDelegate();

    void setNewFastScrollDelegate(FastScrollDelegate newDelegate);
}
