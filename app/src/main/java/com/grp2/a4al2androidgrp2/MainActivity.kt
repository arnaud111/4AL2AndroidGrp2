package com.grp2.a4al2androidgrp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)
    }

    fun showSubscribe(view: View) {
        val intent = Intent(this, SubscribeActivity::class.java)
        startActivity(intent)
    }

    fun logIn(view: View) {
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
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

        if (!error) {
            Log.d("LogIn", "Ok")
        }
    }
}