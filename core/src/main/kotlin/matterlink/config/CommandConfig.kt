package matterlink.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import matterlink.bridge.command.CommandType
import matterlink.bridge.command.CustomCommand
import matterlink.instance
import java.io.File

object CommandConfig {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val configFile: File = cfg.cfgDirectory.resolve("commands.json")
    private val default = arrayOf(
            CustomCommand(
                    alias = "tps",
                    type = CommandType.EXECUTE,
                    execute = "forge tps",
                    help = "Print server tps",
                    allowArgs = false
            ),
            CustomCommand(
                    alias = "list",
                    type = CommandType.EXECUTE,
                    execute = "list",
                    help = "List online players",
                    allowArgs = false
            ),
            CustomCommand(
                    alias = "seed",
                    type = CommandType.EXECUTE,
                    execute = "seed",
                    help = "Print server world seed",
                    allowArgs = false
            ),
            CustomCommand(
                    alias = "uptime",
                    type = CommandType.RESPONSE,
                    permLevel = 1,
                    response = "{uptime}",
                    help = "Print server uptime",
                    allowArgs = false
            )
    )
    var commands: Array<CustomCommand> = default
        private set

    fun readConfig(): Boolean {
        if (!configFile.exists()) {
            configFile.createNewFile()
            configFile.writeText(gson.toJson(default))
            return true
        }

        val text = configFile.readText()
        try {
            commands = gson.fromJson(text, Array<CustomCommand>::class.java)
        } catch (e: JsonSyntaxException) {
            instance.fatal("failed to parse $configFile using last good values as fallback")
            return false
        }
        return true
    }


}