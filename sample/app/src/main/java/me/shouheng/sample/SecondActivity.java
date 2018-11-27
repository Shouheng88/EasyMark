package me.shouheng.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import me.shouheng.easymark.tools.Utils;
import me.shouheng.sample.widget.MDEditorLayout;
import me.shouheng.sil.BaseSoftInputLayout.OnKeyboardStateChangeListener;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: SecondActivity, v 0.1 2018/11/26 23:06 shouh Exp$
 */
public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
        return super.onCreateOptionsMenu(menu);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
