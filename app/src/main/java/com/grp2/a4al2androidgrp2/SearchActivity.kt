package com.grp2.a4al2androidgrp2
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.adapter.GameInfoAdapter
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.grp2.a4al2androidgrp2.dto.game.GameResponse
import com.grp2.a4al2androidgrp2.viewmodel.steam.SearchGameViewModel
import java.util.*

class SearchActivity : AppCompatActivity() {
    lateinit var searchGameViewModel: SearchGameViewModel
    var gamesDetail: MutableList<GameInfo> = arrayListOf()
    var context = this
    var language = Locale.getDefault().language

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_activity)
        initSearchGameViewModel()
        initOnChange()
    }

    private fun initSearchGameViewModel() {
        searchGameViewModel = ViewModelProvider(this).get(SearchGameViewModel::class.java)
        searchGameViewModel.searchGameObserver().observe(this, Observer<Map<String, GameResponse>?> {
            if (it != null) {
                gamesDetail = arrayListOf()
                it.forEach { game ->
                    if (game.value.success) {
                        gamesDetail.add(game.value.data)
                    }
                }
                val adapter = GameInfoAdapter(gamesDetail)
                val recyclerView = findViewById<RecyclerView>(R.id.games_list)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(context)
                findViewById<TextView>(R.id.nb_results).text = Html.fromHtml("<u>" + gamesDetail.size.toString() + "</u>")
            }
        })
    }
    private fun initOnChange(){
        findViewById<EditText>(R.id.search_bar).addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s.isNotEmpty()){
                    searchGameViewModel.searchGame(s.toString(), language)
                }else{
                    gamesDetail = arrayListOf()
                    val adapter = GameInfoAdapter(gamesDetail)
                    val recyclerView = findViewById<RecyclerView>(R.id.games_list)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    findViewById<TextView>(R.id.nb_results).text = Html.fromHtml("<u>0</u>")

                }

            }
        })
    }

    fun launchHomepage(view: View) {
        val intent = Intent(this, HomepageActivity::class.java)
        startActivity(intent)
    }

}