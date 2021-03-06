package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FlyCommand implements CommandExecutor{
    private Plugin instance = HubPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if( label.equalsIgnoreCase( "fly" ) ){
            if( !(sender instanceof Player) ) {
                sender.sendMessage("Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if( player.hasPermission("hubplus.fly.self") ) {
                    if(!(player.getGameMode() == GameMode.CREATIVE)) { // Check if player is in creative or not
                        if (player.getAllowFlight()) {
                            player.setAllowFlight(false);
                            player.sendMessage("You can no longer fly!");
                        } else {
                            player.setAllowFlight(true);
                            player.sendMessage("You can now fly!");
                        }
                    } else {
                        player.setAllowFlight(true); // Set to true just in case they are not able to fly
                        player.sendMessage("You are in creative!");
                    }
                } else {
                    Messages.noPerms(player);
                }
            }
        }

        return false;
    }
}
