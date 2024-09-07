package io.github.josephsimutis

object ChatServer {
    private val messages = ArrayList<String>()

    fun sendMessage(message: String) {
        messages += message
    }

    fun listMessages(): Array<String> = messages.reversed().toTypedArray()
}