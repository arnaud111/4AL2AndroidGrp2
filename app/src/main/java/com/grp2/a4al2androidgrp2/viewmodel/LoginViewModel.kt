package com.grp2.a4al2androidgrp2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.api.RetrofitInstance
import com.grp2.a4al2androidgrp2.api.auth.ApiAuthController
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    lateinit var loginTokenLiveDate: MutableLiveData<LoginToken?>
    init {
        loginTokenLiveDate = MutableLiveData();
    }

    fun getLoginTokenObserver(): MutableLiveData<LoginToken?> {
        return loginTokenLiveDate;
    }

    fun login(body: LoginRequest) {
        val api = RetrofitInstance.getRetrofitInstance().create(ApiAuthController::class.java)
        val call = api.login(body)
        call.enqueue(object: Callback<LoginToken> {
            override fun onFailure(call: Call<LoginToken>, t: Throwable) {
                loginTokenLiveDate.postValue(null)
            }

            override fun onResponse(call: Call<LoginToken>, response: Response<LoginToken>) {
                if (response.isSuccessful) {
                    loginTokenLiveDate.postValue(response.body())
                } else {
                    loginTokenLiveDate.postValue(null)
                }
            }
        })
    }
}