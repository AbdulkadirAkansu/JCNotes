package com.example.jcnotes.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    noteId: Int?,
    viewModel: HomeViewModel
) {
    // noteId ile ilgili notu dinleyerek alıyoruz
    val note by viewModel.getNoteById(noteId ?: 0).collectAsState(initial = null)

    // Note içeriği geldikten sonra title ve content state'lerini dolduruyoruz
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var isEditing by remember { mutableStateOf(false) }

    // Not içeriği her güncellendiğinde title ve content state'lerini güncelleriz
    LaunchedEffect(note) {
        title = note?.title ?: ""
        content = note?.content ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Note")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (isEditing) {
                    // Düzenleme modu: TextField göster
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Content") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Kaydetme butonu
                    Button(
                        onClick = {
                            if (note != null && title.isNotBlank() && content.isNotBlank()) {
                                val updatedNote = note!!.copy(
                                    title = title,
                                    content = content,
                                    timestamp = System.currentTimeMillis()
                                )
                                viewModel.updateNote(updatedNote)
                                isEditing = false
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Save")
                    }
                } else {
                    // Görüntüleme modu: Sadece Text göster
                    Text(text = title, style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}
