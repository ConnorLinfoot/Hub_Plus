package com.connorlinfoot.hubplus.Player;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;

public class DoubleJump implements Listener {

    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void onPlayerFlight( PlayerToggleFlightEvent event ){
        Player player = event.getPlayer();
        if( player.getGameMode() == GameMode.CREATIVE || player.hasPermission("hubplus.fly.self") ) return;

        event.setCancelled(true);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event ){
        Player player = event.getPlayer();
        if(player.getGameMode() != GameMode.CREATIVE
                && player.getLocation().subtract(0,1,0).getBlock().getType() != Material.AIR
                && !player.isFlying()){

            player.setAllowFlight(true);
        }
    }
}
