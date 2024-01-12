package com.vangelnum.ailandmark.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Streetview
import androidx.compose.material.icons.outlined.Villa
import androidx.compose.material.icons.outlined.WhereToVote
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.outlined.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vangelnum.ailandmark.data.Address
import com.vangelnum.ailandmark.data.PlaceResponse
import com.vangelnum.ailandmark.helpers.Resource

@Composable
fun InformationAboutPlace(
    state: Resource<List<PlaceResponse>?>
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
            if (state.data?.isNotEmpty() == true) {
                LazyColumn {
                    items(state.data) { place ->
                        PlaceCard(place = place)
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

@Composable
fun PlaceCard(place: PlaceResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEDDDF)), // Light pink background color
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = place.displayName, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Type: ${place.type}", style = MaterialTheme.typography.bodyMedium)
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFC97B99),
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEDDDF))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Address Type: ${place.addressType}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFC97B99),
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEDDDF))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Latitude: ${place.lat}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Longitude: ${place.lon}", style = MaterialTheme.typography.bodyMedium)

            place.address?.let { address ->
                Spacer(modifier = Modifier.height(8.dp))
                AddressSection(address = address)
            }
        }
    }
}

@Composable
fun AddressSection(address: Address) {
    Column {
        Text(
            "Address",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFFC97B99)
        )
        AddressItem(Icons.Outlined.Room, "Road", address.road)
        AddressItem(Icons.Outlined.Villa, "Hamlet", address.hamlet)
        AddressItem(Icons.Outlined.LocationCity, "Town", address.town)
        AddressItem(Icons.Outlined.LocationOn, "Village", address.village)
        AddressItem(Icons.Outlined.Streetview, "City", address.city)
        AddressItem(Icons.Outlined.ZoomIn, "State District", address.state_district)
        AddressItem(Icons.Outlined.ZoomOut, "State", address.state)
        AddressItem(Icons.Outlined.PostAdd, "Postcode", address.postcode)
        AddressItem(Icons.Outlined.WhereToVote, "Country", address.country)
        AddressItem(Icons.Outlined.Send, "Country Code", address.country_code)
    }
}

@Composable
fun AddressItem(icon: ImageVector, title: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFFC97B99),
            modifier = Modifier.size(16.dp)
        )
        Text(title, style = MaterialTheme.typography.bodySmall, color = Color(0xFFC97B99))
        Text(value, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}