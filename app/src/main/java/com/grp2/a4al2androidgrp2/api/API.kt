package com.grp2.a4al2androidgrp2.api

import android.util.Log
import com.grp2.a4al2androidgrp2.api.auth.ApiAuthController
import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.api.auth.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.api.steam.ApiSteamController
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

class API(token: String = "") {

    private val baseUrl: String = "http://192.168.1.35:7589"
    private val apiAuthController: ApiAuthController;
    private val apiSteamController: ApiSteamController = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ApiSteamController::class.java)

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()

        this.apiAuthController = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(ApiAuthController::class.java)
    }

    suspend fun subscribe(email: String, password: String): Account? {

        val subscribeRequest = SubscribeRequest(
            email,
            password
        )

        return try {
            val account = withContext(Dispatchers.IO) {
                apiAuthController.subscribe(subscribeRequest).await()
            }
            account
        } catch (e: Exception) {
            Log.d("/auth/subscribe", e.toString())
            null
        }
    }

    suspend fun login(email: String, password: String): LoginToken? {

        val loginRequest = LoginRequest(
            email,
            password
        )

        return try {
            val loginToken = withContext(Dispatchers.IO) {
                apiAuthController.login(loginRequest).await()
            }
            loginToken
        } catch (e: Exception) {
            Log.d("/auth/login", e.toString())
            null
        }
    }

    suspend fun me(): Account? {

        return try {
            val account = withContext(Dispatchers.IO) {
                apiAuthController.me().await()
            }
            account
        } catch (e: Exception) {
            Log.d("/auth/me", e.toString())
            null
        }
    }

    suspend fun getGameDetails(steam_appId: Int, lang: String): GameInfo? {

        return try {
            val game = withContext(Dispatchers.IO) {
                apiSteamController.getGameDetails(steam_appId, lang).await()
            }
            if (game.containsKey("$steam_appId")) {
                game["$steam_appId"]?.data
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("getGameDetails", e.toString())
            null
        }
    }
}