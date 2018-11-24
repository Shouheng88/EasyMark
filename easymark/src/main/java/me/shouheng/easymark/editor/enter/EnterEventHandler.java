package me.shouheng.easymark.editor.enter;

import android.text.Editable;
import android.widget.EditText;

/**
 * The enter event handler used to handle the enter event in the EditText
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: EnterEventHandler, v 0.1 2018/11/24 1:20 shouh Exp$
 */
public interface EnterEventHandler {

    /**
     * Handle the enter event
     *
     * @param editable the edittable of current EditText
     * @param source the source sequence
     * @param start the start position
     * @param editor the EditText working with
     */
    void handle(Editable editable, CharSequence source, int start, EditText editor);
}
