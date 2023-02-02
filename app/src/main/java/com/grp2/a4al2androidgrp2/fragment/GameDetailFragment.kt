package com.grp2.a4al2androidgrp2.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.adapter.GameOpinionAdapter
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.*
import com.grp2.a4al2androidgrp2.viewmodel.auth.*
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameDetailViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameOpinionsViewModel
import com.grp2.a4al2androidgrp2.viewmodel.steam.SteamAccountViewModel
import java.util.*

class GameDetailFragment: Fragment() {

    var index = 0
    var gameId = 0
    var description_displayed = true
    lateinit var account: Account
    lateinit var meViewModel: MeViewModel
    lateinit var gameDetailViewModel: GameDetailViewModel
    lateinit var addLikeViewModel: AddLikeViewModel
    lateinit var removeLikeViewModel: RemoveLikeViewModel
    lateinit var addWishViewModel: AddWishViewModel
    lateinit var removeWishViewModel: RemoveWishViewModel
    lateinit var gameOpinionsViewModel: GameOpinionsViewModel
    lateinit var steamAccountViewModel: SteamAccountViewModel
    lateinit var token: String
    lateinit var opinionList: List<GameOpinion>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.game_detail, container, false)
        gameId = 0
        initGameDetailViewModel()
        initAllOnClickListener(view)
        initGameOpinionsViewModel()
        initSteamAccountViewModel()
        getUser()
        gameDetailViewModel.getGameDetail(gameId, Locale.getDefault().language)
        gameOpinionsViewModel.getGameOpinions(gameId, Locale.getDefault().language)
        initOnClickButton(view)
        return view
    }

    private fun initOnClickButton(view: View) {
        view.findViewById<TextView>(R.id.description).movementMethod = ScrollingMovementMethod()
    }

    private fun initGameOpinionsViewModel() {
        gameOpinionsViewModel = ViewModelProvider(this).get(GameOpinionsViewModel::class.java)
        gameOpinionsViewModel.getGameOpinionsObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                opinionList = it.reviews
                if (opinionList.size > 0) {
                    steamAccountViewModel.getPlayerPseudo(opinionList[index].author.steamid)
                }
            }
        }
    }

    private fun initSteamAccountViewModel() {
        steamAccountViewModel = ViewModelProvider(this).get(SteamAccountViewModel::class.java)
        steamAccountViewModel.getSteamAccountObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                opinionList[index].author.pseudo = it.pseudo
                index += 1
                if (opinionList.size > index) {
                    steamAccountViewModel.getPlayerPseudo(opinionList[index].author.steamid)
                } else {
                    displayOpinion()
                }
            }
        }
    }

    private fun displayOpinion() {
        val view = requireView()
        val adapter = GameOpinionAdapter(opinionList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.opinion_list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initAllOnClickListener(view: View) {
        initOnClickLike(view)
        initOnClickWish(view)
        initOnClickOpinion(view)
        initOnClickDescription(view)
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            findNavController().navigate(
                GameDetailFragmentDirections.actionGameDetailFragmentToHomePageFragment()
            )
        }
    }

    private fun getUser() {
        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        if (!sharedPref.contains("token")) {
            launchLogin()
        }
        token = sharedPref.getString("token", "")!!
        initMeViewModel()
        meViewModel.me(token)
    }

    private fun initOnClickOpinion(view: View) {
        view.findViewById<Button>(R.id.opinion_button).setOnClickListener {
            if (description_displayed) {
                view.findViewById<Button>(R.id.description_button).setBackgroundResource(R.drawable.border_button)
                view.findViewById<Button>(R.id.opinion_button).setBackgroundResource(R.drawable.button_full)
                view.findViewById<RecyclerView>(R.id.opinion_list).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.description).visibility = View.GONE
                description_displayed = false
            }
        }
    }

    private fun initOnClickDescription(view: View) {
        view.findViewById<Button>(R.id.description_button).setOnClickListener {
            if (!description_displayed) {
                view.findViewById<Button>(R.id.description_button).setBackgroundResource(R.drawable.button_full)
                view.findViewById<Button>(R.id.opinion_button).setBackgroundResource(R.drawable.border_button)
                view.findViewById<TextView>(R.id.description).visibility = View.VISIBLE
                view.findViewById<RecyclerView>(R.id.opinion_list).visibility = View.GONE
                description_displayed = true
            }
        }
    }

    private fun initOnClickLike(view: View) {
        view.findViewById<ImageView>(R.id.like).setOnClickListener {
            if (gameId in account.likes) {
                initRemoveLikeViewModel()
                removeLikeViewModel.removeLike(token, gameId)
            } else {
                initAddLikeViewModel()
                addLikeViewModel.addLike(token, gameId)
            }
        }
    }

    private fun initOnClickWish(view: View) {
        view.findViewById<ImageView>(R.id.wishlist).setOnClickListener {
            if (gameId in account.wishlist) {
                initRemoveWishViewModel()
                removeWishViewModel.removeWish(token, gameId)
            } else {
                initAddWishViewModel()
                addWishViewModel.addWish(token, gameId)
            }
        }
    }

    private fun initMeViewModel() {
        meViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        meViewModel.getAccountObserver().observe(viewLifecycleOwner) {
            if (it == null) {
                launchLogin()
            } else {
                account = it
                checkLikeAndWishList()
            }
        }
    }

    private fun initAddLikeViewModel() {
        addLikeViewModel = ViewModelProvider(this).get(AddLikeViewModel::class.java)
        addLikeViewModel.getAccountObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                account = it
                checkLikeAndWishList()
            }
        }
    }

    private fun initRemoveLikeViewModel() {
        removeLikeViewModel = ViewModelProvider(this).get(RemoveLikeViewModel::class.java)
        removeLikeViewModel.getAccountObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                account = it
                checkLikeAndWishList()
            }
        }
    }

    private fun initAddWishViewModel() {
        addWishViewModel = ViewModelProvider(this).get(AddWishViewModel::class.java)
        addWishViewModel.getAccountObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                account = it
                checkLikeAndWishList()
            }
        }
    }

    private fun initRemoveWishViewModel() {
        removeWishViewModel = ViewModelProvider(this).get(RemoveWishViewModel::class.java)
        removeWishViewModel.getAccountObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                account = it
                checkLikeAndWishList()
            }
        }
    }

    private fun checkLikeAndWishList() {
        val view = requireView()
        if (gameId in account.likes) {
            view.findViewById<ImageView>(R.id.like).setImageResource(R.drawable.like_full)
        } else {
            view.findViewById<ImageView>(R.id.like).setImageResource(R.drawable.like)
        }
        if (gameId in account.wishlist) {
            view.findViewById<ImageView>(R.id.wishlist).setImageResource(R.drawable.wishlist_full)
        } else {
            view.findViewById<ImageView>(R.id.wishlist).setImageResource(R.drawable.wishlist)
        }
    }

    private fun initGameDetailViewModel() {
        gameDetailViewModel = ViewModelProvider(this).get(GameDetailViewModel::class.java)
        gameDetailViewModel.getGameDetailObserver().observe(viewLifecycleOwner) {
            if (it == null) {
                launchHomePage()
            } else {
                if (it["$gameId"]?.success == true) {
                    displayGameContent(it["$gameId"]?.data!!)
                } else {
                    launchHomePage()
                }
            }
        }
    }

    private fun displayGameContent(game: GameInfo) {
        val view = requireView()

        Glide.with(this)
            .load(game.screenshots[0].path_full)
            .into(view.findViewById<ImageView>(R.id.background_image))

        Glide.with(this)
            .load(game.header_image)
            .into(view.findViewById<ImageView>(R.id.header_image))

        val background_image_item = view.findViewById<View>(R.id.background_image_item)
        Glide.with(this)
            .load(game.background)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    background_image_item.background = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        view.findViewById<TextView>(R.id.game_name).text = game.name
        view.findViewById<TextView>(R.id.game_publisher).text = game.publishers[0]
        view.findViewById<TextView>(R.id.description).text = Html.fromHtml(game.detailed_description)
    }

    private fun launchLogin() {
        findNavController().navigate(
            GameDetailFragmentDirections.actionGameDetailFragmentToLoginFragment()
        )
    }

    private fun launchHomePage() {
        findNavController().navigate(
            GameDetailFragmentDirections.actionGameDetailFragmentToHomePageFragment()
        )
    }
}