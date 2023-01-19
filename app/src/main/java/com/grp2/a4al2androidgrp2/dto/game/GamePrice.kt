package com.grp2.a4al2androidgrp2.dto.game

data class GamePrice (
    val currency: String,
    val initial: Int,
    val final: Int,
    val discount_percent: Int,
    val initial_formatted: String,
    val final_formatted: String
)