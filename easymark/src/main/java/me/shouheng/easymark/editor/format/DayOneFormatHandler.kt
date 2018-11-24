package me.shouheng.easymark.editor.format

import android.widget.EditText
import me.shouheng.easymark.editor.Format
import me.shouheng.easymark.editor.dayone.Indent
import me.shouheng.easymark.editor.dayone.Mark
import me.shouheng.easymark.editor.dayone.handler.TodoHandler
import me.shouheng.easymark.editor.dayone.extension.isIndent
import me.shouheng.easymark.editor.dayone.extension.selectedLine

/**
 * This code is cloned from MarkNote, and originally contributed by Uraka.Lee
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: DayOneFormatHandler, v 0.1 2018/11/24 16:14 shouh Exp$ */
class DayOneFormatHandler : DefaultFormatHandler() {

    override fun handle(formatId: Int, source: String?, selectionStart: Int, selectionEnd: Int,
                        selection: String?, editor: EditText?, vararg params: Any?) {
        val format = Format.getFormat(formatId)
        if (format != null) {
            when(format) {
                Format.H1, Format.H2, Format.H3, Format.H4, Format.H5, Format.H6 ->
                    handlePrecedingMark(source!!, Mark.H, selectionStart, selectionEnd, editor)
                Format.NORMAL_LIST, Format.NUMBER_LIST ->
                    handlePrecedingMark(source!!, Mark.LI, selectionStart, selectionEnd, editor)
                Format.CHECKBOX, Format.CHECKBOX_FILLED ->
                    handlePrecedingMark(source!!, Mark.TD, selectionStart, selectionEnd, editor)
                Format.INDENT -> indent(source!!, selectionStart, selectionEnd, selection, editor)
                Format.DEDENT -> dedent(source!!, selectionStart, selectionEnd, selection, editor)
                Format.QUOTE -> handlePrecedingMark(source!!, Mark.QT, selectionStart, selectionEnd, editor)
                else -> {
                    super.handle(formatId, source, selectionStart, selectionEnd, selection, editor, *params)
                }
            }
        }
    }

    /**
     * Handle the intent command
     */
    private fun indent(source: String, selectionStart: Int, selectionEnd: Int, selection: String?, editor: EditText?) {
        val (targetLine, start, end) = source.selectedLine(selectionStart, selectionEnd)
        // parse 出前面格式(header, list, task, quote)之外的文字
        val (mark, indent, content) = detectPrecedingMark(targetLine)
        // XXX: 目前只把 4 个空格当 indent
        val firstNonIndent = start + indent.length
        val originalIndentLength = indent.length
        indent.indent()
        editor?.text?.replace(start, firstNonIndent, indent.content)
        editor?.setSelection(end + indent.length - originalIndentLength) // 简单处理, 光标放在行尾
    }

    /**
     * Handle the dedent command
     */
    private fun dedent(source: String, selectionStart: Int, selectionEnd: Int, selection: String?, editor: EditText?) {
        val (targetLine, start, end) = source.selectedLine(selectionStart, selectionEnd)
        // parse 出前面格式(header, list, task, quote)之外的文字
        val (mark, indent, content) = detectPrecedingMark(targetLine)
        // XXX: 目前只把 4 个空格当 indent
        val firstNonIndent = start + indent.length
        val originalIndentLength = indent.length
        indent.dedent()
        editor?.text?.replace(start, firstNonIndent, indent.content)
        editor?.setSelection(end + indent.length - originalIndentLength) // 简单处理, 光标放在行尾
    }

    /**
     * Handle the mark command
     */
    private fun handlePrecedingMark(source: String, inputMark: Mark,
                                    selectionStart: Int, selectionEnd: Int, editor: EditText?) {
        val (targetLine, start, end) = source.selectedLine(selectionStart, selectionEnd)
        val (mark, indent, content) = detectPrecedingMark(targetLine)
        val newMark = Mark.handle(inputMark, mark)
        val firstNonIndent = start + indent.length
        editor?.text?.replace(firstNonIndent, firstNonIndent + mark.length, newMark)
        editor?.setSelection(end + newMark.length - mark.length) // 简单处理, 光标放在行尾
    }

    /**
     * @return mark, indent, content
     */
    private fun detectPrecedingMark(line: String): Triple<String, Indent, String> {
        // 找到 indent
        val firstNonIndent = line.indexOfFirst {
            !it.isIndent()
        }.let {
            if (it == -1) line.length else it // -1 则全是 indent
        }
        val indent = Indent(if (firstNonIndent == 0) null else line.substring(0 until firstNonIndent))
        // 在 indent 后面找 mark
        val firstBlank = line.indexOf(' ', firstNonIndent)
        if (firstBlank == -1) {
            return Triple("", indent, line)
        } else {
            val mark = line.substring(firstNonIndent until firstBlank)
            if (mark == "-") {
                val todo = detectTodo(line, indent)
                if (todo != null) {
                    return todo
                }
            }
            val content = line.substring(firstBlank + 1)
            return Triple(mark, indent, content)
        }
    }

    /**
     * detect [Mark.TD] if start with '-'
     */
    private fun detectTodo(line: String, indent: Indent): Triple<String, Indent, String>? {
        val endIndex = indent.length + Mark.TD.defaultMark.length + 1 // +1 blank
        if (endIndex > line.length) return null // not long enough, so no TD mark
        if (line[endIndex - 1] != ' ') return null // no following blank, pass
        val mark = line.substring(indent.length until endIndex - 1)
        val content = line.substring(endIndex)
        return TodoHandler.handleTodo(mark,
                {
                    Triple(TodoHandler.UNCHECKED, indent, content)
                },
                {
                    Triple(TodoHandler.CHECKED, indent, content) },
                {
                    null
                })
    }
}
