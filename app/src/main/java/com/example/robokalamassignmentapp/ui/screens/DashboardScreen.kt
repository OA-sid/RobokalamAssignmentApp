package com.example.robokalamassignmentapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.robokalamassignmentapp.data.QuoteRepository
import com.example.robokalamassignmentapp.data.UserPreferencesManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onPortfolioClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogout: () -> Unit
) {
    val preferencesManager = UserPreferencesManager.getInstance()
    val userName = preferencesManager.getUserName()

    var quote by remember { mutableStateOf("Loading quote...") }
    var quoteAuthor by remember { mutableStateOf("") }

    val quoteRepository = QuoteRepository.getInstance()

    LaunchedEffect(key1 = true) {
        try {
            val quoteResponse = quoteRepository.getQuoteOfTheDay()
            quote = quoteResponse.quote
            quoteAuthor = quoteResponse.author
        } catch (e: Exception) {
            quote = "Every moment is a fresh beginning."
            quoteAuthor = "T.S. Eliot"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome, $userName!",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Quote of the day card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Quote of the Day",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"$quote\"",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if (quoteAuthor.isNotEmpty()) {
                        Text(
                            text = "— $quoteAuthor",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Feature buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onPortfolioClick,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text("My Portfolio")
                }

                Button(
                    onClick = onAboutClick,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text("About Robokalam")
                }
            }
        }
    }
}