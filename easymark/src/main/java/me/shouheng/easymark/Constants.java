package me.shouheng.easymark;

/**
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: Constants, v 0.1 2018/11/23 22:36 shouh Exp$
 */
public interface  Constants {
    char CHAR_ENTER = '\n';
    String STRING_ENTER = "\n";
    String STRING_TAB = "\t";
    String DEFAULT_TAB_REPLACEMENT = "    ";
    String POINT_LIST_START_CHARS = "* ";
    String NUMBER_LIST_START_CHARS = "1. ";

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
    String MATH_JAX_REGEX_EXPRESSION = "[$].+.[$]";

    /**
     * Light styled css assets name
     */
    String LIGHT_STYLE_CSS_ASSETS_NAME = "light_style.css";

    /**
     * Dark styled css assets name
     */
    String DARK_STYLE_CSS_ASSETS_NAME = "dark_style.css";
}
