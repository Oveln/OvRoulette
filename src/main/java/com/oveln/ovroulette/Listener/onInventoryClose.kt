package com.oveln.ovroulette.Listener

import com.oveln.ovroulette.GUI.RouletterHolder
import com.oveln.ovroulette.Rouletter.Items
import com.oveln.ovroulette.utils.GUIMaker
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class onInventoryClose :Listener {
    @EventHandler
    fun onListened(event: InventoryCloseEvent):Boolean {
        if (event.inventory.holder !is RouletterHolder) return true
        val holder = event.inventory.holder as RouletterHolder
        holder.isclosed = true
        when(holder.role) {
            "setitem" -> {
                val nowitems = ArrayList<ItemStack>()
                var nowslot = 0
                var id = 0
                do {
                    val item = event.inventory.getItem(nowslot)?: GUIMaker.PANEMaker("空",8)
                    nowitems.add(if (item==Items.probitems[id]) Items.items[id] else item)
                    nowslot = GUIMaker.NextSlot(event.inventory,0,0,8,5,nowslot)?:0
                    id++
                }while(nowslot!=0)
                Items.items = nowitems
                Items.calcRandomTable()
            }
        }
        return true
    }
}