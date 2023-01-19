package com.grp2.a4al2androidgrp2.dto.game

data class GameInfo (
    val steam_appId: Int,
    val name: String,
    val header_image: String,
    val publishers: List<String>,
    val price_overview: GamePrice,
    val background: String,
    val background_raw: String,
    val is_free: Boolean,
)