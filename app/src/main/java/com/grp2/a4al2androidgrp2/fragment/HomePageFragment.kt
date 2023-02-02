package com.grp2.a4al2androidgrp2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameDetailViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameMostPlayedViewModel
import java.util.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.adapter.GameInfoAdapter

class HomePageFragment: Fragment() {
    lateinit var gameMostPlayedViewModel: GameMostPlayedViewModel
    lateinit var gameDetailViewModel: GameDetailViewModel
    lateinit var account: Account
    var gamesDetail: MutableList<GameInfo> = arrayListOf()
    var gamesMostPlayed: MutableList<Int> = arrayListOf()
    var language = Locale.getDefault().language
    var index = 0
    val MAX_SHOWN_GAMES = 50

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!activity?.getPreferences(0)?.contains("token")!!) {
            findNavController().navigate(
                HomePageFragmentDirections.actionHomePageFragmentToLoginFragment()
            )
        }
        initGameMostPlayedViewModel()
        gameMostPlayedViewModel.getGameMostPlayed(language)
        val view = inflater.inflate(R.layout.homepage, container, false)
        initOnClickButton(view)
        initOnFocus(view)
        return view
    }

    private fun initOnFocus(view: View){
        view.findViewById<EditText>(R.id.search_bar).setOnFocusChangeListener{ _, hasFocus->
            if(hasFocus) {
                findNavController().navigate(
                    HomePageFragmentDirections.actionHomePageFragmentToSearchBarFragment()
                )
            }
        }
    }

    private fun initOnClickButton(view: View) {
        view.findViewById<ImageButton>(R.id.wishlist).setOnClickListener {
            launchWishlist()
        }
        view.findViewById<ImageButton>(R.id.likelist).setOnClickListener {
            launchLikelist()
        }
    }

    private fun initGameMostPlayedViewModel() {
        gameMostPlayedViewModel = ViewModelProvider(this).get(GameMostPlayedViewModel::class.java)
        gameMostPlayedViewModel.getGameMostPlayedObserver().observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().navigate(
                    HomePageFragmentDirections.actionHomePageFragmentToLoginFragment()
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
                val view = requireView()
                val adapter = GameInfoAdapter(gamesDetail, R.id.action_homePageFragment_to_gameDetailFragment, R.id.action_gameDetailFragment_to_homePageFragment)
                val recyclerView = view.findViewById<RecyclerView>(R.id.games_list)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                Glide.with(this)
                    .load(gamesDetail[0].background_raw)
                    .into(view.findViewById<ImageView>(R.id.background_image))
                Glide.with(this)
                    .load(gamesDetail[0].header_image)
                    .into(view.findViewById<ImageView>(R.id.header_image))
                view.findViewById<TextView>(R.id.title).text = gamesDetail[0].name
                var description: String = gamesDetail[0].short_description.slice(0..100) + "..."
                view.findViewById<TextView>(R.id.description).text = description

                view.findViewById<Button>(R.id.main_detail).setOnClickListener {
                    val action = bundleOf(
                        "gameId" to gamesDetail[0].steam_appid,
                        "return_destination" to R.id.action_gameDetailFragment_to_homePageFragment
                    )
                    Navigation.findNavController(view).navigate(R.id.action_homePageFragment_to_gameDetailFragment, action)
                }


                index += 1
                if (index < gamesMostPlayed.size && index < MAX_SHOWN_GAMES)
                    gameDetailViewModel.getGameDetail(gamesMostPlayed[index], language)
            }
        }
    }

    private fun launchWishlist() {
        findNavController().navigate(
            HomePageFragmentDirections.actionHomePageFragmentToWishlistFragment()
        )
    }

    private fun launchLikelist() {
        findNavController().navigate(
            HomePageFragmentDirections.actionHomePageFragmentToLikeListFragment()
        )
    }
}