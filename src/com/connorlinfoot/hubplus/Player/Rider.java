package com.connorlinfoot.hubplus.Player;

import com.connorlinfoot.hubplus.Global.ChatColor;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

public class Rider implements Listener {

    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void playerInteractEvent( PlayerInteractEntityEvent event ){
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if(entity instanceof Player) {
            String world =  player.getWorld().getName();
            String currentworld = instance.getConfig().getString("Hub World");
            if(world.equals(currentworld)) {
                if (player.hasPermission("hubplus.ride")) {
                    //String rider = String.valueOf(player.getPassenger());
                    //Entity riderentity = player.getPassenger();
                    //if (rider.equals("null")) { // Used for having 2 players riding! Seems to currently cause server crash?
                    player.setPassenger(entity);
                    ((Player) entity).sendMessage(ChatColor.getChatColor() + "You are now riding " + player.getDisplayName() + " (Use shift to dismount)");
                    //} else {
                        //riderentity.setPassenger(entity);
                    //}
                }
            }
        }
    }

}
