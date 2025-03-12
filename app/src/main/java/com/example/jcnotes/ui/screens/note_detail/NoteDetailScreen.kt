package com.example.jcnotes.ui.screens.note_detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.data.model.Task
import com.example.jcnotes.presentation.viewmodel.NoteDetailViewModel
import com.example.jcnotes.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Int?,
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val note by viewModel.note.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()
    val color by viewModel.color.collectAsStateWithLifecycle()
    val isEditing by viewModel.isEditing.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    
    var showColorPicker by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showTaskInput by remember { mutableStateOf(false) }
    var taskText by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    
    val selectedColor = remember(color) { Color(color) }
    
    val titleFocusRequester = remember { FocusRequester() }
    val contentFocusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val isDarkTheme = isSystemInDarkTheme()
    
    // Metin rengini arka plan rengine göre ayarla
    val textColor = if (isDarkTheme) {
        if (selectedColor == Color.White || selectedColor.luminance() > 0.7f) {
            darkNoteTextColor
        } else {
            darkNoteTextColor
        }
    } else {
        if (selectedColor == Color.White || selectedColor.luminance() > 0.7f) {
            lightNoteTextColor
        } else {
            lightNoteTextColor
        }
    }
    
    val colorOptions = listOf(
        Color(0xFFF28B82), // Kırmızı
        Color(0xFFFBBC04), // Sarı
        Color(0xFFF475), // Açık sarı
        Color(0xFFCBFF90), // Açık yeşil
        Color(0xFFA7FFEB), // Turkuaz
        Color(0xFFCAFFE3), // Mint
        Color(0xFFAECBFA), // Açık mavi
        Color(0xFFD7AEFB), // Mor
        Color(0xFFFDCFE8), // Pembe
        Color(0xFFE6C9A8), // Kahverengi
        Color(0xFFE8EAED), // Gri
        Color(0xFFFFFFFF)  // Beyaz
    )
    
    val formattedDate = remember(note) {
        note?.let { currentNote ->
            val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr"))
            sdf.format(Date(currentNote.timestamp!!))
        } ?: ""
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri",
                            tint = textColor
                        )
                    }
                },
                actions = {
                    // Renk seçme butonu
                    IconButton(onClick = { showColorPicker = true }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Renk Seç",
                            tint = textColor
                        )
                    }
                    
                    // Görev ekleme butonu
                    IconButton(onClick = { showTaskInput = true }) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Görev Ekle",
                            tint = textColor
                        )
                    }
                    
                    // Silme butonu
                    if (noteId != null) {
                        IconButton(onClick = { showDeleteConfirmation = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Sil",
                                tint = textColor
                            )
                        }
                    }
                    
                    // Kaydetme butonu
                    IconButton(
                        onClick = {
                            if (title.isNotBlank() || content.isNotBlank()) {
                                if (noteId == null) {
                                    // Yeni not
                                    viewModel.addNote(
                                        title,
                                        content,
                                        selectedColor.toArgb()
                                    )
                                } else {
                                    // Mevcut notu güncelle
                                    viewModel.updateNote(
                                        title,
                                        content,
                                        selectedColor.toArgb()
                                    )
                                }
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Kaydet",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = selectedColor,
                    navigationIconContentColor = textColor,
                    actionIconContentColor = textColor
                )
            )
        },
        containerColor = selectedColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Tarih gösterimi
            if (formattedDate.isNotEmpty()) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            // Başlık
            BasicTextField(
                value = title,
                onValueChange = { viewModel.updateTitle(it) },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                cursorBrush = SolidColor(textColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(titleFocusRequester)
                    .padding(bottom = 16.dp)
            ) { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (title.isEmpty()) {
                        Text(
                            text = "Başlık",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor.copy(alpha = 0.6f)
                            )
                        )
                    }
                    innerTextField()
                }
            }
            
            // İçerik
            BasicTextField(
                value = content,
                onValueChange = { viewModel.updateContent(it) },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = textColor
                ),
                cursorBrush = SolidColor(textColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(contentFocusRequester)
            ) { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (content.isEmpty()) {
                        Text(
                            text = "Not",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = textColor.copy(alpha = 0.6f)
                            )
                        )
                    }
                    innerTextField()
                }
            }
        }
    }

    // Renk seçici dialog
    if (showColorPicker) {
        AlertDialog(
            onDismissRequest = { showColorPicker = false },
            title = { Text("Renk Seç") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(6),
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(colorOptions.size) { index ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(colorOptions[index])
                                .border(
                                    width = 2.dp,
                                    color = if (selectedColor == colorOptions[index])
                                        MaterialTheme.colorScheme.primary
                                    else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    viewModel.updateColor(colorOptions[index].toArgb())
                                    showColorPicker = false
                                }
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Silme onay dialogu
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Notu Sil") },
            text = { Text("Bu notu silmek istediğinizden emin misiniz?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteNote { onNavigateBack() }
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Sil")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("İptal")
                }
            }
        )
    }
}

@Composable
fun isSystemInDarkTheme(): Boolean {
    return androidx.compose.foundation.isSystemInDarkTheme()
} 