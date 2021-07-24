package com.oveln.ovroulette.command

import com.oveln.ovroulette.GUI.RouletterHolder
import com.oveln.ovroulette.Main
import com.oveln.ovroulette.Rouletter.Items
import com.oveln.ovroulette.Rouletter.keys
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
            "/roulette open                        打开抽奖界面",
            "/roulette query                       查询${Config.KeyName}数量"
    )
    private val OPHelpMessage = listOf(
            "/roulette give <玩家名> <数量>          给玩家一定数量的钥匙",
            "/roulette setitem                     打开物品设置界面",
            "/roulette setprob                     打开概率设置界面",
            "/roulette reload                      重载配置文件",
            "/roulette save                        保存配置文件"

    )
    override fun onCommand(commandsender: CommandSender, cmd: Command, flag: String, args: Array<out String>): Boolean {
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
            if (args[0] == "query") {
                player.sendMessage(CharUtils.t("&6你当前有${keys.keys[player.uniqueId]?:0}个${Config.KeyName}"))
                return true
            }
            if (player.isOp) {
                if (args[0] == "setitem") RouletterHolder("setitem" , player).open(player)
                if (args[0] == "setprob") RouletterHolder("setprob" , player).open(player)
                if (args[0] == "reload") {
                    Items.load()
                    keys.load()
                }
                if (args[0] == "save") {
                    Items.save()
                    keys.save()
                }
                return true
            }
        }
        if (args.size == 3) {
            if (args[0] == "give") {
                val p = Bukkit.getPlayer(args[1]) ?: return false
                keys.keys[p.uniqueId] = (keys.keys[p.uniqueId]?:0) + args[2].toInt()
                p.sendMessage("你获得了${args[2]}个${Config.KeyName}")
            }
            return true
        }
        return false
    }

    override fun onTabComplete(commandsender: CommandSender, cmd: Command, flag: String, args: Array<out String>): MutableList<String>? {
        if (args.size == 1) {
            if (commandsender.isOp) return listOf("setitem" , "setprob", "give" , "open").toMutableList()
            else return listOf("open").toMutableList()
        }
        return listOf("").toMutableList()
    }

}