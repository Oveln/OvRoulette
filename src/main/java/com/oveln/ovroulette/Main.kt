package com.oveln.ovroulette

import com.oveln.ovroulette.Listener.onInventoryClick
import com.oveln.ovroulette.Listener.onInventoryClose
import com.oveln.ovroulette.Listener.onPlayerChat
import com.oveln.ovroulette.Rouletter.Items
import com.oveln.ovroulette.Rouletter.keys
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
        keys.load()

        Bukkit.getPluginManager().run {
            registerEvents(onInventoryClick() , this@Main)
            registerEvents(onInventoryClose() , this@Main)
            registerEvents(onPlayerChat() , this@Main)
        }
        getCommand("roulette")!!.setExecutor(onroulette())

        logger.info("&6${description.name}&f ${description.version}&2 启动成功   &c作者${description.authors}".colorful())
    }
    override fun onDisable() {
        Items.save()
        keys.save()
        logger.info("&6${description.name}&f ${description.version}&2 关闭成功   &c作者${description.authors}".colorful())
    }
    fun String.colorful():String {
        return this.replace("&" , "§")
    }
}