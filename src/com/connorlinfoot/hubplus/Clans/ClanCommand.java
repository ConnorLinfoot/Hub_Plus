package com.connorlinfoot.hubplus.Clans;

import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.HubPlus;
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
import java.util.UUID;

public class ClanCommand implements CommandExecutor {
    Plugin instance = HubPlus.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin instance = HubPlus.getInstance();
        if (label.equalsIgnoreCase("clan") || label.equalsIgnoreCase("c")) {
            if(instance.getConfig().getString("Clans Enabled").equalsIgnoreCase("true")) {
                Player player = (Player) sender;
                if (player.hasPermission("hubplus.clan")) {
                    if (args.length == 0) {
                        player.sendMessage(Messages.getChatColor() + "=== Clans Beta, Provided as part of Hub Plus ===");
                        player.sendMessage(Messages.getChatColor() + "/clan create <name> - Create a clan");
                        player.sendMessage(Messages.getChatColor() + "/clan join <name> - Join a clan");
                        player.sendMessage(Messages.getChatColor() + "/clan invite <player> - Invite player to clan");
                        player.sendMessage(Messages.getChatColor() + "/clan info - View information on your clan");
                        player.sendMessage(Messages.getChatColor() + "/clan members - View who's in your clan");
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
                                            player.sendMessage(Messages.getChatColor() + "That clan does not exist!");
                                        } else {
                                            // Clan Name is already set
                                            Integer points = getPoints(clanName); // get points
                                            String created = getCreated(clanName); // get created
                                            Integer members = getClanMembersCount(clanName); // get members
                                            player.sendMessage(Messages.getChatColor() + "====== Clan Info ======");
                                            player.sendMessage(Messages.getChatColor() + "Name: " + clanName);
                                            player.sendMessage(Messages.getChatColor() + "Points: " + points);
                                            player.sendMessage(Messages.getChatColor() + "Created: " + created);
                                            player.sendMessage(Messages.getChatColor() + "Members: " + members + "/" + instance.getConfig().getInt("Clan Member Limit"));
                                            // Things to be added soon!
                                            //player.sendMessage(com.connorlinfoot.hubplus.Global.ChatColor.getChatColor() + "Owner: Unknown!");
                                            //player.sendMessage(com.connorlinfoot.hubplus.Global.ChatColor.getChatColor() + "Level: Coming Soon!");
                                        }
                                    }
                                } else {
                                    String clanName = getClan(player);
                                    if (clanName == null) {
                                        player.sendMessage(Messages.getChatColor() + "You are not part of a clan!");
                                    } else {
                                        String owner = getOwner(clanName);
                                        String uuid = String.valueOf(player.getUniqueId());
                                        Integer points = getPoints(clanName); // get points
                                        String created = getCreated(clanName); // get created
                                        Integer members = getClanMembersCount(clanName); // get members
                                        player.sendMessage(Messages.getChatColor() + "====== Clan Info ======");
                                        player.sendMessage(Messages.getChatColor() + "Name: " + clanName);
                                        player.sendMessage(Messages.getChatColor() + "Points: " + points);
                                        player.sendMessage(Messages.getChatColor() + "Created: " + created);
                                        player.sendMessage(Messages.getChatColor() + "Members: " + members + "/" + instance.getConfig().getInt("Clan Member Limit"));

                                        if (owner.equalsIgnoreCase(uuid)) {
                                            player.sendMessage(Messages.getChatColor() + "You are the clan owner!");
                                        }
                                        //player.sendMessage(com.connorlinfoot.hubplus.Global.ChatColor.getChatColor() + "Level: Coming Soon!");
                                    }
                                }
                            } else {
                                Messages.noPerms(player);
                            }
                        } else if (args[0].equalsIgnoreCase("leave")) {
                            if (args.length == 2 && args[1].equalsIgnoreCase("confirm")){
                                String clanName = getClan(player);
                                leaveClan(player);
                                String owner = getOwner(clanName);
                                String uuid = String.valueOf(player.getUniqueId());
                                if (owner.equalsIgnoreCase(uuid)) {
                                    deleteClan(clanName);
                                    player.sendMessage(Messages.getChatColor() + "The clan " + clanName + " has been deleted!");
                                }
                            } else{
                                String clanName = getClan(player);
                                if (clanName == null) {
                                    player.sendMessage(Messages.getChatColor() + "You are not part of a clan!");
                                } else {
                                    // Check if user leaving is Clan owner
                                    String owner = getOwner(clanName);
                                    String uuid = String.valueOf(player.getUniqueId());
                                    if (owner.equalsIgnoreCase(uuid)) {
                                        player.sendMessage(Messages.getChatColor() + "You are the owner of " + clanName + ", confirming will DELETE your clan.");
                                        player.sendMessage(Messages.getChatColor() + "Do '/clan leave confirm' if you wish to continue!");
                                    } else {
                                        player.sendMessage(Messages.getChatColor() + "If you are you sure you want to leave the clan " + clanName + ", do /clan leave confirm");
                                    }
                                }
                            }
                        } else if (args[0].equalsIgnoreCase("create")) {
                            if( player.hasPermission("hubplus.clan.create")) {
                                if (args.length == 1) {
                                    player.sendMessage(Messages.getChatColor() + "/clan create <name>");
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
                                Messages.noPerms(player);
                            }
                        } else if( args[0].equalsIgnoreCase("join")){
                            if( player.hasPermission("hubplus.clan.join")) {
                                if (args.length == 1) {
                                    player.sendMessage(Messages.getChatColor() + "/clan join <name>");
                                } else {
                                    //String name = args[1];
                                    StringBuilder builder = new StringBuilder();
                                    Integer i = 0;
                                    for (String value : args) {
                                        if( i != 0 ) {
                                            builder.append(value).append(" ");
                                        }
                                        i = i + 1;
                                    }
                                    String name = builder.toString();
                                    joinClan(name, player, 0);
                                }
                            } else {
                                Messages.noPerms(player);
                            }
                        } else if( args[0].equalsIgnoreCase("invite") ){
                            if( args.length == 1){
                                player.sendMessage(Messages.getChatColor() + "/clan invite <username>");
                            } else {
                                String clanName = getClan(player);
                                String owner = getOwner(clanName);
                                String uuid = String.valueOf(player.getUniqueId());
                                if (clanName == null || !owner.equalsIgnoreCase(uuid)) {
                                    player.sendMessage(Messages.getChatColor() + "You are not part of a clan or the owner of the clan!");
                                } else {
                                    Player p = instance.getServer().getPlayer(args[1]);
                                    if (p == null) {
                                        player.sendMessage(Messages.getChatColor() + "Player not found!");
                                    } else {
                                        if(player.getName().equalsIgnoreCase(p.getName())){
                                            player.sendMessage(Messages.getChatColor() + "You can't invite yourself!");
                                        } else {
                                            inviteClan(clanName, p);
                                            player.sendMessage(Messages.getChatColor() + "You invited " + p.getDisplayName() + " to your clan!" );
                                            p.sendMessage(Messages.getChatColor() + player.getDisplayName() + " has invited you to join there clan, " + clanName);
                                        }
                                    }
                                }
                            }
                        } else if( args[0].equalsIgnoreCase("settings") ){
                            if( args.length == 1) {
                                player.sendMessage(Messages.getChatColor() + "/clan settings owner <Player> - Change Clan Owner");
                            } else if( args.length == 2 ){
                                if(args[1].equalsIgnoreCase("owner")){
                                    player.sendMessage(Messages.getChatColor() + "/clan settings owner <Player>");
                                }
                            } else if( args.length == 3 ){
                                if(args[1].equalsIgnoreCase("owner")){
                                    // Change to new owner
                                    String clanName = getClan(player);
                                    String owner = getOwner(clanName);
                                    String uuid = String.valueOf(player.getUniqueId());
                                    if (clanName == null || !owner.equalsIgnoreCase(uuid)) {
                                        player.sendMessage(Messages.getChatColor() + "You are not part of a clan or the owner of the clan!");
                                    } else {
                                        Player p = instance.getServer().getPlayer(args[2]);
                                        if (p == null) {
                                            player.sendMessage(Messages.getChatColor() + "Player not found!");
                                        } else {
                                            if( !getClan(p).equalsIgnoreCase(clanName) ){
                                                player.sendMessage("Player must be in your clan to be owner!");
                                            } else {
                                                // Owner Change here
                                                String newuuid = String.valueOf(p.getUniqueId());
                                                setOwner(clanName, newuuid);
                                                player.sendMessage(Messages.getChatColor() + "Owner is now " + p.getDisplayName());
                                            }
                                        }
                                    }
                                }
                            }
                        } else if( args[0].equalsIgnoreCase("members") ){
                            String clanName = getClan(player);
                            if (clanName == null) {
                                player.sendMessage(Messages.getChatColor() + "You are not part of a clan!");
                            } else {
                                listMembers(clanName, player);
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

    private Boolean listMembers(String clanName, Player p){
        Integer clanId = getClanID(clanName);
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res;
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans_Players WHERE clanId = '" + clanId + "';");
            Integer i = 0;
            while (res.next()) {
                if( i == 10 ) break;
                i++;
                UUID uuid = UUID.fromString(res.getString("UUID"));
                p.sendMessage(Messages.getChatColor() + "- " + instance.getServer().getOfflinePlayer(uuid).getName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
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
            player.sendMessage(Messages.getChatColor() + "You have created the clan " + name);
            joinClan(name, player, 1);
        } else {
            player.sendMessage(Messages.getChatColor() + "You are already in a clan! Do /clan leave!");
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
                    player.sendMessage(Messages.getChatColor() + "This clan has reached the limit of " + instance.getConfig().getInt("Clan Member Limit") + " players!");
                } else {
                    if (created == 0) {
                        Integer open = getOpen(name);
                        if (open == 0) {
                            String iclanname = getInvitedClan(player);
                            if (iclanname != null && iclanname.equalsIgnoreCase(name)) {
                                // Player is invited, allow join!
                                try {
                                    assert statement != null;
                                    statement.executeUpdate("INSERT INTO HubPlus_Clans_Players (`UUID`,`clanID`) VALUES ('" + uuid + "','" + getClanID(name) + "');");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                deleteClanInvites(player);
                                player.sendMessage(Messages.getChatColor() + "You have joined the clan " + name);
                            } else {
                                player.sendMessage(Messages.getChatColor() + "You must be invited to join this clan!");
                            }
                        } else {
                            // Clan is open so player can join!
                            try {
                                assert statement != null;
                                statement.executeUpdate("INSERT INTO HubPlus_Clans_Players (`UUID`,`clanID`) VALUES ('" + uuid + "','" + getClanID(name) + "');");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            player.sendMessage(Messages.getChatColor() + "You have joined the clan " + name);
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
                player.sendMessage(Messages.getChatColor() + "You are already in a clan! Do /clan leave!");
            }
        } else {
            player.sendMessage(Messages.getChatColor() + name + " doesn't exist!");
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
            player.sendMessage(Messages.getChatColor() + name + " doesn't exist!");
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

        player.sendMessage(Messages.getChatColor() + "You have left the clan!");

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

        player.sendMessage(Messages.getChatColor() + "You have left the clan!");

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

    private Boolean setOwner (String name, String uuid){
        Statement statement = null;
        try {
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            assert statement != null;
            statement.executeUpdate("UPDATE HubPlus_Clans SET owner = " + uuid + " WHERE name = '" + name + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}