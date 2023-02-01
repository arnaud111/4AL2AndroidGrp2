package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import com.grp2.a4al2androidgrp2.viewmodel.auth.LoginViewModel
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel

class MainActivityOld : AppCompatActivity() {

    lateinit var loginViewModel: LoginViewModel
    lateinit var meViewModel: MeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        if (sharedPref.contains("token")) {
            initMeViewModel()
            meViewModel.me(sharedPref.getString("token", "")!!)
        }
        setContentView(R.layout.login_main)
    }

    private fun initMeViewModel() {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        meViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it != null) {
                launchHomePage()
            }
        })
    }

    private fun launchHomePage() {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showSubscribe(view: View) {
        val intent = Intent(this, SubscribeActivity::class.java)
        startActivity(intent)
    }

    fun login(view: View) {
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        if (!loginChecker(email, password)) {
            loginRequest(email, password)
        }
    }

    private fun loginRequest(email: EditText, password: EditText) {
        initLoginViewModel();
        val loginRequest = LoginRequest(
            email.text.toString(),
            password.text.toString()
        )
        loginViewModel.login(loginRequest)
    }

    private fun initLoginViewModel() {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.getLoginTokenObserver().observe(this, Observer<LoginToken?> {
            if (it == null) {
                Toast.makeText(this@MainActivityOld, "Failed to login", Toast.LENGTH_LONG).show()
            } else {
                val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("token", it.token)
                editor.apply()

                launchHomePage()
            }
        })
    }

    private fun loginChecker(email: EditText, password: EditText): Boolean {
        var error = false

        email.setBackgroundResource(R.drawable.edit_text_background)
        password.setBackgroundResource(R.drawable.edit_text_background)

        if (email.text.toString() == "") {
            email.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        if (password.text.toString() == "") {
            password.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        return error;
    }
}