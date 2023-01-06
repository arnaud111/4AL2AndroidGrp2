package com.grp2.a4al2androidgrp2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showNewLayout(view: View) {
        val intent = Intent(this, SubscribeActivity::class.java)
        startActivity(intent)
    }
}