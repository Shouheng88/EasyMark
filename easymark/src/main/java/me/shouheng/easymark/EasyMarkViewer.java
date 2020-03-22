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
 * Markdown viewer
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: EasyMarkViewer, v 0.1 2018/11/23 18:56 shouh Exp$
 */
public class EasyMarkViewer extends FastScrollWebView {

    private static final String TAG = "EasyMarkViewer";

    private boolean useMathJax;
    private boolean escapeHtml;

    private String customCss;

    private MdWebViewClient webViewClient;

    private LifecycleListener lifecycleListener;
    private OnUrlClickListener onUrlClickListener;
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

    @RequiresPermission(value = Manifest.permission.INTERNET)
    public void setUseMathJax(boolean useMathJax) {
        this.useMathJax = useMathJax;
    }

    public void setEscapeHtml(boolean escapeHtml) {
        this.escapeHtml = escapeHtml;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
    }

    public final void processMarkdown(String content) {
        if (lifecycleListener != null) {
            lifecycleListener.beforeProcessMarkdown(content);
        }
        new MarkdownParser.Builder(getContext())
                .setEscapeHtml(escapeHtml)
                .setCustomCss(customCss)
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

    public void setOnUrlClickListener(OnUrlClickListener onUrlClickListener) {
        this.onUrlClickListener = onUrlClickListener;
        if (webViewClient != null) {
            webViewClient.setOnUrlClickListener(onUrlClickListener);
        }
    }

    public void setLifecycleListener(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
        if (webViewClient != null) {
            webViewClient.setLifecycleListener(lifecycleListener);
        }
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @JavascriptInterface
    public void showPhotosInGallery(String url, String[] urls) {
        Log.d(TAG, "showPhotosInGallery: " + url);
        if (onImageClickListener != null) {
            onImageClickListener.onImageClick(url, urls);
        }
    }
}
