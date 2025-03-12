package com.example.jcnotes.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jcnotes.ui.screens.add.AddNoteScreen
import com.example.jcnotes.ui.screens.home.HomeScreen

object NavRoutes {
    const val HOME = "home"
    const val NOTE_DETAIL = "note/{noteId}"
    const val ADD_NOTE = "add_note"
    
    // Parametre ile rota oluşturmak için yardımcı fonksiyonlar
    fun noteDetail(noteId: Int) = "note/$noteId"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        // Ana Sayfa
        composable(NavRoutes.HOME) {
            HomeScreen(
                onNavigateToNote = { noteId -> 
                    noteId?.let {
                        navController.navigate(NavRoutes.noteDetail(it))
                    }
                },
                onNavigateToAddNote = {
                    navController.navigate(NavRoutes.ADD_NOTE)
                }
            )
        }
        
        // Not Detay Sayfası
        composable(
            route = NavRoutes.NOTE_DETAIL,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId")
            // Not detay sayfası burada olacak
            // NoteDetailScreen(
            //     noteId = noteId,
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }
        
        // Not Ekleme Sayfası
        composable(NavRoutes.ADD_NOTE) {
            AddNoteScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
