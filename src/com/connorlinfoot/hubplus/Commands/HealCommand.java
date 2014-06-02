package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HealCommand implements CommandExecutor{
    private Plugin instance = HubPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if( label.equalsIgnoreCase( "heal" ) ){
            if( !(sender instanceof Player) ) {
                sender.sendMessage("Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if( player.hasPermission("hubplus.heal.self") ) {
                    player.setHealth(20);
                    Double health = Double.valueOf(20);
                    player.setHealth(health);
                    player.setFoodLevel(20);
                    player.sendMessage("You have been healed!");
                } else {
                    HubPlus.noPerms(player);
                }
            }
        }

        return false;
    }
}
