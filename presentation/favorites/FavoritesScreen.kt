package com.dastan.weatherfinal.presentation.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dastan.weatherfinal.data.firebase.Favorite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(vm: FavoritesViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<Favorite?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Favorites") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { editingItem = null; showDialog = true }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { pad ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(pad).padding(16.dp)) {
                items(state.favorites, key = { it.id }) { item ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        ListItem(
                            headlineContent = { Text(item.city) },
                            supportingContent = { Text(item.note) },
                            trailingContent = {
                                Row {
                                    IconButton(onClick = { editingItem = item; showDialog = true }) { Icon(Icons.Default.Edit, null) }
                                    IconButton(onClick = { vm.delete(item.id) }) { Icon(Icons.Default.Delete, null) }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        var city by remember { mutableStateOf(editingItem?.city ?: "") }
        var note by remember { mutableStateOf(editingItem?.note ?: "") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (editingItem == null) "Add Favorite" else "Edit Favorite") },
            text = {
                Column {
                    TextField(city, { city = it }, placeholder = { Text("City Name") })
                    Spacer(Modifier.height(8.dp))
                    TextField(note, { note = it }, placeholder = { Text("Note") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (editingItem == null) vm.add(city, note) else vm.update(editingItem!!.id, city, note)
                    showDialog = false
                }) { Text("Save") }
            }
        )
    }
}
