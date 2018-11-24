package me.shouheng.easymark.editor;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import me.shouheng.easymark.R;

/**
 * The enums for markdown format with drawable resources
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: Format, v 0.1 2018/11/24 0:04 shouh Exp$
 */
public enum Format {
    H1(-1, R.drawable.ic_format_header_1, "# "),
    H2(-2, R.drawable.ic_format_header_2, "## "),
    H3(-3, R.drawable.ic_format_header_3, "### "),
    H4(-4, R.drawable.ic_format_header_4, "#### "),
    H5(-5, R.drawable.ic_format_header_5, "##### "),
    H6(-6, R.drawable.ic_format_header_6, "###### "),

    NORMAL_LIST(-7, R.drawable.ic_format_list_bulleted_white_24dp, "* "),
    NUMBER_LIST(8, R.drawable.ic_format_list_numbered_white_24dp, "1. "),
    CHECKBOX(-9, R.drawable.ic_format_check_box_unfilled_white_24dp, "- [ ] "),
    CHECKBOX_FILLED(-10, R.drawable.ic_format_check_box_filled_white_24dp, "- [x] "),

    INDENT(-11, R.drawable.ic_format_indent_increase_white_24dp, "    "),
    DEDENT(-12, R.drawable.ic_format_indent_decrease_white_24dp, ""),
    QUOTE(-13, R.drawable.ic_format_quote_white_24dp, "> "),
    CODE_INLINE(-14, R.drawable.ic_format_code_inline, "`"),
    CODE_BLOCK(-15, R.drawable.ic_format_code_block, "```"),

    STRIKE(-16, R.drawable.ic_format_strikethrough_white_24dp, "~~"),
    HORIZONTAL_LINE(-17, R.drawable.ic_format_horizontal_line_white_24dp, "-------\n"),

    ITALIC(-18, R.drawable.ic_format_italic_white_24dp, "*"),
    BOLD(19, R.drawable.ic_format_bold_white_24dp, "**"),
    MARK(-20, R.drawable.ic_format_mark_white_24dp, "=="),

    MATH_JAX(-21, R.drawable.ic_format_mathjax_white_24dp, "$$"),
    SUB_SCRIPT(-22, R.drawable.ic_format_subscript,  "~"),
    SUPER_SCRIPT(-23, R.drawable.ic_format_superscript,  "^"),

    IMAGE(-24, R.drawable.ic_format_image_white_24dp, "![](https://)"),
    LINK(-25, R.drawable.ic_format_link_white_24dp, "[](https://)"),
    TABLE(-26, R.drawable.ic_format_table_white_24dp, "|HEADER|\n|:----:|");

    public final int id;

    @DrawableRes
    public final int drawableResId;

    public final String symbol;

    Format(int id, @DrawableRes int drawableResId, String symbol) {
        this.id = id;
        this.drawableResId = drawableResId;
        this.symbol = symbol;
    }

    /**
     * Get the format of given id, will return null if not found.
     *
     * @param formatId the format id
     * @return the format
     */
    @Nullable
    public static Format getFormat(int formatId) {
        for (Format format : values()) {
            if (format.id == formatId) {
                return format;
            }
        }
        return null;
    }
}
