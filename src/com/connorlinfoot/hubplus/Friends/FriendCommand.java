package com.connorlinfoot.hubplus.Friends;

import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FriendCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin instance = HubPlus.getInstance();
        if (label.equalsIgnoreCase("friend") || label.equalsIgnoreCase("f")) {
            if(instance.getConfig().getString("Friends Enabled").equals("true")) {
                Player player = (Player) sender;
                if (player.hasPermission("hubplus.friend")) {
                    if (args.length == 0) {
                        player.sendMessage(Messages.getChatColor() + "=== Friends Beta, Provided as part of Hub Plus ===");
                        player.sendMessage(Messages.getChatColor() + "/friend add <player> - Send a friend request");
                        player.sendMessage(Messages.getChatColor() + "/friend accept <player> - Accept friend request");
                        player.sendMessage(Messages.getChatColor() + "/friend deny <player> - Deny friend request");
                        player.sendMessage(Messages.getChatColor() + "/friend remove <player> - Remove a friend");
                        player.sendMessage(Messages.getChatColor() + "/friend list - View all your friends");
                    } else {
                        if (args[0].equalsIgnoreCase("add")) {
                            if (player.hasPermission("hubplus.friend.add")) {
                                if( args.length < 2){
                                    player.sendMessage(Messages.getChatColor() + "/friend add <player>");
                                } else {
                                    Player p = instance.getServer().getPlayer(args[1]);
                                    if (p == null) {
                                        player.sendMessage(Messages.getChatColor() + "Player not found!");
                                    } else {
                                        if( addFriend(player, p) ) {
                                            player.sendMessage(Messages.getChatColor() + "You have invited " + p.getDisplayName() + " to be your friend!");
                                            p.sendMessage(Messages.getChatColor() + " has invited you to be their friend!");
                                            p.sendMessage(Messages.getChatColor() + "Do /friend accept " + player.getDisplayName() + ", if you wish to add them!");
                                        }
                                    }
                                }
                            } else {
                                Messages.noPerms(player);
                            }
                        } else if (args[0].equalsIgnoreCase("list")) {
                            if (player.hasPermission("hubplus.friend.list")) {
                                player.sendMessage(Messages.getChatColor() + "=== My Friends ===");
                                player.sendMessage(Messages.getChatColor() + "- Friend1");
                                player.sendMessage(Messages.getChatColor() + "- Friend2");
                                player.sendMessage(Messages.getChatColor() + "- Friend3");
                                player.sendMessage(Messages.getChatColor() + "- Friend4");
                                player.sendMessage(Messages.getChatColor() + "- Friend5");
                            } else {
                                Messages.noPerms(player);
                            }
                        }
                    }
                } else {
                    Messages.noPerms(player);
                }
            }
        }
        return false;
    }

    private boolean addFriend(Player inviter, Player invited){
        if( !checkInvited(inviter, invited) ) {
            Statement statement = null;
            try {
                statement = HubPlus.getConnection().createStatement();
            } catch (SQLException ignored){ }
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            try {
                assert statement != null;
                statement.executeUpdate("INSERT INTO HubPlus_Friends_Requests (`friend1`,`friend2`,`date`) VALUES ('" + inviter.getUniqueId() + "','" + invited.getUniqueId() + "','" + timeStamp + "');");
                return true;
            } catch (SQLException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean checkInvited(Player inviter, Player invited){
        return false;
    }

}