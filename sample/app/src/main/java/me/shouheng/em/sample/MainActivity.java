package me.shouheng.em.sample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import me.shouheng.easymark.Constants;
import me.shouheng.easymark.EasyMarkEditor;
import me.shouheng.easymark.editor.Format;
import me.shouheng.easymark.editor.format.DayOneFormatHandler;
import me.shouheng.easymark.editor.format.DefaultFormatHandler;
import me.shouheng.easymark.scroller.FastScrollScrollView;
import me.shouheng.easymark.tools.Utils;
import me.shouheng.em.sample.databinding.ActivityMainBinding;
import me.shouheng.em.sample.utils.MyUtils;
import me.shouheng.em.sample.vm.MainViewModel;
import me.shouheng.mvvm.base.CommonActivity;

public class MainActivity extends CommonActivity<ActivityMainBinding, MainViewModel> {

    private static final boolean isDarkTheme = false;

    private EasyMarkEditor eme;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void doCreateView(@Nullable Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("EasyMarkEditor");
        }

        /* Get the note content from assets */
        String defContent = Utils.readAssetsContent(this, "note.md");

        /* Get markdown editor */
        eme = findViewById(R.id.eme);
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

        /* Add markdown command buttons to layout. */
        LinearLayout ll = findViewById(R.id.ll);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                Utils.dp2px(this, 40), ViewGroup.LayoutParams.MATCH_PARENT);
        int dp8 = Utils.dp2px(this, 8);
        for (final Format format : Format.values()) {
            /* Add the format button */
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(params);
            iv.setPadding(dp8, dp8, dp8, dp8);
            iv.setImageDrawable(Utils.tintDrawable(this, format.drawableResId, Color.WHITE));
            ll.addView(iv);
            /* Set the click event */
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (format == Format.TABLE) {
                        eme.useFormat(format, 3, 3);
                    } else {
                        eme.useFormat(format);
                    }
                }
            });
        }

        // test string resources case sensitive
//        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, R.string.App_name, Toast.LENGTH_SHORT).show();
    }

    /**
     * Custom format handler. Use the {@link EasyMarkEditor#useFormat(Format, Object...)} to set the
     * format id and the params to editor, and then handle your own id by the custom format handler.
     *
     * The {@link DefaultFormatHandler} has a default implementation of format handler. You can also
     * implement your own logic by directly implement the {@link me.shouheng.easymark.editor.format.FormatHandler}
     */
    private static class CustomFormatHandler extends DayOneFormatHandler {

        /**
         * Implement your own format handler
         *
         * @param formatId the format id from {@link EasyMarkEditor#useFormat(int, Object...)}}
         *                 or {@link EasyMarkEditor#useFormat(Format, Object...)}
         * @param source the source text
         * @param selectionStart the start position of selection
         * @param selectionEnd the end position of selection
         * @param selection the selected text
         * @param editor the editor
         * @param params the params from {@link EasyMarkEditor#useFormat(int, Object...)}}
         *               or {@link EasyMarkEditor#useFormat(Format, Object...)}
         */
        @Override
        public void handle(int formatId, String source, int selectionStart,
                           int selectionEnd, String selection, EditText editor, Object... params) {
            super.handle(formatId, source, selectionStart, selectionEnd, selection, editor, params);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
            case R.id.item_undo:
                eme.undo();
                break;
            case R.id.item_redo:
                eme.redo();
                break;
            case R.id.item_view:
                Intent intent = new Intent(this, me.shouheng.em.sample.ViewerActivity.class);
                intent.putExtra(me.shouheng.em.sample.ViewerActivity.EXTRA_KEY_NOTE_CONTENT, eme.getText().toString());
                startActivity(intent);
                break;
            case R.id.item_second:
                Intent intent1 = new Intent(this, me.shouheng.em.sample.SecondActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
