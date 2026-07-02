package com.all_here.appmakkie.network

import com.all_here.appmakkie.data.InternetItem
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json



private const val BASE_URL = "https://training-uploads.internshala.com"
//private val json = Json {
//    ignoreUnknownKeys = true // prevents crash if API has extra fields
//    prettyPrint = true
//    isLenient = true
//}
private val retrofit = Retrofit.Builder()
    .addConverterFactory(
//        ScalarsConverterFactory()
        Json.asConverterFactory(
            "application/json".toMediaType()
        )
    )
    .baseUrl(BASE_URL)
    .build()



interface FlashApiService {
    @GET("android/grocery_delivery_app/items.json")
    suspend fun getItems(): List<InternetItem>
}

object FlashApi {
    val retroService: FlashApiService by lazy {
        retrofit.create(FlashApiService::class.java)
    }
}
