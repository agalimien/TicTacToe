package com.harvey.tictactoe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TicTacToe(
    onPlayerWin: (Player) -> Unit,
    onNewRound: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var isGameRunning by remember { mutableStateOf(true) }
    var currentPlayer by remember { mutableStateOf<Player>(Player.X) }
    var gameState by remember { mutableStateOf(emptyGameState()) }
    var animations = remember { emptyAnimations() }

    Canvas(
        modifier = Modifier
            .size(300.dp)
            .padding(10.dp)
            .pointerInput(true) {
                detectTapGestures {
                    if (!isGameRunning) {
                        return@detectTapGestures
                    }
                    when {
                        //Row 1
                        (it.x < size.width / 3f) && (it.y < size.height / 3f) -> {
                            //Top Left
                            if (gameState[0][0] == 'E') {
                                gameState = updateGameState(gameState, 0, 0, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[0][0])
                                currentPlayer = !currentPlayer
                            }
                        }
                        (it.x in (size.width / 3f)..(2 * size.width / 3f)) && (it.y < size.height / 3f) -> {
                            //Top Middle
                            if (gameState[0][1] == 'E') {
                                gameState = updateGameState(gameState, 0, 1, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[0][1])
                                currentPlayer = !currentPlayer
                            }
                        }
                        (it.x > 2 * size.width / 3f) && (it.y < size.height / 3f) -> {
                            //Top Right
                            if (gameState[0][2] == 'E') {
                                gameState = updateGameState(gameState, 0, 2, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[0][2])
                                currentPlayer = !currentPlayer
                            }
                        }

                        //Row2
                        (it.x < size.width / 3f) && (it.y in (size.height / 3f)..(2 * size.height / 3f)) -> {
                            //Middle Left
                            if (gameState[1][0] == 'E') {
                                gameState = updateGameState(gameState, 1, 0, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[1][0])
                                currentPlayer = !currentPlayer
                            }
                        }
                        (it.x in (size.width / 3f)..(2 * size.width / 3f)) && (it.y in (size.height / 3f)..(2 * size.height / 3f)) -> {
                            //Middle Middle
                            if (gameState[1][1] == 'E') {
                                gameState = updateGameState(gameState, 1, 1, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[1][1])
                                currentPlayer = !currentPlayer
                            }
                        }
                        (it.x > 2 * size.width / 3f) && (it.y in (size.height / 3f)..(2 * size.height / 3f)) -> {
                            //Middle Right
                            if (gameState[1][2] == 'E') {
                                gameState = updateGameState(gameState, 1, 2, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[1][2])
                                currentPlayer = !currentPlayer
                            }
                        }

                        //Row3
                        (it.x < size.width / 3f) && (it.y > (2 * size.height / 3f)) -> {
                            //Bottom Left
                            if (gameState[2][0] == 'E') {
                                gameState = updateGameState(gameState, 2, 0, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[2][0])
                                currentPlayer = !currentPlayer
                            }
                        }
                        (it.x in (size.width / 3f)..(2 * size.width / 3f)) && (it.y > (2 * size.height / 3f)) -> {
                            //Bottom Middle
                            if (gameState[2][1] == 'E') {
                                gameState = updateGameState(gameState, 2, 1, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[2][1])
                                currentPlayer = !currentPlayer
                            }
                        }
                        (it.x > 2 * size.width / 3f) && (it.y > (2 * size.height / 3f)) -> {
                            //Bottom Right
                            if (gameState[2][2] == 'E') {
                                gameState = updateGameState(gameState, 2, 2, currentPlayer.symbol)
                                scope.animateFloatToOne(animations[2][2])
                                currentPlayer = !currentPlayer
                            }
                        }
                    }
                    val isFieldFull = gameState.all { row ->
                        row.all { element -> (element != 'E') }
                    }
                    val didXWin = didPlayerWin(gameState, Player.X)
                    val didOWin = didPlayerWin(gameState, Player.O)
                    when {
                        didXWin -> onPlayerWin(Player.X)
                        didOWin -> onPlayerWin(Player.O)
                    }
                    if (isFieldFull || didXWin || didOWin) {
                        scope.launch {
                            isGameRunning = false
                            delay(5000L)
                            isGameRunning = true
                            gameState = emptyGameState()
                            animations = emptyAnimations()
                            onNewRound()
                        }
                    }
                }
            }
    ) {
        //vertical line 1
        drawLine(
            color = Color.Black,
            start = Offset(size.width / 3f, 0f),
            end = Offset(size.width / 3f, size.height),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )

        //vertical line 2
        drawLine(
            color = Color.Black,
            start = Offset(2 * size.width / 3f, 0f),
            end = Offset(2 * size.width / 3f, size.height),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )

        //horizontal line 1
        drawLine(
            color = Color.Black,
            start = Offset(0f, size.height / 3f),
            end = Offset(size.width, size.height / 3f),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )

        //horizontal line 2
        drawLine(
            color = Color.Black,
            start = Offset(0f, 2 * size.height / 3f),
            end = Offset(size.width,  2 * size.height / 3f),
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round
        )

        gameState.forEachIndexed { i, row ->
            row.forEachIndexed { j, symbol ->
                when(symbol) {
                    Player.X.symbol -> {
                        // 'X' top-left to bottom-right
                        val path1 = Path().apply {
                            moveTo(
                                j * size.width / 3f + size.width / 6f - 50f,
                                i * size.height / 3f + size.height / 6f - 50f
                            )
                            lineTo(
                                j * size.width / 3f + size.width / 6f + 50f,
                                i * size.height / 3f + size.height / 6f + 50f
                            )
                        }

                        // 'X' top-right to bottom-left
                        val path2 = Path().apply {
                            moveTo(
                                j * size.width / 3f + size.width / 6f + 50f,
                                i * size.height / 3f + size.height / 6f - 50f
                            )
                            lineTo(
                                j * size.width / 3f + size.width / 6f - 50f,
                                i * size.height / 3f + size.height / 6f + 50f
                            )
                        }

                        val outPath1 = Path()
                        PathMeasure().apply {
                            setPath(path1, false)
                            getSegment(0f, animations[i][j].value * length, outPath1)
                        }

                        val outPath2 = Path()
                        PathMeasure().apply {
                            setPath(path2, false)
                            getSegment(0f, animations[i][j].value * length, outPath2)
                        }

                        drawPath(
                            path = outPath1,
                            color = Color.Red,
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )

                        drawPath(
                            path = outPath2,
                            color = Color.Red,
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }
                    Player.O.symbol -> {
                        drawArc(
                            color = Color.Green,
                            startAngle = 0f,
                            sweepAngle = animations[i][j].value * 360f,
                            useCenter = false,
                            topLeft = Offset(
                                j * size.width / 3f + size.width / 6f - 50f,
                                i * size.height / 3f + size.height / 6f - 50f
                            ),
                            size = Size(100f, 100f),
                            style = Stroke(
                                width = 5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun emptyAnimations(): ArrayList<ArrayList<Animatable<Float, AnimationVector1D>>> {
    val arrayList = arrayListOf<ArrayList<Animatable<Float, AnimationVector1D>>>()
    for (i in 0..2) {
        arrayList.add(arrayListOf())
        for (j in 0..2) {
            arrayList[i].add(Animatable(0f))
        }
    }
    return arrayList
}

private fun emptyGameState(): Array<CharArray> {
    return arrayOf(
        charArrayOf('E', 'E', 'E'),
        charArrayOf('E', 'E', 'E'),
        charArrayOf('E', 'E', 'E'),
    )
}

private fun updateGameState(gameState: Array<CharArray>, i: Int, j: Int, symbol: Char): Array<CharArray> {
    val arrayCopy = gameState.copyOf()
    arrayCopy[i][j] = symbol
    return arrayCopy
}

private fun CoroutineScope.animateFloatToOne(animatable: Animatable<Float, AnimationVector1D>) {
    launch {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500
            )
        )
    }
}

private fun didPlayerWin(gameState: Array<CharArray>, player: Player): Boolean {
    //Row Spec
    val firstRowFull = (gameState[0][0] == gameState[1][0]) &&
            (gameState[1][0] == gameState[2][0]) &&
            (gameState[0][0] == player.symbol)

    val secondRowFull = (gameState[0][1] == gameState[1][1]) &&
            (gameState[1][1] == gameState[2][1]) &&
            (gameState[0][1] == player.symbol)

    val thirdRowFull = (gameState[0][2] == gameState[1][2]) &&
            (gameState[1][2] == gameState[2][2]) &&
            (gameState[0][2] == player.symbol)

    //Column Spec
    val firstColFull = (gameState[0][0] == gameState[0][1]) &&
            (gameState[0][1] == gameState[0][2]) &&
            (gameState[0][0] == player.symbol)

    val secondColFull = (gameState[1][0] == gameState[1][1]) &&
            (gameState[1][1] == gameState[1][2]) &&
            (gameState[1][0] == player.symbol)

    val thirdColFull = (gameState[2][0] == gameState[2][1]) &&
            (gameState[2][1] == gameState[2][2]) &&
            (gameState[2][0] == player.symbol)

    //Diagonal Spec
    val firstDiagonalFull = (gameState[0][0] == gameState[1][1]) &&
            (gameState[1][1] == gameState[2][2]) &&
            (gameState[0][0] == player.symbol)

    val secondDiagonalFull = (gameState[2][0] == gameState[1][1]) &&
            (gameState[1][1] == gameState[0][2]) &&
            (gameState[2][0] == player.symbol)

    return firstRowFull || secondRowFull || thirdRowFull
            || firstColFull || secondColFull || thirdColFull
            || firstDiagonalFull || secondDiagonalFull
}
