package com.connorlinfoot.hubplus.Friends;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class onEnableFriends extends JavaPlugin {

    public static boolean onEnableFriends() {
        Plugin instance = HubPlus.getInstance();

        if(instance.getConfig().getString("Friends Enabled").equals("true")){
            Connection c = HubPlus.getConnection();
            Statement statement = null;
            try {
                statement = c.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                assert statement != null;
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS HubPlus_Friends(friendID INT ,friend1 VARCHAR(250), friend2 VARCHAR(250), PRIMARY KEY (friendID), date DATETIME );");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Friend 1 is person sending invite
            try {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS HubPlus_Friends_Requests(friendID INT ,friend1 VARCHAR(250), friend2 VARCHAR(250), PRIMARY KEY (friendID), date DATETIME );");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
