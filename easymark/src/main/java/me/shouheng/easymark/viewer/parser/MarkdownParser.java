package me.shouheng.easymark.viewer.parser;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ast.util.TextCollectingVisitor;
import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.IndependentAttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.superscript.SuperscriptExtension;
import com.vladsch.flexmark.util.html.Attributes;
import com.vladsch.flexmark.util.html.Escaping;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.shouheng.easymark.Constants;
import me.shouheng.easymark.tools.Utils;
import me.shouheng.easymark.viewer.ext.mark.MarkExtension;
import me.shouheng.easymark.viewer.ext.mathjax.MathJax;
import me.shouheng.easymark.viewer.ext.mathjax.MathJaxExtension;

import static me.shouheng.easymark.EasyMarkViewer.DARK_STYLE_CSS;

/**
 * The async task used to parse the markdown content to html.
 *
 * @author WngShhng (shouheng2015@gmail.com)
 * @version $Id: MarkdownParser, v 0.1 2018/11/24 22:44 shouh Exp$
 */
public class MarkdownParser extends AsyncTask<String, String, String> {

    private static final String TAG = "MarkdownParser";

    /**
     * The MathJax regen expression pattern
     */
    private static Pattern mathJaxPattern;

    /**
     * extensions to use on this parser
     */
    private final List<Extension> extensions;

    /**
     * Options for parser
     */
    private final DataHolder options;

    /**
     * Weak reference to the context
     */
    private WeakReference<Context> contextWeakRef;

    /**
     * Whether use MathJax
     */
    private boolean useMathJax;

    /**
     * Escape html
     */
    private boolean escapeHtml;

    /**
     * The styled css id, should be one of
     * {@link me.shouheng.easymark.EasyMarkViewer#DARK_STYLE_CSS} and
     * {@link me.shouheng.easymark.EasyMarkViewer#LIGHT_STYLE_CSS}
     */
    private int styleCssId;

    /**
     * The custom css content
     */
    private String customCssContent;

    /**
     * The result parse callback
     */
    private OnGetResultListener onGetResultListener;

    public static class Builder {
        private Context context;
        private boolean useMathJax;
        private boolean escapeHtml;
        private int styleCssId;
        private String customCssContent;
        private OnGetResultListener onGetResultListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setUseMathJax(boolean useMathJax) {
            this.useMathJax = useMathJax;
            return this;
        }

        public Builder setEscapeHtml(boolean escapeHtml) {
            this.escapeHtml = escapeHtml;
            return this;
        }

        public Builder setStyleCssId(int styleCssId) {
            this.styleCssId = styleCssId;
            return this;
        }

        public Builder setCustomCssContent(String customCssContent) {
            this.customCssContent = customCssContent;
            return this;
        }

        public Builder setOnGetResultListener(OnGetResultListener onGetResultListener) {
            this.onGetResultListener = onGetResultListener;
            return this;
        }

        public MarkdownParser build() {
            return new MarkdownParser(this);
        }
    }

    private MarkdownParser(Builder builder) {
        this.contextWeakRef = new WeakReference<>(builder.context);
        this.escapeHtml = builder.escapeHtml;
        this.useMathJax = builder.useMathJax;
        this.styleCssId = builder.styleCssId;
        this.customCssContent = builder.customCssContent;
        this.onGetResultListener = builder.onGetResultListener;

        /* Handle the extensions */
        extensions = new LinkedList<>();
        extensions.addAll(Arrays.asList(
                TablesExtension.create(),
                TaskListExtension.create(),
                AbbreviationExtension.create(),
                AutolinkExtension.create(),
                MarkExtension.create(),
                StrikethroughSubscriptExtension.create(),
                SuperscriptExtension.create(),
                FootnoteExtension.create(),
                AttributesExtension.create()));
        /* Add MathJax */
        if (useMathJax) {
            extensions.add(MathJaxExtension.create());
        }

        /* Handle options */
        options = new MutableDataSet()
                .set(FootnoteExtension.FOOTNOTE_REF_PREFIX, "[")
                .set(FootnoteExtension.FOOTNOTE_REF_SUFFIX, "]")
                .set(HtmlRenderer.FENCED_CODE_LANGUAGE_CLASS_PREFIX, "")
                .set(HtmlRenderer.FENCED_CODE_NO_LANGUAGE_CLASS, "nohighlight");
    }

