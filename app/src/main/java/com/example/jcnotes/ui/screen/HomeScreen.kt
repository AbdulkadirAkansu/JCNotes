package com.example.jcnotes.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.ui.Screen
import com.example.jcnotes.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val notes = viewModel.notes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.AddNote.route) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Note")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (notes.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "No Notes Available",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            NoteList(
                notes = notes.value,
                onNoteClick = { noteId ->
                    navController.navigate(Screen.Detail.createRoute(noteId))
                },
                onNoteLongClick = { note -> viewModel.deleteNote(note)},
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun NoteList(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit,
    onNoteLongClick: (Note)-> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(notes) { note ->
            NoteItem(
                note = note,
                onClick = { onNoteClick(note.id) },
                onLongClick = { onNoteLongClick(note)}
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
