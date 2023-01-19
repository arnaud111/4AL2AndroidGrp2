package com.grp2.a4al2androidgrp2.dto

data class Game(
    val success: Boolean,
    val data: GameInfo
)

data class GameInfo (
    val steam_appId: Int,
    val name: String,
    val header_image: String,
    val publishers: List<String>,
)