package com.example.robokalamassignmentapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.robokalamassignmentapp.data.PortfolioDatabase
import com.example.robokalamassignmentapp.data.PortfolioEntry
import com.example.robokalamassignmentapp.data.UserPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val portfolioDao = PortfolioDatabase.getDatabase(context).portfolioDao()
    val userPreferencesManager = UserPreferencesManager.getInstance()

    var portfolioEntries by remember { mutableStateOf<List<PortfolioEntry>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        withContext(Dispatchers.IO) {
            portfolioEntries = portfolioDao.getAllEntries()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Portfolio") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        if (portfolioEntries.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Your portfolio is empty",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showAddDialog = true }) {
                    Text("Add Portfolio Entry")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(portfolioEntries) { entry ->
                    PortfolioEntryCard(
                        entry = entry,
                        onDelete = {
                            CoroutineScope(Dispatchers.IO).launch {
                                portfolioDao.deleteEntry(entry)
                                portfolioEntries = portfolioDao.getAllEntries()
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddPortfolioDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, college, skills, projectTitle, projectDescription ->
                CoroutineScope(Dispatchers.IO).launch {
                    val entry = PortfolioEntry(
                        name = name,
                        college = college,
                        skills = skills,
                        projectTitle = projectTitle,
                        projectDescription = projectDescription,
                        userId = userPreferencesManager.getUserEmail()
                    )
                    portfolioDao.insert(entry)
                    portfolioEntries = portfolioDao.getAllEntries()
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
fun PortfolioEntryCard(
    entry: PortfolioEntry,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Text(
                text = entry.college,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Skills:",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = entry.skills,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Project: ${entry.projectTitle}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = entry.projectDescription,
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}



@Composable
fun AddPortfolioDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, college: String, skills: String, projectTitle: String, projectDescription: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var projectTitle by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Add Portfolio Entry",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = college,
                    onValueChange = { college = it },
                    label = { Text("College") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text("Skills (e.g. Kotlin, Android, Java)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = projectTitle,
                    onValueChange = { projectTitle = it },
                    label = { Text("Project Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = projectDescription,
                    onValueChange = { projectDescription = it },
                    label = { Text("Project Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (name.isNotEmpty() && college.isNotEmpty() && skills.isNotEmpty() &&
                                projectTitle.isNotEmpty() && projectDescription.isNotEmpty()
                            ) {
                                onSave(name, college, skills, projectTitle, projectDescription)
                            }
                        },
                        enabled = name.isNotEmpty() && college.isNotEmpty() && skills.isNotEmpty() &&
                                projectTitle.isNotEmpty() && projectDescription.isNotEmpty()
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}