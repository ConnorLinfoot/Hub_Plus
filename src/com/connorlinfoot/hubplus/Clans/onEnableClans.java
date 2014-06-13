package com.connorlinfoot.hubplus.Clans;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class onEnableClans extends JavaPlugin {

    public static boolean onEnableClans() {
        Plugin instance = HubPlus.getInstance();

        if (instance.getConfig().getString("Clans Enabled").equals("true")) {
            Connection c = HubPlus.getConnection();

            Statement statement = null;
            try {
                statement = c.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                assert statement != null;
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS HubPlus_Clans(clanID INT NOT NULL AUTO_INCREMENT,name VARCHAR(250), PRIMARY KEY (clanID), points INT, open INT, created DATETIME, owner VARCHAR(250) );");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS HubPlus_Clans_Players(UUID VARCHAR(250),clanID INT, PRIMARY KEY (UUID) );");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS HubPlus_Clans_Invites(UUID VARCHAR(250),clanID INT, PRIMARY KEY (UUID) );");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
