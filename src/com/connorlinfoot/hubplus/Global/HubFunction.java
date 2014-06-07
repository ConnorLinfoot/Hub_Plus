package com.connorlinfoot.hubplus.Global;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class HubFunction extends JavaPlugin {

   public static boolean goToHub(Player player){
        Plugin instance = HubPlus.getInstance();
        if (instance.getConfig().isSet("Hub TP X") && instance.getConfig().isSet("Hub TP Y") && instance.getConfig().isSet("Hub TP Z")) {
            Location newLocation = player.getLocation();
            newLocation.setX((Double) instance.getConfig().get("Hub TP X"));
            newLocation.setY((Double) instance.getConfig().get("Hub TP Y"));
            newLocation.setZ((Double) instance.getConfig().get("Hub TP Z"));
            Float newpitch = Float.valueOf((float) instance.getConfig().getDouble("Hub TP Pitch"));
            Float newyaw = Float.valueOf((float) instance.getConfig().getDouble("Hub TP Yaw"));
            newLocation.setPitch(newpitch);
            newLocation.setYaw(newyaw);
            String world = null;
            if( instance.getConfig().isSet("Hub TP World")){
                world = instance.getConfig().getString("Hub TP World");
            } else {
                world = instance.getConfig().getString("Hub World");
            }
            newLocation.setWorld(Bukkit.getServer().getWorld(world));
            player.teleport(newLocation);
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Welcome to the hub!");
        } else {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "No hub is set!");
        }
        return false;
    }

}
