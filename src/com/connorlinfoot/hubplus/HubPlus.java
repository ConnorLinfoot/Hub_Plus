package com.connorlinfoot.hubplus;

import com.connorlinfoot.hubplus.Clans.ClanCommand;
import com.connorlinfoot.hubplus.Clans.onEnableClans;
import com.connorlinfoot.hubplus.Commands.*;
import com.connorlinfoot.hubplus.Friends.FriendCommand;
import com.connorlinfoot.hubplus.Friends.onEnableFriends;
import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.Other.Metrics;
import com.connorlinfoot.hubplus.Other.MySQL;
import com.connorlinfoot.hubplus.Player.*;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HubPlus extends JavaPlugin implements Listener {
    private static Plugin instance;

    String Database_Host = getConfig().getString("Database Host");
    String Database_User = getConfig().getString("Database User");
    String Database_Pass = getConfig().getString("Database Pass");
    String Database_Name = getConfig().getString("Database Name");
    String Database_Port = getConfig().getString("Database Port");

    PluginManager pluginmanager = Bukkit.getPluginManager();
    Plugin plugin = pluginmanager.getPlugin("Hub Plus");
    com.connorlinfoot.hubplus.Other.MySQL MySQL = new MySQL(plugin, Database_Host, Database_Port, Database_Name, Database_User, Database_Pass);
    static Connection c = null;

    public void onEnable() {
        instance = this;
        Server server = getServer();
        ArrayList hidden = new ArrayList();
        ArrayList stackerdisabled = new ArrayList();
        ArrayList protectedworlds = new ArrayList();
        getConfig().set("PlayersHiding", hidden);
        getConfig().set("PlayersStackerDisabled", stackerdisabled);
        saveConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        ConsoleCommandSender console = server.getConsoleSender();

        oldConfigChecks(console, protectedworlds);

        if(getConfig().getString( "Send Stats" ).equals(" true" ) ) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-( <-- Dat face doe
            }
        }

        if(instance.getConfig().getString("Clans Enabled").equals("true") || instance.getConfig().getString("Friends Enabled").equals("true")) {
            c = MySQL.openConnection();

            if( c == null ){
                console.sendMessage(ChatColor.RED + "Hub Plus Failed To Start, Check Your MySQL Settings or Disable Clans + Friends!");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }

        onEnableClans.onEnableClans();

        onEnableFriends.onEnableFriends();

        registerEvents();

        if(getConfig().getString("Clans Enabled").equalsIgnoreCase("true") || getConfig().getString("Clans Enabled").equalsIgnoreCase("enabled")){
            getCommand("clan").setExecutor(new ClanCommand()); // /clan command
            getCommand("c").setExecutor(new ClanCommand()); // /clan command
        }

        if(getConfig().getString("Friends Enabled").equalsIgnoreCase("true") || getConfig().getString("Friends Enabled").equalsIgnoreCase("enabled")){
            getCommand("friend").setExecutor(new FriendCommand()); // /friend command
            getCommand("f").setExecutor(new FriendCommand()); // /friend command
        }

        // Include Command Classes
        getCommand("hub").setExecutor(new HubCommand()); // /hub command
        getCommand("hubplus").setExecutor(new HubPlusCommand()); // /HubPlus command
        getCommand("hp").setExecutor(new HubPlusCommand()); // /hp Command
        getCommand("broadcast").setExecutor(new BroadcastCommand()); // Broadcast Command
        getCommand("bc").setExecutor(new BroadcastCommand()); // /bc Command
        getCommand("fw").setExecutor(new FireworkCommand()); // /fw command
        getCommand("fly").setExecutor(new FlyCommand()); // /fly command
        getCommand("heal").setExecutor(new HealCommand()); // /heal command
        getCommand("gmc").setExecutor(new GameModeCommands()); // /gmc command
        getCommand("gms").setExecutor(new GameModeCommands()); // /gms command
        getCommand("gma").setExecutor(new GameModeCommands()); // /gma  command

        console.sendMessage(ChatColor.GREEN + "============= HUB PLUS =============");
        console.sendMessage(ChatColor.GREEN + "=========== VERSION: 0.5 ===========");
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
                Messages.noPerms(player);
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

    private boolean registerEvents(){
        if(getConfig().getString("Hide Players Enabled").equalsIgnoreCase("true") || getConfig().getString("Hide Players Enabled").equalsIgnoreCase("enabled"))  Bukkit.getPluginManager().registerEvents(new HidePlayers(), this);

        if(getConfig().getString("Preferences Enabled").equalsIgnoreCase("true") || getConfig().getString("Preferences Enabled").equalsIgnoreCase("enabled"))  Bukkit.getPluginManager().registerEvents(new PlayerPreferences(), this);

        if(getConfig().getString("Broadcasts Enabled").equalsIgnoreCase("true") || getConfig().getString("Broadcasts Enabled").equalsIgnoreCase("enabled"))  Bukkit.getPluginManager().registerEvents(new PlayerBroadcasts(), this);

        if(getConfig().getString("Launch Pads Enabled").equalsIgnoreCase("true") || getConfig().getString("Launch Pads Enabled").equalsIgnoreCase("enabled")) Bukkit.getPluginManager().registerEvents(new LaunchPads(), this);

        Bukkit.getPluginManager().registerEvents(new ChatSensor(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new SignListener(), this);
        Bukkit.getPluginManager().registerEvents(new CustomHubCommand(), this);
        Bukkit.getPluginManager().registerEvents(new PluginsCommand(), this);
        Bukkit.getPluginManager().registerEvents(new Stacker(), this);
        Bukkit.getPluginManager().registerEvents(new DoubleJump(), this);
        //Bukkit.getPluginManager().registerEvents(new Scoreboard(), this);
        return true;
    }

    private boolean oldConfigChecks(ConsoleCommandSender console, List protectedworlds){
        if(getConfig().getInt("Config Version") == 2){ // If user has multiple protected worlds
            if(getConfig().isSet("Clock Cooldown") && !getConfig().getString("Clock Cooldown").equals("")){ // If using old Clock Cooldown
                getConfig().set("Hide Players Cooldown", getConfig().getString("Clock Cooldown")); // Save old as new
                getConfig().set("Clock Cooldown", null); // Clear old
                saveConfig(); // Save Config
            }

            if(getConfig().isSet("Launch Pads") && !getConfig().getString("Launch Pads").equals("")){ // If using old Launch Pads
                getConfig().set("Launch Pads Enabled", getConfig().getString("Launch Pads")); // Save old as new
                getConfig().set("Launch Pads", null); // Clear old
                saveConfig(); // Save Config
            }

            if(getConfig().isSet("Blood Enabled") && !getConfig().getString("Blood Enabled").equals("")){ // If using blood
                console.sendMessage("Blood effect has been removed from Hub Plus!");
                console.sendMessage("Please use 'Minecraft Needs Blood' Plugin!");
                getConfig().set("Blood Enabled", null); // Clear old
                saveConfig(); // Save Config
            }
            String worlds = null;
            worlds = instance.getConfig().getString("Protected Worlds");
            List<String> worldList = Arrays.asList(worlds.split(","));
            for( String s : worldList){
                s = s.replace(" ",""); // Strip spaces
                protectedworlds.add(s);
            }
            getConfig().set("Protected Worlds", protectedworlds);
            getConfig().set("Config Version", 3);
            saveConfig();
        }
        return true;
    }
}