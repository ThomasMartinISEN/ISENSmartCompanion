package fr.isen.janowski.isensmartcompanion.api

import fr.isen.janowski.isensmartcompanion.models.EventModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("events.json")
    fun getEvents(): Call<List<EventModel>>
}