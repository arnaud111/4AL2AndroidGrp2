package com.grp2.a4al2androidgrp2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.grp2.a4al2androidgrp2.api.auth.request.LoginRequest
import com.grp2.a4al2androidgrp2.api.auth.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.account.LoginToken
import com.grp2.a4al2androidgrp2.viewmodel.auth.LoginViewModel
import com.grp2.a4al2androidgrp2.viewmodel.auth.SubscribeViewModel

class SubscribeActivity: AppCompatActivity() {

    lateinit var loginViewModel: LoginViewModel
    lateinit var subscribeViewModel: SubscribeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subscribe_activity)
        this.popup()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun popup() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_password, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val passwordField = findViewById<EditText>(R.id.password)
        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= passwordField.right - passwordField.compoundDrawables[2].bounds.width()) {
                    popupWindow.showAsDropDown(passwordField, 0, 0, Gravity.END)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    fun subscribe(view: View) {
        val username = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirm_password)

        if (!this.checkSubscribeInfo(username, email, password, confirmPassword)) {
            initSubscribeViewModel()
            val subscribeRequest = SubscribeRequest (
                email.text.toString(),
                password.text.toString()
            )
            subscribeViewModel.subscribe(subscribeRequest)
        }
    }

    private fun initSubscribeViewModel() {
        subscribeViewModel = ViewModelProvider(this).get(SubscribeViewModel::class.java)
        subscribeViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it == null) {
                Toast.makeText(this@SubscribeActivity, "Failed to subscribe", Toast.LENGTH_LONG).show()
            } else {
                loginRequest()
            }
        })
    }

    private fun launchHomePage() {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchLoginPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginRequest() {

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

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
                Toast.makeText(this@SubscribeActivity, "Failed to login", Toast.LENGTH_LONG).show()
                launchLoginPage()
            } else {
                val sharedPref: SharedPreferences = getSharedPreferences("token_pref", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("token", it.token)
                editor.apply()

                launchHomePage()
            }
        })
    }

    private fun checkSubscribeInfo(username: EditText, email: EditText, password: EditText, confirmPassword: EditText): Boolean {
        var error = false

        username.setBackgroundResource(R.drawable.edit_text_background)
        email.setBackgroundResource(R.drawable.edit_text_background)
        password.setBackgroundResource(R.drawable.edit_text_background)
        confirmPassword.setBackgroundResource(R.drawable.edit_text_background)

        if (username.text.toString() == "") {
            username.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        if (email.text.toString() == "" ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        if (password.text.toString().length < 8 ||
            !password.text.toString().matches(Regex(".*[A-Z].*")) ||
            !password.text.toString().matches(Regex(".*[a-z].*")) ||
            !password.text.toString().matches(Regex(".*\\d.*"))) {
            password.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        if (confirmPassword.text.toString() == "" ||
            confirmPassword.text.toString() != password.text.toString()) {
            confirmPassword.setBackgroundResource(R.drawable.edit_text_error)
            error = true
        }

        return error;
    }
}
