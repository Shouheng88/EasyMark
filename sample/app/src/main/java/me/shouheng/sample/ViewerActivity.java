package me.shouheng.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import me.shouheng.easymark.EasyMarkViewer;
import me.shouheng.easymark.tools.Utils;
import me.shouheng.easymark.viewer.listener.LifecycleListener;
import me.shouheng.easymark.viewer.listener.OnImageClickListener;
import me.shouheng.easymark.viewer.listener.OnUrlClickListener;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: ViewerActivity, v 0.1 2018/11/25 12:22 shouh Exp$
 */
public class ViewerActivity extends AppCompatActivity {

    private static final String TAG = "ViewerActivity";

    public final static String EXTRA_KEY_NOTE_CONTENT = "__extra_key_note_content";

    private final static boolean isDarkTheme = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("EasyMarkViewer");
        }

        String noteContent = getIntent().getStringExtra(EXTRA_KEY_NOTE_CONTENT);

        EasyMarkViewer emv = findViewById(R.id.emv);
        /* Set the fast scroller */
        emv.getFastScrollDelegate().setThumbDrawable(getResources().getDrawable(isDarkTheme ?
                R.drawable.fast_scroll_bar_dark : R.drawable.fast_scroll_bar_light));
        emv.getFastScrollDelegate().setThumbSize(16, 40);
        emv.getFastScrollDelegate().setThumbDynamicHeight(false);

        /* Set should use MathJax */
        emv.setUseMathJax(true);
        /* Set should escape html */
        emv.setEscapeHtml(false);
        /* Set the image url click listener */
        emv.setOnImageClickListener(new OnImageClickListener() {
            @Override
            public void onImageClick(String url, String[] urls) {
                Log.d(TAG, "onImageClick: " + url);
                Log.d(TAG, "onImageClick: " + urls);
                Toast.makeText(ViewerActivity.this, url, Toast.LENGTH_SHORT).show();
            }
        });
        /* Set the url click listener */
        emv.setOnUrlClickListener(new OnUrlClickListener() {
            @Override
            public void onUrlClick(String url) {
                Log.d(TAG, "onUrlClick: " + url);
                Toast.makeText(ViewerActivity.this, url, Toast.LENGTH_SHORT).show();
            }
        });
        /* Set the life cycle listener */
        emv.setLifecycleListener(new LifecycleListener() {
            @Override
            public void onLoadFinished(WebView webView, String str) {
                Log.d(TAG, "onLoadFinished: " + str);
            }

            @Override
            public void beforeProcessMarkdown(String content) {
                Log.d(TAG, "beforeProcessMarkdown: " + content);
            }

            @Override
            public void afterProcessMarkdown(String document) {
                Log.d(TAG, "afterProcessMarkdown: " + document);
            }
        });
        /* Set the css content */
        emv.setCustomCssContent(Utils.readAssetsContent(this, "dark_style.css"));

        emv.processMarkdown(noteContent);
    }
}
