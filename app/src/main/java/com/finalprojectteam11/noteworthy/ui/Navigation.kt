package com.finalprojectteam11.noteworthy.ui

import androidx.compose.foundation.layout.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.currentBackStackEntryAsState
import com.finalprojectteam11.noteworthy.R

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddNote : Screen("add_note")
    object Settings : Screen("settings")
    object EditNote : Screen("edit_note/{note_id}")
    object Search: Screen("search/{query}")
}
@Composable
fun AppNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.AddNote.route) { NoteScreen(navController, "")  }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable(
            Screen.EditNote.route,
            arguments = listOf(
                navArgument("note_id") {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            NoteScreen(navController, backStackEntry.arguments?.getString("note_id"))
        }
        composable(
            Screen.Search.route,
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                },
            )
        ) { backStackEntry ->
            SearchScreen(navController, backStackEntry.arguments?.getString("query"))

        }
    }
}

fun getTitleForScreen(screen: Screen): String {
    return when (screen) {
        is Screen.Home -> "Noteworthy"
        is Screen.AddNote -> "Add New Note"
        is Screen.Settings -> "Settings"
        else -> {""}
    }
}

@Composable
fun FloatingActionButton(navController: NavController){
    if (navController.currentBackStackEntryAsState().value?.destination?.route == Screen.Home.route) {
        var context = LocalContext.current
        FloatingActionButton(
            shape = CircleShape,
            onClick = {
                navController.navigate(Screen.AddNote.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            backgroundColor = Color(0xFF3694C9),
            contentColor = Color.White,
            modifier = Modifier
                .size(72.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.edit_fill1_wght400_grad0_opsz48),
                contentDescription = "Add Note",
                modifier = Modifier
                    .padding(18.dp)
            )
        }
    }
}


@Composable
fun TopNavBar (navController: NavHostController) {
    val canGoBack = navController.previousBackStackEntry != null
    val appBarTitle = when (navController.currentBackStackEntry?.destination?.route) {
        Screen.AddNote.route -> "Add Note"
        Screen.Settings.route -> "Settings"
        Screen.EditNote.route -> "Edit Note"
        Screen.Search.route -> "Search Results"
        else -> "Noteworthy"
    }

    TopAppBar(
        title = { Text(appBarTitle) },
        navigationIcon = if (canGoBack) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        } else null,
        backgroundColor = Color(0xFF3694C9),
        contentColor = Color.White
    )
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(Screen.Home, Screen.AddNote, Screen.Settings)
    // Display regular TopAppBar for non-child screens
    if (navController.currentBackStackEntryAsState().value?.destination?.route == Screen.Home.route) {
        BottomAppBar(
            cutoutShape = CircleShape,
            contentColor = Color(0xFF3694C9),
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(0.dp),
            elevation = 0.dp // Remove elevation to avoid multiple layers
        ) {
            BottomNavigation(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(0.dp),
                contentColor = Color(0xFF3694C9),
                backgroundColor = Color(0xFFFFFFFF),
                elevation = 0.dp // Remove elevation to avoid multiple layers
            ) {
                items.forEach { screen ->
                    if (screen is Screen.AddNote) return@forEach
                    BottomNavigationItem(
                        icon = {
                            when (screen) {
                                is Screen.Home -> Icon(
                                    Icons.Filled.Home,
                                    contentDescription = "Home"
                                )
                                is Screen.Settings -> Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = "Settings"
                                )
                                else -> Unit
                            }
                        },
                        label = {
                            Text(
                                text = when (screen) {
                                    is Screen.Home -> "Home"
                                    is Screen.Settings -> "Settings"
                                    else -> ""
                                }
                            )
                        },
                        selected = navController.currentBackStackEntryAsState().value?.destination?.route == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        },
                        selectedContentColor = Color.Black,
                        unselectedContentColor = Color.Gray,
                    )
                }
            }
        }
    }
}