package com.oveln.ovroulette

import com.oveln.ovroulette.Listener.onInventoryClick
import com.oveln.ovroulette.Listener.onInventoryClose
import com.oveln.ovroulette.Listener.onPlayerChat
import com.oveln.ovroulette.Rouletter.Items
import com.oveln.ovroulette.command.onroulette
import com.oveln.ovroulette.utils.Config
import org.bukkit.Bukkit
import org.bukkit.entity.HumanEntity
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    val SettingPlayers = HashMap<HumanEntity , Int>()

    companion object {
        lateinit var Instance : Main
    }
    override fun onEnable() {
        Instance = this

        Config.load()
        Items.load()

        Bukkit.getPluginManager().run {
            registerEvents(onInventoryClick() , this@Main)
            registerEvents(onInventoryClose() , this@Main)
            registerEvents(onPlayerChat() , this@Main)
        }
        getCommand("roulette")!!.setExecutor(onroulette())

        logger.info("${description.name}${description.version}启动成功\n作者${description.authors}")
    }
    override fun onDisable() {
        Items.save()
        logger.info("${description.name}${description.version}关闭成功\n作者${description.authors}")
    }
}