package com.peter.landing.ui.greeting

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.peter.landing.data.local.terms.Terms

@Composable
fun LegalText(
    loadTerms: (Terms.Type) -> Unit,
    modifier: Modifier
) {
    val termsTypeTag = "termsType"
    val linkStyle = SpanStyle(
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold
    )

    val annotatedText = buildAnnotatedString {
        append("请阅读上岸单词的")

        pushStringAnnotation(
            tag = termsTypeTag, annotation = Terms.Type.SERVICE.name
        )
        withStyle(
            style = linkStyle
        ) {
            append("服务条款")
        }
        pop()

        append("，")

        pushStringAnnotation(
            tag = termsTypeTag, annotation = Terms.Type.PRIVACY.name
        )
        withStyle(
            style = linkStyle
        ) {
            append("隐私政策")
        }
        pop()

        append("和")

        pushStringAnnotation(
            tag = termsTypeTag, annotation = Terms.Type.ACKNOWLEDGE.name
        )
        withStyle(
            style = linkStyle
        ) {
            append("软件声明")
        }
        pop()

        append("。")
    }

    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier,
        onClick = { offset ->

            annotatedText.getStringAnnotations(
                tag = termsTypeTag, start = offset, end = offset
            ).firstOrNull()?.let { annotation ->
                loadTerms(Terms.Type.valueOf(annotation.item))
            }

        }
    )
}
