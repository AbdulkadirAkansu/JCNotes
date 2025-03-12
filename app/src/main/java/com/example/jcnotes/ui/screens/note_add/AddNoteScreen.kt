package com.example.jcnotes.ui.screens.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jcnotes.presentation.viewmodel.AddNoteViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddNoteViewModel = hiltViewModel()
) {
    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()
    val color by viewModel.color.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    
    val isDarkTheme = true // Koyu tema kullanıyoruz
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val contentColor = if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF666666)
    
    val availableColors = listOf(
        Color(0xFFFFE4E1), // Pembe
        Color(0xFFE6E6FA), // Mavi
        Color(0xFFF0FFF0)  // Yeşil
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Yeni Not",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                },
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
                    IconButton(
                        onClick = { viewModel.saveNote() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Kaydet",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = backgroundColor,
                contentColor = textColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Renk seçenekleri
                    availableColors.forEach { currentColor ->
                        ColorOption(
                            color = currentColor,
                            isSelected = currentColor.toArgb() == color,
                            onClick = { viewModel.updateColor(currentColor.toArgb()) }
                        )
                    }
                    
                    // Diğer seçenekler
                    IconButton(onClick = { /* Kategori seçimi */ }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Kategori",
                            tint = textColor
                        )
                    }
                    
                    IconButton(onClick = { /* Hatırlatıcı ekle */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Hatırlatıcı",
                            tint = textColor
                        )
                    }
                    
                    IconButton(onClick = { /* Daha fazla seçenek */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Daha Fazla",
                            tint = textColor
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(backgroundColor)
        ) {
            // Başlık
            TextField(
                value = title,
                onValueChange = { viewModel.updateTitle(it) },
                placeholder = { Text("Başlık", color = contentColor) },
                textStyle = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    disabledContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            
            // İçerik
            TextField(
                value = content,
                onValueChange = { viewModel.updateContent(it) },
                placeholder = { Text("İçerik", color = contentColor) },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = textColor
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    disabledContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .padding(4.dp)
            .background(color, RoundedCornerShape(percent = 50))
            .padding(2.dp)
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f), RoundedCornerShape(percent = 50))
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seçildi",
                    tint = Color.White,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center)
                )
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isSelected) 0.dp else 4.dp)
                .clickable(onClick = onClick)
        )
    }
} 