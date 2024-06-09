package ktor.models

import kotlinx.serialization.Serializable

@Serializable
data class AddTipRequest(
    val title:String,
    val description:String,
    val imageSrc:String?,
    val tags: TipTags?,
    val color:Long?
)