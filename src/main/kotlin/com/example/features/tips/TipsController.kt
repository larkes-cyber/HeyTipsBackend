package com.example.features.tips

import com.example.database.tips.Tips
import com.example.database.tips.toTipDTO
import com.example.database.tips.toTipDto
import com.example.database.tips.toTipResponse
import com.example.utils.Constants
import com.example.utils.Constants.SERVER_URL
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ktor.models.AddTipRequest
import ktor.models.AddTipResponse
import ktor.models.DeleteTipRequest
import ktor.models.EditTipRequest
import org.litote.kmongo.limit
import java.io.File
import java.util.*

class TipsController(private val call:ApplicationCall) {

    suspend fun fetchTips(){
        val receivedOffset = call.parameters["offset"]?.toInt() ?: 0
        val receivedLimit = call.parameters["limit"]?.toInt() ?: 0
        val tipsList = Tips.fetchTips()
        val offset = if(receivedOffset >= tipsList.size) 1 else receivedOffset
        val limit = if(receivedLimit + offset > tipsList.size) tipsList.size - offset - 1 else receivedLimit
        val list = Tips.fetchTips().subList(offset-1,offset + limit - 1).map {
            it.toTipResponse()
        }
        call.respond(list)
    }

    suspend fun deleteTip(){
        val id = call.receive<DeleteTipRequest>().id
        Tips.deleteTip(id)
        call.respond(HttpStatusCode.OK)
    }

    suspend fun addTip(){
        val tip = call.receive<AddTipRequest>()
        val id = UUID.randomUUID().toString()
        Tips.insertTip(tip.toTipDTO(id))
        call.respond(AddTipResponse(id))
    }

    suspend fun uploadTipImage(){
        val multipartData = call.receiveMultipart()

        val id = UUID.randomUUID().toString()
        val name = "$id.jpeg"

        multipartData.forEachPart {part ->
            if(part is PartData.FileItem){
                val fileBytes = part.streamProvider().readBytes()

                File("images/$name").writeBytes(fileBytes)
                part.dispose()
            }
        }
        call.respondText(text = "$SERVER_URL/tips/image/get?id=$id", status =  HttpStatusCode.OK)
    }

    suspend fun getImage(){
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest)
        call.respondFile(File("images/$id.jpeg"))
    }

    suspend fun fetchOne(){
        val id = call.parameters["id"] ?: return call.respond(HttpStatusCode.BadRequest)
        val tip = Tips.fetchTips().find { it.id == id }
        if(tip == null) call.respond(HttpStatusCode.BadRequest)
        call.respond(tip?.toTipResponse()!!)
    }

    suspend fun editTip(){
        val tip = call.receive<EditTipRequest>()
        Tips.editTip(tip.toTipDto())
        call.respond(HttpStatusCode.OK)
    }


}