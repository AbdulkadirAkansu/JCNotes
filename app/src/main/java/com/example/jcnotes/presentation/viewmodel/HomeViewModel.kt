package com.example.jcnotes.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    private val _isGridView = MutableStateFlow(true)
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    private val _showFilterDialog = MutableStateFlow(false)
    val showFilterDialog: StateFlow<Boolean> = _showFilterDialog.asStateFlow()

    private val _showDrawer = MutableStateFlow(false)
    val showDrawer: StateFlow<Boolean> = _showDrawer.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.DATE)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val notes: StateFlow<List<Note>> = combine(
        repository.getAllNotes(),
        _searchQuery,
        _sortOrder
    ) { notes, query, sortOrder ->
        var result = if (query.isEmpty()) notes else notes.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }

        result = when (sortOrder) {
            SortOrder.DATE -> result.sortedByDescending { it.timestamp }
            SortOrder.TITLE -> result.sortedBy { it.title }
            SortOrder.COLOR -> result.sortedBy { it.color }
            SortOrder.FAVORITE -> result.sortedByDescending { it.favorite }
        }

        result
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getNotes() {
        viewModelScope.launch {
            repository.getAllNotes()
        }
    }

    fun sortNotes(order: SortOrder) {
        _sortOrder.value = order
    }

    // Arama sorgusunu güncelle
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Arama durumunu ayarla
    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
        if (!active) {
            _searchQuery.value = ""
        }
    }

    // Grid görünümünü değiştir
    fun toggleGridView() {
        _isGridView.value = !_isGridView.value
    }

    // Filtre dialogunu aç/kapat
    fun toggleFilterDialog() {
        _showFilterDialog.value = !_showFilterDialog.value
    }

    // Drawer'ı aç/kapat
    fun toggleDrawer() {
        _showDrawer.value = !_showDrawer.value
    }

    // Drawer'ı kapat
    fun closeDrawer() {
        _showDrawer.value = false
    }

    // Notları sırala
    fun updateSortOrder(order: SortOrder) {
        _sortOrder.value = order
        _showFilterDialog.value = false
    }

    fun toggleFavorite(note: Note) {
        viewModelScope.launch {
            repository.update(note.copy(favorite = !note.favorite))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    private fun getNoteColor(note: Note): Color {
        // Not ID'sine göre renk seç
        return PremiumColors.noteColors[note.id % PremiumColors.noteColors.size]
    }

    private fun getCategoryForNote(note: Note): String {
        // Not içeriğine göre kategori belirle (gerçek uygulamada veritabanından gelecek)
        return when {
            note.title.contains("Meeting", ignoreCase = true) -> "Work"
            note.title.contains("Shopping", ignoreCase = true) -> "Personal"
            note.title.contains("Idea", ignoreCase = true) -> "Ideas"
            note.title.contains("List", ignoreCase = true) -> "Lists"
            else -> "All"
        }
    }

    private fun getCategoryColor(category: String): Color {
        return when (category) {
            "Personal" -> PremiumColors.categoryColors[1]
            "Work" -> PremiumColors.categoryColors[2]
            "Ideas" -> PremiumColors.categoryColors[3]
            "Lists" -> PremiumColors.categoryColors[4]
            else -> PremiumColors.categoryColors[0]
        }
    }

    private fun isPinned(note: Note): Boolean {
        // Gerçek uygulamada veritabanından gelecek
        return note.id % 5 == 0
    }

    private fun isFavorite(note: Note): Boolean {
        // Gerçek uygulamada veritabanından gelecek
        return note.id % 3 == 0
    }

    private fun formatDate(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val noteDay = calendar.get(Calendar.DAY_OF_YEAR)
        val noteYear = calendar.get(Calendar.YEAR)

        return when {
            // Bugün
            currentDay == noteDay && currentYear == noteYear -> {
                "Bugün"
            }
            // Dün
            currentDay - noteDay == 1 && currentYear == noteYear -> {
                "Dün"
            }
            // Bu hafta içinde
            currentDay - noteDay < 7 && currentYear == noteYear -> {
                val formatter = SimpleDateFormat("EEEE", Locale("tr"))
                formatter.format(Date(timestamp))
            }
            // Bu yıl içinde
            currentYear == noteYear -> {
                val formatter = SimpleDateFormat("d MMM", Locale("tr"))
                formatter.format(Date(timestamp))
            }
            // Geçmiş yıllar
            else -> {
                val formatter = SimpleDateFormat("d MMM yyyy", Locale("tr"))
                formatter.format(Date(timestamp))
            }
        }
    }

    object PremiumColors {
        val primary = Color(0xFF1A73E8)
        val onPrimary = Color.White
        val background = Color(0xFFF8F9FA)
        val surface = Color.White
        val onSurface = Color(0xFF202124)
        val onSurfaceVariant = Color(0xFF5F6368)
        val outline = Color(0xFFDADCE0)

        // Not kartları için renkler
        val noteColors = listOf(
            Color.White,                  // Beyaz
            Color(0xFFF1F8E9),            // Açık yeşil
            Color(0xFFE1F5FE),            // Açık mavi
            Color(0xFFFFF8E1),            // Açık sarı
            Color(0xFFF3E5F5),            // Açık mor
            Color(0xFFE8F5E9),            // Mint yeşili
            Color(0xFFFCE4EC),            // Açık pembe
            Color(0xFFEFEBE9)             // Açık bej
        )

        // Kategori renkleri
        val categoryColors = listOf(
            Color(0xFF4285F4),            // Mavi
            Color(0xFF0F9D58),            // Yeşil
            Color(0xFFDB4437),            // Kırmızı
            Color(0xFFF4B400),            // Sarı
            Color(0xFF9C27B0)             // Mor
        )
    }

    // Not kartları için renkler
    object NoteColors {
        val pink = Color(0xFFFFE4E1)    // Figma'daki pembe renk
        val blue = Color(0xFFE6E6FA)    // Figma'daki mavi renk
        val green = Color(0xFFF0FFF0)   // Figma'daki yeşil renk
    }

    enum class SortOrder {
        DATE, TITLE, COLOR, FAVORITE
    }
}