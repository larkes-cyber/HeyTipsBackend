package com.example.database.tips

import com.example.database.tips.dto.TipDTO
import com.example.utils.Constants.SERVER_URL
import com.mongodb.client.model.Filters
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.encodeToString
import ktor.models.AddTipRequest
import ktor.models.EditTipRequest
import ktor.models.TipResponse
import ktor.models.TipTags
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.json
import org.litote.kmongo.reactivestreams.KMongo
import java.io.File
import java.util.UUID

object Tips {

    private val database = KMongo.createClient()
            .coroutine
            .getDatabase("tips_db")

    private val queries = database.getCollection<TipDTO>()

    suspend fun insertTip(tipDTO: TipDTO){
        queries.insertOne(tipDTO)
    }

    suspend fun fetchTips():List<TipDTO>{
        return queries.find().toList()
    }

    suspend fun deleteTip(id:String){
        val filter = Filters.eq("id", id)
        queries.deleteOne(filter)
    }

    suspend fun editTip(tipDTO: TipDTO){
        val filter = Filters.eq("id", tipDTO.id)
        queries.deleteOne(filter)
        queries.insertOne(tipDTO)
    }


}

fun TipDTO.toTipResponse():TipResponse{
    return TipResponse(
        serverId = id,
        color = color,
        description = description,
        imageSrc = imageSrc,
        tags = if(tags != null) Json.decodeFromString<TipTags>(tags) else null,
        title = title
    )
}

fun AddTipRequest.toTipDTO(id:String):TipDTO{
    return TipDTO(
        id = id,
        color = color,
        description = description,
        imageSrc = imageSrc,
        tags = if(tags != null) Json.encodeToString(tags) else null,
        title = title
    )
}

fun EditTipRequest.toTipDto():TipDTO{
    return TipDTO(
            id = serverId,
            color = color,
            description = description,
            imageSrc = imageSrc,
            tags = if(tags != null) Json.encodeToString(tags) else null,
            title = title
    )
}