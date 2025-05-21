package org.example.frikollection_mobile_desktop.ui.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selected: String?,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onValueSelected: (String?) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandToggle() }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title.uppercase(), fontWeight = FontWeight.Bold)
                if (!selected.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(selected, fontSize = 12.sp)
                }
            }
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                contentDescription = "Expand $title"
            )
        }

        if (isExpanded) {
            options.forEach { option ->
                val isChecked = selected == option
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onValueSelected(if (isChecked) null else option)
                        }
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            onValueSelected(if (isChecked) null else option)
                        }
                    )
                    Text(option)
                }
            }
        }
    }
}

@Composable
fun FilterTagSection(
    title: String,
    options: List<String>,
    selectedTags: Set<String>,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    onToggle: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onExpandToggle() }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title.uppercase(), fontWeight = FontWeight.Bold)
                if (selectedTags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        selectedTags.joinToString(", "),
                        fontSize = 12.sp
                    )
                }
            }
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                contentDescription = "Expand $title"
            )
        }

        if (isExpanded) {
            options.forEach { tag ->
                val isChecked = tag in selectedTags
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggle(tag) }
                        .padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onToggle(tag) }
                    )
                    Text(tag)
                }
            }
        }
    }
}