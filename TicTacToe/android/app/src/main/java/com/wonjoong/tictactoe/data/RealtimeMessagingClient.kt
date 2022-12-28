package com.wonjoong.tictactoe.data

import kotlinx.coroutines.flow.Flow

interface RealtimeMessagingClient {
    fun getGameStateStream(): Flow<GameState>
    suspend fun sendAction(action: MakeTurn)
    suspend fun close() // close connection
}