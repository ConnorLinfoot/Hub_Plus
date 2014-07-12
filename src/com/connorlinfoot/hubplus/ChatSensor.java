package com.connorlinfoot.hubplus;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChatSensor implements Listener {

    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void onChat( AsyncPlayerChatEvent event ){
        Player player = event.getPlayer();
        String chat = event.getMessage();
        ArrayList bannedWords = new ArrayList<List>();
        String[] wordsList = new String[] {"fucking","fucks","cunt","fuck","bitch","skets","sket","sluts","slut","bastards","bastard","twat","faggots","faggot","whore","shit","pussy","dicks","dick","cocks","cock","vagina","tits","tit","boobs","boob","penis"};
        bannedWords.addAll(Arrays.asList(wordsList));

        ArrayList randomWords = new ArrayList<List>();
        String[] randomWordsList = new String[] {"apple","pear","love","amazing","onion","grape fruit"};
        randomWords.addAll(Arrays.asList(randomWordsList));

        if(instance.getConfig().getString("Chat Sensor").equalsIgnoreCase("replace")){ // Checks if config is set to replace
            for( Object sensor1 : bannedWords ) { // Changes the word into stars
                String sensor = String.valueOf(sensor1);
                if(chat.contains(sensor)){
                    chat = chat.toLowerCase();
                }
                chat = chat.replaceAll(sensor, "****" ); // Replaces string with stars
                //chat = chat.replace(sensor, ChatColor.MAGIC + "****" + ChatColor.RESET ); // Replaces string with magic
                event.setMessage(chat); // Return the new sensor message
            }
        } else if(instance.getConfig().getString("Chat Sensor").equalsIgnoreCase("block")){ // Checks if config is set to block
            for( Object sensor1 : bannedWords ) { // Blocks the chat message all together!
                String sensor = String.valueOf(sensor1);
                if (chat.toLowerCase().contains(sensor.toLowerCase())) { // Checks if any words from bannedWords is in the message
                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "--------------- Hub Plus! ---------------"); // Custom server name here?
                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "Swearing is not allowed on this server!"); // Custom message here?
                    player.sendMessage(ChatColor.BOLD + "" + ChatColor.RED + "---------------------------------------");
                    event.setCancelled(true); // Stops chat message showing
                }
            }
        } else if(instance.getConfig().getString("Chat Sensor").equalsIgnoreCase("random")){ // Checks if config is set to random
            for( Object sensor1 : bannedWords ) { // Changes the word into a random word
                String sensor = String.valueOf(sensor1);
                Random randomGen = new Random();
                int index = randomGen.nextInt(randomWords.size());
                if(chat.contains(sensor)){
                    chat = chat.toLowerCase();
                }
                String word = String.valueOf(randomWords.get(index));
                chat = chat.replaceAll(sensor, word ); // Replaces string with random word
                event.setMessage(chat);
            }
        }
    }
}
