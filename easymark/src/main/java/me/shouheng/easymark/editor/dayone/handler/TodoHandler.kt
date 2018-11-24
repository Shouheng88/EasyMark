package me.shouheng.easymark.editor.dayone.handler

import me.shouheng.easymark.editor.dayone.Mark
import me.shouheng.easymark.editor.dayone.MarkHandler

/**
 * @author Uraka.Lee
 */
object TodoHandler : MarkHandler {

    val CHECKED = "- [X]"
    val UNCHECKED = "- [ ]"

    override fun handleTodo(inputMark: Mark, source: String, sourceMark: Mark): String {
        return handleTodo(source,
                {
                    CHECKED
                },
                {
                    UNCHECKED
                },
                {
                    super.handleTodo(inputMark, source, sourceMark)
                })
    }

    fun <T> handleTodo(source: String,
                       handleUnchecked: () -> T,
                       handleChecked: () -> T,
                       handleDefault: () -> T): T {
        return when (source.toUpperCase()) {
            UNCHECKED -> {
                handleUnchecked.invoke()
            }
            CHECKED -> {
                handleChecked.invoke()
            }
            else -> handleDefault.invoke()
        }
    }
}