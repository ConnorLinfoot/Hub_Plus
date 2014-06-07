package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.NoPermsFunction;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GameModeCommands  implements CommandExecutor {
    private Plugin instance = HubPlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if( label.equalsIgnoreCase( "gmc" ) ){
            if( !(sender instanceof Player) ) {
                sender.sendMessage("Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if( player.hasPermission("hubplus.gamemode.creative") ) {
                    player.setGameMode(GameMode.CREATIVE);
                } else {
                    NoPermsFunction.noPerms(player);
                }
            }
        } else if( label.equalsIgnoreCase( "gms" ) ){
            if( !(sender instanceof Player) ) {
                sender.sendMessage("Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if( player.hasPermission("hubplus.gamemode.survival") ) {
                    player.setGameMode(GameMode.SURVIVAL);
                } else {
                    NoPermsFunction.noPerms(player);
                }
            }
        } else if( label.equalsIgnoreCase( "gma" ) ){
            if( !(sender instanceof Player) ) {
                sender.sendMessage("Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if( player.hasPermission("hubplus.gamemode.adventure") ) {
                    player.setGameMode(GameMode.ADVENTURE);
                } else {
                    NoPermsFunction.noPerms(player);
                }
            }
        }

        return false;
    }
}
