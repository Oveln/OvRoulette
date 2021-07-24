package com.oveln.ovroulette.utils

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object GUIMaker {
    // 以左上为原点
    fun fill(gui: Inventory, item: Material, x1: Int, y1: Int, x2: Int, y2: Int):Inventory? {
        if (x1>x2 || y1>y2 || x2>8 || y2>=gui.size/9) return null
        for (x in x1..x2)
            for (y in y1..y2)
                gui.setItem(x+y*9 , ItemStack(item))
        return gui
    }
    fun fill(gui: Inventory, item: ItemStack, x1: Int, y1: Int, x2: Int, y2: Int):Inventory? {
        if (x1>x2 || y1>y2 || x2>8 || y2>=gui.size/9) return null
        for (x in x1..x2)
            for (y in y1..y2)
                gui.setItem(x+y*9 , item)
        return gui
    }
    fun NextSlot(gui: Inventory, x1: Int, y1: Int, x2: Int, y2: Int ,id: Int ,wise : Boolean = true):Int? {
        if (id == -1) return 0
        val idx = id%9
        val idy = id/9
        val flg = if(wise) 1 else -1
        when {
            idx== 0 -> if (idy == 0) return idx+flg else return (idy-flg)*9
            idx== 8 -> if (idy == (gui.size/9)-flg) return (idx-flg)+idy*9 else return idx+(idy+flg)*9
            idy== 0 -> return (idx+flg)+idy*9
            idy== (gui.size/9)-flg -> return (idx-flg)+idy*9
        }
        return null
    }
    fun PANEMaker(name: String, damage: Int , lore: List<String>):ItemStack {
        val ret = ItemStack(Material.STAINED_GLASS_PANE , 1, damage.toShort())
        val meta = ret.itemMeta
        meta.displayName = name
        meta.lore = lore
        ret.itemMeta = meta
        return ret
    }
    fun PANEMaker(name: String, damage: Int):ItemStack {
        val ret = ItemStack(Material.STAINED_GLASS_PANE , 1, damage.toShort())
        val meta = ret.itemMeta
        meta.displayName = name
        ret.itemMeta = meta
        return ret
    }
    fun Slot2ID(gui: Inventory, x1: Int, y1: Int, x2: Int, y2: Int ,id: Int ,wise : Boolean = true):Int {
        var ret = 0
        var nid = 0
        while (nid !=id) {
            ret++
            nid = NextSlot(gui,x1,y1,x2,y2,nid)?:0
        }
        return ret
    }
}