package com.wonjoong.tictactoe.data

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtoRealtimeMessagingClient(
    private val client: HttpClient
) : RealtimeMessagingClient {

    private var session: WebSocketSession? = null

    override fun getGameStateStream(): Flow<GameState> {
        return flow {
            session = client.webSocketSession {
                url("ws://172.16.22.53/play")
            }

            // 새로운 값이 수신될 때마다 game state에 입력된다
            val gameState = session!!
                .incoming
                .consumeAsFlow()// Flow<Frame> 형식이다.
                .filterIsInstance<Frame.Text>() // Text 형식만 받겠다는 것
                .mapNotNull { Json.decodeFromString<GameState>(it.readText()) }

            emitAll(gameState)
        }
    }

    override suspend fun sendAction(action: MakeTurn) {
        session?.outgoing?.send(
            Frame.Text("make_turn#${Json.encodeToString(action)}")
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }
}