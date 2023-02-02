package com.grp2.a4al2androidgrp2.viewmodel.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.api.RetrofitInstance
import com.grp2.a4al2androidgrp2.api.auth.ApiAuthController
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.dto.account.Account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgottenPasswordViewModel: ViewModel() {

    lateinit var accountData: MutableLiveData<Account?>
    init {
        accountData = MutableLiveData();
    }

    fun getAccountDataObserver(): MutableLiveData<Account?> {
        return accountData;
    }

    fun sendNewPassword(body: LoginRequest) {
        val api = RetrofitInstance.getRetrofitInstance().create(ApiAuthController::class.java)
        val call = api.resetPassword(body)
            call.enqueue(object: Callback<Account> {
            override fun onFailure(call: Call<Account>, t: Throwable) {
                accountData.postValue(null)
            }

            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.isSuccessful) {
                    accountData.postValue(response.body())
                } else {
                    accountData.postValue(null)
                }
            }
        })
    }
}