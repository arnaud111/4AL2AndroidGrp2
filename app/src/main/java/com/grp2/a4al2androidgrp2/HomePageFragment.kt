package com.grp2.a4al2androidgrp2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.grp2.a4al2androidgrp2.dto.game.GameMostPlayedResponse
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameDetailViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameMostPlayedViewModel
import java.util.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grp2.a4al2androidgrp2.adapter.GameInfoAdapter
import com.grp2.a4al2androidgrp2.dto.game.GameResponse

class HomePageFragment: Fragment() {
    lateinit var gameMostPlayedViewModel: GameMostPlayedViewModel
    lateinit var gameDetailViewModel: GameDetailViewModel
    lateinit var account: Account
    var gamesDetail: MutableList<GameInfo> = arrayListOf()
    var gamesMostPlayed: MutableList<Int> = arrayListOf()
    var context = this
    var language = Locale.getDefault().language
    var index = 0;
    val MAX_SHOWN_GAMES = 50
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("Guys, we are in")
        if (!activity?.getPreferences(0)?.contains("token")!!) {
            findNavController().navigate(
                SubscribeFragmentDirections.actionSubscribeFragmentToLoginFragment()
            )
        }
        initGameMostPlayedViewModel()
        gameMostPlayedViewModel.getGameMostPlayed(language)
        return inflater.inflate(R.layout.homepage, container, false)
    }

    private fun initGameMostPlayedViewModel() {
        gameMostPlayedViewModel = ViewModelProvider(this).get(GameMostPlayedViewModel::class.java)
        gameMostPlayedViewModel.getGameMostPlayedObserver().observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().navigate(
                    SubscribeFragmentDirections.actionSubscribeFragmentToLoginFragment()
                )
            } else {
                it.response.ranks.forEach { game ->
                    //TODO mettre les prix payants
                    gamesMostPlayed.add(game.appid)
                }
                initGameDetailViewModel()
                if (gamesMostPlayed.size > 0) {
                    gameDetailViewModel.getGameDetail(gamesMostPlayed[0], language)

                }

            }
        }
    }

    private fun initGameDetailViewModel() {
        gameDetailViewModel = ViewModelProvider(this).get(GameDetailViewModel::class.java)
        gameDetailViewModel.getGameDetailObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                it.forEach { game ->
                    if (game.value.success) {
                        gamesDetail.add(game.value.data)
                    }
                }
                val adapter = GameInfoAdapter(gamesDetail)
                val recyclerView = requireView().findViewById<RecyclerView>(R.id.games_list)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                Glide.with(this)
                    .load(gamesDetail[0].background_raw)
                    .into(requireView().findViewById<ImageView>(R.id.background_image))
                Glide.with(this)
                    .load(gamesDetail[0].header_image)
                    .into(requireView().findViewById<ImageView>(R.id.header_image))
                requireView().findViewById<TextView>(R.id.title).text = gamesDetail[0].name
                var description: String = gamesDetail[0].short_description.slice(0..100) + "..."
                requireView().findViewById<TextView>(R.id.description).text = description

                requireView().findViewById<Button>(R.id.main_detail).setOnClickListener {
                    //TODO: Navigate to gamedetail fragment with id in bundle
                    /*val intent = Intent(this, GameDetailActivity::class.java)
                    intent.putExtra("game_id", gamesDetail[0].steam_appid)
                    startActivity(intent)*/
                }

                index += 1
                if (index < gamesMostPlayed.size && index < MAX_SHOWN_GAMES)
                    gameDetailViewModel.getGameDetail(gamesMostPlayed[index], language)
            }
        }
    }
}