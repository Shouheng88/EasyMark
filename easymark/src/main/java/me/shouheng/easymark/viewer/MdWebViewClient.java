package me.shouheng.easymark.viewer;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import me.shouheng.easymark.viewer.listener.LifecycleListener;
import me.shouheng.easymark.viewer.listener.OnUrlClickListener;

/**
 * Created by shouh on 2018/3/25. */
public class MdWebViewClient extends WebViewClient {

    private static final String TAG = "MdWebViewClient";

    /**
     * The callback when the page loading action finished
     */
    private LifecycleListener lifecycleListener;

    /**
     * The callback when the url is clicked
     */
    private OnUrlClickListener onUrlClickListener;

    public MdWebViewClient(LifecycleListener finishListener, OnUrlClickListener clickedListener) {
        this.lifecycleListener = finishListener;
        this.onUrlClickListener = clickedListener;
    }

    @Override
    public final void onPageFinished(WebView webView, String str) {
        if (lifecycleListener != null) {
            lifecycleListener.onLoadFinished(webView, str);
        }
    }

    @Override
    public final void onReceivedError(WebView webView, int i, String str, String str2) {
        Log.e(TAG, "onReceivedError :errorCode:" + i + "description:" + str + "failingUrl" + str2);
    }

    @Override
    public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (onUrlClickListener != null) {
            onUrlClickListener.onUrlClick(url);
        }
        return true;
    }

    /**
     * Set the page load finished callback
     *
     * @param lifecycleListener the callback
     */
    public void setLifecycleListener(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
    }

    /**
     * Set the url click event callback
     *
     * @param onUrlClickListener the url click event callback
     */
    public void setOnUrlClickListener(OnUrlClickListener onUrlClickListener) {
        this.onUrlClickListener = onUrlClickListener;
    }
}
