package fr.isen.martin.isensmartcompanion.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.martin.isensmartcompanion.ai.GeminiViewModel
import fr.isen.martin.isensmartcompanion.data.ChatExchanges
import java.text.SimpleDateFormat
import java.util.*
import fr.isen.martin.isensmartcompanion.utils.formatDate

@Composable
fun HistoryScreen(innerPadding: PaddingValues, viewModel: GeminiViewModel = viewModel()) {
    val exchanges by viewModel.chatHistory.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text("Historique des conversations", style = MaterialTheme.typography.headlineLarge)

        if (exchanges.isEmpty()) {
            Text("Aucun échange enregistré", modifier = Modifier.padding(top = 16.dp))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(exchanges) { exchange ->
                    ExchangeCard(exchange, onDelete = { viewModel.deleteExchange(exchange.id) })
                }
            }
        }

        Button(
            onClick = { viewModel.deleteAllExchanges() },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text("Supprimer tout l'historique")
        }
    }
}

@Composable
fun ExchangeCard(exchange: ChatExchanges, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDelete() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Utilisateur : ${exchange.userMessage}", style = MaterialTheme.typography.bodyLarge)
            Text("IA : ${exchange.aiResponse}", style = MaterialTheme.typography.bodyMedium)
            Text("Date : ${formatDate(exchange.timestamp)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

