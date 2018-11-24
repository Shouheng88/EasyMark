package me.shouheng.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.shouheng.easymark.Constants;
import me.shouheng.easymark.EasyMarkEditor;
import me.shouheng.easymark.editor.Format;
import me.shouheng.easymark.editor.format.DayOneFormatHandler;
import me.shouheng.easymark.editor.format.DefaultFormatHandler;
import me.shouheng.easymark.scroller.FastScrollScrollView;
import me.shouheng.easymark.tools.Utils;

public class MainActivity extends AppCompatActivity {

    private final static boolean isDarkTheme = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                if ((view instanceof TextView)) {
                    ((TextView) view).setTypeface(ResourcesCompat.getFont(MainActivity.this, R.font.a));
                }
                return view;
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(me.shouheng.sample.R.layout.activity_main);

        /* Get markdown editor */
        final EasyMarkEditor eme = findViewById(R.id.eme);
        eme.setFormatPasteEnable(true);
        /* Set the format handler, custom your own handler by implementing FormatHandler */
        eme.setFormatHandler(new DayOneFormatHandler());
//        eme.setFormatHandler(new CustomFormatHandler());
        eme.setPasteTabReplacement(Constants.DEFAULT_TAB_REPLACEMENT);

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
    }

    /**
     * Custom format handler. Use the {@link EasyMarkEditor#useFormat(Format, Object...)} to set the
     * format id and the params to editor, and then handle your own id by the custom format handler.
     *
     * The {@link DefaultFormatHandler} has a default implementation of format handler. You can also
     * implement your own logic by directly implement the {@link me.shouheng.easymark.editor.format.FormatHandler}
     */
    private static class CustomFormatHandler extends DefaultFormatHandler {

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
}
