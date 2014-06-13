package com.connorlinfoot.hubplus.Player;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class PlayerListener implements Listener {

    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void onItemDrop( PlayerDropItemEvent event ){
        Player player = event.getPlayer();
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        if(world.equals(currentworld)) {
            if (!player.hasPermission("hubplus.inv.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLoginEvent( PlayerLoginEvent event ){
        // Currently does fuck all :-)
    }

    /*@EventHandler
    public void onServerPing( ServerListPingEvent event ){
        String IP = String.valueOf( event.getAddress() );
        if(!IP.equals("/127.0.0.1")){
            event.setMotd("Your IP is: " + IP);
            CachedServerIcon serverIcon = instance.getServer().getServerIcon();
        } else {
            event.setMotd( "Your User is: " + ChatColor.GREEN + "MoogleMan!" );
        }

        event.setMaxPlayers(1);
    }*/


    @EventHandler
    public void onItemPickup( PlayerPickupItemEvent event ){
        Player player = event.getPlayer();
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        if(world.equals(currentworld)) {
            if (!player.hasPermission("hubplus.inv.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSprint( PlayerToggleSprintEvent event ){
        Player player = event.getPlayer();
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        if(world.equals(currentworld)) {
            //player.sendMessage("I see you sprinting! Have a boost!");
            Integer time = 1;
            Integer time2 = time * 60;
            Integer time3 = time2 * 20;
            player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, time3, 1)));
        }
    }

    @EventHandler
    public void onInvDrag( InventoryClickEvent event ){
        Player player = (Player) event.getWhoClicked();
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        if(world.equals(currentworld)) {
            if (!player.hasPermission("hubplus.inv.bypass")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoinTP( PlayerJoinEvent event ){
        Player player = event.getPlayer();
        if( !player.hasPermission( "hubplus.jointp.bypass" ) && instance.getConfig().getString( "Hub On Join" ).equals( "true" ) ) {
            Location newLocation = player.getLocation();
            newLocation.setX((Double) instance.getConfig().get("Hub TP X"));
            newLocation.setY((Double) instance.getConfig().get("Hub TP Y"));
            newLocation.setZ((Double) instance.getConfig().get("Hub TP Z"));
            newLocation.setPitch(0);
            newLocation.setYaw(180);
            String world = instance.getConfig().getString("Hub World");
            newLocation.setWorld(Bukkit.getServer().getWorld(world));
            player.teleport(newLocation);
        }
    }

    @EventHandler
    public void onQuitTP( PlayerQuitEvent event ){
        Player player = event.getPlayer();
        if( !player.hasPermission( "hubplus.jointp.bypass" ) && instance.getConfig().getString( "Hub On Join" ).equals( "true" ) ) {
            Location newLocation = player.getLocation();
            newLocation.setX((Double) instance.getConfig().get("Hub TP X"));
            newLocation.setY((Double) instance.getConfig().get("Hub TP Y"));
            newLocation.setZ((Double) instance.getConfig().get("Hub TP Z"));
            newLocation.setPitch(0);
            newLocation.setYaw(180);
            String world = instance.getConfig().getString("Hub World");
            newLocation.setWorld(Bukkit.getServer().getWorld(world));
            player.teleport(newLocation);
        }
    }
}
