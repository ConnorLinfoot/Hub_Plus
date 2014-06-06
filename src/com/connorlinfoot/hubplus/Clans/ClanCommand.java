package com.connorlinfoot.hubplus.Clans;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin instance = HubPlus.getInstance();
        if (label.equalsIgnoreCase("clan") || label.equalsIgnoreCase("clans")) {
            if(instance.getConfig().getString("Clans Enabled").equals("true")) {
                Player player = (Player) sender;
                if (player.hasPermission("hubplus.clan")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.AQUA + "Clans Beta, Provided as part of Hub Plus.");
                    } else {
                        if (args[0].equalsIgnoreCase("info")) {
                            if (player.hasPermission("hubplus.clan.info")) {
                                if( args.length >= 2 ){
                                    if (player.hasPermission("hubplus.clan.info.other")) {
                                        //String clanName = args[1];
                                        StringBuilder builder = new StringBuilder();
                                        Integer i = 0;
                                        for (String value : args) {
                                            if( i != 0 ) {
                                                builder.append(value).append(" ");
                                            }
                                            i = i + 1;
                                        }
                                        String clanName = builder.toString();
                                        if (clanName == null || getPoints(clanName) == null) {
                                            player.sendMessage(ChatColor.AQUA + "That clan does not exist!");
                                        } else {
                                            player.sendMessage(ChatColor.AQUA + "====== Clan Info ======");
                                            player.sendMessage(ChatColor.AQUA + "Name: " + clanName);
                                            player.sendMessage(ChatColor.AQUA + "Points: " + getPoints(clanName));
                                            player.sendMessage(ChatColor.AQUA + "Created: " + getCreated(clanName));
                                            player.sendMessage(ChatColor.AQUA + "Members: " + getClanMembersCount(clanName) + "/" + instance.getConfig().getInt("Clan Member Limit"));
                                            // Things to be added soon!
                                            //player.sendMessage(ChatColor.AQUA + "Owner: Unknown!");
                                            //player.sendMessage(ChatColor.AQUA + "Level: Coming Soon!");
                                        }
                                    }
                                } else {
                                    String clanName = getClan(player);
                                    if (clanName == null) {
                                        player.sendMessage(ChatColor.AQUA + "You are not part of a clan!");
                                    } else {
                                        String owner = getOwner(clanName);
                                        String uuid = String.valueOf(player.getUniqueId());
                                        player.sendMessage(ChatColor.AQUA + "====== Clan Info ======");
                                        player.sendMessage(ChatColor.AQUA + "Name: " + clanName);
                                        player.sendMessage(ChatColor.AQUA + "Points: " + getPoints(clanName));
                                        player.sendMessage(ChatColor.AQUA + "Created: " + getCreated(clanName));
                                        player.sendMessage(ChatColor.AQUA + "Members: " + getClanMembersCount(clanName) + "/" + instance.getConfig().getInt("Clan Member Limit"));

                                        if (owner.equals(uuid)) {
                                            player.sendMessage(ChatColor.AQUA + "You are the clan owner!");
                                        }
                                        //player.sendMessage(ChatColor.AQUA + "Level: Coming Soon!");
                                    }
                                }
                            } else {
                                HubPlus.noPerms(player);
                            }
                        } else if (args[0].equalsIgnoreCase("leave")) {
                            if (args.length == 2 && args[1].equalsIgnoreCase("confirm")){
                                String clanName = getClan(player);
                                leaveClan(player);
                                String owner = getOwner(clanName);
                                String uuid = String.valueOf(player.getUniqueId());
                                if (owner.equals(uuid)) {
                                    deleteClan(clanName);
                                    player.sendMessage(ChatColor.AQUA + "The clan " + clanName + " has been deleted!");
                                }
                            } else{
                                String clanName = getClan(player);
                                if (clanName == null) {
                                    player.sendMessage(ChatColor.AQUA + "You are not part of a clan!");
                                } else {
                                    // Check if user leaving is Clan owner
                                    String owner = getOwner(clanName);
                                    String uuid = String.valueOf(player.getUniqueId());
                                    if (owner.equals(uuid)) {
                                        player.sendMessage(ChatColor.AQUA + "You are the owner of " + clanName + ", confirming will DELETE your clan.");
                                        player.sendMessage(ChatColor.AQUA + "Do '/clan leave confirm' if you wish to continue!");
                                    } else {
                                        player.sendMessage(ChatColor.AQUA + "If you are you sure you want to leave the clan " + clanName + ", do /clan leave confirm");
                                    }
                                }
                            }
                        } else if (args[0].equalsIgnoreCase("create")) {
                            if( player.hasPermission("hubplus.clan.create")) {
                                if (args.length == 1) {
                                    player.sendMessage(ChatColor.AQUA + "/clan create <name>");
                                } else {
                                    StringBuilder builder = new StringBuilder();
                                    Integer i = 0;
                                    for (String value : args) {
                                        if( i != 0 ) {
                                            builder.append(value).append(" ");
                                        }
                                        i = i + 1;
                                    }
                                    String name = builder.toString();
                                    createClan(name, player);
                                    Integer points = instance.getConfig().getInt("Clan Starting Points");
                                    givePoints(player, points); // Gives points on clan creation!
                                }
                            } else {
                                HubPlus.noPerms(player);
                            }
                        } else if( args[0].equalsIgnoreCase("join")){
                            if( player.hasPermission("hubplus.clan.join")) {
                                if (args.length == 1) {
                                    player.sendMessage(ChatColor.AQUA + "/clan join <name>");
                                } else {
                                    String name = args[1];
                                    joinClan(name, player, 0);
                                }
                            } else {
                                HubPlus.noPerms(player);
                            }
                        } else if( args[0].equalsIgnoreCase("invite") ){
                            if( args.length == 1){
                                player.sendMessage(ChatColor.AQUA + "/clan invite <username>");
                            } else {
                                String clanName = getClan(player);
                                String owner = getOwner(clanName);
                                String uuid = String.valueOf(player.getUniqueId());
                                if (clanName == null || !owner.equals(uuid)) {
                                    player.sendMessage(ChatColor.AQUA + "You are not part of a clan or the owner of the clan!");
                                } else {
                                    Player p = instance.getServer().getPlayer(args[1]);
                                    if (p == null) {
                                        player.sendMessage(ChatColor.AQUA + "Player not found!");
                                    } else {
                                        if(player.getName().equals(p.getName())){
                                            player.sendMessage(ChatColor.AQUA + "You can't invite yourself!");
                                        } else {
                                            inviteClan(clanName, p);
                                            player.sendMessage(ChatColor.AQUA + "You invited " + p.getDisplayName() + " to your clan!" );
                                            p.sendMessage(ChatColor.AQUA + player.getDisplayName() + " has invited you to join there clan, " + clanName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    HubPlus.noPerms(player);
                }
            }
        }
        return false;
    }

    private String createClan(String name, Player player){
        //MySQL Statement for adding clan to DB.
        String clanName = getClan(player);
        if( clanName == null ) {
            Statement statement = null;
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            String uuid = String.valueOf(player.getUniqueId());
            try {
                statement = HubPlus.getConnection().createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                assert statement != null;
                statement.executeUpdate("INSERT INTO HubPlus_Clans (`name`,`points`,`open`,`created`,`owner`) VALUES ('" + name + "',0,0,'" + timeStamp + "','" + uuid + "');");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            player.sendMessage(ChatColor.AQUA + "You have created the clan " + name);
            joinClan(name, player, 1);
        } else {
            player.sendMessage(ChatColor.AQUA + "You are already in a clan! Do /clan leave!");
        }
        return "";
    }

    private String joinClan(String name, Player player, Integer created){
        Plugin instance = HubPlus.getInstance();
        //MySQL Statement for adding player to clan
        Statement statement = null;
        try {
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String uuid = String.valueOf(player.getUniqueId());
        if( checkClan(name) ) {
            String clanName = getClan(player);
            if( clanName == null ) {
                if( checkClanMembersCount(name) ){
                    player.sendMessage(ChatColor.AQUA + "This clan has reached the limit of " + instance.getConfig().getInt("Clan Member Limit") + " players!");
                } else {
                    if (created == 0) {
                        Integer open = getOpen(name);
                        if (open == 0) {
                            String iclanname = getInvitedClan(player);
                            if (iclanname != null && iclanname.equals(name)) {
                                // Player is invited, allow join!
                                try {
                                    assert statement != null;
                                    statement.executeUpdate("INSERT INTO HubPlus_Clans_Players (`UUID`,`clanID`) VALUES ('" + uuid + "','" + getClanID(name) + "');");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                deleteClanInvites(player);
                                player.sendMessage(ChatColor.AQUA + "You have joined the clan " + name);
                            } else {
                                player.sendMessage(ChatColor.AQUA + "You must be invited to join this clan!");
                            }
                        } else {
                            // Clan is open so player can join!
                            try {
                                assert statement != null;
                                statement.executeUpdate("INSERT INTO HubPlus_Clans_Players (`UUID`,`clanID`) VALUES ('" + uuid + "','" + getClanID(name) + "');");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(ChatColor.AQUA + "You have joined the clan " + name);
                        }
                    } else {
                        // Clan has just been created so must be adding owner!
                        try {
                            assert statement != null;
                            statement.executeUpdate("INSERT INTO HubPlus_Clans_Players (`UUID`,`clanID`) VALUES ('" + uuid + "','" + getClanID(name) + "');");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.AQUA + "You are already in a clan! Do /clan leave!");
            }
        } else {
            player.sendMessage(ChatColor.AQUA + name + " doesn't exist!");
        }
        return "";
    }

    private String inviteClan(String name, Player player){
        //MySQL Statement for adding player to clan
        Statement statement = null;
        try {
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String uuid = String.valueOf(player.getUniqueId());
        if( checkClan(name) ) {
            String clanName = getClan(player);
            if( clanName == null ) {
                try {
                    assert statement != null;
                    statement.executeUpdate("INSERT INTO HubPlus_Clans_Invites (`UUID`,`clanID`) VALUES ('" + uuid + "','" + getClanID(name) + "');");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            player.sendMessage(ChatColor.AQUA + name + " doesn't exist!");
        }
        return "";
    }

    private String deleteClanInvites(Player player){
        String uuid = String.valueOf(player.getUniqueId());
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert statement != null;
            statement.executeUpdate("DELETE FROM HubPlus_Clans_Invites WHERE uuid = '" + uuid + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        player.sendMessage(ChatColor.AQUA + "You have left the clan!");

        return "";
    }

    private Integer getClanID(String name){
        //MySQL Statement to get clan ID
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res;
        Integer clanID = null;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans WHERE name = '" + name + "';");

            res.next();
            if( res.getString("clanID") == null ){
                clanID = 0;
            } else {
                clanID = res.getInt("clanID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clanID;
    }

    private boolean checkClan(String name){
        Integer clanID = getClanID(name);
        return clanID != null;
    }

    private String getClan(Player player){
        String uuid = String.valueOf(player.getUniqueId());
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res;
        Integer clanID = null;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans_Players WHERE uuid = '" + uuid + "';");
            res.next();
            if( res.getString("clanID") == null ){
                clanID = 0;
            } else {
                clanID = res.getInt("clanID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String clanName = null;
        try {
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans WHERE clanID = '" + clanID + "';");
            res.next();
            if( res.getString("name") == null ){
                clanName = "";
            } else {
                clanName = res.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clanName;
    }

    private String leaveClan(Player player){
        String uuid = String.valueOf(player.getUniqueId());
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert statement != null;
            statement.executeUpdate("DELETE FROM HubPlus_Clans_Players WHERE uuid = '" + uuid + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        player.sendMessage(ChatColor.AQUA + "You have left the clan!");

        return "";
    }

    private String deleteClan(String name){
        Integer clanID = getClanID(name);
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            assert statement != null;
            statement.executeUpdate("DELETE FROM HubPlus_Clans_Players WHERE clanID = '" + clanID + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate("DELETE FROM HubPlus_Clans WHERE clanID = '" + clanID + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    private Integer getPoints(String name){
        //MySQL Statement to get clan points
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res;
        Integer points = null;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans WHERE name = '" + name + "';");
            res.next();
            if( res.getString("points") == null ){
                points = 0;
            } else {
                points = res.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return points;
    }

    private Boolean givePoints(Player player, Integer points){
        // MySQL to give points to the players clan
        String clanname = getClan(player);
        Integer currentpoints = getPoints(clanname);
        Integer newpoints = currentpoints + points;

        Statement statement = null;
        try {
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            assert statement != null;
            statement.executeUpdate("UPDATE HubPlus_Clans SET points = " + newpoints + " WHERE name = '" + clanname + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private Boolean takePoints(Player player, Integer points){
        // MySQL to take points from the players clan
        String clanname = getClan(player);
        Integer currentpoints = getPoints(clanname);
        Integer newpoints = currentpoints - points;

        if( points > currentpoints ){

        } else {
            Statement statement = null;
            try {
                statement = HubPlus.getConnection().createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                assert statement != null;
                statement.executeUpdate("UPDATE HubPlus_Clans SET points = " + newpoints + " WHERE name = '" + clanname + "';");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private Boolean listClans(Player player){
        // Adding soon

        return false;
    }

    private String getCreated(String name){
        //MySQL Statement to get clan points
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res;
        String created = null;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans WHERE name = '" + name + "';");
            res.next();
            if( res.getString("created") == null ){
                created = "";
            } else {
                created = res.getString("created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return created;
    }

    private String getOwner(String name){
        //MySQL Statement to get clan points
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res;
        String owner = null;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans WHERE name = '" + name + "';");
            res.next();
            if( res.getString("owner") == null ){
                owner = "";
            } else {
                owner = res.getString("owner");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return owner;
    }

    private Integer getOpen(String name){
        //MySQL Statement to get clan points
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res;
        Integer open = null;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans WHERE name = '" + name + "';");
            res.next();
            if( res.getString("open") == null ){
                open = 0;
            } else {
                open = res.getInt("open");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return open;
    }

    private String getInvitedClan(Player player){
        String uuid = String.valueOf(player.getUniqueId());
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res;
        Integer clanID = null;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans_Invites WHERE uuid = '" + uuid + "';");
            res.next();
            if( res.getString("clanID") == null ){
                clanID = 0;
            } else {
                clanID = res.getInt("clanID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String clanName = null;
        try {
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans WHERE clanID = '" + clanID + "';");
            res.next();
            if( res.getString("name") == null ){
                clanName = "";
            } else {
                clanName = res.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clanName;
    }

    private Integer getClanMembersCount(String name){
        Integer clanID = getClanID(name);

        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res;
        Integer amount = null;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans_Players WHERE clanID = '" + clanID + "';");
            res.last();
            amount = res.getRow();
            res.beforeFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return amount;
    }

    private Boolean checkClanMembersCount(String name){
        Plugin instance = HubPlus.getInstance();
        Integer allowed = instance.getConfig().getInt("Clan Member Limit");
        Integer current = getClanMembersCount(name);
        return current.equals(allowed);
    }
}