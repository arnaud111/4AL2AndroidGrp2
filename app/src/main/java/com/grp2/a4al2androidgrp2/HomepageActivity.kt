package com.grp2.a4al2androidgrp2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class HomepageActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
    }

    fun launchLikedGames(view: View) {
        val intent = Intent(this, LikedGamesActivity::class.java)
        startActivity(intent)
        finish()
    }
}