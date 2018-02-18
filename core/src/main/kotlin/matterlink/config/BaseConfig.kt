package matterlink.config

import java.io.File
import java.util.regex.Pattern

lateinit var cfg: BaseConfig

abstract class BaseConfig(rootDir: File) {
    companion object {
        fun reload() {
            cfg = cfg.load()
        }
    }

    val cfgDirectory: File = rootDir.resolve("matterlink")
    val mainCfgFile: File = cfgDirectory.resolve("matterlink.cfg")


    var relay = RelayOptions()
    var connect = ConnectOptions()
    var formatting = FormattingOptions()
    var joinLeave = FormattingJoinLeave()
    var command = CommandOptions()
    var death = DeathOptions()
    var update = UpdateOptions()

    data class RelayOptions(
            var systemUser: String = "Server",
            var advancements: Boolean = true,
            var logLevel: String = "INFO"
    )

    data class FormattingOptions(
            var chat: String = "<{username}> {text}",
            var joinLeave: String = "§6-- {username} {text}",
            var action: String = "§5* {username} {text}"
    )

    data class FormattingJoinLeave(
            var showJoin: Boolean = true,
            var showLeave: Boolean = true,
            var joinServer: String = "{username:antiping} has connected to the server",
            var leaveServer: String = "{username:antiping} has disconnected from the server"
    )

    data class ConnectOptions(
            var url: String = "http://localhost:4242",
            var authToken: String = "",
            var gateway: String = "minecraft",
            var autoConnect: Boolean = true
    )

    data class CommandOptions(
            var prefix: String = "$",
            var enable: Boolean = true,
            var commandMapping: Map<String, String> = mapOf(
                    "tps" to "forge tps",
                    "list" to "list",
                    "seed" to "seed"
            )
    )

    data class UpdateOptions(
            var enable: Boolean = true
    )

    data class DeathOptions(
            var showDeath: Boolean = true,
            var showDamageType: Boolean = true,
            var damageTypeMapping: Map<String, String> = mapOf(
                    "inFire" to "\uD83D\uDD25", //🔥
                    "lightningBolt" to "\uD83C\uDF29", //🌩
                    "onFire" to "\uD83D\uDD25", //🔥
                    "lava" to "\uD83D\uDD25", //🔥
                    "hotFloor" to "♨️",
                    "inWall" to "",
                    "cramming" to "",
                    "drown" to "\uD83C\uDF0A", //🌊
                    "starve" to "\uD83D\uDC80", //💀
                    "cactus" to "\uD83C\uDF35", //🌵
                    "fall" to "\u2BEF️", //⯯️
                    "flyIntoWall" to "\uD83D\uDCA8", //💨
                    "outOfWorld" to "\u2734", //✴
                    "generic" to "\uD83D\uDC7B", //👻
                    "magic" to "✨ ⚚",
                    "indirectMagic" to "✨ ⚚",
                    "wither" to "\uD83D\uDD71", //🕱
                    "anvil" to "",
                    "fallingBlock" to "",
                    "dragonBreath" to "\uD83D\uDC32", //🐲
                    "fireworks" to "\uD83C\uDF86", //🎆

                    "mob" to "\uD83D\uDC80", //💀
                    "player" to "\uD83D\uDDE1", //🗡
                    "arrow" to "\uD83C\uDFF9", //🏹
                    "thrown" to "彡°",
                    "thorns" to "\uD83C\uDF39", //🌹
                    "explosion" to "\uD83D\uDCA3 \uD83D\uDCA5", //💣 💥
                    "explosion.player" to "\uD83D\uDCA3 \uD83D\uDCA5" //💣 💥
            )
    )

