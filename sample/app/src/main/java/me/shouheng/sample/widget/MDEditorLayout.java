package me.shouheng.sample.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import me.shouheng.sample.R;
import me.shouheng.sil.BaseSoftInputLayout;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: MarkdownEditLayout, v 0.1 2018/11/26 22:59 shouh Exp$
 */
public class MDEditorLayout extends BaseSoftInputLayout {

    private View frame;
    private View container;
    private EditText editText;

    public MDEditorLayout(Context context) {
        super(context);
    }

    public MDEditorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MDEditorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MDEditorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void doInitView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.layout_markdown_editor, this, true);

        frame = findViewById(R.id.frame);
        container = findViewById(R.id.container);
        editText = findViewById(R.id.eme);
    }

    @Override
    protected View getFrame() {
        return frame;
    }

    @Override
    protected View getContainer() {
        return container;
    }

    @Override
    protected EditText getEditText() {
        return editText;
    }
}
