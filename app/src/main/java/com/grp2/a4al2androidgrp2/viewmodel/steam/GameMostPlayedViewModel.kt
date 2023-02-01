package com.grp2.a4al2androidgrp2.viewmodel.steam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.api.RetrofitInstance
import com.grp2.a4al2androidgrp2.api.steam.ApiSteamController
import com.grp2.a4al2androidgrp2.dto.game.GameMostPlayedResponse
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameMostPlayedViewModel: ViewModel() {

    lateinit var gameMostPlayedLiveDate: MutableLiveData<GameMostPlayedResponse?>
    init {
        gameMostPlayedLiveDate = MutableLiveData();
    }

    fun getGameMostPlayedObserver(): MutableLiveData<GameMostPlayedResponse?> {
        return gameMostPlayedLiveDate;
    }

    fun getGameMostPlayed(lang: String) {
        val api = RetrofitInstance.getRetrofitInstance().create(ApiSteamController::class.java)
        val call = api.getGameMostPlayed(lang)
        call.enqueue(object: Callback<GameMostPlayedResponse> {
            override fun onResponse(call: Call<GameMostPlayedResponse>, response: Response<GameMostPlayedResponse>) {
                if (response.isSuccessful) {
                    gameMostPlayedLiveDate.postValue(response.body())
                } else {
                    gameMostPlayedLiveDate.postValue(null);
                }
            }

            override fun onFailure(call: Call<GameMostPlayedResponse>, t: Throwable) {
                gameMostPlayedLiveDate.postValue(null)
            }
        })
    }
}