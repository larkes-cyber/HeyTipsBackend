package ktor.models

import kotlinx.serialization.Serializable

@Serializable
data class TipResponse(
    val title:String,
    val description:String,
    val imageSrc:String?,
    val tags: TipTags?,
    val color:Long?,
    val serverId:String?
)

@Serializable
data class TipTags(
    val tags:List<String>
)