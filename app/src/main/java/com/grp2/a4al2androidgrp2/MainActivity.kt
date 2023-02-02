package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import com.grp2.a4al2androidgrp2.viewmodel.auth.LoginViewModel
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel
import okhttp3.internal.cache2.Relay.Companion.edit

class MainActivity : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)
    }
}
