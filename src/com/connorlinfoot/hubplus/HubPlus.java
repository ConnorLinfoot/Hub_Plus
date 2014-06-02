package com.connorlinfoot.hubplus;

import com.connorlinfoot.hubplus.Clans.ClanCommand;
import com.connorlinfoot.hubplus.Commands.*;
import com.connorlinfoot.hubplus.Other.Metrics;
import com.connorlinfoot.hubplus.Other.MySQL;
import com.connorlinfoot.hubplus.Player.BloodEffect;
import com.connorlinfoot.hubplus.Player.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HubPlus extends JavaPlugin implements Listener {
    private static Plugin instance;

    String Database_Host = getConfig().getString("Database Host");
    String Database_User = getConfig().getString("Database User");
    String Database_Pass = getConfig().getString("Database Pass");
    String Database_Name = getConfig().getString("Database Name");
    PluginManager pluginmanager = Bukkit.getPluginManager();
    Plugin plugin = pluginmanager.getPlugin("Hub Plus");
    com.connorlinfoot.hubplus.Other.MySQL MySQL = new MySQL(plugin, Database_Host, "3306", Database_Name, Database_User, Database_Pass);
    static Connection c = null;

    public void onEnable() {
        instance = this;
        Server server = getServer();
        ArrayList hidden = new ArrayList();
        getConfig().set("PlayersHiding", hidden);
        saveConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        ConsoleCommandSender console = server.getConsoleSender();
        if(getConfig().getString( "Send Stats" ).equals(" true" ) ) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-( <-- Dat face doe
            }
        }

        if(instance.getConfig().getString("Clans Enabled").equals("true")) {
            c = MySQL.openConnection();

            if( c == null ){
                console.sendMessage(ChatColor.RED + "Hub Plus Failed To Start, Check Your MySQL Settings or Disable Clans!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
            Statement statement = null;
            try {
                statement = c.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
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

        Bukkit.getPluginManager().registerEvents(new ChatSensor(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new BloodEffect(), this);
        Bukkit.getPluginManager().registerEvents(new SignListener(), this);
        Bukkit.getPluginManager().registerEvents(new CustomHubCommand(), this);
        Bukkit.getPluginManager().registerEvents(new PluginsCommand(), this);

        // Include Command Classes
        getCommand("hub").setExecutor(new HubCommand()); // /hub command
        getCommand("hubplus").setExecutor(new HubPlusCommand()); // /HubPlus command
        getCommand("hp").setExecutor(new HubPlusCommand()); // /hp Command
        getCommand("broadcast").setExecutor(new BroadcastCommand()); // Broadcast Command
        getCommand("bc").setExecutor(new BroadcastCommand()); // /bc Command
        getCommand("clan").setExecutor(new ClanCommand()); // /clan command
        getCommand("fw").setExecutor(new FireworkCommand()); // /fw command
        getCommand("fly").setExecutor(new FlyCommand()); // /fly command
        getCommand("heal").setExecutor(new HealCommand()); // /heal command

        console.sendMessage(ChatColor.GREEN + "============= HUB PLUS =============");
        console.sendMessage(ChatColor.GREEN + "========== VERSION: 0.4b3 ==========");
        console.sendMessage(ChatColor.GREEN + "======== BY CONNOR LINFOOT! ========");
    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if( label.equalsIgnoreCase( "world" ) ) {
            Player player = (Player) sender;
            if( player.hasPermission( "hubplus.world" ) ) {
                String world = player.getWorld().getName().toLowerCase();
                player.sendMessage("Current World: " + ChatColor.RED + ChatColor.BOLD + world);
            } else {
                noPerms(player);
            }
        }

        return false;
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static Connection getConnection(){
        return c;
    }

    public static String noPerms(Player player){
        player.sendMessage(ChatColor.RED + "No Permission!");
        return "";
    }

}
