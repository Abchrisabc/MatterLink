package matterlink.handlers

import matterlink.antiping
import matterlink.bridge.ApiMessage
import matterlink.bridge.MessageHandler
import matterlink.config.cfg

object ProgressHandler {

    fun handleProgress(name: String, text: String) {
        if (!cfg!!.relay.advancements) return
        val usr = name.antiping()
        MessageHandler.transmit(ApiMessage(
                username = cfg!!.relay.systemUser,
                text = "$usr $text"
        ))
    }
}