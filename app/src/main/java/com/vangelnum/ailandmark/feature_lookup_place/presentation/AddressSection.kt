package com.vangelnum.ailandmark.feature_lookup_place.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.vangelnum.ailandmark.feature_lookup_place.data.AddressX

@Composable
fun AddressSection(address: AddressX) {
    Column {
        Text(
            "Address"
        )
        AddressItem("Road", address.road)
        AddressItem("City", address.city)
        AddressItem("State", address.state)
        AddressItem("Postcode", address.postcode)
        AddressItem("Country", address.country)
        AddressItem("Country Code", address.countryCode)
    }
}

@Composable
fun AddressItem(title: String, value: String?) {
    if (value != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title)
            Text(value)
        }
    }
}