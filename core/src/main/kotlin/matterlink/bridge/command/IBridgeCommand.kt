package matterlink.bridge.command

import matterlink.config.PermissionConfig

interface IBridgeCommand {
    val alias : String
    val help : String
    val permLevel : Int

    fun execute(user:String, userId:String, server:String, args:String) : Boolean

    fun validate() = true

    companion object {


        fun getPermLevel(userId: String, server: String): Int {
            if (PermissionConfig.perms[server] == null) return 0
            return PermissionConfig.perms[server]?.get(userId) ?: 0
        }
    }
}