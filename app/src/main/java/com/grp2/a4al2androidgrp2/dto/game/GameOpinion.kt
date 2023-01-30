package com.grp2.a4al2androidgrp2.dto.game

data class GameOpinion(
    val author: SteamAccount,
    val review: String,
    val voted_up: Boolean,
)
