package matterlink.command

import matterlink.bridge.command.IMinecraftCommandSender
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChunkCoordinates
import net.minecraft.util.IChatComponent
import net.minecraft.world.World

class MatterLinkCommandSender(user: String, userId: String, server: String, op: Boolean) : IMinecraftCommandSender(user, userId, server, op), ICommandSender {

    private var level: Int = 0

    override fun execute(cmdString: String): Boolean {
        return 0 < MinecraftServer.getServer().commandManager.executeCommand(
                this,
                cmdString
        )
    }

    override fun getFormattedCommandSenderName(): IChatComponent {
        return ChatComponentText(user)
    }

    override fun getCommandSenderName() = accountName

    override fun getEntityWorld(): World {
        return MinecraftServer.getServer().worldServerForDimension(0)
    }

    override fun canCommandSenderUseCommand(permLevel: Int, commandName: String): Boolean {
        //we do permission
        return canExecute(commandName)
    }

    override fun addChatMessage(component: IChatComponent) {
        sendReply(component.unformattedText)
    }

    override fun getCommandSenderPosition(): ChunkCoordinates = ChunkCoordinates(0, 0, 0)
}