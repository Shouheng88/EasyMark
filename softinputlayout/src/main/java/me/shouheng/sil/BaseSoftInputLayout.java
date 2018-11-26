package me.shouheng.sil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION_CODES;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

/**
 * Created on 2018/11/26.
 */
public abstract class BaseSoftInputLayout extends LinearLayout {

    private int mLastHitBottom;
    private int mLastCoverHeight;
    private int mNavigationBarHeight = -1;

    /**
     * Decor view, or current view
     */
    private View rootView;

    public BaseSoftInputLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BaseSoftInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BaseSoftInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = VERSION_CODES.LOLLIPOP)
    public BaseSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Initialize view, for example get attributes from xml, etc. The child can implement this method
     * to add custom behaviors.
     *
     * @param context the context
     * @param attrs the attribute set
     * @param defStyleAttr default style attributes
     * @param defStyleRes default style resources
     */
    protected void doInitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        // Empty, do something to initialize view.
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /* Initialize view */
        doInitView(context, attrs, defStyleAttr, defStyleRes);

        if (context instanceof Activity) {
            rootView = ((Activity) context).getWindow().getDecorView();
        } else {
            rootView = this;
        }

        /* Add layout change observer to the root view. */
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                detectKeyboardState();
            }
        });
    }

    /**
     * Detect the keyboard state when the global layout state changed.
     */
    private void detectKeyboardState() {
        /* Get the visible display frame, used to detect the keyboard height. */
        Rect visibleRect = new Rect();
        rootView.getWindowVisibleDisplayFrame(visibleRect);

        /* Get the focusable size on rootView. */
        Rect hitRect = new Rect();
        rootView.getHitRect(hitRect);

        /* Get the cover height, we will use to judge keyboard state. */
        int coverHeight = hitRect.bottom - visibleRect.bottom;


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mNavigationBarHeight == -1) {
            frame.getLayoutParams().height = getMeasuredHeight();
            mNavigationBarHeight = getNavigationBarHeight(getContext());
        }
    }
}
