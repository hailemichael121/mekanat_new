package com.example.mekanat_new.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mekanat_new.data.util.EthiopianCalendar
import com.example.mekanat_new.ui.theme.SignalRed
import com.example.mekanat_new.ui.viewmodel.MekanatUiState
import com.example.mekanat_new.ui.viewmodel.MekanatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChurchScreen(
    uiState: MekanatUiState,
    viewModel: MekanatViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var nameAmharic by remember { mutableStateOf("") }
    var churchType by remember { mutableStateOf("PARISH") }
    var region by remember { mutableStateOf("") }
    var diocese by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf(String.format("%.4f", uiState.userLat)) }
    var lngText by remember { mutableStateOf(String.format("%.4f", uiState.userLng)) }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var history by remember { mutableStateOf("") }
    var tabotName by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableIntStateOf(1) }
    var selectedDay by remember { mutableIntStateOf(21) }
    var contactPhone by remember { mutableStateOf("") }

    var typeExpanded by remember { mutableStateOf(false) }
    val churchTypes = listOf(
        "PARISH" to "Parish Church (ደብር)",
        "MONASTERY" to "Historic Monastery (ገዳም)",
        "CATHEDRAL" to "Patriarchal Cathedral",
        "ROCK_HEWN" to "Rock-Hewn Sanctuary"
    )

    var monthExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Submit Holy Sanctuary", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Community Contribution",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Help map Ethiopia's sacred places. Submissions appear immediately in your app and are reviewed for diocese verification.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }

            // Church Identification Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Church Identification",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            shape = RoundedCornerShape(16.dp),
                            label = { Text("Sanctuary Name (English) *") },
                            placeholder = { Text("e.g. Debre Libanos Monastery") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("add_church_name_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = nameAmharic,
                            onValueChange = { nameAmharic = it },
                            shape = RoundedCornerShape(16.dp),
                            label = { Text("Name in Ge'ez / Amharic") },
                            placeholder = { Text("e.g. ደብረ ሊባኖስ ገዳም") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Church Type Dropdown
                        ExposedDropdownMenuBox(
                            expanded = typeExpanded,
                            onExpandedChange = { typeExpanded = !typeExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = churchTypes.find { it.first == churchType }?.second ?: churchType,
                                onValueChange = {},
                                readOnly = true,
                                shape = RoundedCornerShape(16.dp),
                                label = { Text("Sanctuary Type *") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = typeExpanded,
                                onDismissRequest = { typeExpanded = false }
                            ) {
                                churchTypes.forEach { (typeKey, typeLabel) ->
                                    DropdownMenuItem(
                                        text = { Text(typeLabel) },
                                        onClick = {
                                            churchType = typeKey
                                            typeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Location & GPS Coordinates Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Geographic Location & Diocese",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = region,
                                onValueChange = { region = it },
                                shape = RoundedCornerShape(16.dp),
                                label = { Text("Region *") },
                                placeholder = { Text("e.g. Amhara") },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = diocese,
                                onValueChange = { diocese = it },
                                shape = RoundedCornerShape(16.dp),
                                label = { Text("Diocese *") },
                                placeholder = { Text("e.g. Gondar Diocese") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = latText,
                                onValueChange = { latText = it },
                                shape = RoundedCornerShape(16.dp),
                                label = { Text("Latitude") },
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = lngText,
                                onValueChange = { lngText = it },
                                shape = RoundedCornerShape(16.dp),
                                label = { Text("Longitude") },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                latText = String.format("%.4f", uiState.userLat)
                                lngText = String.format("%.4f", uiState.userLng)
                            },
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Use My Current Location Coordinates")
                        }
                    }
                }
            }

            // Sacred Tabot & Nigs Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Sacred Tabot & Annual Nigs",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = tabotName,
                            onValueChange = { tabotName = it },
                            shape = RoundedCornerShape(16.dp),
                            label = { Text("Primary Tabot Name") },
                            placeholder = { Text("e.g. ኪዳነ ምሕረት (Kidane Mihret)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Month Dropdown
                            ExposedDropdownMenuBox(
                                expanded = monthExpanded,
                                onExpandedChange = { monthExpanded = !monthExpanded },
                                modifier = Modifier.weight(1f)
                            ) {
                                val currentMonthName = EthiopianCalendar.monthNames.getOrElse(selectedMonth - 1) { "Meskerem" }
                                val currentMonthAmh = EthiopianCalendar.monthNamesAmharic.getOrElse(selectedMonth - 1) { "" }

                                OutlinedTextField(
                                    value = "$currentMonthAmh ($currentMonthName)",
                                    onValueChange = {},
                                    readOnly = true,
                                    shape = RoundedCornerShape(16.dp),
                                    label = { Text("Nigs Month") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = monthExpanded,
                                    onDismissRequest = { monthExpanded = false }
                                ) {
                                    EthiopianCalendar.monthNames.forEachIndexed { index, mName ->
                                        val amh = EthiopianCalendar.monthNamesAmharic.getOrElse(index) { "" }
                                        DropdownMenuItem(
                                            text = { Text("$amh ($mName)") },
                                            onClick = {
                                                selectedMonth = index + 1
                                                monthExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = "$selectedDay",
                                onValueChange = {
                                    it.toIntOrNull()?.let { day ->
                                        if (day in 1..30) selectedDay = day
                                    }
                                },
                                shape = RoundedCornerShape(16.dp),
                                label = { Text("Nigs Day (1-30)") },
                                modifier = Modifier.weight(0.7f)
                            )
                        }
                    }
                }
            }

            // History & Narrative Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "History & Sacred Tradition",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            shape = RoundedCornerShape(16.dp),
                            label = { Text("Overview & Sacred Features") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = history,
                            onValueChange = { history = it },
                            shape = RoundedCornerShape(16.dp),
                            label = { Text("Spiritual History & Founding") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val parsedLat = latText.toDoubleOrNull() ?: uiState.userLat
                            val parsedLng = lngText.toDoubleOrNull() ?: uiState.userLng
                            viewModel.onSubmitNewChurch(
                                name = name.trim(),
                                nameAmharic = nameAmharic.trim().ifBlank { null },
                                latitude = parsedLat,
                                longitude = parsedLng,
                                region = region.trim(),
                                diocese = diocese.trim(),
                                churchType = churchType,
                                description = description.trim().ifBlank { null },
                                history = history.trim().ifBlank { null },
                                address = address.trim().ifBlank { null },
                                contactPhone = contactPhone.trim().ifBlank { null },
                                contactEmail = null,
                                tabotNames = if (tabotName.isNotBlank()) listOf(tabotName.trim()) else emptyList(),
                                nigsMonth = selectedMonth,
                                nigsDay = selectedDay
                            )
                            onBack()
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SignalRed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("submit_church_button")
                ) {
                    Text("Submit Sanctuary for Verification", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
