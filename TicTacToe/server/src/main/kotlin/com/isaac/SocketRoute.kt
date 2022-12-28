package com.isaac

import com.isaac.models.MakeTurn
import com.isaac.models.TicTacToeGame
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun Route.socket(game: TicTacToeGame) {
    route("/play") {
        webSocket {
            // 여기서 this로 접근할 수 있는
            // DefaultWebSocketServerSession이 클라이언트와 소통하는 데 필요한 것이다.

            // this.incoming 이렇게 하면 클라이언트로 값을 받을 수 있고
            // this.outgoing 이렇게 하면 클라이언트로 값을 내보낼 수 있다.

            val player = game.connectPlayer(this)
            if (player == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "2 players already connected."))
                return@webSocket
            }

            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val action = extractAction(frame.readText())
                        game.finishTurn(player, action.x, action.y)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                game.disconnectPlayer(player)
            }
        }
    }
}

private fun extractAction(message: String): MakeTurn {
    val type = message.substringBefore("#")
    val body = message.substringAfter("#")
    return if (type == "make_turn") {
        Json.decodeFromString(body)
    } else {
        MakeTurn(-1, -1)
    }
}