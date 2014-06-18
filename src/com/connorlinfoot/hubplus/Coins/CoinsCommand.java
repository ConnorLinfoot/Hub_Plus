package com.connorlinfoot.hubplus.Coins;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/*
 *
 * Coins? What!
 *
 */

public class CoinsCommand implements CommandExecutor {
    Plugin instance = HubPlus.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin instance = HubPlus.getInstance();



        return false;
    }
}
