package com.grp2.a4al2androidgrp2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.dto.game.GameInfo
import com.grp2.a4al2androidgrp2.viewholder.GameInfoViewHolder

class GameInfoAdapter(private val games: List<GameInfo>) : RecyclerView.Adapter<GameInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_info_item, parent, false)
        return GameInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameInfoViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount(): Int {
        return games.size
    }
}