package com.oveln.ovroulette.Listener

import com.oveln.ovroulette.GUI.RouletterHolder
import com.oveln.ovroulette.Main
import com.oveln.ovroulette.utils.CharUtils
import com.oveln.ovroulette.utils.Config
import com.oveln.ovroulette.utils.GUIMaker
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.scheduler.BukkitRunnable

class onInventoryClick : Listener {
    @EventHandler
    fun onListened(event: InventoryClickEvent): Boolean {
        if (event.slot <0) return true
        if (event.inventory.holder !is RouletterHolder) return true
        var s = (event.inventory.holder as RouletterHolder).role
        when(s) {
//            "setitem" -> setitem(event)
            "setprob" -> setprob(event)
            "roulette" -> roulette(event)
            "reload" -> Config.load()
        }
        return true
    }
    fun roulette(event: InventoryClickEvent) {
        event.isCancelled = true
        if ((event.inventory.holder as RouletterHolder).isrouling) return
        val idx = event.slot%9
        val idy = event.slot/9
        if (idy!=3 || (idx!=3&&idx!=4&&idx!=5)) return
        var flg = true
        when(idx) {
            3-> flg = (event.inventory.holder as RouletterHolder).roulette(1)
            4-> flg = (event.inventory.holder as RouletterHolder).roulette(3)
            5-> flg = (event.inventory.holder as RouletterHolder).roulette(5)
        }
//        if (!flg) {
//            (object : BukkitRunnable() {
//                override fun run() = event.whoClicked.closeInventory()
//            }).runTaskLater(Main.Instance , 1)
//        }
    }
//    fun setitem(event: InventoryClickEvent) {
//        val item = event.inventory.getItem(event.slot)
//        if (item != null) {
//            val disname = item.itemMeta.displayName?: ""
//            when (disname) {
//                " " -> event.isCancelled = true
//                "单抽" -> event.isCancelled = true
//                "三抽" -> event.isCancelled = true
//                "五抽" -> event.isCancelled = true
//            }
//        }
//    }
    fun setprob(event: InventoryClickEvent) {
        event.isCancelled = true
        val idx = event.slot%9
        val idy = event.slot/9
        if (idx!=0&&idy!=0&&idx!=8&&idy!=5) return
        Main.Instance.SettingPlayers.put(event.whoClicked , GUIMaker.Slot2ID(event.inventory,0,0,8,5,event.slot))
        event.whoClicked.sendMessage(CharUtils.t("&6请输入你要设置的权重"))
        (object : BukkitRunnable() {
            override fun run() = event.whoClicked.closeInventory()
        }).runTaskLater(Main.Instance , 1)
    }
}