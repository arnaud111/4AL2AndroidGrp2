package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameDetailViewModel
import java.util.*

class GameDetailActivity : AppCompatActivity() {

    var gameId = 0
    lateinit var account: Account
    lateinit var meViewModel: MeViewModel
    lateinit var gameDetailViewModel: GameDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_detail)
        gameId = intent.getIntExtra("game_id", 0)
        initGameDetailViewModel()
        gameDetailViewModel.getGameDetail(gameId, Locale.getDefault().language)
        getUser()
    }

    private fun getUser() {
        val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        if (!sharedPref.contains("token")) {
            launchLogin()
        }
        initMeViewModel()
        meViewModel.me(sharedPref.getString("token", "")!!)
    }

    private fun initMeViewModel() {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        meViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it == null) {
                launchLogin()
            } else {
                account = it
                checkLikeAndWishList()
            }
        })
    }

    private fun checkLikeAndWishList() {
        if (gameId in account.likes) {
            findViewById<ImageView>(R.id.like).setImageResource(R.drawable.like_full)
        }
        if (gameId in account.wishlist) {
            findViewById<ImageView>(R.id.wishlist).setImageResource(R.drawable.wishlist_full)
        }
    }

    private fun initGameDetailViewModel() {
        gameDetailViewModel = ViewModelProvider(this).get(GameDetailViewModel::class.java)
        gameDetailViewModel.getGameDetailObserver().observe(this, Observer<Map<String, GameResponse>?> {
            if (it == null) {
                launchHomePage()
            } else {
                if (it["$gameId"]?.success == true) {
                    Glide.with(this)
                        .load(it["$gameId"]?.data?.screenshots?.get(0)?.path_full)
                        .into(findViewById<ImageView>(R.id.background_image))
                } else {
                    launchHomePage()
                }
            }
        })
    }

    private fun launchLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchHomePage() {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }
}
