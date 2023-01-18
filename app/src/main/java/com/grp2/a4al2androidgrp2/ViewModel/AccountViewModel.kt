package com.grp2.a4al2androidgrp2.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grp2.a4al2androidgrp2.dto.Account

class AccountViewModel : ViewModel() {

    val currentAccount: MutableLiveData<Account> by lazy {
        MutableLiveData<Account>()
    }
}