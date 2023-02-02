package com.grp2.a4al2androidgrp2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.adapter.GameInfoAdapter
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.grp2.a4al2androidgrp2.viewmodel.auth.MeViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameDetailViewModel
import java.util.*

class WishlistFragment: Fragment() {

    lateinit var meViewModel: MeViewModel
    lateinit var gameDetailViewModel: GameDetailViewModel
    lateinit var account: Account
    var gamesDetail: MutableList<GameInfo> = arrayListOf()
    var language = Locale.getDefault().language
    var index = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.wishlist_activity, container, false)
        if (!activity?.getPreferences(0)?.contains("token")!!) {
            findNavController().navigate(
                HomePageFragmentDirections.actionHomePageFragmentToLoginFragment()
            )
        }
        initMeViewModel()
        meViewModel.me(activity?.getPreferences(0)?.getString("token", "")!!)
        initOnClick(view)
        return view
    }

    private fun initOnClick(view: View) {
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            this.launchHomePage()
        }
    }

    private fun initMeViewModel() {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        meViewModel.getAccountObserver().observe(viewLifecycleOwner) {
            if (it == null) {
                launchLogin()
            } else {
                if (it.wishlist.isNotEmpty()) {
                    account = it
                    initGameDetailViewModel()
                    gameDetailViewModel.getGameDetail(it.wishlist[index], language)
                } else {
                    val view = requireView()
                    view.findViewById<RecyclerView>(R.id.games_list).visibility = View.GONE
                    view.findViewById<ViewStub>(R.id.empty_wishlist).visibility = View.VISIBLE
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
                val adapter = GameInfoAdapter(gamesDetail, R.id.action_wishlistFragment_to_gameDetailFragment, R.id.action_gameDetailFragment_to_wishlistFragment)
                val recyclerView = view.findViewById<RecyclerView>(R.id.games_list)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(view.context)
                index += 1
                if (index < account.wishlist.size)
                    gameDetailViewModel.getGameDetail(account.wishlist[index], language)
            }
        }
    }

    private fun launchLogin() {
        findNavController().navigate(
            WishlistFragmentDirections.actionWishlistFragmentToLoginFragment()
        )
    }

    private fun launchHomePage() {
        findNavController().navigate(
            WishlistFragmentDirections.actionWishlistFragmentToHomePageFragment()
        )
    }
}