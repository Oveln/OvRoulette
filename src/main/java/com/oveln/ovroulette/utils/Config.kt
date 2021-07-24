package com.oveln.ovroulette.utils

import com.oveln.ovroulette.Main
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash

object Config {
    private val config = Main.Instance.config
    lateinit var Title : String
    lateinit var KeyName : String
    lateinit var Probability : String
    lateinit var PlayerMessage : HashMap<Int , List<String>>
    lateinit var Announcement : HashMap<Int , List<String>>
    fun load() {
        Main.Instance.saveDefaultConfig()
        Title = CharUtils.t(config.getString("Title")?: "抽奖")
        KeyName = CharUtils.t(config.getString("Keyname")?: "钥匙")
        Probability = CharUtils.t(config.getString("Probability")?: "%Probability%")
        PlayerMessage = HashMap()
        Announcement = HashMap()
        for (i in listOf(1 , 3 , 5)) {
            var messages = config.getConfigurationSection("PlayerMessages")?.getStringList(i.toString())
            if (messages != null) {
                for (index in messages.indices) messages[index] = CharUtils.t(messages[index])
            }else messages = listOf("%item1%")
            PlayerMessage[i] = messages

            messages = config.getConfigurationSection("Announcement")?.getStringList(i.toString())
            if (messages != null) {
                for (index in messages.indices) messages[index] = CharUtils.t(messages[index])
            }else messages = listOf("%item1%")
            Announcement[i] = messages
        }
    }
}