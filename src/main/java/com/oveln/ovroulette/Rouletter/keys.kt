package com.oveln.ovroulette.Rouletter

import com.oveln.ovroulette.Main
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

object keys {
    lateinit var keys : HashMap<UUID, Int>
    private val path = "plugins/${Main.Instance.description.name}/keys.yml"
    fun load() {
        val file = File(path)
        if (!file.exists()) file.createNewFile()
        val keysfile = YamlConfiguration()
        keys = HashMap()
        keysfile.load(file)
        keysfile.getKeys(false).forEach() {
            keys.put(UUID.fromString(it), keysfile.getInt(it))
        }
    }
    fun save() {
        val file = File(path)
        if (!file.exists()) file.createNewFile()
        val keysfile = YamlConfiguration()
        keys.keys.forEach() {
            keysfile.set(it.toString() , keys[it])
        }
        keysfile.save(file)
    }
}