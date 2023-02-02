package com.grp2.a4al2androidgrp2.viewmodel.steam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.api.RetrofitInstance
import com.grp2.a4al2androidgrp2.api.steam.ApiSteamController
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchGameViewModel: ViewModel() {
    lateinit var searchGameLiveData: MutableLiveData<Map<String, GameResponse>?>
    init {
        searchGameLiveData = MutableLiveData();
    }

    fun searchGameObserver(): MutableLiveData<Map<String, GameResponse>?> {
        return searchGameLiveData;
    }

    fun searchGame(searched: String, lang: String) {
        val api = RetrofitInstance.getRetrofitInstance().create(ApiSteamController::class.java)
        val call = api.searchGames(searched, lang)
        call.enqueue(object: Callback<Map<String, GameResponse>> {
            override fun onResponse(call: Call<Map<String, GameResponse>>, response: Response<Map<String, GameResponse>>) {
                if (response.isSuccessful) {
                    searchGameLiveData.postValue(response.body())
                } else {
                    searchGameLiveData.postValue(null);
                }
            }

            override fun onFailure(call: Call<Map<String, GameResponse>>, t: Throwable) {
                searchGameLiveData.postValue(null)
            }
        })
    }
}