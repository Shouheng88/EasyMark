package me.shouheng.easymark;

/**
 * Constants for EasyMark
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: Constants, v 0.1 2018/11/23 22:36 shouh Exp$
 */
public class Constants {
    public static final char CHAR_ENTER                 = '\n';
    public static final String STRING_ENTER             = "\n";
    public static final String STRING_TAB               = "\t";
    public static final String DEFAULT_TAB_REPLACEMENT  = "    ";
    public static final String POINT_LIST_START_CHARS   = "* ";
    public static final String NUMBER_LIST_START_CHARS  = "1. ";

    /**
     * TODO change the MathJax regex
     *
     * The MathJax regex expression is used to match the MathJax expression in parsed html
     * and then replace some chars in the expression so that the WebView can successfully
     * draw the math expression.
     *
     * The char need to replace:
     * 1. change \ to \\
     * 2. change ^ to \^
     * 3. change { to \{
     *
     * The problem now: the regex expression can only recognize the single lined MathJax expression
     */
    public static final String MATH_JAX_REGEX_EXPRESSION    = "[$].+.[$]";
    public static final String DARK_STYLE_CSS_ASSETS_NAME   = "mark.css";
}
