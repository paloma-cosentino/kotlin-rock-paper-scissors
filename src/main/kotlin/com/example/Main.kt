package com.example

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.FormField
import org.http4k.lens.Validator
import org.http4k.lens.webForm
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.HandlebarsTemplates

val moves = listOf(
    "rock",
    "paper",
    "scissors"
)

val playerMove = FormField.required("move")


val movesListLens = Body.webForm(
    Validator.Strict,
    playerMove
).toLens()

val renderer = HandlebarsTemplates().HotReload("src/main/resources")

val app: HttpHandler = routes(
    "/" bind GET to { request: Request ->
        val viewModel = MovesListViewModel(moves)
        val finalHTML = renderer(viewModel)
        Response(OK).body(finalHTML)
    },
    "/play" bind POST to { request: Request ->
        val form = movesListLens(request)
        val userMove = playerMove(form)
        val computerMove= randomMoves()
        val gameResult = playRockPaperScissors(userMove, computerMove)
      Response(OK).body(gameResult)
    }
)

fun playRockPaperScissors(playerMove: String, randomMoves: String): String {
    return if (playerMove == randomMoves) {
        "Draw!"
    } else if (
        (playerMove == "rock" && randomMoves == "scissors") ||
        (playerMove == "scissors" && randomMoves == "paper") ||
        (playerMove == "paper" && randomMoves == "rock")
    ) {
        "User wins!"
    } else {
        "Computer wins!"
    }
}

fun randomMoves(): String {
    return moves.random()
}

fun main() {
    val server = app.asServer(Undertow(9000)).start()

    println("Server started on " + server.port())
}
