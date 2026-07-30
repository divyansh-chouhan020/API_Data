package com.dlancers.api_data.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import Product
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.ContentScale


fun String.cleanUrl(): String {
    return this.replace("[", "")
        .replace("]", "")
        .replace("\"", "")
        .trim()
}
@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier
){

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape =RoundedCornerShape(12.dp),
        elevation =CardDefaults.cardElevation( defaultElevation = 4.dp)
    ){
        Column(
            modifier =Modifier.padding(8.dp),
            verticalArrangement =Arrangement.spacedBy( 8.dp )
        ){
            AsyncImage(
                model =product.images?.firstOrNull()?.cleanUrl(),
                contentDescription =product.description ?: "No Description",
                modifier =Modifier.fillMaxWidth().height(160.dp) ,
                        contentScale = ContentScale.Crop
            )
            Text (
                text= product.name ?: " Unknown Product Name ",
                style = MaterialTheme.typography.titleMedium,
                maxLines=1
            )
            Text (
                text = "$${product.price ?: 0.0}",
                style = MaterialTheme.typography.bodyMedium
            )
          }

    }
}