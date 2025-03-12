package com.example.jcnotes.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.presentation.viewmodel.HomeViewModel
import com.example.jcnotes.data.model.SortOrder
import com.example.jcnotes.ui.components.notes.NoteCard
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToNote: (Int?) -> Unit,
    onNavigateToAddNote: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // ViewModel'den state'leri al
    val notes by viewModel.notes.collectAsStateWithLifecycle(initialValue = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()
    val isGridView by viewModel.isGridView.collectAsStateWithLifecycle()
    val showFilterDialog by viewModel.showFilterDialog.collectAsStateWithLifecycle()
    val showDrawer by viewModel.showDrawer.collectAsStateWithLifecycle()
    
    // Dark tema kontrolü
    val isDarkTheme = isSystemInDarkTheme()
    val iconTint = if (isDarkTheme) Color.White else Color.Black
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color.White
    val contentColor = if (isDarkTheme) Color(0xFFCCCCCC) else Color.Black.copy(alpha = 0.7f)
    val dateColor = if (isDarkTheme) Color(0xFFAAAAAA) else Color.Black.copy(alpha = 0.5f)
    val searchBackgroundColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color(0xFFF5F5F5)

    Box(modifier = Modifier.fillMaxSize()) {
        // Drawer
        AnimatedVisibility(
            visible = showDrawer,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { viewModel.closeDrawer() }
            ) {
                Surface(
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart),
                    color = if (isDarkTheme) Color(0xFF121212) else Color.White,
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                ) {
                    NavigationDrawer(
                        onClose = { viewModel.closeDrawer() },
                        onNavigateToNote = onNavigateToNote,
                        isDarkTheme = isDarkTheme
                    )
                }
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            containerColor = backgroundColor,
            contentColor = textColor,
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                ) {
                    // Row 1: Başlık ve not sayısı
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "All Notes",
                            style = TextStyle(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                textAlign = TextAlign.Center
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "${notes.size} notes",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = contentColor,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Row 2: Menü ve arama ikonları
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleDrawer() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = iconTint,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        IconButton(
                            onClick = { viewModel.setSearchActive(true) },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = iconTint,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    
                    // Row 3: Grid ve filtre ikonları
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleGridView() },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = if (isGridView) Icons.Default.Star else Icons.Default.Star,
                                contentDescription = "Change View",
                                tint = iconTint.copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        IconButton(
                            onClick = { viewModel.toggleFilterDialog() },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Filter",
                                tint = iconTint.copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                Box(
                    modifier = Modifier
                        .padding(bottom = 24.dp, end = 8.dp)
                        .offset(y = (-8).dp)
                ) {
                    FloatingActionButton(
                        onClick = { onNavigateToAddNote() },
                        containerColor = Color(0xFF2196F3), // Material Blue
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Note",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { paddingValues ->
            // Not listesi
            val filteredNotes = notes.filter { note ->
                searchQuery.isEmpty() || note.title.contains(searchQuery, ignoreCase = true) ||
                        note.content.contains(searchQuery, ignoreCase = true)
            }

            if (isGridView) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalItemSpacing = 20.dp
                ) {
                    items(filteredNotes) { note ->
                        ModernNoteCard(
                            note = note,
                            isGridView = true,
                            onClick = { onNavigateToNote(note.id) },
                            onFavoriteClick = { viewModel.toggleFavorite(note) },
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(filteredNotes) { note ->
                        ModernNoteCard(
                            note = note,
                            isGridView = false,
                            onClick = { onNavigateToNote(note.id) },
                            onFavoriteClick = { viewModel.toggleFavorite(note) },
                            isDarkTheme = isDarkTheme
                        )
                    }
                }
            }

            // Filtre dialog
            if (showFilterDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.toggleFilterDialog() },
                    title = { Text("Sıralama", style = MaterialTheme.typography.titleLarge) },
                    containerColor = if (isDarkTheme) Color(0xFF2A2A2A) else Color.White,
                    titleContentColor = textColor,
                    textContentColor = contentColor,
                    shape = RoundedCornerShape(24.dp),
                    text = {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            SortOption(
                                title = "Tarihe göre",
                                isSelected = true,
                                isDarkTheme = isDarkTheme
                            ) { viewModel.sortNotes(HomeViewModel.SortOrder.DATE) }
                            
                            SortOption(
                                title = "Başlığa göre",
                                isSelected = false,
                                isDarkTheme = isDarkTheme
                            ) { viewModel.sortNotes(HomeViewModel.SortOrder.TITLE) }
                            
                            SortOption(
                                title = "Renge göre",
                                isSelected = false,
                                isDarkTheme = isDarkTheme
                            ) { viewModel.sortNotes(HomeViewModel.SortOrder.COLOR) }
                            
                            SortOption(
                                title = "Favorilere göre",
                                isSelected = false,
                                isDarkTheme = isDarkTheme
                            ) { viewModel.sortNotes(HomeViewModel.SortOrder.FAVORITE) }
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { viewModel.toggleFilterDialog() }
                        ) {
                            Text(
                                "Tamam",
                                color = Color(0xFF5E35B1),
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onFavoriteClick: (Note) -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(notes) { note ->
            NoteItem(
                note = note,
                onClick = { onNoteClick(note) },
                onFavoriteClick = { onFavoriteClick(note) },
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun GridNotesList(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onFavoriteClick: (Note) -> Unit,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notes.size) { index ->
            val note = notes[index]
            NoteItem(
                note = note,
                onClick = { onNoteClick(note) },
                onFavoriteClick = { onFavoriteClick(note) },
                isDarkTheme = isDarkTheme
            )
        }
    }
}

@Composable
fun NavigationDrawer(
    onClose: () -> Unit,
    onNavigateToNote: (Int?) -> Unit,
    isDarkTheme: Boolean
) {
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color.White
    val accentColor = Color(0xFF5E35B1)
    val dividerColor = if (isDarkTheme) Color(0xFF444444) else Color.LightGray
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onClose() }
    ) {
        Card(
            modifier = Modifier
                .width(280.dp)
                .fillMaxHeight()
                .clickable(enabled = false) { /* Boş tıklama engelleme */ },
            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Drawer başlığı
                Text(
                    text = "JCNotes",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    ),
                    modifier = Modifier.padding(vertical = 24.dp)
                )
                
                // Drawer menü öğeleri
                DrawerMenuItem(
                    icon = Icons.Default.Home,
                    title = "Tüm Notlar",
                    isSelected = true,
                    isDarkTheme = isDarkTheme,
                    accentColor = accentColor
                ) { onClose() }
                
                DrawerMenuItem(
                    icon = Icons.Default.Star,
                    title = "Favoriler",
                    isDarkTheme = isDarkTheme,
                    accentColor = accentColor
                ) { onClose() }
                
                DrawerMenuItem(
                    icon = Icons.Default.Delete,
                    title = "Çöp Kutusu",
                    isDarkTheme = isDarkTheme,
                    accentColor = accentColor
                ) { onClose() }
                
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = dividerColor
                )
                
                DrawerMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Ayarlar",
                    isDarkTheme = isDarkTheme,
                    accentColor = accentColor
                ) { onClose() }
                
                DrawerMenuItem(
                    icon = Icons.Default.Info,
                    title = "Hakkında",
                    isDarkTheme = isDarkTheme,
                    accentColor = accentColor
                ) { onClose() }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    isSelected: Boolean = false,
    isDarkTheme: Boolean = false,
    accentColor: Color = Color(0xFF5E35B1),
    onClick: () -> Unit
) {
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val iconTint = if (isSelected) accentColor else if (isDarkTheme) Color.LightGray else Color.Gray
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) accentColor else textColor
            )
        )
    }
}

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isDarkTheme: Boolean
) {
    ModernNoteCard(
        note = note,
        isGridView = true,
        onClick = onClick,
        onFavoriteClick = onFavoriteClick,
        isDarkTheme = isDarkTheme
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernNoteCard(
    note: Note,
    isGridView: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val noteColor = when ((note.id ?: 0) % 3) {
        0 -> if (isDarkTheme) Color(0xFFFCE4EC) else Color(0xFFFFE0B2) // Light: Turuncu pastel
        1 -> if (isDarkTheme) Color(0xFFE3F2FD) else Color(0xFFE1BEE7) // Light: Mor pastel
        else -> if (isDarkTheme) Color(0xFFF1F8E9) else Color(0xFFC8E6C9) // Light: Yeşil pastel
    }
    
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formattedDate = note.timestamp?.let { dateFormat.format(Date(it)) } ?: ""
    
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val contentColor = if (isDarkTheme) Color(0xFFCCCCCC) else Color.Black.copy(alpha = 0.7f)
    val dateColor = if (isDarkTheme) Color(0xFFAAAAAA) else Color.Black.copy(alpha = 0.5f)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isGridView)
                    Modifier.aspectRatio(1.1f)
                else
                    Modifier.height(150.dp)
            )
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = noteColor,
            contentColor = textColor
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 24.sp
                ),
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                color = contentColor,
                maxLines = if (isGridView) 4 else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formattedDate,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                ),
                color = dateColor
            )
        }
    }
}

@Composable
fun SortOption(
    title: String,
    isSelected: Boolean,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    val textColor = if (isDarkTheme) Color.White else Color.Black
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF5E35B1),
                unselectedColor = if (isDarkTheme) Color.LightGray else Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = textColor
            )
        )
    }
}

@Composable
private fun FilterOption(
    text: String,
    sortOrder: SortOrder,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )
    }
}
