package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grp2.a4al2androidgrp2.adapter.GameInfoAdapter
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.grp2.a4al2androidgrp2.dto.game.GameMostPlayedResponse
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameDetailViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameMostPlayedViewModel
import java.util.*

class HomepageActivity : AppCompatActivity()  {
    lateinit var gameMostPlayedViewModel: GameMostPlayedViewModel
    lateinit var gameDetailViewModel: GameDetailViewModel
    lateinit var account: Account
    var gamesDetail: MutableList<GameInfo> = arrayListOf()
    var gamesMostPlayed: MutableList<Int> = arrayListOf()
    var context = this
    var language = Locale.getDefault().language
    var index = 0;
    val MAX_SHOWN_GAMES = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
        val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        if (!sharedPref.contains("token")) {
            launchLogin()
        }
        initGameMostPlayedViewModel()
        gameMostPlayedViewModel.getGameMostPlayed(language)
    }

    private fun initGameMostPlayedViewModel() {
        gameMostPlayedViewModel = ViewModelProvider(this).get(GameMostPlayedViewModel::class.java)
        gameMostPlayedViewModel.getGameMostPlayedObserver().observe(this, Observer<GameMostPlayedResponse?> {
            if (it == null) {
                launchLogin()
            } else {
                it.response.ranks.forEach { game ->
                    //TODO mettre les prix payants
                    gamesMostPlayed.add(game.appid)
                }
                initGameDetailViewModel()
                if(gamesMostPlayed.size > 0){
                    gameDetailViewModel.getGameDetail(gamesMostPlayed[0], language)

                }

            }
        })
    }

    private fun initGameDetailViewModel() {
        gameDetailViewModel = ViewModelProvider(this).get(GameDetailViewModel::class.java)
        gameDetailViewModel.getGameDetailObserver().observe(this, Observer<Map<String, GameResponse>?> {
            if (it != null) {
                it.forEach { game ->
                    if (game.value.success) {
                        gamesDetail.add(game.value.data)
                    }
                }
                val adapter = GameInfoAdapter(gamesDetail)
                val recyclerView = findViewById<RecyclerView>(R.id.games_list)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(context)
                Glide.with(this)
                    .load(gamesDetail[0].background_raw)
                    .into(findViewById<ImageView>(R.id.background_image))
                Glide.with(this)
                    .load(gamesDetail[0].header_image)
                    .into(findViewById<ImageView>(R.id.header_image))
                findViewById<TextView>(R.id.title).text = gamesDetail[0].name
                var description: String = gamesDetail[0].short_description.slice(0..100) + "..."
                findViewById<TextView>(R.id.description).text = description

                findViewById<Button>(R.id.main_detail).setOnClickListener {
                    val intent = Intent(this, GameDetailActivity::class.java)
                    intent.putExtra("game_id", gamesDetail[0].steam_appid)
                    startActivity(intent)
                }

                index += 1
                if (index < gamesMostPlayed.size && index < MAX_SHOWN_GAMES)
                    gameDetailViewModel.getGameDetail(gamesMostPlayed[index], language)
            }
        })
    }

    private fun launchLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun launchLikedGames(view: View) {
        val intent = Intent(this, LikedGamesActivity::class.java)
        startActivity(intent)
    }

    fun launchWishlist(view: View) {
        val intent = Intent(this, WishlistActivity::class.java)
        startActivity(intent)
    }
}