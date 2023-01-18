package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.grp2.a4al2androidgrp2.api.API
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        if (sharedPref.contains("token")) {
            val api = API(sharedPref.getString("token", "")!!);
            lifecycleScope.launch {
                val account = api.me()
                Log.d("My Account", account.toString())
            }
        }
        setContentView(R.layout.login_main)
    }

    fun showSubscribe(view: View) {
        val intent = Intent(this, SubscribeActivity::class.java)
        startActivity(intent)
    }

    fun login(view: View) {
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        if (!loginChecker(email, password)) {
            val api = API()
            lifecycleScope.launch {
                val login_token = api.login(email.text.toString(), password.text.toString())
                if (login_token != null) {

                    val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("token", login_token.token)
                    editor.apply()

                }
                Log.d("Login", login_token.toString())
            }
        }
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