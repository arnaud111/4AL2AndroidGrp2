package com.grp2.a4al2androidgrp2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.grp2.a4al2androidgrp2.api.API
import kotlinx.coroutines.launch

class SubscribeActivity: AppCompatActivity() {
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
            val api = API()
            lifecycleScope.launch {
                val account = api.subscribe(email.text.toString(), password.text.toString())
                Log.d("Subscribe", account.toString())
                if (account != null) {
                    loginRequest(email, password)
                }
            }
        }
    }

    private fun launchHomePage() {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginRequest(email: EditText, password: EditText) {
        val api = API()
        lifecycleScope.launch {
            val login_token = api.login(email.text.toString(), password.text.toString())
            if (login_token != null) {

                val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("token", login_token.token)
                editor.apply()

                launchHomePage()
            }
            Log.d("Login", login_token.toString())
        }
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