    @Override
    protected String doInBackground(String...strings) {
        /* Prepare the parser */
        Parser parser = Parser.builder(options)
                .extensions(extensions)
                .build();

        /* Prepare the renderer */
        HtmlRenderer renderer = HtmlRenderer.builder(options)
                .escapeHtml(escapeHtml)
                .attributeProviderFactory(new IndependentAttributeProviderFactory() {
                    @Override
                    public AttributeProvider create(NodeRendererContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .nodeRendererFactory(new NodeRendererFactoryImpl())
                .extensions(extensions)
                .build();

        /* Get the css content */
        String cssContent = getCssContent();

        /* Handler MathJax before parse */
        String afterMJ = useMathJax ? handleMathJax(strings[0]) : strings[0];
        String noteHtml = renderer.render(parser.parse(afterMJ));

        return getHtml(cssContent, noteHtml);
    }

    /**
     * Get the final html used to display
     *
     * @param cssContent the styled css
     * @param noteHtml the note html
     * @return the full note html
     */
    private String getHtml(String cssContent, String noteHtml) {
        return "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <style type=\"text/css\">\n" +
                cssContent +
                "    </style>\n" +
                "    <script type=\"text/x-mathjax-config\">\n" +
                "        MathJax.Hub.Config({\n" +
                "            showProcessingMessages: false,\n" +
                "            messageStyle: 'none',\n" +
                "            showMathMenu: false,\n" +
                "            tex2jax: {\n" +
                "                inlineMath: [ ['$','$'], [\"\\\\(\",\"\\\\)\"] ],\n" +
                "                displayMath: [ ['$$','$$'], [\"\\\\[\",\"\\\\]\"] ]\n" +
                "            }\n" +
                "        });\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" async\n" +
                "        src=\"https://cdn.bootcss.com/mathjax/2.7.3/MathJax.js?config=TeX-MML-AM_CHTML\">\n" +
                "    </script>\n" +
                "    <link rel=\"dns-prefetch\" href=\"//cdn.mathjax.org\" />\n" +
                "</head>\n" +
                "<body>\n" +
                "    <article id=\"content\" class=\"markdown-body\">\n" +
                noteHtml +
                "</article>\n" +
                "<script>\n" +
                "    var imgs = document.getElementsByTagName(\"img\");\n" +
                "    var list = new Array();\n" +
                "    for(var i = 0; i < imgs.length; i++){\n" +
                "        list[i] = imgs[i].src;\n" +
                "    }\n" +
                "    for(var i = 0; i < imgs.length; i++){\n" +
                "        imgs[i].onclick = function() {\n" +
                "            jsCallback.showPhotosInGallery(this.src, list);\n" +
                "        }\n" +
                "    }\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
    }

    /**
     * Get the css of markdown content
     *
     * @return the css string
     */
    private String getCssContent() {
        if (TextUtils.isEmpty(customCssContent)) {
            if (contextWeakRef.get() != null) {
                return Utils.readAssetsContent(contextWeakRef.get(), styleCssId == DARK_STYLE_CSS ?
                        Constants.DARK_STYLE_CSS_ASSETS_NAME : Constants.LIGHT_STYLE_CSS_ASSETS_NAME);
            } else {
                return "";
            }
        } else {
            return customCssContent;
        }
    }

    /**
     * Handle MathJax, replace some chars in the MathJax expression for the performance
     *
     * @param content the content to handle
     * @return the handled result
     */
    private String handleMathJax(String content) {
        if (mathJaxPattern == null) {
            mathJaxPattern = Pattern.compile(Constants.MATH_JAX_REGEX_EXPRESSION);
        }

        Matcher matcher = mathJaxPattern.matcher(content);
        String matched, replaced;
        while (matcher.find()) {
            matched = matcher.group();
            replaced = matched.replace("\\\\", "\\\\\\\\");
            replaced = replaced.replace("^", "\\^");
            replaced = replaced.replace("{", "\\{");
            content = content.replace(matched, replaced);
        }

        return content;
    }

    @Override
    protected void onPostExecute(String html) {
        if (onGetResultListener != null) {
            onGetResultListener.onGetResult(html);
        }
    }

    /**
     * Custom attribute provider
     *
     * Extension point for adding/changing attributes on the primary HTML tag for a node.
     */
    public static class CustomAttributeProvider implements AttributeProvider {

        @Override
        public void setAttributes(final Node node, final AttributablePart part, final Attributes attributes) {
            // do nothing
        }
    }

    /**
     * Custom the node renderer
     *
     * Factory for instantiating new node renderers when rendering is done.
     */
    public static class NodeRendererFactoryImpl implements NodeRendererFactory {

        @Override
        public NodeRenderer create(DataHolder options) {
            return new NodeRenderer() {
                @Override
                public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
                    HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
                    set.add(getImageRenderingHandler());
//                    set.add(getMathJaxRenderingHandler());
                    return set;
                }
            };
        }

        private NodeRenderingHandler<Image> getImageRenderingHandler() {
            return new NodeRenderingHandler<>(Image.class, new CustomNodeRenderer<Image>() {
                @Override
                public void render(Image node, NodeRendererContext context, HtmlWriter html) {
                    if (!context.isDoNotRenderLinks()) {
                        String altText = new TextCollectingVisitor().collectAndGetText(node);

                        ResolvedLink resolvedLink = context.resolveLink(LinkType.IMAGE, node.getUrl().unescape(), null);
                        String url = resolvedLink.getUrl();

                        if (!node.getUrlContent().isEmpty()) {
                            // reverse URL encoding of =, &
                            String content = Escaping.percentEncodeUrl(node.getUrlContent())
                                    .replace("+", "%2B")
                                    .replace("%3D", "=")
                                    .replace("%26", "&amp;");
                            url += content;
                        }

                        final int index = url.indexOf('@');

                        if (index >= 0) {
                            String[] dimensions = url.substring(index + 1, url.length()).split("\\|");
                            url = url.substring(0, index);

                            if (dimensions.length == 2) {
                                String width = dimensions[0] == null || dimensions[0].equals("") ? "auto" : dimensions[0];
                                String height = dimensions[1] == null || dimensions[1].equals("") ? "auto" : dimensions[1];
                                html.attr("style", "width: " + width + "; height: " + height);
                            }
                        }

                        html.attr("src", url);
                        html.attr("alt", altText);

                        if (node.getTitle().isNotNull()) {
                            html.attr("title", node.getTitle().unescape());
                        }

                        html.srcPos(node.getChars()).withAttr(resolvedLink).tagVoid("img");
                    }
                }
            });
        }

        private NodeRenderingHandler<MathJax> getMathJaxRenderingHandler() {
            return new NodeRenderingHandler<>(MathJax.class, new CustomNodeRenderer<MathJax>() {
                @Override
                public void render(MathJax node, NodeRendererContext context, HtmlWriter html) {

                }
            });
        }
    }

    public interface OnGetResultListener {
        void onGetResult(String html);
    }
}

