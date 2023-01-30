package com.grp2.a4al2androidgrp2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.dto.game.GameOpinion
import com.grp2.a4al2androidgrp2.viewholder.GameOpinionViewHolder

class GameOpinionAdapter(private val gamesOpinion: List<GameOpinion>) : RecyclerView.Adapter<GameOpinionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameOpinionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_opinion_item, parent, false)
        return GameOpinionViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameOpinionViewHolder, position: Int) {
        holder.bind(gamesOpinion[position])
    }

    override fun getItemCount(): Int {
        return gamesOpinion.size
    }
}