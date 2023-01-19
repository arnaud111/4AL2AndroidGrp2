package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.adapter.GameInfoAdapter
import com.grp2.a4al2androidgrp2.api.API
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import kotlinx.coroutines.launch
import java.util.*

class LikedGamesActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.liked_games_activity)
        val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        val context = this
        if (sharedPref.contains("token")) {
            val api = API(sharedPref.getString("token", "")!!)
            lifecycleScope.launch {
                val account = api.me()
                Log.d("My Account", account.toString())
                if (account == null) {
                    launchLogin()
                } else {
                    val games = getLikedGames(account, api)
                    if (games.size > 0) {
                        val adapter = GameInfoAdapter(games)
                        val recyclerView = findViewById<RecyclerView>(R.id.games_list)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(context)
                    } else {
                        findViewById<RecyclerView>(R.id.games_list).visibility = View.GONE
                        findViewById<ViewStub>(R.id.empty_likes).visibility = View.VISIBLE
                    }
                }
            }
        } else {
            launchLogin()
        }
    }

    private suspend fun getLikedGames(account: Account, api: API): MutableList<GameInfo> {
        val locale = Locale.getDefault()
        val games= mutableListOf<GameInfo>()
        for (game_id in account.likes) {
            val game = api.getGameDetails(game_id, locale.language)
            Log.d("GameInfo", game.toString())
            if (game != null) {
                games.add(game)
            }
        }
        return games
    }

    private fun launchLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}