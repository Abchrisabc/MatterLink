package matterlink.bridge

//import matterlink.logger
import matterlink.instance
import matterlink.bridge.command.BridgeCommandRegistry
import matterlink.config.cfg

object ServerChatHandler {

    /**
     * This method must be called every server tick with no arguments.
     */
    fun writeIncomingToChat(tick: Int) {
        instance.reconnect(tick)
        if (MessageHandler.rcvQueue.isNotEmpty())
            println("incoming: " + MessageHandler.rcvQueue.toString())
        val nextMessage = MessageHandler.rcvQueue.poll()

        if (nextMessage != null && nextMessage.gateway == cfg!!.connect.gateway) {
            if (!nextMessage.text.isBlank()) {
                val section = '\u00A7'
                val message = when (nextMessage.event) {
                    "user_action" -> nextMessage.format(cfg!!.formatting.action)
                    "" -> {
                        if (BridgeCommandRegistry.handleCommand(nextMessage.text)) return
                        nextMessage.format(cfg!!.formatting.chat)
                    }
                    "join_leave" -> nextMessage.format(cfg!!.formatting.joinLeave)
                    else -> {
                        val user = nextMessage.username
                        val text = nextMessage.text
                        val json = nextMessage.encode()
                        println("Threw out message with unhandled event: ${nextMessage.event}")
                        println(" Message contents:")
                        println(" User: $user")
                        println(" Text: $text")
                        println(" JSON: $json")
                        return
                    }
                }
                instance.wrappedSendToPlayers(message)
            }
        }
    }
}
