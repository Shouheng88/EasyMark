package me.shouheng.easymark;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.util.LinkedList;

import me.shouheng.easymark.editor.Format;
import me.shouheng.easymark.editor.enter.DefaultEnterEventHandler;
import me.shouheng.easymark.editor.enter.EnterEventHandler;
import me.shouheng.easymark.editor.format.DefaultFormatHandler;
import me.shouheng.easymark.editor.format.FormatHandler;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: EasyMarkEditor, v 0.1 2018/11/23 18:55 shouh Exp$
 */
public class EasyMarkEditor extends AppCompatEditText {

    /**
     * Action index
     */
    private int index;

    /**
     * History stack, used for undo option.
     */
    private Stack<Action> history = new Stack<>();

    /**
     * History back stack, used for redo option.
     */
    private Stack<Action> historyBack = new Stack<>();

    /**
     * Will format the pasted text automatically if set true.
     */
    private boolean formatPasteEnable = true;

    /**
     * Edit flag.
     */
    private boolean flag = false;

    /**
     *  Pasted '\t' replacement.
     */
    private String pasteTabReplacement;

    /**
     * The format handler used to handle the markdown format and any other custom format
     * with given format id.
     */
    private FormatHandler formatHandler;

    /**
     * The handler for enter event aka '\n'
     */
    private EnterEventHandler enterEventHandler;

    public EasyMarkEditor(Context context) {
        super(context);
        init(context, null, -1);
    }

    public EasyMarkEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public EasyMarkEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        // TODO get attributes from style

