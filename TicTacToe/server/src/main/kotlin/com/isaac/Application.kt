package com.isaac

import com.isaac.models.TicTacToeGame
import io.ktor.server.application.*
import com.isaac.plugins.*

// 런치되는 부분
fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val game = TicTacToeGame()
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureRouting(game)
}
