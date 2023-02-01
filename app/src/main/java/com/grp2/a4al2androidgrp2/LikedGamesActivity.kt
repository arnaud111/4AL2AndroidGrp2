package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.adapter.GameInfoAdapter
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameDetailViewModel
import java.util.*

class LikedGamesActivity : AppCompatActivity()  {

    lateinit var meViewModel: MeViewModel
    lateinit var gameDetailViewModel: GameDetailViewModel
    lateinit var account: Account
    var gamesDetail: MutableList<GameInfo> = arrayListOf()
    var context = this
    var language = Locale.getDefault().language
    var index = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.liked_games_activity)
        val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        if (!sharedPref.contains("token")) {
            launchLogin()
        }
        initMeViewModel()
        meViewModel.me(sharedPref.getString("token", "")!!)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { this.launchHomePage() }
    }

    private fun initMeViewModel() {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        meViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it == null) {
                launchLogin()
            } else {
                if (it.likes.size > 0) {
                    account = it
                    initGameDetailViewModel()
                    gameDetailViewModel.getGameDetail(it.likes[index], language)
                } else {
                    findViewById<RecyclerView>(R.id.games_list).visibility = View.GONE
                    findViewById<ViewStub>(R.id.empty_likes).visibility = View.VISIBLE
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
                index += 1
                if (index < account.likes.size)
                    gameDetailViewModel.getGameDetail(account.likes[index], language)
            }
        })
    }

    private fun launchLogin() {
        val intent = Intent(this, MainActivityOld::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchHomePage() {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
        finish()
    }
}