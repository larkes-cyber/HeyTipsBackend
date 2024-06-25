package com.example.features.tips

import com.example.database.tips.Tips
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureTipsRouting(){

    routing {

        post("/tips/add") {
            TipsController(call).addTip()
        }

        post("/tips/delete") {
            TipsController(call).deleteTip()
        }

        get("/tips/fetch") {
            TipsController(call).fetchTips()
        }

        get("/tips/fetchOne"){
            TipsController(call).fetchOne()
        }

        get("/tips/image/get"){
            TipsController(call).getImage()
        }

        post("/tips/image/upload") {
            TipsController(call).uploadTipImage()
        }
        post("/tips/edit"){
            TipsController(call).editTip()
        }

        get("/tips/deleteAll") {
            TipsController(call).deleteAll()
        }

        get("/tips/all"){
            TipsController(call).getAllTips()
        }


    }

}