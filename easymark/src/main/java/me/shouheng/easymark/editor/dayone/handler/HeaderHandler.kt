package me.shouheng.easymark.editor.dayone.handler

import me.shouheng.easymark.editor.dayone.Mark
import me.shouheng.easymark.editor.dayone.MarkHandler

/**
 * @author Uraka.Lee
 */
object HeaderHandler : MarkHandler {

    override fun handleHeader(inputMark: Mark, source: String, sourceMark: Mark): String {
        return if (source.length < 6) "$source${Mark.H.defaultMark}" else Mark.H.defaultMark
    }
}