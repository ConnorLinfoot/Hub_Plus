package com.connorlinfoot.hubplus;

import com.connorlinfoot.hubplus.Global.HubFunction;
import com.connorlinfoot.hubplus.Global.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class SignListener implements Listener {
    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void onSignPlace( SignChangeEvent event ){
        if( event.getLine(0).equals( "[Hub]" ) ){
            Player player = event.getPlayer();
            if( player.hasPermission("hubplus.sign.create") ) {
                event.setLine( 0, ChatColor.GREEN + "[Hub]" );
                player.sendMessage( "You have just created a Hub TP Sign" );
            } else {
                event.setLine( 0, ChatColor.RED + "[Hub]"  );
                Messages.noPerms(player);
            }
        }
    }

    @EventHandler
    public void onSignClick( PlayerInteractEvent event ){
        Block block = null;
        block = event.getClickedBlock();
        if ( block != null && ( block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST ) ) {
            Player player = event.getPlayer();
            Sign sign = (Sign) block.getState();
            String signline1 = sign.getLine(0);
            if (signline1.equals(ChatColor.GREEN + "[Hub]")) {
                if (player.hasPermission("hubplus.sign.use")) {
                    String gamemode = String.valueOf(player.getGameMode());
                    if (!gamemode.equals("CREATIVE")) {
                        event.setCancelled(true);
                        HubFunction.goToHub(player);
                    }
                } else {
                    Messages.noPerms(player);
                }
            }
        }
    }
}
