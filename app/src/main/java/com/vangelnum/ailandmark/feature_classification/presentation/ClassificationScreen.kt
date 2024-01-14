package com.vangelnum.ailandmark.feature_classification.presentation

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vangelnum.ailandmark.feature_classification.data.ClassificationResult
import com.vangelnum.ailandmark.feature_classification.domain.Classification

@Composable
fun InformationScreen(
    information: ClassificationResult,
    onNavigateToInformationAboutPlace: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Image(
                        bitmap = information.imageBitmap.asImageBitmap(),
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(information.classifications) { classification ->
                ClassificationRow(
                    classification = classification,
                    onNavigateToInformationAboutPlace
                )
            }
        }
    }
}

@Composable
fun ClassificationRow(
    classification: Classification,
    onNavigateToInformationAboutPlace: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = classification.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(classification.score * 100).toInt()}%",
                    color = Color(0xFF009688)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onNavigateToInformationAboutPlace(classification.name)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD54F),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(text = "More information", textAlign = TextAlign.Center)
            }
        }
    }
}


@Preview(showSystemUi = true, device = "id:pixel_3")
@Composable
fun PreviewInformationScreen() {
    val places = listOf(
        Classification("A VERY BIG PLACE NAME", 0.8f),
        Classification("Paris", 0.6f),
        Classification("New York", 0.4f)
    )

    InformationScreen(
        onNavigateToInformationAboutPlace = {

        },
        information = ClassificationResult(
            classifications = places,
            imageBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        )
    )
}
