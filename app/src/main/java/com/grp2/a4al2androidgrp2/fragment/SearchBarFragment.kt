package com.grp2.a4al2androidgrp2.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.adapter.GameInfoAdapter
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.grp2.a4al2androidgrp2.viewmodel.steam.SearchGameViewModel
import java.util.*

class SearchBarFragment: Fragment() {
    lateinit var searchGameViewModel: SearchGameViewModel
    var gamesDetail: MutableList<GameInfo> = arrayListOf()
    var language = Locale.getDefault().language

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.search_activity, container, false)
        initSearchGameViewModel(view)
        initOnChange(view)
        initOnClick(view)
        return view
    }

    private fun initOnClick(view: View) {
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            launchHomepage()
        }
    }

    private fun initSearchGameViewModel(view: View) {
        searchGameViewModel = ViewModelProvider(this).get(SearchGameViewModel::class.java)
        searchGameViewModel.searchGameObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                gamesDetail = arrayListOf()
                it.forEach { game ->
                    if (game.value.success) {
                        gamesDetail.add(game.value.data)
                    }
                }
                val adapter = GameInfoAdapter(gamesDetail, R.id.action_searchBarFragment_to_gameDetailFragment, R.id.action_gameDetailFragment_to_homePageFragment)
                val recyclerView = view.findViewById<RecyclerView>(R.id.games_list)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(view.context)
                view.findViewById<TextView>(R.id.nb_results).text = Html.fromHtml("<u>" + gamesDetail.size.toString() + "</u>")
            }
        }
    }

    private fun initOnChange(view: View){
        view.findViewById<EditText>(R.id.search_bar).addTextChangedListener(object : TextWatcher {

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
                    val adapter = GameInfoAdapter(gamesDetail, R.id.action_searchBarFragment_to_gameDetailFragment, R.id.action_gameDetailFragment_to_homePageFragment)
                    val recyclerView = view.findViewById<RecyclerView>(R.id.games_list)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(view.context)
                    view.findViewById<TextView>(R.id.nb_results).text = Html.fromHtml("<u>0</u>")
                }
            }
        })
    }

    fun launchHomepage() {
        findNavController().navigate(
            SearchBarFragmentDirections.actionSearchBarFragmentToHomePageFragment()
        )
    }
}