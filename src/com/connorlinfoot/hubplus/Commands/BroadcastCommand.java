package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.Global.UsefulFunctions;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BroadcastCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin instance = HubPlus.getInstance();
        if (label.equalsIgnoreCase("broadcast") || label.equalsIgnoreCase("bc")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if (player.hasPermission("hubplus.broadcast")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Please use the command by doing /broadcast <message>");
                    } else {
                        //String msg = Arrays.toString(args);
                        StringBuilder builder = new StringBuilder();
                        for (String value : args) {
                            builder.append(value).append(" ");
                        }
                        String msg = builder.toString();
                        msg = UsefulFunctions.textColors(msg);
                        instance.getServer().broadcastMessage(ChatColor.GOLD + instance.getConfig().getString("Broadcast Prefix") + " " + ChatColor.RESET + msg);
                        //player.sendMessage("");
                    }
                } else {
                    Messages.noPerms(player);
                }
            }


        }
        return false;
    }

    /* Moved Text Color to Global.UsefulFunctions */
}
