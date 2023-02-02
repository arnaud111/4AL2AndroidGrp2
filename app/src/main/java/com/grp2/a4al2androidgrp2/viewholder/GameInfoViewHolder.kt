package com.grp2.a4al2androidgrp2.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.grp2.a4al2androidgrp2.R
import com.grp2.a4al2androidgrp2.dto.game.GameInfo

class GameInfoViewHolder(itemView: View, val action_destination: Int, val return_destination: Int) : RecyclerView.ViewHolder(itemView) {

    fun bind(game: GameInfo) {
        itemView.findViewById<TextView>(R.id.game_name).text = game.name
        itemView.findViewById<TextView>(R.id.game_publisher).text = game.publishers[0]
        if (!game.is_free && game.price_overview != null) {
            itemView.findViewById<TextView>(R.id.game_price).text = game.price_overview.final_formatted
        }else{
            itemView.findViewById<TextView>(R.id.game_price).setText(R.string.free)
        }
        Glide.with(itemView)
            .load(game.header_image) // "https://steamcdn-a.akamaihd.net/steam/apps/${game.steam_appid}/library_600x900.jpg"
            .into(itemView.findViewById<ImageView>(R.id.header_image))

        val button = itemView.findViewById<Button>(R.id.game_detail)
        button.setOnClickListener {
            val action = bundleOf(
                "gameId" to game.steam_appid,
                "return_destination" to return_destination
            )
            Navigation.findNavController(itemView).navigate(action_destination, action)
        }

        val view = itemView.findViewById<View>(R.id.background_image)
        Glide.with(itemView)
            .load(game.background)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    view.background = resource
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}
