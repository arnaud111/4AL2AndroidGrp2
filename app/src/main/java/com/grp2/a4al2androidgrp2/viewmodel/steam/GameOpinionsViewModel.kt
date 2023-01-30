package com.grp2.a4al2androidgrp2.viewmodel.steam

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.api.RetrofitInstance
import com.grp2.a4al2androidgrp2.api.steam.ApiSteamController
import com.grp2.a4al2androidgrp2.dto.game.GameOpinionsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameOpinionsViewModel: ViewModel() {

    lateinit var gameDetailLiveDate: MutableLiveData<GameOpinionsResponse?>
    init {
        gameDetailLiveDate = MutableLiveData();
    }

    fun getGameOpinionsObserver(): MutableLiveData<GameOpinionsResponse?> {
        return gameDetailLiveDate;
    }

    fun getGameOpinions(steam_appId: Int, lang: String) {
        val api = RetrofitInstance.getRetrofitInstance().create(ApiSteamController::class.java)
        val call = api.getGameOpinions(steam_appId, lang)
        call.enqueue(object: Callback<GameOpinionsResponse> {
            override fun onResponse(call: Call<GameOpinionsResponse>, response: Response<GameOpinionsResponse>) {
                if (response.isSuccessful) {
                    gameDetailLiveDate.postValue(response.body())
                } else {
                    gameDetailLiveDate.postValue(null);
                }
            }

            override fun onFailure(call: Call<GameOpinionsResponse>, t: Throwable) {
                gameDetailLiveDate.postValue(null)
            }
        })
    }
}