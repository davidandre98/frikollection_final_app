package org.example.frikollection_mobile_desktop.register.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.Period

@Composable
fun DatePickerDesktop(
    initialDate: String,
    onDateSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var day by remember { mutableStateOf(1) }
    var month by remember { mutableStateOf(1) }
    var year by remember { mutableStateOf(2025) }
    var ageError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(initialDate) {
        val parts = initialDate.split("-")
        if (parts.size == 3) {
            year = parts[0].toIntOrNull() ?: 2025
            month = parts[1].toIntOrNull() ?: 1
            day = parts[2].toIntOrNull() ?: 1
        }
    }

    val days = (1..31).toList()
    val months = (1..12).toList()
    val years = (2025 downTo 1900).toList()

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .width(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "SELECT YOUR BIRTHDAY",
                    style = MaterialTheme.typography.h6.copy(fontSize = 24.sp),
                    color = Color.Black
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    WheelSelector(
                        label = "DAY",
                        options = days,
                        selected = day,
                        onSelected = { day = it },
                        height = 150.dp
                    )

                    WheelSelector(
                        label = "MONTH",
                        options = months,
                        selected = month,
                        onSelected = { month = it },
                        height = 150.dp
                    )

                    WheelSelector(
                        label = "YEAR",
                        options = years,
                        selected = year,
                        onSelected = { year = it },
                        height = 150.dp
                    )
                }

                ageError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colors.error,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = {
                        val selectedDate = LocalDate.of(year, month, day)
                        val today = LocalDate.now()

                        val age = Period.between(selectedDate, today).years

                        if (age >= 14) {
                            val date = String.format("%04d-%02d-%02d", year, month, day)
                            onDateSelected(date)
                            onDismissRequest()
                        } else {
                            ageError = "You must be at least 14 years old."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                ) {
                    Text("CONFIRM", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun WheelSelector(
    label: String,
    options: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit,
    height: Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 14.sp),
            color = Color.Gray
        )

        Box(
            modifier = Modifier
                .width(80.dp)
                .height(height)
                .background(Color.LightGray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
        ) {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(options) { value ->
                    val isSelected = value == selected

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.3f)
                                else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable { onSelected(value) }
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = value.toString(),
                            color = if (isSelected) MaterialTheme.colors.primary else Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}