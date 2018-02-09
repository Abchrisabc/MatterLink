package matterlink

var cfg: IMatterLinkConfig? = null

abstract class IMatterLinkConfig {
    protected val CATEGORY_RELAY_OPTIONS = "relay"
    protected val CATEGORY_FORMATTING = "formatting"
    protected val CATEGORY_CONNECTION = "connection"
    protected val CATEGORY_COMMAND = "command"

    var relay: RelayOptions = RelayOptions()
    var connect: ConnectOptions = ConnectOptions()
    var formatting: FormattingOptions = FormattingOptions()
    var command: CommandOptions = CommandOptions()

    data class RelayOptions(
            val systemUser: String = "Server",
            val deathEvents: Boolean = true,
            val advancements: Boolean = true,
            val joinLeave: Boolean = true
    )

    data class FormattingOptions(
            val chat: String = "<{username}> {text}",
            val joinLeave: String = "§6-- {username} {text}",
            val action: String = "§5* {username} {text}"
    )

    data class ConnectOptions(
            val url: String = "http://localhost:4242",
            val authToken: String = "",
            val gateway: String = "minecraft"
    )

    data class CommandOptions(
            val prefix: String = "$",
            val enable: Boolean = true
    )
}