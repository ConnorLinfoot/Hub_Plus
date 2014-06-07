package com.connorlinfoot.hubplus.Global;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Messages extends JavaPlugin {

    public static org.bukkit.ChatColor getChatColor(){
        return org.bukkit.ChatColor.AQUA;
    }

    public static String noPerms(Player player){
        player.sendMessage(ChatColor.RED + "No Permission!");
        return "";
    }

    public static String start(){
        return getPrefix() + getChatColor();
    }

    public static String getPrefix(){
        return ChatColor.GREEN + "[Hub Plus] " + ChatColor.RESET;
    }

}
