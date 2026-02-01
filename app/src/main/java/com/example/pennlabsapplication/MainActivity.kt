package com.example.pennlabsapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.example.pennlabsapplication.googlemaps.MapsFragment
import com.example.pennlabsapplication.ui.theme.PennLabsApplicationTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PennLabsApplicationTheme {
                MyScreen()
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(
    foodTruckViewModel: FoodTruckViewModel = FoodTruckViewModel()
) {
    val context = LocalContext.current
    val mainCol = Color(0xFF9933ff)
    val tabs = listOf("Food Trucks", "Maps")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    var showFoodTruckInfo by remember { mutableStateOf(false) }
    var foodTruckInfoId by remember { mutableIntStateOf(0) }
    var expandedFilterList by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf<Location?>(null) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener { loc: Location? ->
                        location = loc
                    }
                }
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener { loc: Location? ->
                        location = loc
                    }
                }
            }
            else -> {
                // No location access granted.
            }
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Scaffold(
        floatingActionButton = {
            Box {
                if (pagerState.currentPage == 0 && !showFoodTruckInfo) {
                    FloatingActionButton(
                        onClick = { expandedFilterList = !expandedFilterList },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                    }

                    if (expandedFilterList) {
                        DropdownMenu(
                            expanded = expandedFilterList,
                            onDismissRequest = {
                                expandedFilterList = false
                            },
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sort by Rating") },
                                onClick = {
                                    expandedFilterList = false
                                    foodTruckViewModel.sortByRating()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Sort by Name") },
                                onClick = {
                                    expandedFilterList = false
                                    foodTruckViewModel.sortByName()
                                }
                            )
                        }
                    }
                }
            }

        },
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Food Truck Finder",
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = mainCol
                    )
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.DarkGray
                )

                val coroutineScope = rememberCoroutineScope()

                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = mainCol,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabIndicator(tabPositions[pagerState.currentPage])
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, label ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                        showFoodTruckInfo = false},
                            text = {
                                Text(
                                    text = label,
                                    color = if (pagerState.currentPage == index) Color.White
                                    else Color.White.copy(alpha = 0.7f)
                                )
                            }
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.DarkGray
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = pagerState.currentPage == 0
            ) { page ->
                when (page) {
                    0 -> if (!showFoodTruckInfo) {
                            FoodTruckContent(foodTruckViewModel, fun(id: Int) {
                                showFoodTruckInfo = true
                                foodTruckInfoId = id
                            })
                        } else {
                        FoodTruckInfo( foodTruckViewModel, foodTruckInfoId, {
                            showFoodTruckInfo = false
                        })
                        }
                    1 -> MapsFragmentContainer(location)
                }
            }
        }
    }
}

@Composable
private fun TabIndicator(tabPosition: TabPosition) {
    Box(
        modifier = Modifier
            .tabIndicatorOffset(tabPosition)
            .height(3.dp)
            .background(Color.White)
    )
}

@Composable
fun MapsFragmentContainer(
    location: Location?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fragmentManager =
        (context as FragmentActivity).supportFragmentManager

    val containerId = remember { View.generateViewId() }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            FragmentContainerView(ctx).apply {
                id = containerId
            }
        }
    )

    DisposableEffect(Unit) {
        if (location != null) {
            val fragment = MapsFragment.newInstance(location)
            Log.d("reached", "reached");
            fragmentManager.commit {
                setReorderingAllowed(true)
                replace(containerId, fragment, "MAPS_FRAGMENT")
            }
        }

        onDispose { }
    }

    DisposableEffect(location) {
        val fragment = MapsFragment.newInstance(location)
        Log.d("reached", "reached");
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace(containerId, fragment, "MAPS_FRAGMENT")
        }

        onDispose { }
    }
}
