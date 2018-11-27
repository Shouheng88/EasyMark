package me.shouheng.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.shouheng.easymark.Constants;
import me.shouheng.easymark.EasyMarkEditor;
import me.shouheng.easymark.editor.format.DayOneFormatHandler;
import me.shouheng.easymark.scroller.FastScrollScrollView;
import me.shouheng.easymark.tools.Utils;
import me.shouheng.sample.utils.MyUtils;
import me.shouheng.sample.widget.MDEditorLayout;
import me.shouheng.sil.BaseSoftInputLayout.OnKeyboardStateChangeListener;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: SecondActivity, v 0.1 2018/11/26 23:06 shouh Exp$
 */
public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";

    private final static boolean isDarkTheme = false;

    private MDEditorLayout mel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("SoftInputLayout");
        }

        mel = findViewById(R.id.mel);
        mel.setOverHeight(Utils.dp2px(this, 50));
        mel.addOnKeyboardStateChangeListener(new OnKeyboardStateChangeListener() {
            @Override
            public void onShown(int height) {
                Log.d(TAG, "onShown: " + height);
            }

            @Override
            public void onHidden(int height) {
                Log.d(TAG, "onHidden: " + height);
            }
        });

        /* Get the note content from assets */
        String defContent = Utils.readAssetsContent(this, "note.md");

        /* Get markdown editor */
        EasyMarkEditor eme = mel.getEditText();
        eme.setFormatPasteEnable(true);
        /* Set the format handler, custom your own handler by implementing FormatHandler */
        eme.setFormatHandler(new DayOneFormatHandler());
//        eme.setFormatHandler(new CustomFormatHandler());
        eme.setPasteTabReplacement(Constants.DEFAULT_TAB_REPLACEMENT);
        /* Set the default note content */
        eme.setDefaultText(defContent);

        /* Custom fast scroller */
        FastScrollScrollView fssv = findViewById(R.id.fssv);
        fssv.getFastScrollDelegate().setThumbSize(16, 40);
        fssv.getFastScrollDelegate().setThumbDynamicHeight(false);
        fssv.getFastScrollDelegate().setThumbDrawable(getResources().getDrawable(isDarkTheme ?
                R.drawable.fast_scroll_bar_dark : R.drawable.fast_scroll_bar_light));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Tint the icon of menu items
     *
     * @param menu the menu
     * @return the result
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /* We want to use the white icon, so we set the isDarkTheme true. */
        MyUtils.themeMenu(menu, true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show:
                mel.showSoftInput();
                break;
            case R.id.show_only:
                mel.showSoftInputOnly();
                break;
            case R.id.hide:
                mel.hideSoftInput();
                break;
            case R.id.hide_only:
                mel.hideSoftInputOnly();
                break;
            case R.id.item_view:
                Intent intent = new Intent(this, ViewerActivity.class);
                intent.putExtra(ViewerActivity.EXTRA_KEY_NOTE_CONTENT, mel.getEditText().getText().toString());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
