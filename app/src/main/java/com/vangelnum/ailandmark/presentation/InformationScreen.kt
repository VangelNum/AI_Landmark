package com.vangelnum.ailandmark.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vangelnum.ailandmark.data.ClassificationResult

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InformationScreen(
    information: ClassificationResult
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF1EFEF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Image(
                bitmap = information.imageBitmap.asImageBitmap(),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Text(
                text = "AI thinks that it can be:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E2E2E)
            )
            Spacer(modifier = Modifier.height(8.dp))

            information.classifications.forEach { classification ->
                println(classification.name)
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "${classification.name}:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3F51B5)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${(classification.score * 100).toInt()}%",
                        color = Color(0xFF009688)
                    )
                }
            }
        }
    }
}
