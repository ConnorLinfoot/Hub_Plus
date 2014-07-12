package com.connorlinfoot.hubplus.Player;

import com.connorlinfoot.hubplus.Global.UsefulFunctions;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerBroadcasts implements Listener {

    private Plugin instance = HubPlus.getInstance();

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoinBroadcast( PlayerJoinEvent event ){
        Player player = event.getPlayer();
        String msg = instance.getConfig().getString("On Join Broadcast");
        String msgnew = msg.replace( "<playername>", player.getDisplayName() );
        msgnew = UsefulFunctions.textColors(msgnew);
        event.setJoinMessage(msgnew);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuitBroadcast( PlayerQuitEvent event ){
        Player player = event.getPlayer();
        String msg = instance.getConfig().getString("On Quit Broadcast");
        String msgnew = msg.replace( "<playername>", player.getDisplayName() );
        event.setQuitMessage(msgnew);
    }

}