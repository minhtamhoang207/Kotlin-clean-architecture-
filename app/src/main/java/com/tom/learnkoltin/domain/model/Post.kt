package com.tom.learnkoltin.domain.model

data class Post (
    val userID: Long,
    val id: Int,
    val title: String,
    val body: String
)