    protected fun load(
            getBoolean: (key: String, category: String, default: Boolean, comment: String) -> Boolean,
            getString: (key: String, category: String, default: String, comment: String) -> String,
            getStringValidated: (key: String, category: String, default: String, comment: String, pattern: Pattern) -> String,
            getStringValidValues: (key: String, category: String, default: String, comment: String, validValues: Array<String>) -> String,
            addCustomCategoryComment: (key: String, comment: String) -> Unit,
            getStringList: (name: String, category: String, defaultValues: Array<String>, comment: String) -> Array<String>
    ) {
        var category = "relay"
        addCustomCategoryComment(category, "Relay options")
        relay = RelayOptions(
                systemUser = getString(
                        "systemUser",
                        category,
                        relay.systemUser,
                        "Name of the server user (used by death and advancement messages and the /say command)"
                ),
                advancements = getBoolean(
                        "advancements",
                        category,
                        relay.advancements,
                        "Relay player advancements"
                ),
                logLevel = getStringValidValues(
                        "logLevel",
                        category,
                        relay.logLevel,
                        "MatterLink log level",
                        arrayOf("INFO", "DEBUG", "TRACE")
                )
        )

        category = "commands"
        addCustomCategoryComment(category, "User commands")
        command = CommandOptions(
                enable = getBoolean(
                        "enable",
                        category,
                        command.enable,
                        "Enable MC bridge commands"
                ),
                prefix = getStringValidated(
                        "prefix",
                        category,
                        command.prefix,
                        "Prefix for MC bridge commands. Accepts a single character (not alphanumeric or /)",
                        Pattern.compile("^[^0-9A-Za-z/]$")
                ),
                commandMapping = getStringList(
                        "commandMapping",
                        category,
                        command.commandMapping.map { entry ->
                            "${entry.key}=${entry.value}"
                        }
                                .toTypedArray(),
                        "MC commands that can be executed through the bridge" +
                                "\nSeparate bridge command and MC command with '='."
                ).associate {
                    val key = it.substringBefore('=')
                    val value = it.substringAfter('=')
                    Pair(key, value)
                }
        )

        category = "formatting"
        addCustomCategoryComment(category, "Gateway -> Server" +
                "Formatting options: " +
                "Available variables: {username}, {text}, {gateway}, {channel}, {protocol}, {username:antiping}")
        formatting = FormattingOptions(
                chat = getString(
                        "chat",
                        category,
                        formatting.chat,
                        "Generic chat event, just talking"
                ),
                joinLeave = getString(
                        "joinLeave",
                        category,
                        formatting.joinLeave,
                        "Join and leave events from other gateways"
                ),
                action = getString(
                        "action",
                        category,
                        formatting.action,
                        "User actions (/me) sent by users from other gateways"
                )
        )

        category = "join_leave"
        addCustomCategoryComment(category, "Server -> Gateway" +
                "Formatting options: " +
                "Available variables: {username}, {username:antiping}")
        joinLeave = FormattingJoinLeave(

                showJoin = getBoolean(
                        "showJoin",
                        category,
                        joinLeave.showJoin,
                        "Relay when a player joins the game"
                ),

                showLeave = getBoolean(
                        "showLeave",
                        category,
                        joinLeave.showLeave,
                        "Relay when a player leaves the game"
                ),
                joinServer = getString(
                        "joinServer",
                        category,
                        joinLeave.joinServer,
                        "user join message sent to other gateways, available variables: {username}, {username:antiping}"
                ),
                leaveServer = getString(
                        "leaveServer",
                        category,
                        joinLeave.leaveServer,
                        "user leave message sent to other gateways, available variables: {username}, {username:antiping}"
                )
        )

        category = "connection"
        addCustomCategoryComment(category, "Connection settings")
        connect = ConnectOptions(
                url = getString(
                        "connectURL",
                        category,
                        connect.url,
                        "The URL or IP address of the bridge server"
                ),
                authToken = getString(
                        "authToken",
                        category,
                        connect.authToken,
                        "Auth token used to connect to the bridge server"
                ),
                gateway = getString(
                        "gateway",
                        category,
                        connect.gateway,
                        "MatterBridge gateway"
                ),
                autoConnect = getBoolean(
                        "autoConnect",
                        category,
                        connect.autoConnect,
                        "Connect the relay on startup"
                )
        )
        category = "death"
        addCustomCategoryComment(category, "Death message settings")
        death = DeathOptions(
                showDeath = getBoolean(
                        "showDeath",
                        category,
                        death.showDeath,
                        "Relay player death messages"
                ),
                showDamageType = getBoolean(
                        "showDamageType",
                        category,
                        death.showDamageType,
                        "Enable Damage type symbols on death messages"
                ),
                damageTypeMapping = getStringList(
                        "damageTypeMapping",
                        category,
                        death.damageTypeMapping.map { entry ->
                            "${entry.key}=${entry.value}"
                        }
                                .toTypedArray(),
                        "Damage type mapping for everything else, " +
                                "\nseparate value and key with '=', " +
                                "\nseparate multiple values with spaces\n"
                ).associate {
                    val key = it.substringBefore('=')
                    val value = it.substringAfter('=')
                    Pair(key, value)
                }
        )


        category = "update"
        addCustomCategoryComment(category, "Update Settings")
        update = UpdateOptions(
                enable = getBoolean(
                        "enable",
                        category,
                        update.enable,
                        "Enable Update checking"
                )
        )
    }

    abstract fun load(): BaseConfig
}