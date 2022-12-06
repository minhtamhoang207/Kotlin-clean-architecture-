package com.tom.learnkoltin.domain.model

data class Post (
    val userID: Long,
    val id: Long,
    val title: String,
    val body: String
)