package com.connorlinfoot.hubplus.Global;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class NoPermsFunction extends JavaPlugin {

    public static String noPerms(Player player){
        player.sendMessage(ChatColor.RED + "No Permission!");
        return "";
    }

}
