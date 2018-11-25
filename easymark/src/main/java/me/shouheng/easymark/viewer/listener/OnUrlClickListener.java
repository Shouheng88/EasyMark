package me.shouheng.easymark.viewer.listener;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: OnUrlClickListener, v 0.1 2018/11/24 20:49 shouh Exp$
 */
public interface OnUrlClickListener {

    /**
     * Callback when url is clicked
     *
     * @param url the url of an attachment or web page
     */
    void onUrlClick(String url);
}
