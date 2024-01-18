package com.vangelnum.ailandmark.feature_history_search.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_history_search.data.SearchHistoryEntity

@Composable
fun SearchHistoryScreen(
    searchState: State<Resource<List<SearchHistoryEntity>>>,
    onNavigateToInformationAboutPlace: (String) -> Unit,
    onDelete:(SearchHistoryEntity)-> Unit,
) {
    when (searchState.value) {
        Resource.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Search History is empty")
            }
        }

        is Resource.Error -> {
            Text(text = "Error: ${(searchState.value as Resource.Error).message}")
        }

        Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            SearchHistorySuccess(
                successState = searchState.value as Resource.Success<List<SearchHistoryEntity>>,
                onNavigateToInformationAboutPlace = onNavigateToInformationAboutPlace,
                onDelete = onDelete
            )
        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchHistorySuccess(
    successState: Resource.Success<List<SearchHistoryEntity>>,
    onNavigateToInformationAboutPlace: (String) -> Unit,
    onDelete:(SearchHistoryEntity)-> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "History of search", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Outlined.History, contentDescription = "history of search", modifier = Modifier.size(28.dp))
            }
        }
        items(successState.data, key = {
            it.id
        }) { searchHistory ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement()
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
                            text = searchHistory.name,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF1976D2)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${(searchHistory.score * 100).toInt()}%",
                            color = Color(0xFF009688)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column (
                        modifier = Modifier.weight(1f)
                    ){
                        Button(
                            onClick = {
                                onNavigateToInformationAboutPlace(searchHistory.name)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD54F),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "More information", textAlign = TextAlign.Center)
                        }
                        Button(
                            onClick = {
                                onDelete(searchHistory)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF6868),
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Delete", textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}
