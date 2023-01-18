package com.grp2.a4al2androidgrp2.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grp2.a4al2androidgrp2.api.API
import com.grp2.a4al2androidgrp2.dto.Account
import kotlinx.coroutines.launch

class AccountViewModel(context: Context) : ViewModel() {

    private val _accountLiveData = MutableLiveData<Account>()
    val account: LiveData<Account>
        get() = _accountLiveData

    init {

    }
}