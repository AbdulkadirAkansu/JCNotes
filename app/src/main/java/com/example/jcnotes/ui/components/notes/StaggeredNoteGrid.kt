package com.example.jcnotes.ui.components.notes

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jcnotes.data.model.Note

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StaggeredNoteGrid(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onNoteLongClick: (Note) -> Unit,
    onNoteFavoriteClick: (Note, Boolean) -> Unit,
    isLoading: Boolean = false,
    gridColumns: Int = 2,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    modifier: Modifier = Modifier
) {
    val state = rememberLazyStaggeredGridState()
    val animatedItems = remember { mutableStateListOf<Int>() }
    
    LaunchedEffect(notes) {
        animatedItems.clear()
        notes.forEachIndexed { index, _ ->
            kotlinx.coroutines.delay(index * 25L)
            animatedItems.add(index)
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(32.dp)
            )
        } else if (notes.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Text(
                    text = "Henüz not eklenmemiş",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Yeni bir not eklemek için + butonuna dokunun",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(gridColumns),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp,
                state = state,
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    items = notes,
                    key = { _, note -> note.id }
                ) { index, note ->
                    AnimatedVisibility(
                        visible = animatedItems.contains(index),
                        enter = fadeIn(
                            initialAlpha = 0.3f,
                            animationSpec = tween(200)
                        ) + slideInVertically(
                            initialOffsetY = { it / 4 },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) + scaleIn(
                            initialScale = 0.95f,
                            animationSpec = tween(200)
                        ),
                        exit = fadeOut() + shrinkOut() + slideOutVertically()
                    ) {
                        NoteCard(
                            note = note,
                            onClick = { onNoteClick(note) },
                            onLongClick = { onNoteLongClick(note) },
                            onFavoriteClick = { isFavorite -> onNoteFavoriteClick(note, isFavorite) },
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                                .animateItemPlacement(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
} 