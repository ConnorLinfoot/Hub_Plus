package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.HubFunction;
import com.connorlinfoot.hubplus.Global.NoPermsFunction;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HubCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin instance = HubPlus.getInstance();
        if( label.equalsIgnoreCase( "hub" ) ){
            if( !(sender instanceof Player) ) {
                sender.sendMessage(ChatColor.RED + "Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if( player.hasPermission( "hubplus.hub" ) ) {
                    if (args.length == 0) {
                        HubFunction.goToHub(player);
                    } else {
                        player.sendMessage("Please use /hubplus help");
                    }
                } else {
                    NoPermsFunction.noPerms(player);
                }
            }
        }

        return false;
    }
}
