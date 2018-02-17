package matterlink

import matterlink.bridge.MessageHandler
import matterlink.bridge.command.BridgeCommandRegistry
import matterlink.command.IMinecraftCommandSender
import matterlink.config.cfg
import matterlink.update.UpdateChecker

lateinit var instance: IMatterLink

abstract class IMatterLink {
    abstract val mcVersion: String
    abstract val modVersion: String

    abstract var commandSender: IMinecraftCommandSender
    //var interrupted: Boolean = false

    abstract fun wrappedSendToPlayers(msg: String)

    abstract fun wrappedPlayerList(): Array<String>

    private var updateChecked: Boolean = false

    fun connect() {
        MessageHandler.start(clear = true)

        if (!updateChecked && cfg.update.enable) {
            Thread(UpdateChecker()).start()
            updateChecked = true
        }
    }

    fun disconnect() {
        MessageHandler.stop()
    }

    abstract fun log(level: String, formatString: String, vararg data: Any)

    fun fatal(formatString: String, vararg data: Any) = log("^FATAL", formatString, *data)
    fun error(formatString: String, vararg data: Any) = log("ERROR", formatString, *data)
    fun warn(formatString: String, vararg data: Any) = log("WARN", formatString, *data)
    fun info(formatString: String, vararg data: Any) = log("INFO", formatString, *data)
    fun debug(formatString: String, vararg data: Any) {
        if (cfg.relay.logLevel == "DEBUG" || cfg.relay.logLevel == "TRACE")
            log("INFO", "DEBUG: " + formatString.replace("\n", "\nDEBUG: "), *data)
    }

    fun trace(formatString: String, vararg data: Any) {
        if (cfg.relay.logLevel == "TRACE")
            log("INFO", "TRACE: " + formatString.replace("\n", "\nTRACE: "), *data)
    }

    /**
     * in milliseconds
     */
    var serverStartTime: Long = 0

    fun getUptimeInSeconds(): Int {
        return ((System.currentTimeMillis() - serverStartTime) / 1000).toInt()
    }

    fun getUptimeAsString(): String {
        val total = this.getUptimeInSeconds()
        val sec = total % 60
        val min = (total / 60) % 60
        val hr = (total / 3600) % 24
        val day = total / 86400

        return "${day}d ${hr}hr ${min}m ${sec}s"
    }

    fun registerBridgeCommands() {
        BridgeCommandRegistry.reloadCommands()
    }
}