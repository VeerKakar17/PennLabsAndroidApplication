package com.example.pennlabsapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.IconButton
import androidx.compose.ui.unit.Dp

data class FoodTruck(
    val id: Int,
    val name: String,
    val rating: Double,
    val location: String,
    val coordinates: String,
    val phone: String? = null,
    val hours: String? = null,
    val categories: String? = null
)

class FoodTruckViewModel : ViewModel() {
    private val _foodTrucks = MutableStateFlow (
        listOf (
            FoodTruck(
                id = 0,
                name = "Mikey's Grill",
                rating = 5.0,
                location = "34th and Market",
                coordinates = "39.955854, -75.191432",
                categories = "American, Sandwiches"
            ),

            FoodTruck(
                id = 1,
                name = "Lyn's",
                rating = 4.5,
                location = "36th and Spruce",
                coordinates = "39.950792, -75.195304",
                categories = "American, Sandwiches",
                hours = "6:00am–3:00pm (MF)"
            ),

            FoodTruck(
                id = 2,
                name = "John's Lunch Cart",
                rating = 4.5,
                location = "33rd and Spruce",
                coordinates = "39.950331, -75.191717",
                categories = "American, Sandwiches"
            ),

            FoodTruck(
                id = 3,
                name = "Rami's",
                rating = 4.5,
                location = "40th and Locust",
                coordinates = "39.952977, -75.202820",
                categories = "Middle-Eastern"
            ),

            FoodTruck(
                id = 4,
                name = "Sandwich Cart at 35th/Market",
                rating = 4.5,
                location = "35th and Market",
                coordinates = "39.956213, -75.193475",
                categories = "American, Sandwiches"
            ),

            FoodTruck(
                id = 5,
                name = "Gul's Breakfast and Lunch Cart",
                rating = 4.5,
                location = "36th and Market",
                coordinates = "39.956191, -75.194148",
                categories = "American, Sandwiches"
            ),

            FoodTruck(
                id = 6,
                name = "Pete's Little Lunch Box",
                rating = 4.5,
                location = "33rd and Lancaster",
                coordinates = "39.956425, -75.189327",
                categories = "American, Sandwiches",
                phone = "(215) 605-1228",
                hours = "6:00am–4:00pm (MF), 6:00am–4:00pm (Sa)"
            ),

            FoodTruck(
                id = 7,
                name = "Magic Carpet at 36th/Spruce",
                rating = 4.5,
                location = "36th and Spruce",
                coordinates = "39.950792, -75.195304",
                categories = "Vegetarian",
                phone = "(215) 327-7533",
                hours = "11:30am–3:00pm (MF)"
            ),

            FoodTruck(
                id = 8,
                name = "Troy Mediterranean at 38th/Spruce",
                rating = 4.5,
                location = "38th and Spruce",
                coordinates = "39.951287, -75.199274",
                categories = "Middle-Eastern",
                phone = "(610) 659-8855"
            ),

            FoodTruck(
                id = 9,
                name = "Fruit Truck at 37th/Spruce",
                rating = 4.5,
                location = "37th and Spruce",
                coordinates = "39.951020, -75.197285",
                categories = "Fruit",
                hours = "11:30am–6:30pm (MF)"
            ),

            FoodTruck(
                id = 10,
                name = "Memo's Lunch Truck",
                rating = 4.5,
                location = "33rd and Arch",
                coordinates = "39.957611, -75.189076",
                categories = "Middle-Eastern",
                phone = "(215) 939-4386",
                hours = "12:00pm–12:00am (MF)"
            ),

            FoodTruck(
                id = 11,
                name = "Hanan House of Pita",
                rating = 4.0,
                location = "38th and Walnut",
                coordinates = "39.953640, -75.198767",
                categories = "Middle-Eastern",
                phone = "(267) 226-5692",
                hours = "11:00am–8:00pm (MF, Sa)"
            ),

            FoodTruck(
                id = 12,
                name = "Fruit Truck at 35th/Market",
                rating = 4.0,
                location = "35th and Market",
                coordinates = "39.956213, -75.193475",
                categories = "Fruit"
            ),

            FoodTruck(
                id = 13,
                name = "Troy Mediterranean at 40th/Spruce",
                rating = 4.0,
                location = "40th and Spruce",
                coordinates = "39.951756, -75.203074",
                categories = "Middle-Eastern",
                phone = "(610) 659-8855"
            ),

            FoodTruck(
                id = 14,
                name = "Sonic's",
                rating = 4.0,
                location = "37th and Spruce",
                coordinates = "39.951030, -75.197268",
                categories = "American, Sandwiches",
                hours = "8:30am–5:30pm (MF)"
            ),

            FoodTruck(
                id = 15,
                name = "Ali Baba",
                rating = 4.0,
                location = "37th and Walnut",
                coordinates = "39.953388, -75.196763",
                categories = "Middle-Eastern",
                hours = "8:00am–4:00pm (MF)"
            )
        )
    )

    val foodTrucks: StateFlow<List<FoodTruck>> = _foodTrucks

    fun sortByRating() {
        viewModelScope.launch {
            _foodTrucks.value = _foodTrucks.value.sortedBy { -it.rating }
        }
    }

    fun sortByName() {
        viewModelScope.launch {
            _foodTrucks.value = _foodTrucks.value.sortedBy { it.name }
        }
    }
}


@Composable
fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier,
    starSize: Dp = 18.dp
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            val filled = rating >= index + 1
            val half = rating >= index + 0.5 && rating < index + 1

            val icon = when {
                filled -> Icons.Filled.Star
                half -> Icons.AutoMirrored.Filled.StarHalf
                else -> Icons.Filled.StarOutline
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFC107), // gold
                modifier = Modifier.size(starSize)
            )
        }
    }
}


@Composable
fun FoodTruckRow(place: FoodTruck, onClick: (Int) -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { onClick(place.id) },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = place.location,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            StarRating(
                rating = place.rating,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Divider()
    }
}

@Composable
fun FoodTruckContent(foodTruckViewModel: FoodTruckViewModel, onItemClick: (Int) -> Unit) {
    val foodTruckItems by foodTruckViewModel.foodTrucks.collectAsState()

    LazyColumn{
        items(
            items = foodTruckItems
        ) { foodTruck: FoodTruck ->
            FoodTruckRow(foodTruck, onItemClick);
        }
    }
}

@Composable
fun FoodTruckInfo(foodTruckModel: FoodTruckViewModel, id: Int, backArrowClicked: () -> Unit) {
    val currentList: List<FoodTruck> = foodTruckModel.foodTrucks.collectAsState().value
    val foodTruck = currentList[id]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF9933ff))
                .padding(16.dp)
        ) {
            IconButton(
                onClick = backArrowClicked
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = foodTruck.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            StarRating(
                rating = foodTruck.rating,
                modifier = Modifier
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            InfoRow(
                icon = Icons.Filled.LocationOn,
                label = "Location",
                value = foodTruck.location
            )

            foodTruck.phone?.let {
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow(
                    icon = Icons.Filled.Phone,
                    label = "Phone",
                    value = it
                )
            }

            foodTruck.categories?.let {
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow(
                    icon = Icons.Filled.Category,
                    label = "Categories",
                    value = it
                )
            }

            foodTruck.hours?.let {
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow(
                    icon = Icons.Filled.Schedule,
                    label = "Hours",
                    value = it
                )
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF9933ff),
            modifier = Modifier
                .size(24.dp)
                .padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}