package com.grp2.a4al2androidgrp2.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.dto.game.GameInfo

class GameInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(game: GameInfo) {
        itemView.findViewById<TextView>(R.id.game_name).text = game.name
        itemView.findViewById<TextView>(R.id.game_publisher).text = game.publishers[0]
        if (game.is_free) {
            itemView.findViewById<TextView>(R.id.game_price).text = ""
        } else {
            itemView.findViewById<TextView>(R.id.game_price).text = game.price_overview.final_formatted
        }
    }
}