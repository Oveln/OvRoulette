package com.oveln.ovroulette.Listener

import com.oveln.ovroulette.Main
import com.oveln.ovroulette.Rouletter.Items
import com.oveln.ovroulette.utils.CharUtils
import org.apache.commons.lang.StringUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class onPlayerChat :Listener {
    @EventHandler
    fun onListened(event: AsyncPlayerChatEvent):Boolean {
        if (Main.Instance.SettingPlayers.keys.contains(event.player)) {
            if (StringUtils.isNumeric(event.message)) {
                Items.wights[Main.Instance.SettingPlayers[event.player]?: 0] = event.message.toInt()
                Main.Instance.SettingPlayers.remove(event.player)
                event.player.sendMessage(CharUtils.t("&2成功修改为${event.message.toInt()}"))
                event.isCancelled = true
            } else {
                event.player.sendMessage(CharUtils.t("&c请输入一个数字"))
                event.isCancelled = true
            }
            Items.calcRandomTable()
        }
        return true
    }
}