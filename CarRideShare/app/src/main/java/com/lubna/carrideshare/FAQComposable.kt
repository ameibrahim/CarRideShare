package com.lubna.carrideshare

// Compose runtime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// UI and layout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

// Material components
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons

// Animation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp

// Modifiers & utilities
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 1. Your data model
data class FAQItem(
    val question: String,
    val answer: String
)

// 3. The FAQSection: purely stateless UI
@Composable
fun FAQSection(
    faqs: List<FAQItem>,
) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("FAQs")
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            itemsIndexed(faqs) { _, faq ->
                FAQItemCard(
                    faq = faq
                )
            }
        }
    }
}

// 4. A single FAQ row with expand/collapse + edit/delete
@Composable
fun FAQItemCard(
    faq: FAQItem
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(faq.question)
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            AnimatedVisibility(expanded) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    Text(faq.answer)
                }
            }
        }
    }
}

// New Composable - 1