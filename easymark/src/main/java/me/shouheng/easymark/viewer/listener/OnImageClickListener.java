package me.shouheng.easymark.viewer.listener;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: OnImageClickListener, v 0.1 2018/11/24 23:32 shouh Exp$
 */
public interface OnImageClickListener {

    /**
     * The callback when the image of the web page is clicked
     *
     * @param url the url clicked
     * @param urls the urls of all images
     */
    void onImageClick(String url, String[] urls);
}
