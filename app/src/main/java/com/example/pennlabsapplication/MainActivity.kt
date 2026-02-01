package com.example.pennlabsapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.pennlabsapplication.ui.theme.PennLabsApplicationTheme
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import kotlinx.coroutines.launch
import com.example.pennlabsapplication.googlemaps.MapsFragment
import com.google.accompanist.permissions.rememberMultiplePermissionsState

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.accompanist.permissions.ExperimentalPermissionsApi

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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MyScreen(
    foodTruckViewModel: FoodTruckViewModel = FoodTruckViewModel()
) {
    val mainCol = Color(0xFF9933ff)
    val tabs = listOf("Food Trucks", "Maps")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    var showFoodTruckInfo by remember { mutableStateOf(false) }
    var foodTruckInfoId by remember { mutableIntStateOf(0) }
    var expandedFilterList by remember { mutableStateOf(false) }

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
                    1 -> MapsFragmentContainer()
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
        if (fragmentManager.findFragmentByTag("MAPS_FRAGMENT") == null) {
            fragmentManager.commit {
                setReorderingAllowed(true)
                replace(containerId, MapsFragment(), "MAPS_FRAGMENT")
            }
        }

        onDispose {
            fragmentManager.findFragmentByTag("MAPS_FRAGMENT")?.let {
                fragmentManager.commit {
                    remove(it)
                }
            }
        }
    }
}
