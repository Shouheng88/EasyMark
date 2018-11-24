package me.shouheng.easymark.editor.enter;

import android.text.Editable;
import android.widget.EditText;

import me.shouheng.easymark.Constants;
import me.shouheng.easymark.tools.Utils;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: DefaultEnterEventHandler, v 0.1 2018/11/24 1:29 shouh Exp$
 */
public class DefaultEnterEventHandler implements EnterEventHandler {

    @Override
    public void handle(Editable editable, CharSequence source, int start, EditText editor) {
        // Get the text before new '\n'
        String tempStr = source.subSequence(0, start).toString();
        // The last '\n' before new '\n'
        int lastEnter = tempStr.lastIndexOf(Constants.CHAR_ENTER);
        if (lastEnter > 0) {
            // Text between last '\n' and current '\n'
            tempStr = tempStr.substring(lastEnter + 1, start);
        }

        String mString = tempStr.trim();
        String startSpace = Utils.getStartSpace(tempStr);

        // TODO handle the case when the else case.
        if (mString.startsWith(Constants.POINT_LIST_START_CHARS)) {
            // "* ": Handle the '\n' when start with "* "
            if (mString.length() > Constants.POINT_LIST_START_CHARS.length()) {
                editable.insert(start + 1, startSpace + Constants.POINT_LIST_START_CHARS);
            }
        } else if (mString.startsWith(Constants.NUMBER_LIST_START_CHARS)) {
            // "1. ": Handle the '\n' when start with "1. "
            if (mString.length() > Constants.NUMBER_LIST_START_CHARS.length()) {
                editable.insert(start + 1, startSpace + Constants.NUMBER_LIST_START_CHARS);
            }
        } else if (mString.length() > 1) {
            editable.insert(start + 1, startSpace);
        }
    }
}
