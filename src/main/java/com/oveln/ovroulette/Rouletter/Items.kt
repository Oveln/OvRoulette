package com.oveln.ovroulette.Rouletter

import com.oveln.ovroulette.Main
import com.oveln.ovroulette.utils.Config
import com.oveln.ovroulette.utils.GUIMaker
import net.minecraft.server.v1_12_R1.*
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.io.File

import java.io.FileInputStream
import java.io.FileOutputStream
import net.minecraft.server.v1_12_R1.ItemStack as ItemStack1

object Items {
    private val path = "plugins/${Main.Instance.description.name}/items.bin"
    private lateinit var file : File
    var items : MutableList<ItemStack> = ArrayList()
    var probitems: MutableList<ItemStack> = ArrayList()
    var enchitems : MutableList<ItemStack> = ArrayList()
    var wights : MutableList<Int> = ArrayList()
    var allwights : Int = 0

    fun getprob(id: Int):Double {
        return wights[id].div(allwights.toDouble())*100
    }
    fun calcRandomTable() {
        allwights = 0
        enchitems.clear()
        probitems.clear()
        wights.forEach() { allwights+=it }
        items.forEachIndexed() {
            index,it ->
            val i = it.clone();
            val meta = i.itemMeta
            val lore = meta.lore?:ArrayList<String>()
            lore.add(Config.Probability.replace("%probability%", "${String.format("%.2f",getprob(index))}%"))
            meta.lore = lore
            i.itemMeta = meta
            probitems.add(i)
            val q = i.clone()
            q.addUnsafeEnchantment(Enchantment.DURABILITY , 1)
            enchitems.add(q)
        }
    }
    fun WhatItem(randomint: Int): Int {
        var count = 0
        wights.forEachIndexed() {
            Index,wight ->
            count += wight
            if (count>=randomint) return Index
        }
        return 0
    }
    fun load() {
        file = File(path)
        if (!file.exists()) {
            file.createNewFile()
            while (items.size<26) items.add(GUIMaker.PANEMaker("空" , 8))
            while (wights.size<26) wights.add(0)
            calcRandomTable()
            return
        }
        val NBTin = NBTCompressedStreamTools.a(FileInputStream(path))
        val NBTitems = NBTin.get("items") as NBTTagList
        val NBTwights = NBTin.get("wights") as NBTTagIntArray
        for (i in 0 until NBTitems.size()) {
            items.add(CraftItemStack.asBukkitCopy(ItemStack1(NBTitems.get(i))))
            wights.add(NBTwights.d()[i])
        }
        Main.Instance.logger.info("读取了${items.size}个物品")
        while (items.size<26) items.add(GUIMaker.PANEMaker("空" , 8))
        while (wights.size<26) wights.add(0)
        calcRandomTable()
    }
    fun save() {
        val NBTout = NBTTagCompound()
        val NBTitems = NBTTagList()
        val NBTwights = NBTTagIntArray(wights)
        for (item in items) {
            val nbt = NBTTagCompound()
            CraftItemStack.asNMSCopy(item).save(nbt)
            NBTitems.add(nbt)
        }
        val nbt = NBTTagCompound()
        NBTout.set("key" , nbt)
        NBTout.set("wights" , NBTwights)
        NBTout.set("items" , NBTitems)
        NBTCompressedStreamTools.a(NBTout , FileOutputStream(path))
        Main.Instance.logger.info("保存了${items.size}个物品")
    }
}