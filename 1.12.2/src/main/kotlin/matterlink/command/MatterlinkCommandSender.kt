package matterlink.command

import matterlink.bridge.ApiMessage
import matterlink.bridge.MessageHandler
import matterlink.bridge.command.IMinecraftCommandSender
import matterlink.config.cfg
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.common.FMLCommonHandler
import javax.annotation.Nonnull

object MatterlinkCommandSender : IMinecraftCommandSender, ICommandSender {

    override fun execute(cmdString: String): Boolean {
        return 0 < FMLCommonHandler.instance().minecraftServerInstance.commandManager.executeCommand(
                this,
                cmdString
        )
    }

    override fun getName(): String {
        return "MatterLink"
    }

    override fun getEntityWorld(): World {
        return FMLCommonHandler.instance().minecraftServerInstance.getWorld(0)
    }

    override fun canUseCommand(permLevel: Int, commandName: String?): Boolean {
        //permissions are checked on our end
        return true
    }

    override fun getServer(): MinecraftServer? {
        return FMLCommonHandler.instance().minecraftServerInstance
    }

    override fun sendMessage(@Nonnull component: ITextComponent?) {
        MessageHandler.transmit(ApiMessage(
                username = cfg.relay.systemUser,
                text = component!!.unformattedComponentText
        ))

    }

    override fun sendCommandFeedback(): Boolean {
        return true
    }
}