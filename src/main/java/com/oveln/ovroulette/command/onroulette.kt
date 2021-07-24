package com.oveln.ovroulette.command

import com.oveln.ovroulette.GUI.RouletterHolder
import com.oveln.ovroulette.Main
import com.oveln.ovroulette.Rouletter.Items
import com.oveln.ovroulette.utils.CharUtils
import com.oveln.ovroulette.utils.Config
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class onroulette : CommandExecutor,TabCompleter{
    private val HelpMessage = listOf(
            "/roulette open                        打开抽奖界面"
    )
    private val OPHelpMessage = listOf(
            "/roulette give <玩家名> <数量>          给玩家一定数量的钥匙",
            "/roulette setitem                     打开物品设置界面",
            "/roulette setprob                     打开概率设置界面",
            "/roulette setkey                      将手上物品设置为Key"
    )
    override fun onCommand(commandsender: CommandSender, cmd: Command, flag: String, args: Array<out String>): Boolean {
        Main.Instance.logger.info(args.size.toString())
        if (args.isEmpty()) {
            HelpMessage.forEach{commandsender.sendMessage(it)}
            if (commandsender.isOp)
                OPHelpMessage.forEach{commandsender.sendMessage(it)}
            return true
        }
        if (commandsender !is Player) commandsender.sendMessage(CharUtils.t("&c只有玩家能输入这个指令"))
        val player = commandsender as Player
        if (args.size == 1) {
            if (args[0] == "open") {
                RouletterHolder("roulette" , player).open(player)
                return true
            }
            if (player.isOp) {
                if (args[0] == "setitem") RouletterHolder("setitem" , player).open(player)
                if (args[0] == "setprob") RouletterHolder("setprob" , player).open(player)
                if (args[0] == "setkey") {
                    val item = player.inventory.itemInMainHand
                    item.amount = 1
                    Items.key = item
                    player.sendMessage(CharUtils.t("&2更改成功"))
                    return true
                }
            }
        }
        if (args.size == 3) {
            if (args[0] == "give") {
                val p = Bukkit.getPlayer(args[1]) ?: return false
                val key = Items.key.clone()
                key.amount = args[2].toInt()
                p.inventory.addItem(key)
                p.sendMessage("你获得了${args[2]}个${Config.KeyName}")
            }
            return true
        }
        return false
    }

    override fun onTabComplete(commandsender: CommandSender, cmd: Command, flag: String, args: Array<out String>): MutableList<String>? {
        if (args.size == 1) {
            if (commandsender.isOp) return listOf("setitem" , "setprob", "setkey" , "give" , "open").toMutableList()
            else return listOf("open").toMutableList()
        }
        return listOf("").toMutableList()
    }

}