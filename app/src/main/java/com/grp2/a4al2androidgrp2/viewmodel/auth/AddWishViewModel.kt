package com.grp2.a4al2androidgrp2.viewmodel.auth

import  androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.api.RetrofitInstance
import com.grp2.a4al2androidgrp2.api.auth.ApiAuthController
import com.grp2.a4al2androidgrp2.dto.account.Account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddWishViewModel: ViewModel() {

    lateinit var accountLiveDate: MutableLiveData<Account?>
    init {
        accountLiveDate = MutableLiveData();
    }

    fun getAccountObserver(): MutableLiveData<Account?> {
        return accountLiveDate;
    }

    fun addWish(token: String, steam_appid: Int) {
        val api = RetrofitInstance.getRetrofitInstance(token).create(ApiAuthController::class.java)
        val call = api.addWish(steam_appid)
        call.enqueue(object: Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.isSuccessful) {
                    accountLiveDate.postValue(response.body())
                } else {
                    accountLiveDate.postValue(null)
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                accountLiveDate.postValue(null)
            }
        })
    }
}