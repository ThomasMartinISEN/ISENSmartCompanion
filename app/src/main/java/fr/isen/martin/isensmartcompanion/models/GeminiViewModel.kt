package fr.isen.martin.isensmartcompanion.ai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.martin.isensmartcompanion.data.ChatDatabase
import fr.isen.martin.isensmartcompanion.data.ChatExchanges
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeminiViewModel(application: Application) : AndroidViewModel(application) {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyA5WCHuQf2OLKh9R1xli5MHDevBiBorXXI"
    )

    private val chatDao = ChatDatabase.getDatabase(application).chatExchangesDao()

    private val _chatHistory = MutableStateFlow<List<ChatExchanges>>(emptyList())
    val chatHistory: StateFlow<List<ChatExchanges>> = _chatHistory.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            chatDao.getAllExchanges().collect { exchanges ->
                _chatHistory.value = exchanges
            }
        }
    }

    fun generateTextResponse(inputText: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val lastExchanges = _chatHistory.value.takeLast(10)

                val conversationHistory = lastExchanges.joinToString("\n") {
                    "Utilisateur: ${it.userMessage}\nIA: ${it.aiResponse}"
                }

                val prompt = """
                    Historique des derniers échanges :
                    $conversationHistory
                    
                    Question : $inputText
                    Réponds en gardant le contexte de l'historique ci-dessus.
                """.trimIndent()

                val response = generativeModel.generateContent(prompt)
                val aiResponse = response.text ?: "Pas de réponse trouvée"

                val exchange = ChatExchanges(userMessage = inputText, aiResponse = aiResponse)
                chatDao.insertExchange(exchange)

                loadHistory()
                onResult(aiResponse)
            } catch (e: Exception) {
                onResult("Erreur : ${e.localizedMessage}")
            }
        }
    }

    fun deleteExchange(exchangeId: Int) {
        viewModelScope.launch {
            chatDao.deleteExchange(exchangeId)
            _chatHistory.value = _chatHistory.value.filter { it.id != exchangeId }
        }
    }

    fun deleteAllExchanges() {
        viewModelScope.launch {
            chatDao.deleteAllExchanges()
            _chatHistory.value = emptyList()
        }
    }
}
