package com.grp2.a4al2androidgrp2.viewmodel.steam

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.api.RetrofitInstance
import com.grp2.a4al2androidgrp2.api.steam.ApiSteamController
import com.grp2.a4al2androidgrp2.dto.game.SteamAccount
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SteamAccountViewModel: ViewModel() {

    lateinit var gameDetailLiveDate: MutableLiveData<SteamAccount?>
    init {
        gameDetailLiveDate = MutableLiveData();
    }

    fun getSteamAccountObserver(): MutableLiveData<SteamAccount?> {
        return gameDetailLiveDate;
    }

    fun getPlayerPseudo(steamid: Int) {
        val api = RetrofitInstance.getRetrofitInstance().create(ApiSteamController::class.java)
        val call = api.getPlayerPseudo(steamid)
        call.enqueue(object: Callback<SteamAccount> {
            override fun onResponse(call: Call<SteamAccount>, response: Response<SteamAccount>) {
                if (response.isSuccessful) {
                    gameDetailLiveDate.postValue(response.body())
                } else {
                    gameDetailLiveDate.postValue(null);
                }
            }

            override fun onFailure(call: Call<SteamAccount>, t: Throwable) {
                gameDetailLiveDate.postValue(null)
            }
        })
    }
}