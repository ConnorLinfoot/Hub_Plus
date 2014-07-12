package com.connorlinfoot.hubplus.Player;

import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class Stacker implements Listener {

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
                    ArrayList stackerdisabled = (ArrayList) instance.getConfig().getList("PlayersStackerDisabled");
                    if( !stackerdisabled.contains(((Player) entity).getName())) {
                        if(!stackerdisabled.contains(player.getName())) {
                            Entity rider = player.getPassenger();
                            if( rider == null ) {
                                //String rider = String.valueOf(player.getPassenger());
                                //Entity riderentity = player.getPassenger();
                                //if (rider.equals("null")) { // Used for having 2 players riding! Seems to currently cause server crash?
                                player.setPassenger(entity);
                                ((Player) entity).sendMessage(Messages.getChatColor() + "You are now riding " + player.getDisplayName() + " (Use shift to dismount)");
                                //} else {
                                //riderentity.setPassenger(entity);
                                //}
                            } else {
                                player.eject();
                            }
                        } else {
                            player.sendMessage("You have stacker disabled!");
                        }
                    } else {
                        player.sendMessage(((Player) entity).getName() + " has stacker disabled!");
                    }
                }
            }
        }
    }
}