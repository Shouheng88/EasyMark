package me.shouheng.easymark.editor.format;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import me.shouheng.easymark.editor.Format;

import static me.shouheng.easymark.tools.Utils.insertList;
import static me.shouheng.easymark.tools.Utils.isSingleLine;
import static me.shouheng.easymark.tools.Utils.isTwoSingleLines;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: DefaultFormatHandler, v 0.1 2018/11/24 0:40 shouh Exp$
 */
public class DefaultFormatHandler implements FormatHandler {

    private static final String TAG = "DefaultFormatHandler";

    @Override
    public void handle(int formatId, String source, int selectionStart, int selectionEnd,
                       String selection, EditText editor, Object... params) {
        Format format = Format.getFormat(formatId);
        if (format != null) {
            switch (format) {
                case H1:
                case H2:
                case H3:
                case H4:
                case H5:
                case H6:
                case QUOTE:
                case HORIZONTAL_LINE: {
                    String prefix = format.symbol + " ";
                    String result = isSingleLine(source, selectionStart) ? prefix + selection : "\n" + prefix + selection;
                    editor.getText().replace(selectionStart, selectionEnd, result);
                    editor.setSelection(selectionStart + result.length());
                    break;
                }
                case BOLD:
                case ITALIC:
                case STRIKE:
                case CODE_INLINE:
                case MARK:
                case SUB_SCRIPT:
                case SUPER_SCRIPT:
                case MATH_JAX:{
                    String result = format.symbol + selection + format.symbol;
                    if (TextUtils.isEmpty(selection)) {
                        editor.getText().replace(selectionStart, selectionEnd, result);
                        editor.setSelection(selectionStart + result.length() - format.symbol.length());
                        return;
                    }
                    editor.getText().replace(selectionStart, selectionEnd, result);
                    editor.setSelection(selectionStart + result.length());
                    break;
                }
                case CODE_BLOCK: {
                    String result = isSingleLine(source, selectionStart) ?
                            "```\n" + selection + "\n```\n" : "\n```\n" + selection + "\n```\n";
                    if (TextUtils.isEmpty(selection)) {
                        editor.getText().replace(selectionStart, selectionEnd, result);
                        editor.setSelection(selectionStart + result.length() - 5);
                        return;
                    }
                    editor.getText().replace(selectionStart, selectionEnd, result);
                    editor.setSelection(selectionStart + result.length());
                    break;
                }
                case LINK:
                case IMAGE:{
                    String result = format.symbol;
                    editor.getText().insert(selectionStart, result);
                    editor.setSelection(selectionStart + result.length());
                    break;
                }
                case NORMAL_LIST:
                case NUMBER_LIST:
                case CHECKBOX:
                case CHECKBOX_FILLED: {
                    insertList(format.symbol, source, selectionStart, selectionEnd, editor);
                    break;
                }
                case TABLE: {
                    try {
                        int rows = (int) params[0], cols = (int) params[1];
                        StringBuilder sb = new StringBuilder();

                        if (!isTwoSingleLines(source, selectionStart)) {
                            sb.append(isSingleLine(source, selectionStart) ? "\n" : "\n\n");
                        }

                        sb.append("|");
                        for (int i = 0; i < cols; i++) sb.append(" HEADER |");

                        sb.append("\n|");
                        for (int i = 0; i < cols; i++) sb.append(":----------:|");

                        sb.append("\n");
                        for (int i2 = 0; i2 < rows; i2++) {
                            sb.append("|");
                            for (int i = 0; i < cols; i++) {
                                sb.append("            |");
                            }
                            sb.append("\n");
                        }

                        String result = sb.toString();
                        editor.getText().insert(selectionStart, result);
                        editor.setSelection(selectionStart + result.length());
                    } catch (Exception ex) {
                        Log.e(TAG, "Error when using table format " + ex);
                    }
                    break;
                }
                case DEDENT:{
                    break;
                }
                case INDENT:{
                    break;
                }
            }
        }
    }
}
