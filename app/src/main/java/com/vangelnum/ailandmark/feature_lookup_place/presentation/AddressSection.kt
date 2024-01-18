package com.vangelnum.ailandmark.feature_lookup_place.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.vangelnum.ailandmark.feature_lookup_place.data.AddressX

@Composable
fun AddressSection(address: AddressX) {
    Column {
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
        Text("$title: $value")
    }
}