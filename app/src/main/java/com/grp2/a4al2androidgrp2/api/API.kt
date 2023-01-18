package com.grp2.a4al2androidgrp2.api

import android.util.Log
import com.grp2.a4al2androidgrp2.api.Auth.ApiAuthController
import com.grp2.a4al2androidgrp2.api.Auth.Response.LoginToken
import com.grp2.a4al2androidgrp2.api.Auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.api.Auth.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.dto.Account
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

class API(token: String = "") {

    private val baseUrl: String = "http://192.168.239.1:7589"
    private val apiAuthController: ApiAuthController;

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
            .addConverterFactory(
                GsonConverterFactory.create())
            .addCallAdapterFactory(
                CoroutineCallAdapterFactory())
            .build()
            .create(ApiAuthController::class.java)
    }

    suspend fun subscribe(email: String, password: String): Account? {

        val subscribeRequest = SubscribeRequest(
            email,
            password
        )

        try {
            val account = withContext(Dispatchers.IO) {
                apiAuthController.subscribe(subscribeRequest).await()
            }
            return account
        } catch (e: Exception) {
            Log.d("/auth/subscribe", e.toString())
            return null
        }
    }

    suspend fun login(email: String, password: String): LoginToken? {

        val loginRequest = LoginRequest(
            email,
            password
        )

        try {
            val loginToken = withContext(Dispatchers.IO) {
                apiAuthController.login(loginRequest).await()
            }
            return loginToken
        } catch (e: Exception) {
            Log.d("/auth/login", e.toString())
            return null
        }
    }

    suspend fun me(): Account? {

        try {
            val account = withContext(Dispatchers.IO) {
                apiAuthController.me().await()
            }
            return account
        } catch (e: Exception) {
            Log.d("/auth/me", e.toString())
            return null
        }
    }
}