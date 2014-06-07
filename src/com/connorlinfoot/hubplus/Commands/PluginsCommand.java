package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

public class PluginsCommand implements Listener {
    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void command( PlayerCommandPreprocessEvent event ){
        String test = event.getMessage();
        Player player = event.getPlayer();
        if( test.equals( "/plugins" ) && instance.getConfig().getString("Disable Plugins Command").equals("true")) {
            event.setCancelled(true);
            Messages.noPerms(player);
        }
    }
}
