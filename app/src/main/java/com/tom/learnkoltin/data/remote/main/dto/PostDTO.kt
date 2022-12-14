package com.tom.learnkoltin.data.remote.main.dto
import com.google.gson.annotations.SerializedName
import com.tom.learnkoltin.domain.model.Post

data class PostDTO(
    @SerializedName("userId")
    val userID: Long,
    val id: Int,
    val title: String,
    val body: String
)

fun PostDTO.toPost(): Post {
    return Post(
        id = id,
        userID = userID,
        title = title,
        body = body
    )
}
