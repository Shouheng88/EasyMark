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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * Base soft input layout.
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: BaseSoftInputLayout, v 0.1 2018/11/26 22:36 shouh Exp$
 */
public abstract class BaseSoftInputLayout extends FrameLayout {

    private View rootView;
    private View frame;
    private View container;
    private EditText editText;

    /**
     * If you want to add a control panel to the container, add it the layout and set the inner
     * panel height using {@link #setOverHeight(int)}. The control panel will be managed by the
     * base soft input layout automatically.
     */
    private int overHeight;
    private boolean keyboardShowing;
    private int navigationBarHeight = -1;
    private int keyboardHeight;
    @SILState
    private int silState;
    private int lastHitBottom;
    private int lastCoverHeight;
    private int hiddenHeight;

    private List<OnKeyboardStateChangeListener> onKeyboardStateChangeListeners;

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
    protected abstract void doInitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes);

    /**
     * Get the layout frame view.
     *
     * @return the frame view
     */
    protected abstract View getFrame();

    /**
     * Get the container view.
     *
     * @return the container view
     */
    protected abstract View getContainer();

    /**
     * Method for child to implement to get the associated EditText.
     *
     * @return the associated EditText
     */
    protected abstract EditText getEditText();

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /* Config the keyboard state change listeners. */
        this.onKeyboardStateChangeListeners = new LinkedList<>();
        this.onKeyboardStateChangeListeners.add(new OnKeyboardStateChangeListener() {
            @Override
            public void onShown(int height) {
                if (silState == SILState.AUTOMATIC) {
                    showContainer();
                }
                silState = SILState.AUTOMATIC;
            }

            @Override
            public void onHidden(int height) {
                if (silState == SILState.AUTOMATIC) {
                    hideContainer();
                }
                silState = SILState.AUTOMATIC;
            }
        });

        /* Initialize view */
        doInitView(context, attrs, defStyleAttr, defStyleRes);

        /* Get the views from the child implementation. */
        frame = getFrame();
        container = getContainer();
        editText = getEditText();

        /* Get the root view. */
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
        /* Get the visible size of root view. */
        Rect visibleRect = new Rect();
        rootView.getWindowVisibleDisplayFrame(visibleRect);

        /* Get size of root view. */
        Rect hitRect = new Rect();
        rootView.getHitRect(hitRect);

        /* Get the cover height, we will use to judge keyboard state. */
        int coverHeight = hitRect.bottom - visibleRect.bottom;

        /* Fix show/hide navigation bar for MeiZu */
        if (lastCoverHeight == coverHeight && lastHitBottom == hitRect.bottom) {
            return;
        }

        /* Keep last values */
        lastHitBottom = hitRect.bottom;
        int deltaCoverHeight = coverHeight - lastCoverHeight;
        lastCoverHeight = coverHeight;

        if (coverHeight > navigationBarHeight) {
            /* Fix show/hide navigation bar for HuaWei */
            if ((deltaCoverHeight == navigationBarHeight
                    || deltaCoverHeight == -navigationBarHeight) && keyboardShowing) {
                hiddenHeight += deltaCoverHeight;
            }
            int shownHeight = coverHeight - hiddenHeight;

            /* Calculate the height of the container. */
            if (keyboardHeight != shownHeight) {
                keyboardHeight = shownHeight;
                container.getLayoutParams().height = shownHeight + overHeight;
                container.requestLayout();
            }

            /* Keyboard state callback */
            if (!onKeyboardStateChangeListeners.isEmpty()) {
                for (OnKeyboardStateChangeListener listener : onKeyboardStateChangeListeners) {
                    listener.onShown(hiddenHeight);
                }
            }

            keyboardShowing = true;

            refreshFrameLayout(visibleRect.bottom + shownHeight);
        } else {
            /* Fix show/hide navigation bar for HuaWei */
            if ((deltaCoverHeight == navigationBarHeight
                    || deltaCoverHeight == -navigationBarHeight) && !keyboardShowing) {
                hiddenHeight += deltaCoverHeight;
            }

            if (coverHeight != hiddenHeight) {
                hiddenHeight = coverHeight;
            }

            /* Keyboard state callback */
            if (!onKeyboardStateChangeListeners.isEmpty()) {
                for (OnKeyboardStateChangeListener listener : onKeyboardStateChangeListeners) {
                    listener.onHidden(hiddenHeight);
                }
            }

            keyboardShowing = false;

            refreshFrameLayout(visibleRect.bottom);
        }
    }

    /**
     * Refresh the layout of {@link #frame}. This method is used to config the height of the frame,
     * that is, the height of the parent of the whole view.
     *
     * @param bottom the bottom
     */
    private void refreshFrameLayout(int bottom) {
        /* Get the focusable size of frame.. */
        Rect rect = new Rect();
        frame.getHitRect(rect);

        /* Get the coordinate x, y of the frame */
        int[] location = new int[2];
        frame.getLocationInWindow(location);
        int height = bottom - rect.top - location[1];

        /* Refresh the height of the frame */
        if (height != frame.getLayoutParams().height) {
            frame.getLayoutParams().height = height;
            frame.requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (navigationBarHeight == -1) {
            frame.getLayoutParams().height = getMeasuredHeight();
            navigationBarHeight = SILUtils.getNavigationBarHeight(getContext());
        }
    }

    public void showSoftInput() {
        if(editText == null) return;
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public void hideSoftInput() {
        if(editText == null) return;
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showSoftInputOnly() {
        silState = SILState.SHOW_SOFT_INPUT_ONLY;
        showSoftInput();
    }

    public void hideSoftInputOnly() {
        silState = SILState.HIDE_SOFT_INPUT_ONLY;
        hideSoftInput();
    }

    public void showContainer() {
        if (container != null) {
            container.setVisibility(VISIBLE);
        }
    }

    public void hideContainer() {
        if (container != null) {
            container.setVisibility(GONE);
        }
    }

    public void setOverHeight(int overHeight) {
        this.overHeight = overHeight;
    }

    public boolean isKeyboardShowing() {
        return keyboardShowing;
    }

    public int getKeyboardHeight() {
        return keyboardHeight;
    }

    /**
     * Add the keyboard state change listener to the list.
     *
     * @param onKeyboardStateChangeListener the keyboard state change listener
     */
    public void addOnKeyboardStateChangeListener(OnKeyboardStateChangeListener onKeyboardStateChangeListener) {
        this.onKeyboardStateChangeListeners.add(onKeyboardStateChangeListener);
    }

    public interface OnKeyboardStateChangeListener {
        /**
         * Called when the keyboard is showing
         *
         * @param height the height of keyboard
         */
        void onShown(int height);

        /**
         * Called when the keyboard is hidden
         *
         * @param height the height of hidden height
         */
        void onHidden(int height);
    }
}
