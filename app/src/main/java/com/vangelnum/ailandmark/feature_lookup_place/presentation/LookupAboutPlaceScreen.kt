package com.vangelnum.ailandmark.feature_lookup_place.presentation

import android.view.MotionEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vangelnum.ailandmark.feature_core.helpers.Resource
import com.vangelnum.ailandmark.feature_lookup_place.data.LookupPlaceInfo

@Composable
@AvoidUsingLazyColumn("Using LazyColumn is causing lag with maps, so a regular Column is used instead.")
fun LookupAboutPlace(
    state: Resource<List<LookupPlaceInfo>>
) {
    when (state) {
        Resource.Empty -> {}
        is Resource.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }

        Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            if (state.data.isNotEmpty()) {
                var scrollEnable by remember {
                    mutableStateOf(true)
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState(), scrollEnable),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.data.forEach { place ->
                        Box(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                            PlaceCard(place = place) {
                                scrollEnable = it
                            }
                        }
                    }
                    val license = state.data.first().license
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "License", textAlign = TextAlign.Center)
                        Text(text = license, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No results found")
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlaceCard(
    place: LookupPlaceInfo, onScrollEnableChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SelectionContainer {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInteropFilter(onTouchEvent = {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    onScrollEnableChange(true)
                                    false
                                }

                                else -> true
                            }
                        }),
                ) {

                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Type: ${place.type}"
                    )
                    Text(
                        text = "Address Type: ${place.addressType}"
                    )
                    Text(
                        text = "Latitude: ${place.latitude}"
                    )
                    Text(
                        text = "Longitude: ${place.longitude}"
                    )
                    AddressSection(address = place.address)
                    place.extraTags?.let { TagsSection(tags = it) }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            MapSection(
                place = place, onScrollEnableChange = onScrollEnableChange
            )
        }
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class AvoidUsingLazyColumn(val reason: String)