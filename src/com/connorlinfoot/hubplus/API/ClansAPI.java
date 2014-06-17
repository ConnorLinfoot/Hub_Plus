package com.connorlinfoot.hubplus.API;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

/*
*
* This API is a work in progress!
* Features are not fully tested!
* You have been warned!
*
 */

public class ClansAPI extends JavaPlugin {

    public static String getClan (Player player){
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

    public static ArrayList<String> getClanPlayerNames(String name){
        Plugin instance = HubPlus.getInstance();
        Integer clanId = getClanID(name);
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res;
        ArrayList<String> members = new ArrayList<String>();
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans_Players WHERE clanId = '" + clanId + "';");
            Integer i = 0;
            while (res.next()) {
                if( i == 10 ) break;
                i++;
                UUID uuid = UUID.fromString(res.getString("UUID"));
                members.add(instance.getServer().getOfflinePlayer(uuid).getName());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    public static ArrayList<OfflinePlayer> getClanPlayers(String name){
        Plugin instance = HubPlus.getInstance();
        Integer clanId = getClanID(name);
        Statement statement = null;
        try {
            //statement = HubPlus.c.createStatement();
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res;
        ArrayList<OfflinePlayer> members = new ArrayList<OfflinePlayer>();
        try {
            assert statement != null;
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans_Players WHERE clanId = '" + clanId + "';");
            Integer i = 0;
            while (res.next()) {
                if( i == 10 ) break;
                i++;
                UUID uuid = UUID.fromString(res.getString("UUID"));
                OfflinePlayer p = instance.getServer().getOfflinePlayer(uuid);
                members.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    public static Integer getClanID(String name){
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

    public static boolean clanExists(String name){
        Statement statement = null;
        try {
            statement = HubPlus.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res;
        try {
            res = statement.executeQuery("SELECT * FROM HubPlus_Clans WHERE name = '" + name + "';");
            res.next();
            if( res.getString("name") == null ){
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}