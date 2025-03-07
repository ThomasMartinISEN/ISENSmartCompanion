package fr.isen.martin.isensmartcompanion.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatExchangesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchange(exchange: ChatExchanges): Long

    @Query("SELECT * FROM chat_exchanges ORDER BY timestamp DESC")
    fun getAllExchanges(): Flow<List<ChatExchanges>>

    @Query("DELETE FROM chat_exchanges WHERE id = :exchangeId")
    suspend fun deleteExchange(exchangeId: Int): Int

    @Query("DELETE FROM chat_exchanges")
    suspend fun deleteAllExchanges(): Int
}

