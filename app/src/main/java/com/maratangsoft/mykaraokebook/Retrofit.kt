package com.maratangsoft.mykaraokebook

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

object RetrofitHelper {
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.manana.kr/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

interface RetrofitService {
    @GET("karaoke/{query}/{word}/{brand}.json")
    fun loadSearchData(@Path("query")query:String, @Path("word")word:String, @Path("brand")brand:String): Call<MutableList<Item>>

    @GET("karaoke/release/{targetMonth}/{brand}.json")
    fun loadNewSongData(@Path("targetMonth")targetMonth:String, @Path("brand")brand:String): Call<MutableList<Item>>

    @GET("karaoke/{query}/{word}/{brand}.json")
    fun loadSearchDataScalars(@Path("query")query:String, @Path("word")word:String, @Path("brand")brand:String): Call<String>

    @GET("karaoke/{brand}.json")
    fun loadNewSongDataScalars(@Path("brand")brand:String): Call<String>
}