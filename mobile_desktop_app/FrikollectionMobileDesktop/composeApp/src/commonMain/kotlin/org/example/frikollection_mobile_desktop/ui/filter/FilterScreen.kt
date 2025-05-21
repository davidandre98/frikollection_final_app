package org.example.frikollection_mobile_desktop.ui.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.frikollection_mobile_desktop.BottomMenuItem
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.ui.footers.AppFooter
import org.example.frikollection_mobile_desktop.ui.headers.AppHeader

@Composable
fun FilterScreen(
    initialFilter: ProductFilter = ProductFilter(),
    availableProductTypes: List<String>,
    availableSubtypes: List<String>,
    availableSupertypes: List<String>,
    availableStatus: List<String>,
    availableLicenses: List<String>,
    availableTags: List<String>,
    onApply: (ProductFilter) -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit,
    onSearch: () -> Unit,
    selectedBottomItem: BottomMenuItem,
    onBottomItemSelected: (BottomMenuItem) -> Unit
) {
    var productType by remember { mutableStateOf(initialFilter.productType) }
    var subtype by remember { mutableStateOf(initialFilter.subtype) }
    var supertype by remember { mutableStateOf(initialFilter.supertype) }
    var status by remember { mutableStateOf(initialFilter.status) }
    var license by remember { mutableStateOf(initialFilter.license) }
    var tags by remember { mutableStateOf(initialFilter.tags.toSet()) }

    var expandedSection by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            AppHeader(
                showBack = true,
                onBack = { onBack() },
                showSearch = true,
                onSearch = { onSearch() }
            )
        },
        bottomBar = {
            AppFooter(
                selectedBottomItem = selectedBottomItem,
                onBottomItemSelected = onBottomItemSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            // Botons de dalt
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D47A1))
                ) {
                    Text("CANCEL")
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        onApply(
                            ProductFilter(
                                productType = productType,
                                subtype = subtype,
                                supertype = supertype,
                                status = status,
                                license = license,
                                tags = tags.toList()
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("APPLY", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("FILTER BY:", fontWeight = FontWeight.Bold)
                TextButton(
                    onClick = {
                        productType = null
                        subtype = null
                        supertype = null
                        license = null
                        tags = emptySet()
                        onReset()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0D47A1))
                ) {
                    Text("RESET FILTERS")
                }
            }

            FilterSection(
                title = "PRODUCT TYPE",
                options = availableProductTypes,
                selected = productType,
                isExpanded = expandedSection == "PRODUCT TYPE",
                onExpandToggle = { expandedSection = toggleSection(expandedSection, "PRODUCT TYPE") },
                onValueSelected = {
                    productType = if (productType == it) null else it
                }
            )
            Divider()

            FilterSection(
                title = "PRODUCT SUBTYPE",
                options = availableSubtypes,
                selected = subtype,
                isExpanded = expandedSection == "PRODUCT SUB TYPE",
                onExpandToggle = { expandedSection = toggleSection(expandedSection, "PRODUCT SUB TYPE") },
                onValueSelected = {
                    subtype = if (subtype == it) null else it
                }
            )
            Divider()

            FilterSection(
                title = "PRODUCT SUPERTYPE",
                options = availableSupertypes,
                selected = supertype,
                isExpanded = expandedSection == "PRODUCT SUPERTYPE",
                onExpandToggle = { expandedSection = toggleSection(expandedSection, "PRODUCT SUPERTYPE") },
                onValueSelected = {
                    supertype = if (supertype == it) null else it
                }
            )
            Divider()

            FilterSection(
                title = "STATUS",
                options = availableStatus,
                selected = status,
                isExpanded = expandedSection == "STATUS",
                onExpandToggle = { expandedSection = toggleSection(expandedSection, "STATUS") },
                onValueSelected = {
                    status = if (status == it) null else it
                }
            )
            Divider()

            FilterSection(
                title = "LICENSE",
                options = availableLicenses,
                selected = license,
                isExpanded = expandedSection == "LICENSE",
                onExpandToggle = { expandedSection = toggleSection(expandedSection, "LICENSE") },
                onValueSelected = {
                    license = if (license == it) null else it
                }
            )
            Divider()

            FilterTagSection(
                title = "TAGS",
                options = availableTags,
                selectedTags = tags,
                isExpanded = expandedSection == "TAGS",
                onExpandToggle = { expandedSection = toggleSection(expandedSection, "TAGS") },
                onToggle = { tag ->
                    tags = if (tag in tags) tags - tag else tags + tag
                }
            )
        }
    }
}

private fun toggleSection(current: String?, target: String): String? =
    if (current == target) null else target