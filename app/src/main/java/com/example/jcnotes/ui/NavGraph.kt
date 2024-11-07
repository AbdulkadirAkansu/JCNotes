package com.example.jcnotes.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jcnotes.ui.screen.AddNoteScreen
import com.example.jcnotes.ui.screen.DetailScreen
import com.example.jcnotes.ui.screen.HomeScreen
import com.example.jcnotes.ui.viewmodel.HomeViewModel


sealed class Screen(val route: String){
    object Home : Screen("home")
    object Detail : Screen("detail/{noteId}"){
        fun createRoute(noteId: Int) = "detail/$noteId"
    }
    object AddNote : Screen("add_note")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            HomeScreen(navController = navController, homeViewModel)
        }
        composable(Screen.Detail.route) { navBackStackEntry ->
            val noteId = navBackStackEntry.arguments?.getString("noteId")?.toInt()
            val homeViewModel: HomeViewModel = hiltViewModel()
            DetailScreen(navController = navController, noteId = noteId, homeViewModel)
        }
        composable(Screen.AddNote.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            AddNoteScreen(navController = navController, viewModel = homeViewModel)
        }
    }
}
