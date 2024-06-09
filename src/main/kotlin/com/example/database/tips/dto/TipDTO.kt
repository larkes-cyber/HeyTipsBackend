package com.example.database.tips.dto

import kotlinx.serialization.Serializable

@Serializable
data class TipDTO(
    val id:String,
    val title:String,
    val description:String,
    val imageSrc:String?,
    val tags: String?,
    val color:Long?,
)