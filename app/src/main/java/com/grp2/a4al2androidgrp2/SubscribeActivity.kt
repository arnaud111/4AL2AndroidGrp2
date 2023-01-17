package com.grp2.a4al2androidgrp2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.grp2.a4al2androidgrp2.api.API
import com.grp2.a4al2androidgrp2.api.request.SubscribeRequest
import com.grp2.a4al2androidgrp2.dto.Account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

        if (!error) {
            val api = Retrofit.Builder()
                .baseUrl("http://192.168.137.1:7589")
                .addConverterFactory(
                    GsonConverterFactory.create())
                .build()
                .create(API::class.java)

            val subscribeRequest = SubscribeRequest(
                email.text.toString(),
                password.text.toString()
            )

            api.subscribe(subscribeRequest)
                .enqueue(object: Callback<Account> {

                override fun onResponse(call: Call<Account>, response: Response<Account>) {
                    val account = response.body();
                    Log.d("Subscribe", account.toString())
                }

                override fun onFailure(call: Call<Account>, t: Throwable) {
                    t.printStackTrace();
                }
            })
        }
    }
}
