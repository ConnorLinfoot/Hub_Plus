package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

public class HubPlusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin instance = HubPlus.getInstance();
        if( label.equalsIgnoreCase( "hubplus" ) || label.equalsIgnoreCase( "hp" ) ){
            if( !(sender instanceof Player) ) {
                sender.sendMessage(ChatColor.RED + "Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if( player.hasPermission( "hubplus.admin" ) ) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.GREEN + "====== HUB PLUS ======");
                        player.sendMessage(ChatColor.GREEN + "Version: " + instance.getDescription().getVersion() );
                        player.sendMessage(ChatColor.GREEN + "Created By: " + ChatColor.GOLD + "" + ChatColor.MAGIC + "CL" + ChatColor.RESET + "" + ChatColor.GREEN + "Connor Linfoot" + ChatColor.GOLD + "" + ChatColor.MAGIC + "CL");
                    } else if( args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
                        player.sendMessage(ChatColor.GREEN + "====== HUB PLUS ======");
                        player.sendMessage(ChatColor.GREEN + "/hubplus sethub - Used to set the 'hub' point.");
                        player.sendMessage(ChatColor.GREEN + "/hubplus reload - Reload Hub Plus Config.");
                        player.sendMessage(ChatColor.GREEN + "/hubplus stats <enable/disable> - Enable/Disable MC Stats.");
                        player.sendMessage(ChatColor.GREEN + "/hubplus clockcooldown <seconds> - Set Clock Cooldown.");
                        player.sendMessage(ChatColor.GREEN + "/hub - Go to Hub.");
                        player.sendMessage(ChatColor.GREEN + "/broadcast <message> - Send a broadcast to the server.");
                        player.sendMessage(ChatColor.GREEN + "/hubplus help - Shows this screen.");
                        player.sendMessage(ChatColor.GREEN + "/fw - Launches a firework.");
                        // Work on adding more or making this better?
                    } else if( args[0].equalsIgnoreCase( "sethub" ) ) {
                        if( player.hasPermission( "hubplus.admin" ) ) {
                            Location location = player.getLocation();
                            double xlocation = location.getX();
                            double ylocation = location.getY();
                            double zlocation = location.getZ();
                            double locationYaw = location.getYaw();
                            double locationPitch = location.getPitch();
                            String hubworld = location.getWorld().getName();
                            instance.getConfig().set("Hub TP X", xlocation);
                            instance.getConfig().set("Hub TP Y", ylocation);
                            instance.getConfig().set("Hub TP Z", zlocation);
                            instance.getConfig().set("Hub TP Yaw", locationYaw);
                            instance.getConfig().set("Hub TP Pitch", locationPitch);
                            instance.getConfig().set("Hub TP World", hubworld);
                            instance.saveConfig();
                            player.sendMessage(ChatColor.GREEN + "Hub Point Set!");
                        } else {
                            Messages.noPerms(player);
                        }
                    } else if( args[0].equalsIgnoreCase( "reload" ) ){
                        if( player.hasPermission( "hubplus.admin" ) ) {
                            instance.reloadConfig();
                            player.sendMessage( "Hub Plus Config Reloaded!" );
                        } else {
                            Messages.noPerms(player);
                        }
                    } else if( args[0].equalsIgnoreCase( "stats" ) ){
                        if( player.hasPermission( "hubplus.admin" ) ) {
                            if( args.length == 2 && args[1].equals( "enable" ) ) {
                                // Enable stats
                                instance.getConfig().set( "Send Stats", "true" );
                                player.sendMessage( ChatColor.GREEN + "You have enabled MC Stats!" );
                            } else if( args.length == 2 && args[1].equals( "disable" ) ){
                                // Disable Stats
                                instance.getConfig().set( "Send Stats", "false" );
                                player.sendMessage( ChatColor.GREEN + "You have disabled MC Stats!" );
                            } else {
                                // Show Help Message
                                player.sendMessage( ChatColor.GREEN + "Please use the command /hubplus stats <enable/disable>" );
                            }
                        } else {
                            Messages.noPerms(player);
                        }
                    } else if( args[0].equalsIgnoreCase( "world" ) ){
                        if( player.hasPermission( "hubplus.admin" ) ) {
                            String world = player.getWorld().getName().toLowerCase();
                            player.sendMessage("Current World: " + ChatColor.RED + ChatColor.BOLD + world);
                        }
                    } else if( args[0].equalsIgnoreCase( "setworld" ) ){
                        if( player.hasPermission( "hubplus.admin" ) ) {
                            Location loc = player.getLocation();
                            World world = loc.getWorld();
                            String worldname = world.getName();
                            instance.getConfig().set("Hub World", worldname);
                            instance.saveConfig();
                            player.sendMessage( ChatColor.GREEN + "You have updated the Hub world too " + worldname );
                        }
                    } else if( args[0].equalsIgnoreCase( "hideplayers" ) ){
                        if( player.hasPermission( "hubplus.admin" ) ) {
                            if( args.length == 1){
                                // Show info on commands here
                            } else {
                                if( args[1].equalsIgnoreCase("cooldown") ){
                                    if( args.length == 3 ) {
                                        Integer count = Integer.valueOf(args[2]);
                                        instance.getConfig().set( "Hide Players Cooldown", count );
                                        instance.saveConfig();
                                        player.sendMessage( ChatColor.GREEN + "Clock Cooldown is now set to " + count );
                                    } else {
                                        // Show Help Message
                                        player.sendMessage( ChatColor.GREEN + "Please use the command /hubplus hideplayers cooldown <seconds>" );
                                    }
                                } else if( args[1].equalsIgnoreCase("item") ){
                                    // Change item from hand here
                                    ItemStack item = player.getItemInHand(); // Get Item from hand
                                    MaterialData itemdata = item.getData(); // Get Item Data
                                    Material itemmaterial = itemdata.getItemType(); // Get Item Material
                                    String material = String.valueOf(itemmaterial); // Material to string
                                    if( material == null || material.equalsIgnoreCase("AIR")){ // Check if hand is empty
                                        player.sendMessage("Please hold the item you wish to set in your hand!"); // If hand empty alert the user
                                    } else {
                                        instance.getConfig().set("Hide Players Item", material); // Store in config
                                        instance.saveConfig(); // Save config
                                        player.sendMessage("You have updated the hide players item to " + material); // Alert user item has been changed
                                    }
                                }
                            }
                            // Will allow user to edit hide players features such as item, text (Maybe) and cooldown will be moved here
                        }
                    }
                } else {
                    Messages.noPerms(player);
                }
            }
        }

        return false;
    }
}
