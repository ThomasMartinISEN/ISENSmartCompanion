package fr.isen.janowski.isensmartcompanion.screens

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EventsScreen(innerPadding: PaddingValues) {
    Button(
        onClick = {
            Log.d("eventscreen", "onclick")
                  },
        content = {
            Text("Temporary button")
                  }
    )
}