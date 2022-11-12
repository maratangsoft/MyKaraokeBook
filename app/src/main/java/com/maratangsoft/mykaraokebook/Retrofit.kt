package com.maratangsoft.mykaraokebook

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitHelper {
    fun getInstance(baseUrl:String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface RetrofitService {
    @GET("karaoke/{query}/{word}/{brand}.json")
    fun loadSearchData(
        @Path("query")query: String,
        @Path("word")word: String,
        @Path("brand")brand: String
    ): Call<MutableList<SongItem>>

    @GET("karaoke/release/{targetMonth}/{brand}.json")
    fun loadNewSongData(
        @Path("targetMonth")targetMonth: String,
        @Path("brand")brand: String
    ): Call<MutableList<SongItem>>

    @GET("v1/search/local.json")
    fun locateKaraoke(
        @Query("query")query: String,
        @Query("display")display: Int,
        @Header("X-Naver-Client-Id")clientId: String,
        @Header("X-Naver-Client-Secret")clientSecret: String
    ): Call<NaverResult>

//    @GET("v1/search/local.json")
//    fun locateKaraokeScalars(
//        @Query("query")query: String,
//        @Query("display")display: Int,
//        @Header("X-Naver-Client-Id")clientId: String,
//        @Header("X-Naver-Client-Secret")clientSecret: String
//    ): Call<String>
}