        /* The enter event handler */
        this.enterEventHandler = new DefaultEnterEventHandler();
        /* The markdown format handler */
        this.formatHandler = new DefaultFormatHandler();
        /* The default pasted '\t' replacement */
        this.pasteTabReplacement = Constants.DEFAULT_TAB_REPLACEMENT;
        /* Add a text watcher to watch the text change. */
        this.addTextChangedListener(new InputWatcher());
        /* Add a text watcher to watch the action event. */
        this.addTextChangedListener(new ActionWatcher());
    }

    /**
     * After editable changed, the child could handle this event.
     *
     * @param s thee editable
     */
    protected void onEditableChanged(Editable s) { }

    /**
     * After text changed, the child could handle this vent.
     *
     * @param s the editable
     */
    protected void onTextChanged(Editable s) { }

    /**
     * Use the format in EditText, the format implementation depend on the {@link #formatHandler}
     * you defined by {@link #setFormatHandler(FormatHandler)}, the params will be send to your
     * {@link #formatHandler}.
     *
     * @param format format
     */
    public final void useFormat(Format format) {
        String source = this.getText().toString();
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        String selection = source.substring(selectionStart, selectionEnd);
        if (formatHandler != null) {
            formatHandler.handle(format.id, source, selectionStart, selectionEnd, selection, this);
        }
    }

    /**
     * Use the format in EditText, the format implementation depend on the {@link #formatHandler}
     * you defined by {@link #setFormatHandler(FormatHandler)}, the params will be send to your
     * {@link #formatHandler}.
     *
     * @param formatId format id
     */
    public final void useFormat(int formatId) {
        String source = this.getText().toString();
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        String selection = source.substring(selectionStart, selectionEnd);
        if (formatHandler != null) {
            formatHandler.handle(formatId, source, selectionStart, selectionEnd, selection, this);
        }
    }

    /**
     * Use the format in EditText, the format implementation depend on the {@link #formatHandler}
     * you defined by {@link #setFormatHandler(FormatHandler)}, the params will be send to your
     * {@link #formatHandler}.
     *
     * @param format the format
     * @param params the params
     */
    public final void useFormat(Format format, Object...params) {
        String source = this.getText().toString();
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        String selection = source.substring(selectionStart, selectionEnd);
        if (formatHandler != null) {
            formatHandler.handle(format.id, source, selectionStart, selectionEnd, selection, this, params);
        }
    }

    /**
     * Use the format in EditText, the format implementation depend on the {@link #formatHandler}
     * you defined by {@link #setFormatHandler(FormatHandler)}, the params will be send to your
     * {@link #formatHandler}.
     *
     * @param formatId the format id
     * @param params the params
     */
    public final void useFormat(int formatId, Object... params) {
        String source = this.getText().toString();
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        String selection = source.substring(selectionStart, selectionEnd);
        if (formatHandler != null) {
            formatHandler.handle(formatId, source, selectionStart, selectionEnd, selection, this, params);
        }
    }

    /**
     * Clear the redo and undo history
     */
    public final void clearHistory() {
        history.clear();
        historyBack.clear();
    }

    /**
     * Undo
     */
    public final void undo() {
        if (history.empty()) return;
        flag = true;
        Action action = history.pop();
        historyBack.push(action);
        if (action.isAdd) {
            getText().delete(action.startCursor, action.startCursor + action.actionTarget.length());
            this.setSelection(action.startCursor, action.startCursor);
        } else {
            getText().insert(action.startCursor, action.actionTarget);
            if (action.endCursor == action.startCursor) {
                this.setSelection(action.startCursor + action.actionTarget.length());
            } else {
                this.setSelection(action.startCursor, action.endCursor);
            }
        }
        flag = false;
        // Undo until current action
        if (!history.empty() && history.peek().index == action.index) {
            undo();
        }
    }

    /**
     * Redo
     */
    public final void redo() {
        if (historyBack.empty()) return;
        flag = true;
        Action action = historyBack.pop();
        history.push(action);
        if (action.isAdd) {
            getText().insert(action.startCursor, action.actionTarget);
            this.setSelection(action.startCursor, action.endCursor);
        } else {
            getText().delete(action.startCursor, action.startCursor + action.actionTarget.length());
            this.setSelection(action.startCursor, action.startCursor);
        }
        flag = false;
        // Redo until current action22
        if (!historyBack.empty() && historyBack.peek().index == action.index) {
            redo();
        }
    }

    /**
     * Set default text
     *
     * @param text the text to set
     */
    public final void setDefaultText(CharSequence text) {
        clearHistory();
        flag = true;
        getText().replace(0, getText().length(), text);
        flag = false;
    }

    /**
     * Set the format handler to handle the markdown grammar formats and other custom formats.
     * Implement the {@link FormatHandler} or {@link DefaultFormatHandler} to handle your own
     * custom format.
     *
     * @param formatHandler the format handler
     */
    public void setFormatHandler(FormatHandler formatHandler) {
        this.formatHandler = formatHandler;
    }

    /**
     * Set the enter event handler for EditText
     *
     * @param enterEventHandler the enter event handler
     */
    public void setEnterEventHandler(EnterEventHandler enterEventHandler) {
        this.enterEventHandler = enterEventHandler;
    }

    /**
     * Automatically change the '\t' if set true. Use {@link #setPasteTabReplacement(String)} method
     * to set the replacement, otherwise the {@link Constants#DEFAULT_TAB_REPLACEMENT} will be used.
     *
     * @param enable automatically change the '\t' if set true
     */
    public void setFormatPasteEnable(boolean enable) {
        formatPasteEnable = enable;
    }

    /**
     * The replacement for '\t'. The {@link Constants#DEFAULT_TAB_REPLACEMENT} will be used as default
     *
     * @param pasteTabReplacement the replacement
     */
    public void setPasteTabReplacement(String pasteTabReplacement) {
        this.pasteTabReplacement = pasteTabReplacement;
    }

    /**
     * Text watcher to handle the user input
     */
    private class InputWatcher implements TextWatcher {

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * are about to be replaced by new text with length <code>after</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param s     the changed chars
         * @param start the cursor start position
         * @param count the selection count
         * @param after the new text length
         */
        @Override
        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (flag) return;
            int end = start + count;
            if (end > start && end <= s.length()) {
                CharSequence subSequence = s.subSequence(start, end);
                if (subSequence.length() > 0) {
                    onTextSubtracted(s, subSequence, start);
                }
            }
        }

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * have just replaced old text that had length <code>before</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param start  the start cursor
         * @param before the old text length
         * @param count  the text count
         */
        @Override
        public final void onTextChanged(CharSequence s, int start, int before, int count) {
            if (flag) return;
            int end = start + count;
            if (end > start) {
                CharSequence subSequence = s.subSequence(start, end);
                if (subSequence.length() > 0) {
                    onTextAdded(s, subSequence, start);
                }
            }
        }

        @Override
        public final void afterTextChanged(Editable s) { }
    }

    /**
     * Handle the action according to changed text.
     */
    private class ActionWatcher implements TextWatcher {

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * are about to be replaced by new text with length <code>after</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param s     the changed chars
         * @param start the cursor start position
         * @param count the selection count
         * @param after the new text length
         */
        @Override
        public final void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (flag) return;
            int end = start + count;
            if (end > start && end <= s.length()) {
                CharSequence charSequence = s.subSequence(start, end);
                if (charSequence.length() > 0) {
                    // Action delete
                    Action action = new Action(charSequence, start, false);
                    if (count > 1) {
                        // The user want to delete or replace the selected text
                        action.setSelectCount(count);
                    } else if (count == 1 && count == after) {
                        // Replace one char
                        action.setSelectCount(count);
                    }
                    history.push(action);
                    historyBack.clear();
                    action.setIndex(++index);
                }
            }
        }

        /**
         * This method is called to notify you that, within <code>s</code>,
         * the <code>count</code> characters beginning at <code>start</code>
         * have just replaced old text that had length <code>before</code>.
         * It is an error to attempt to make changes to <code>s</code> from
         * this callback.
         *
         * @param start  the start cursor
         * @param before the old text length
         * @param count  the text count
         */
        @Override
        public final void onTextChanged(CharSequence s, int start, int before, int count) {
            if (flag) return;
            int end = start + count;
            if (end > start) {
                CharSequence charSequence = s.subSequence(start, end);
                if (charSequence.length() > 0) {
                    // Action add
                    Action action = new Action(charSequence, start, true);
                    history.push(action);
                    historyBack.clear();
                    if (before > 0) {
                        // Replace action only change the char sequence
                        action.setIndex(index);
                    } else {
                        action.setIndex(++index);
                    }
                }
            }
        }

        /**
         * This method is called to notify you that, somewhere within
         * <code>s</code>, the text has been changed.
         *
         * @param s the editable
         */
        @Override
        public final void afterTextChanged(Editable s) {
            if (flag) return;
            if (s != getText()) {
                onEditableChanged(s);
            }
            EasyMarkEditor.this.onTextChanged(s);
        }
    }

    /**
     * On text added to the EditText
     *
     * @param source the text source
     * @param charSequence the added text
     * @param start the start position cursor
     */
    private void onTextAdded(CharSequence source, CharSequence charSequence, int start) {
        flag = true;
        if ("\n".equals(charSequence.toString())) {
            onEnterAdded(getText(), source, start);
        }
        flag = false;
    }

    /**
     * On text subtracted from the source
     *
     * @param source the text source
     * @param charSequence the subtracted text
     * @param start the start position cursor
     */
    private void onTextSubtracted(CharSequence source, CharSequence charSequence, int start) {
        flag = true;
        // Code here
        flag = false;
    }

    /**
     * On the enter char '\n' is added to the EditText
     *
     * @param editable the editable
     * @param source the char sequence
     * @param start the start cursor
     */
    private void onEnterAdded(Editable editable, CharSequence source, int start) {
        if (enterEventHandler != null) {
            enterEventHandler.handle(editable, source, start, this);
        }
    }

    /**
     * Listen the context menu item event
     *
     * @param id the menu item id
     * @return is the event handled
     */
    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.paste:
                if (!formatPasteEnable) break;
                ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String content;
                if (clip != null && !TextUtils.isEmpty(content = clip.getText().toString())) {
                    clip.setText(content.replace(Constants.STRING_TAB, pasteTabReplacement));
                }
                break;
        }
        return super.onTextContextMenuItem(id);
    }

    /**
     * The class to wrap an action.
     */
    private static class Action {
        /** The text for action */
        CharSequence actionTarget;
        /** The start and end position of cursor */
        int startCursor, endCursor;
        /** Is ADD option */
        boolean isAdd;
        /** Action index */
        int index;

        Action(CharSequence actionTarget, int startCursor, boolean isAdd) {
            this.actionTarget = actionTarget;
            this.startCursor = startCursor;
            this.endCursor = startCursor;
            this.isAdd = isAdd;
        }

        void setSelectCount(int count) {
            this.endCursor = endCursor + count;
        }

        void setIndex(int index) {
            this.index = index;
        }

        @NonNull
        @Override
        public String toString() {
            return "Action{" +
                    "actionTarget=" + actionTarget +
                    ", startCursor=" + startCursor +
                    ", endCursor=" + endCursor +
                    ", isAdd=" + isAdd +
                    ", index=" + index +
                    '}';
        }
    }

    /**
     * The stack made by the {@link LinkedList}, we don't use the {@link Stack} for it synchronized
     * many methods, which may weaken the performance.
     *
     * @param <E>
     */
    private static class Stack<E> extends LinkedList<E> {

        boolean empty() {
            return size() == 0;
        }
    }
}
