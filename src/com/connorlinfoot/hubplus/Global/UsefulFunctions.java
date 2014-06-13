package com.connorlinfoot.hubplus.Global;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class UsefulFunctions extends JavaPlugin {

    public static String textColors(String msg){
        msg = msg.replace("&0", ChatColor.BLACK + "" );
        msg = msg.replace("&1", ChatColor.DARK_BLUE + "" );
        msg = msg.replace("&2", ChatColor.DARK_GREEN + "" );
        msg = msg.replace("&3", ChatColor.DARK_AQUA + "" );
        msg = msg.replace("&4", ChatColor.DARK_RED + "" );
        msg = msg.replace("&5", ChatColor.DARK_PURPLE + "" );
        msg = msg.replace("&6", ChatColor.GOLD + "" );
        msg = msg.replace("&7", ChatColor.GRAY + "" );
        msg = msg.replace("&8", ChatColor.DARK_GRAY + "" );
        msg = msg.replace("&9", ChatColor.BLUE + "" );
        msg = msg.replace("&a", ChatColor.GREEN + "" );
        msg = msg.replace("&b", ChatColor.AQUA + "" );
        msg = msg.replace("&c", ChatColor.RED + "" );
        msg = msg.replace("&e", ChatColor.YELLOW + "" );
        msg = msg.replace("&f", ChatColor.WHITE + "" );
        msg = msg.replace("&k", ChatColor.MAGIC + "" );
        msg = msg.replace("&l", ChatColor.BOLD + "" );
        msg = msg.replace("&n", ChatColor.UNDERLINE + "" );
        msg = msg.replace("&o", ChatColor.ITALIC + "" );
        msg = msg.replace("&m", ChatColor.STRIKETHROUGH + "" );
        msg = msg.replace("&r", ChatColor.RESET + "" );
        return msg;
    }

}
