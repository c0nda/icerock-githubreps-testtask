package com.vorontsov.githubrepsicerock.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iffly.compose.markdown.MarkdownView
import com.iffly.compose.markdown.config.AbstractMarkdownRenderPlugin
import com.iffly.compose.markdown.config.MarkdownRenderConfig
import com.iffly.compose.markdown.core.renders.BlockQuoteRenderer
import com.iffly.compose.markdown.core.renders.BreakLineRenderer
import com.iffly.compose.markdown.core.renders.CodeAnnotator
import com.iffly.compose.markdown.core.renders.FencedCodeBlockRenderer
import com.iffly.compose.markdown.core.renders.HeadingRenderer
import com.iffly.compose.markdown.core.renders.IndentedCodeBlockRenderer
import com.iffly.compose.markdown.core.renders.ListRenderer
import com.iffly.compose.markdown.core.renders.ParagraphRenderer
import com.iffly.compose.markdown.render.IBlockRenderer
import com.iffly.compose.markdown.style.CodeBlockTheme
import com.iffly.compose.markdown.style.CodeBlockTheme.CodeContentTheme
import com.iffly.compose.markdown.style.ListTheme
import com.iffly.compose.markdown.style.MarkdownTheme
import com.vladsch.flexmark.ast.BlockQuote
import com.vladsch.flexmark.ast.BulletList
import com.vladsch.flexmark.ast.FencedCodeBlock
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.IndentedCodeBlock
import com.vladsch.flexmark.ast.ListBlock
import com.vladsch.flexmark.ast.OrderedList
import com.vladsch.flexmark.ast.Paragraph
import com.vladsch.flexmark.ast.ThematicBreak
import com.vladsch.flexmark.util.ast.Block
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownBody
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownCode
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownCodeBlock
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownHeading1
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownHeading2
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownHeading3
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownHeading4
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownHeading5
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownHeading6
import com.vorontsov.githubrepsicerock.presentation.ui.theme.MarkdownListMarker

@Composable
fun MarkdownContent(
    markdown: String,
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 16.dp
) {
    val markdownTheme =
        MarkdownTheme(
            textStyle = MarkdownBody,
            headStyle =
                mapOf(
                    1 to MarkdownHeading1,
                    2 to MarkdownHeading2,
                    3 to MarkdownHeading3,
                    4 to MarkdownHeading4,
                    5 to MarkdownHeading5,
                    6 to MarkdownHeading6,
                ),
            code = MarkdownCode,
            breakLineHeight = 24.dp,
            listTheme = ListTheme(
                markerTextStyle = MarkdownListMarker,
                markerSpacerWidth = 0.dp,
                tightListSpacerHeight = 2.dp
            ),
            codeBlockTheme = CodeBlockTheme(
                backgroundColor = MaterialTheme.colorScheme.outlineVariant,
                borderColor = MaterialTheme.colorScheme.outlineVariant,
                shape = RectangleShape,
                borderWidth = 0.dp,
                blockModifier = Modifier,
                showHeader = false,
                showCopyButton = false,
                contentTheme = CodeContentTheme(
                    showLineNumber = false,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier,
                    codeTextStyle = MarkdownCodeBlock
                )
            )
        )

    val config = remember(horizontalPadding) {
        MarkdownRenderConfig.Builder()
            .markdownTheme(markdownTheme)
            .addPlugin(ListStartPaddingPlugin(horizontalPadding))
            .build()
    }

    MarkdownView(
        content = markdown,
        markdownRenderConfig = config,
        modifier = modifier
    )
}

private class StartPaddedListRenderer<T : ListBlock> : ListRenderer<T>() {
    @Composable
    override fun Invoke(node: T, modifier: Modifier) {
        super.Invoke(node, modifier.padding(start = 8.dp))
    }
}

private class HorizontalPaddedBlockRenderer<T : Block>(
    private val inner: IBlockRenderer<T>,
    private val horizontalPadding: Dp,
) : IBlockRenderer<T> {
    override fun shouldSkipRender(node: T): Boolean = inner.shouldSkipRender(node)

    override fun supportTextMode(): Boolean = inner.supportTextMode()

    @Composable
    override fun Invoke(node: T, modifier: Modifier) {
        inner.Invoke(node, modifier.padding(horizontal = horizontalPadding))
    }
}

private class ListStartPaddingPlugin(
    private val horizontalPadding: Dp,
) : AbstractMarkdownRenderPlugin() {
    override fun blockRenderers(): Map<Class<out Block>, IBlockRenderer<*>> =
        mapOf(
            Paragraph::class.java to HorizontalPaddedBlockRenderer(ParagraphRenderer(), horizontalPadding),
            Heading::class.java to HorizontalPaddedBlockRenderer(HeadingRenderer(), horizontalPadding),
            OrderedList::class.java to HorizontalPaddedBlockRenderer(StartPaddedListRenderer<OrderedList>(), horizontalPadding),
            BulletList::class.java to HorizontalPaddedBlockRenderer(StartPaddedListRenderer<BulletList>(), horizontalPadding),
            ThematicBreak::class.java to HorizontalPaddedBlockRenderer(BreakLineRenderer(), horizontalPadding),
            BlockQuote::class.java to HorizontalPaddedBlockRenderer(BlockQuoteRenderer(), horizontalPadding),
            FencedCodeBlock::class.java to FencedCodeBlockRenderer(codeAnnotator = PlainCodeAnnotator),
            IndentedCodeBlock::class.java to IndentedCodeBlockRenderer(codeAnnotator = PlainCodeAnnotator),
        )
}

private val PlainCodeAnnotator: CodeAnnotator =
    CodeAnnotator { code, _, _ -> AnnotatedString(code) }