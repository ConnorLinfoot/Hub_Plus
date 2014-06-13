package com.connorlinfoot.hubplus.Scoreboard;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard implements Listener {
    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event ){
        Player player = event.getPlayer();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("test","dummy");
        obj.setDisplayName("Testing!");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score score = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Online:"));
        Integer count = instance.getServer().getOnlinePlayers().length;
        player.setScoreboard(board);
    }

    @EventHandler
    public void onPlayerMove( PlayerMoveEvent event ){
        Player player = event.getPlayer();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        player.setScoreboard(manager.getNewScoreboard());
        org.bukkit.scoreboard.Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("test","dummy");
        obj.setDisplayName("Testing!");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score score = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Online:"));
        Integer count = instance.getServer().getOnlinePlayers().length;
        //Score score = obj.getScore(player);
        //score.setScore((int) player.getHealth());
        player.setScoreboard(board);
    }

}
