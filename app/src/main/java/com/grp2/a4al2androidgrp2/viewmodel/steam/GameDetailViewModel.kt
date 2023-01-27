package com.grp2.a4al2androidgrp2.viewmodel.steam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.api.RetrofitInstance
import com.grp2.a4al2androidgrp2.api.steam.ApiSteamController
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameDetailViewModel: ViewModel() {

    lateinit var gameDetailLiveDate: MutableLiveData<Map<String, GameResponse>?>
    init {
        gameDetailLiveDate = MutableLiveData();
    }

    fun getGameDetailObserver(): MutableLiveData<Map<String, GameResponse>?> {
        return gameDetailLiveDate;
    }

    fun getGameDetail(steam_appId: Int, lang: String) {
        val api = RetrofitInstance.getRetrofitInstance().create(ApiSteamController::class.java)
        val call = api.getGameDetails(steam_appId, lang)
        call.enqueue(object: Callback<Map<String, GameResponse>> {
            override fun onResponse(call: Call<Map<String, GameResponse>>, response: Response<Map<String, GameResponse>>) {
                if (response.isSuccessful) {
                    gameDetailLiveDate.postValue(response.body())
                } else {
                    gameDetailLiveDate.postValue(null);
                }
            }

            override fun onFailure(call: Call<Map<String, GameResponse>>, t: Throwable) {
                gameDetailLiveDate.postValue(null)
            }
        })
    }
}