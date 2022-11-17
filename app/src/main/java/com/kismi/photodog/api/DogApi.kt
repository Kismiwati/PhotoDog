package com.kismi.photodog.api


import com.kismi.photodog.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DogApi {

    companion object {

        //link URL digunakan untuk mengirimkan permintaan
        const val BASE_URL = "https://api.unsplash.com/"
        //bagian ini digunakan untuk menghasilkan api yang ditambahkan ke file gradle
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET("search/photos")

    //bagian ini digunakan untuk menangani threading dengan kotlin coroutines
    //function digunakan untuk menjeda dan melanjutkan
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchResponse
}