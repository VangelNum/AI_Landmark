package com.vangelnum.ailandmark.feature_lookup_place.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.vangelnum.ailandmark.feature_lookup_place.data.Extratags

@Composable
fun TagsSection(tags: Extratags) {
    TagsItem("Architect", tags.architect)
    TagsItem("Architect Wikidata", tags.architectWikidata)
    TagsItem("Building", tags.building)
    TagsItem("Description", tags.description)
    TagsItem("Heritage", tags.heritage)
    TagsItem("Heritage Operator", tags.heritageOperator)
    TagsItem("Heritage Website", tags.heritageWebsite)
    TagsItem("Image", tags.image)
    TagsItem("Tourism", tags.tourism)
    TagsItem("Wheelchair", tags.wheelchair)
    TagsItem("Wikidata", tags.wikidata)
    TagsItem("Wikipedia", tags.wikipedia)
    TagsItem("Year of Construction", tags.yearOfConstruction)
    TagsItem("Website", tags.website)
    TagsItem("Opening Hours", tags.openingHours)
}

@Composable
fun TagsItem(title: String, value: String?) {
    val context = LocalContext.current
    if (value != null) {
        when {
            title.equals("Opening Hours", ignoreCase = true) -> {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = 16.sp)) {
                            append("Opening Hours: ")
                        }
                        withStyle(style = SpanStyle(color = Color(0xFFFF6868), fontSize = 16.sp)) {
                            append(value)
                        }
                    }
                )
            }
            title.equals("Website", ignoreCase = true) -> {
                ClickableTextWithUrl(title, value, context)
            }
            else -> {
                Text("$title: $value")
            }
        }
    }
}

@Composable
fun ClickableTextWithUrl(title: String, url: String, context: android.content.Context) {
    ClickableText(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)) {
                append("$title: ")
            }
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, color = Color(0xFF40A2D8), fontSize = 16.sp)) {
                append(url)
            }
        },
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }
    )
}
