package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.HubFunction;
import com.connorlinfoot.hubplus.Global.NoPermsFunction;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public class CustomHubCommand implements Listener {

    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void command( PlayerCommandPreprocessEvent event ){
        String test = event.getMessage();
        Player player = event.getPlayer();
        if( test.equals( "/" + instance.getConfig().getString( "Custom Hub Command" ) ) ) {
            if( player.hasPermission( "hubplus.hub" ) ) {
                HubFunction.goToHub(player);
            } else {
                NoPermsFunction.noPerms(player);
            }
            event.setCancelled(true);
        }
    }
}
