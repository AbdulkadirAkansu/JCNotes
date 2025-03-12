package com.example.jcnotes.ui.components.notes

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.data.model.Task
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavoriteClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor = getCardColor(note.color)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Başlık
            Text(
                text = note.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // İçerik
            Text(
                text = note.content,
                fontSize = 14.sp,
                color = Color(0xFF808080),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
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

private fun getCardColor(colorCode: Int): Color {
    // Figma tasarımındaki renk paletinden bir renk seçelim
    val colors = listOf(
        Color.White,
        Color(0xFFE8F4FF), // Açık mavi
        Color(0xFFFFECE8), // Açık kırmızı
        Color(0xFFE8FFF0), // Açık yeşil
        Color(0xFFF8E8FF), // Açık mor
        Color(0xFFFFF8E8), // Açık sarı
        Color(0xFFE8F0FF)  // Açık lacivert
    )
    
    // colorCode'u kullanarak renk seçelim
    val index = (colorCode.absoluteValue % colors.size)
    return colors[index]
} 