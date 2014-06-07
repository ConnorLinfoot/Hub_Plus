package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.NoPermsFunction;
import com.connorlinfoot.hubplus.HubPlus;
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
                    if (player.getAllowFlight()) {
                        player.setAllowFlight(false);
                        player.sendMessage("You can no longer fly!");
                    } else {
                        player.setAllowFlight(true);
                        player.sendMessage("You can now fly!");
                    }
                } else {
                    NoPermsFunction.noPerms(player);
                }
            }
        }

        return false;
    }
}
