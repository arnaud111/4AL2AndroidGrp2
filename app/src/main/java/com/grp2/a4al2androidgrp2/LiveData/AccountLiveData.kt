package com.grp2.a4al2androidgrp2.LiveData

import androidx.lifecycle.MutableLiveData
import com.grp2.a4al2androidgrp2.dto.Account

class AccountLiveData: MutableLiveData<Account>() {

    override fun onActive() {
        // Récupérer la donnée
    }
    override fun onInactive() {
        // Tout stopper, peu importe que la donnée
        // soit récupérée ou non
    }
}