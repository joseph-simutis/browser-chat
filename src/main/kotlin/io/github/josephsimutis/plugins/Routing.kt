package io.github.josephsimutis.plugins

import io.github.josephsimutis.ChatServer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("""
                <form method="post">
                    <input type="text" id="message" name="message">
                    <input type="submit" value="Send">
                </form>
            """.trimIndent() + ChatServer.listMessages().joinToString("<br>"), ContentType.parse("text/html"))
        }
        post {
            val content = call.receiveParameters()
            content["message"]?.let { message -> ChatServer.sendMessage(message) }
            call.respondRedirect("/")
        }
    }
}
