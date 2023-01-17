package com.grp2.a4al2androidgrp2.dto

import java.util.*

data class Account (
    val email: String,
    val password: String,
    val sessions: List<String>,
    val likes: List<String>,
    val wishlist: List<String>,
    val _id: String,
    val createdAt: Date,
    val updatedAt: Date
)
