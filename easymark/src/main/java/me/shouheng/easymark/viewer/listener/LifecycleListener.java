package me.shouheng.easymark.viewer.listener;

import android.webkit.WebView;

/**
 * Markdown viewer life cycle state callback.
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: LifecycleListener, v 0.1 2018/11/24 20:45 shouh Exp$
 */
public interface LifecycleListener {

    /**
     * Callback when WebView load finished
     */
    void onLoadFinished(WebView webView, String str);

    /**
     * Lifecycle method, will be called before process markdown content
     *
     * @param content the markdown text
     */
    void beforeProcessMarkdown(String content);

    /**
     * After the markdown content parsed and got the document
     *
     * @param document html document
     */
    void afterProcessMarkdown(String document);
}
