package me.shouheng.easymark.editor.format;

import android.widget.EditText;

import me.shouheng.easymark.editor.Format;

/**
 * The handler used to handle the markdown grammar formats and other language formats.
 * This interface has an default implementation {@link DefaultFormatHandler}.
 * Call the {@link me.shouheng.easymark.EasyMarkEditor#setFormatHandler(FormatHandler)} to use
 * your handle in your markdown editor.
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: FormatHandler, v 0.1 2018/11/24 0:39 shouh Exp$
 */
public interface FormatHandler {

    /**
     * handle the format
     *
     * @param formatId format id, your custom id should be positive, since the pre-defined format ids
     *                 are negative. {@link me.shouheng.easymark.editor.Format}
     * @param source the source text of EditText
     * @param selectionStart the start position of selection
     * @param selectionEnd the end position of selection
     * @param selection the selected text
     * @param editor the EditText the handler is working with
     * @param params the params you offered by
     *              {@link me.shouheng.easymark.EasyMarkEditor#useFormat(Format, Object...)} etc.
     */
    void handle(int formatId, String source, int selectionStart, int selectionEnd, String selection,
                EditText editor, Object ...params);
}

