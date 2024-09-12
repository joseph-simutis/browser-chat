package io.github.josephsimutis

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.io.File
import java.time.Duration
import java.util.*

fun main() {
    embeddedServer(
        Netty,
        port=1234,
        host="0.0.0.0",
        module=Application::module
    ).start(wait=true)
}

fun Application.module() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        val clients = Collections.synchronizedList<WebSocketServerSession>(ArrayList())

        get {
            call.respondText(Thread.currentThread().contextClassLoader.getResource("index.html")!!.readBytes().decodeToString(), ContentType.parse("text/html"))
        }

        webSocket("/socket") {
            clients += this
            for (frame in incoming) {
                val message = (frame as? Frame.Text ?: continue).readText()
                clients.forEach {
                    it.send(message)
                }
            }
        }
    }
}
