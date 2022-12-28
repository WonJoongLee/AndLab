package com.isaac.models

import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

// 처음 방에 들어온 사람이 x, 두번째로 들어온 사람이 o
class TicTacToeGame {

    private val state = MutableStateFlow(GameState())

    // race condition을 없애주기 위해 ConcurrentHashMap을 사용한다.
    // Char은 O 플레이언지 X 플레이언지를 나타낸다.
    // 각각의 플레이어들은 각각의 WebSocketSession을 갖는다.
    private val playerSockets = ConcurrentHashMap<Char, WebSocketSession>()

    // 이 클래스(TicTacToe) 내에서 사용하기 위한 스코프
    private val gameScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var delayGameJob: Job? = null

    init {
        state.onEach(::broadcast).launchIn(gameScope)
    }

    fun connectPlayer(session: WebSocketSession): Char? {
        // player X가 존재하는지 여
        val isPlayerX = state.value.connectedPlayers.any { it == 'X' }
        val player = if (isPlayerX) 'O' else 'X'
        state.update {
            // 이미 연결되었다면 update하지 않음.
            if (state.value.connectedPlayers.contains(player)) {
                return null
            }

            // 연결되어 있는 connection이 없다면
            if (!playerSockets.containsKey(player)) {
                playerSockets[player] = session
            }

            it.copy(
                connectedPlayers = it.connectedPlayers + player
            )
        }
        return player
    }

    fun disconnectPlayer(player: Char) {
        playerSockets.remove(player)
        state.update {
            it.copy(
                connectedPlayers = it.connectedPlayers - player
            )
        }
    }

    // send 함수를 사용하려면 suspend 붙여야 함.
    suspend fun broadcast(state: GameState) {
        playerSockets.values.forEach { socket ->
            // send의 파라미터로 frame이 들어가는데 frame은 text base여서
            // 형태로 들어간다. 바이트 형태가 될 수도 있다.
            socket.send(
                Json.encodeToString(state)
            )
        }
    }

    fun finishTurn(player: Char, x: Int, y: Int) {
        // 누군가 x, y 위치에 알을 두지 않았다면
        if (state.value.field[y][x] != null || state.value.winningPlayer != null) {
            return
        }
        // 해당 player가 자기 턴이라면
        if (state.value.playerAtTurn != player) {
            return
        }

        val currentPlayer = state.value.playerAtTurn
        state.update {
            val newField = it.field.also { field ->
                field[y][x] = currentPlayer
            }
            // 모든 필드가 다 찼는지 확인
            val isBoardFull = newField.all { it.all { it != null } }
            if (isBoardFull) {
                startNewRoundDelayed()
            }
            it.copy(
                playerAtTurn = if (currentPlayer == 'X') 'O' else 'X',
                field = newField,
                isBoardFull = isBoardFull,
                winningPlayer = getWinningPlayer()?.also {
                    startNewRoundDelayed()
                }
            )
        }
    }

    private fun getWinningPlayer(): Char? {
        val field = state.value.field
        return if (field[0][0] != null && field[0][0] == field[0][1] && field[0][1] == field[0][2]) {
            field[0][0]
        } else if (field[1][0] != null && field[1][0] == field[1][1] && field[1][1] == field[1][2]) {
            field[1][0]
        } else if (field[2][0] != null && field[2][0] == field[2][1] && field[2][1] == field[2][2]) {
            field[2][0]
        } else if (field[0][0] != null && field[0][0] == field[1][0] && field[1][0] == field[2][0]) {
            field[0][0]
        } else if (field[0][1] != null && field[0][1] == field[1][1] && field[1][1] == field[2][1]) {
            field[0][1]
        } else if (field[0][2] != null && field[0][2] == field[1][2] && field[1][2] == field[2][2]) {
            field[0][2]
        } else if (field[0][0] != null && field[0][0] == field[1][1] && field[1][1] == field[2][2]) {
            field[0][0]
        } else if (field[0][2] != null && field[0][2] == field[1][1] && field[1][1] == field[2][0]) {
            field[0][2]
        } else null
    }

    private fun startNewRoundDelayed() {
        delayGameJob?.cancel()
        delayGameJob = gameScope.launch {
            delay(5000L)
            state.update {
                it.copy(
                    playerAtTurn = 'X',
                    field = GameState.emptyField(),
                    winningPlayer = null,
                    isBoardFull = false,
                )
            }
        }
    }
}
