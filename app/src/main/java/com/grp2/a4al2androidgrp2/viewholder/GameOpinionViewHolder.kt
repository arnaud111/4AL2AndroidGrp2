package com.grp2.a4al2androidgrp2.viewholder

import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.dto.game.GameOpinion

class GameOpinionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(gameOpinion: GameOpinion) {
        itemView.findViewById<TextView>(R.id.user_name).text = Html.fromHtml("<u>${gameOpinion.author.pseudo}</u>")
        itemView.findViewById<TextView>(R.id.content).text = Html.fromHtml(gameOpinion.review)
    }
}