package me.shouheng.easymark;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import me.shouheng.easymark.scroller.FastScrollWebView;
import me.shouheng.easymark.viewer.MdWebViewClient;
import me.shouheng.easymark.viewer.listener.LifecycleListener;
import me.shouheng.easymark.viewer.listener.OnImageClickListener;
import me.shouheng.easymark.viewer.listener.OnUrlClickListener;
import me.shouheng.easymark.viewer.parser.MarkdownParser;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: EasyMarkViewer, v 0.1 2018/11/23 18:56 shouh Exp$
 */
public class EasyMarkViewer extends FastScrollWebView {

    private static final String TAG = "EasyMarkViewer";

    public final static int LIGHT_STYLE_CSS = 0;

    public final static int DARK_STYLE_CSS = 1;

    /**
     * Whether use MathJax
     */
    private boolean useMathJax;

    /**
     * Whether escape html
     */
    private boolean escapeHtml;

    /**
     * The styled css id, should be one of {@link #DARK_STYLE_CSS} and {@link #LIGHT_STYLE_CSS}
     */
    private int styleCssId;

    /**
     * The styled custom css content
     */
    private String customCssContent;

    /**
     * WebView client
     */
    private MdWebViewClient webViewClient;

    /**
     * The callback for the markdown viewer lifecycle
     */
    private LifecycleListener lifecycleListener;

    /**
     * The callback when the image clicked
     */
    private OnUrlClickListener onUrlClickListener;

    /**
     * The callback when the image on the page is clicked
     */
    private OnImageClickListener onImageClickListener;

    public EasyMarkViewer(Context context) {
        super(context);
        init(context, null, -1, -1);
    }

    public EasyMarkViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1, -1);
    }

    public EasyMarkViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle, -1);
    }

    public EasyMarkViewer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (Build.VERSION.SDK_INT >= 21) WebView.enableSlowWholeDocumentDraw();
        this.getSettings().setJavaScriptEnabled(true);
        this.setVerticalScrollBarEnabled(true);
        this.setHorizontalScrollBarEnabled(false);
        this.addJavascriptInterface(this, "jsCallback");
        this.webViewClient = new MdWebViewClient(lifecycleListener, onUrlClickListener);
        this.setWebViewClient(webViewClient);
    }

    /**
     * Whether MathJax should be used
     *
     * @param useMathJax true to use MathJax
     */
    @RequiresPermission(value = Manifest.permission.INTERNET)
    public void setUseMathJax(boolean useMathJax) {
        this.useMathJax = useMathJax;
    }

    /**
     * Set whether should escape html
     *
     * @param escapeHtml whether escape html
     */
    public void setEscapeHtml(boolean escapeHtml) {
        this.escapeHtml = escapeHtml;
    }

    /**
     * Set the styled css id, should be one of {@link #LIGHT_STYLE_CSS} and {@link #DARK_STYLE_CSS}
     * To use the styled css, you must call this method before trying to parse the content
     *
     * @param styleCssId the style css id
     */
    public void useStyleCss(int styleCssId) {
        this.styleCssId = styleCssId;
    }

    /**
     * Set the custom css content
     *
     * @param customCssContent the custom css content
     */
    public void setCustomCssContent(String customCssContent) {
        this.customCssContent = customCssContent;
    }

    /**
     * Process the markdown content
     *
     * @param content the markdown content
     */
    public final void processMarkdown(String content) {
        if (lifecycleListener != null) {
            lifecycleListener.beforeProcessMarkdown(content);
        }
        new MarkdownParser.Builder(getContext())
                .setEscapeHtml(escapeHtml)
                .setStyleCssId(styleCssId)
                .setCustomCssContent(customCssContent)
                .setOnGetResultListener(new MarkdownParser.OnGetResultListener() {
                    @Override
                    public void onGetResult(String html) {
                        if (lifecycleListener != null) {
                            lifecycleListener.afterProcessMarkdown(html);
                        }
                        loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                    }
                })
                .setUseMathJax(useMathJax)
                .build().execute(content);
    }

    /**
     * Set the url click event handler
     *
     * @param onUrlClickListener the url click listener
     */
    public void setOnUrlClickListener(OnUrlClickListener onUrlClickListener) {
        this.onUrlClickListener = onUrlClickListener;
        if (webViewClient != null) {
            webViewClient.setOnUrlClickListener(onUrlClickListener);
        }
    }

    /**
     * Set the life cycle listener
     *
     * @param lifecycleListener the life cycle listener
     */
    public void setLifecycleListener(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
        if (webViewClient != null) {
            webViewClient.setLifecycleListener(lifecycleListener);
        }
    }

    /**
     * Set the image click event listener
     *
     * @param onImageClickListener image click event listener
     */
    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    /**
     * The js callback when the image is clicked
     *
     * @param url the url of clicked image
     * @param urls urls of all images in the web page
     */
    @JavascriptInterface
    public void showPhotosInGallery(String url, String[] urls) {
        Log.d(TAG, "showPhotosInGallery: " + url);
        if (onImageClickListener != null) {
            onImageClickListener.onImageClick(url, urls);
        }
    }
}
