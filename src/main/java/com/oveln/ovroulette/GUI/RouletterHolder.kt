package com.oveln.ovroulette.GUI

import com.oveln.ovroulette.Main
import com.oveln.ovroulette.Rouletter.Items
import com.oveln.ovroulette.Rouletter.keys
import com.oveln.ovroulette.utils.CharUtils
import com.oveln.ovroulette.utils.Config
import com.oveln.ovroulette.utils.GUIMaker
import com.oveln.ovroulette.utils.GUIMaker.PANEMaker
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.scheduler.BukkitRunnable

class RouletterHolder(val role : String,val player: Player) : InventoryHolder {
    private val gui : Inventory = Bukkit.createInventory(this , 9*6 , Config.Title)
    private var giftslot = 3
    private var nowgift = 0
    private val gift = ArrayList<Int>()
    var isclosed = false
    var isrouling = false
    var rouled = false
    init {
        var nowslot = 0
        Items.probitems.forEachIndexed{
            index, itemStack ->
            val item = itemStack.clone()
            if (role == "setprob") {
                val meta = item.itemMeta
                val lore = item.itemMeta.lore?: ArrayList<String>()
                lore.add("wight:${Items.wights[index]}")
                meta.lore = lore
                item.itemMeta = meta
            }
            gui.setItem(nowslot, item)
            nowslot = GUIMaker.NextSlot(gui , 0,0,8,5,nowslot)?: 0
        }
        GUIMaker.fill(gui , PANEMaker(" ", 15) , 1,1,7,4)
        gui.setItem(3*9+3 , PANEMaker(CharUtils.t("&f单抽") , 5 , listOf(Config.RemainKey.replace("%keys%","${keys.keys[player.uniqueId]?:0}"))))
        gui.setItem(3*9+4 , PANEMaker(CharUtils.t("&f三抽") , 5 , listOf(Config.RemainKey.replace("%keys%","${keys.keys[player.uniqueId]?:0}"))))
        gui.setItem(3*9+5 , PANEMaker(CharUtils.t("&f五抽") , 5 , listOf(Config.RemainKey.replace("%keys%","${keys.keys[player.uniqueId]?:0}"))))
        (2..6).forEach() {
//            gui.setItem(2*9+it , PANEMaker(" " , 3))
            gui.clear(2*9+it)
        }
    }
    override fun getInventory(): Inventory {
        return gui
    }
    fun open(player : Player) {
        player.openInventory(gui)
    }
    fun addgift() {
        gui.setItem(2*9+giftslot , Items.items[gift[nowgift]])
        giftslot++
        nowgift++
    }
    fun roulette(i: Int):Boolean {
        if (i!=1&&i!=5&&i!=3) return false
        when(i) {
            1 -> giftslot = 4
            3 -> giftslot = 3
            5 -> giftslot = 2
        }
        var emptyslot = 0
        repeat(27){
            if (player.enderChest.getItem(it)==null) emptyslot++
        }
        if (emptyslot<i) {
            player.sendMessage(CharUtils.t("&c你没有足够的空间"))
            return false
        }
        if (!keys.keys.containsKey(player.uniqueId) || keys.keys[player.uniqueId]!! < i) {
            player.sendMessage(CharUtils.t("&c你没有足够的${Config.KeyName}"))
            return false
        } else {
            keys.keys[player.uniqueId] = keys.keys[player.uniqueId]!! - i
        }
//开始抽奖
        rouled = true
        isrouling = true
        (2..6).forEach() {
            gui.clear(2*9+it)
        }
        gui.setItem(3*9+3 , PANEMaker(CharUtils.t("单抽") , 5 , listOf(Config.RemainKey.replace("%keys%","${keys.keys[player.uniqueId]?:0}"))))
        gui.setItem(3*9+4 , PANEMaker(CharUtils.t("三抽") , 5 , listOf(Config.RemainKey.replace("%keys%","${keys.keys[player.uniqueId]?:0}"))))
        gui.setItem(3*9+5 , PANEMaker(CharUtils.t("五抽") , 5 , listOf(Config.RemainKey.replace("%keys%","${keys.keys[player.uniqueId]?:0}"))))
        gift.clear();nowgift = 0
        repeat(i) {
            val randomint = (0..Items.allwights).random()
            gift.add(Items.WhatItem(randomint))
            player.enderChest.addItem(Items.items[gift[it]])
        }
        var alltime = 0.toLong()
        (0 until i).forEach() {
            val x = (0..2).random()
            RouletteTask(gui, 0, gift[it] + 26*x, 1,0,true).runTaskLater(Main.Instance, alltime*2)
            alltime+= gift[it]+26*x+5
        }
        (object : BukkitRunnable() {
            override fun run() {
                Config.PlayerMessage[i]!!.forEach() {
                    var s = it
                    repeat(i){ now ->
                        s = s.replace("%item${now+1}%" , Items.items[gift[now]].itemMeta.displayName?:Items.items[gift[now]].itemMeta.localizedName?:Items.items[gift[now]].type.name)
                    }
                    player.sendMessage(s)
                }
                Config.Announcement[i]!!.forEach() {
                    var s = it
                    s = s.replace("%player%" , player.name)
                    repeat(i){ now ->
                        s = s.replace("%item${now+1}%" , Items.items[gift[now]].itemMeta.displayName?:Items.items[gift[now]].itemMeta.localizedName?:Items.items[gift[now]].type.name)
                    }
                    Bukkit.broadcastMessage(s)
                }
               isrouling = false
            }
        }).runTaskLater(Main.Instance , (alltime*2+10))
        return true
    }
    class RouletteTask(val gui: Inventory,val slotid: Int,val times: Int,val t: Int,var id: Int,var flg: Boolean) : BukkitRunnable() {
        override fun run() {
            if ((gui.holder as RouletterHolder).isclosed) return
            if (!flg) {
                gui.setItem(slotid, Items.probitems[id])
            }else {
                gui.setItem(slotid, Items.enchitems[id])
                (gui.holder as RouletterHolder).player.playSound((gui.holder as RouletterHolder).player.location , Sound.BLOCK_LEVER_CLICK , 1.toFloat(),1.toFloat())
                (gui.holder as RouletterHolder).player.updateInventory()
            }
            if (flg || times != 0) {
                val nextid = if(!flg) GUIMaker.NextSlot(gui, 0, 0, 8, 5, slotid) ?: 0 else slotid
                RouletteTask(gui, nextid, times - (if (flg) 0 else 1), t , if (flg) id else (id+1)%26 , !flg).runTaskLater(Main.Instance, t.toLong())
            } else if (!flg){
                gui.setItem(slotid, Items.items[id])
                (gui.holder as RouletterHolder).player.playSound((gui.holder as RouletterHolder).player.location , Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 1.toFloat(),1.toFloat())
                (gui.holder as RouletterHolder).addgift()
            }
        }
    }
}