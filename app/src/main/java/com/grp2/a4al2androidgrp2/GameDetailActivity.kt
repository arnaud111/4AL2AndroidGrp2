package com.grp2.a4al2androidgrp2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewStub
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.grp2.a4al2androidgrp2.dto.account.Account
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import com.grp2.a4al2androidgrp2.viewmodel.auth.*
import com.grp2.a4al2androidgrp2.viewmodel.steam.GameDetailViewModel
import java.util.*

class GameDetailActivity : AppCompatActivity() {

    var gameId = 0
    var description_displayed = true
    lateinit var account: Account
    lateinit var meViewModel: MeViewModel
    lateinit var gameDetailViewModel: GameDetailViewModel
    lateinit var addLikeViewModel: AddLikeViewModel
    lateinit var removeLikeViewModel: RemoveLikeViewModel
    lateinit var addWishViewModel: AddWishViewModel
    lateinit var removeWishViewModel: RemoveWishViewModel
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_detail)
        gameId = intent.getIntExtra("game_id", 0)
        initGameDetailViewModel()
        gameDetailViewModel.getGameDetail(gameId, Locale.getDefault().language)
        getUser()
        initOnClickLike()
        initOnClickWish()
        initOnClickOpinion()
        initOnClickDescription()
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener { finish() }
    }

    private fun getUser() {
        val sharedPref: SharedPreferences = getSharedPreferences("token_pref", Context.MODE_PRIVATE)
        if (!sharedPref.contains("token")) {
            launchLogin()
        }
        token = sharedPref.getString("token", "")!!
        initMeViewModel()
        meViewModel.me(token)
    }

    private fun initOnClickOpinion() {
        findViewById<Button>(R.id.opinion_button).setOnClickListener {
            if (description_displayed) {
                findViewById<Button>(R.id.description_button).setBackgroundResource(R.drawable.border_button)
                findViewById<Button>(R.id.opinion_button).setBackgroundResource(R.drawable.button_full)
                findViewById<RecyclerView>(R.id.opinion_list).visibility = View.VISIBLE
                findViewById<TextView>(R.id.description).visibility = View.GONE
                description_displayed = false
            }
        }
    }

    private fun initOnClickDescription() {
        findViewById<Button>(R.id.description_button).setOnClickListener {
            if (!description_displayed) {
                findViewById<Button>(R.id.description_button).setBackgroundResource(R.drawable.button_full)
                findViewById<Button>(R.id.opinion_button).setBackgroundResource(R.drawable.border_button)
                findViewById<TextView>(R.id.description).visibility = View.VISIBLE
                findViewById<RecyclerView>(R.id.opinion_list).visibility = View.GONE
                description_displayed = true
            }
        }
    }

    private fun initOnClickLike() {
        findViewById<ImageView>(R.id.like).setOnClickListener {
            if (gameId in account.likes) {
                initRemoveLikeViewModel()
                removeLikeViewModel.removeLike(token, gameId)
            } else {
                initAddLikeViewModel()
                addLikeViewModel.addLike(token, gameId)
            }
        }
    }

    private fun initOnClickWish() {
        findViewById<ImageView>(R.id.wishlist).setOnClickListener {
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
        meViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it == null) {
                launchLogin()
            } else {
                account = it
                checkLikeAndWishList()
            }
        })
    }

    private fun initAddLikeViewModel() {
        addLikeViewModel = ViewModelProvider(this).get(AddLikeViewModel::class.java)
        addLikeViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it != null) {
                account = it
                checkLikeAndWishList()
            }
        })
    }

    private fun initRemoveLikeViewModel() {
        removeLikeViewModel = ViewModelProvider(this).get(RemoveLikeViewModel::class.java)
        removeLikeViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it != null) {
                account = it
                checkLikeAndWishList()
            }
        })
    }

    private fun initAddWishViewModel() {
        addWishViewModel = ViewModelProvider(this).get(AddWishViewModel::class.java)
        addWishViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it != null) {
                account = it
                checkLikeAndWishList()
            }
        })
    }

    private fun initRemoveWishViewModel() {
        removeWishViewModel = ViewModelProvider(this).get(RemoveWishViewModel::class.java)
        removeWishViewModel.getAccountObserver().observe(this, Observer<Account?> {
            if (it != null) {
                account = it
                checkLikeAndWishList()
            }
        })
    }

    private fun checkLikeAndWishList() {
        if (gameId in account.likes) {
            findViewById<ImageView>(R.id.like).setImageResource(R.drawable.like_full)
        } else {
            findViewById<ImageView>(R.id.like).setImageResource(R.drawable.like)
        }
        if (gameId in account.wishlist) {
            findViewById<ImageView>(R.id.wishlist).setImageResource(R.drawable.wishlist_full)
        } else {
            findViewById<ImageView>(R.id.wishlist).setImageResource(R.drawable.wishlist)
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

                    Glide.with(this)
                        .load(it["$gameId"]?.data?.header_image)
                        .into(findViewById<ImageView>(R.id.header_image))

                    val view = findViewById<View>(R.id.background_image_item)
                    Glide.with(this)
                        .load(it["$gameId"]?.data?.background)
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                                view.background = resource
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })

                    findViewById<TextView>(R.id.game_name).text = it["$gameId"]?.data?.name
                    findViewById<TextView>(R.id.game_publisher).text = it["$gameId"]?.data?.publishers!![0]
                    findViewById<TextView>(R.id.description).text = Html.fromHtml(it["$gameId"]?.data?.detailed_description)
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