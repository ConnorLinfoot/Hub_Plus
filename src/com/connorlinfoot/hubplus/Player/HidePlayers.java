package com.connorlinfoot.hubplus.Player;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class HidePlayers implements Listener {

    private Plugin instance = HubPlus.getInstance();

    private List<Player> cantUseClock = new ArrayList<Player>();

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event){
        Player player = event.getPlayer();
        Material material = Material.matchMaterial(instance.getConfig().getString("Hide Players Item"));
        if( material == null) material = Material.WATCH;
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        if(world.equals(currentworld)) {
            PlayerInventory inventory = player.getInventory();
            Plugin instance = HubPlus.getInstance();
            if (instance.getConfig().getString("Clear Inventory On Join").equals("true")) {
                player.getInventory().clear();
                instance.getLogger().info("Works!!");
            }

            Inventory inv = player.getInventory();
            if (inv.contains(material)) {

            } else {
                inventory.addItem(new ItemStack(material));
            }


            for (int n = 0; n < inv.getSize(); n++) { //loop threw all items in the inventory
                ItemStack itm = inv.getItem(n); //get the items
                if (itm != null) { //make sure the item is not null, or you'll get a NullPointerException

                    if (itm.getType().equals(material)) { //if the item equals a contraband item
                        ItemMeta imeta = itm.getItemMeta();
                        imeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Hide Players!");
                        List<String> lore = new ArrayList<String>();
                        lore.add("Hide and Show players with this magic tool!");
                        imeta.setLore(lore);
                        itm.setItemMeta(imeta);
                    }
                }
            }
        }
    }

    @EventHandler
    public void newOnClockClick( PlayerInteractEvent event ){
        Plugin instance = HubPlus.getInstance();
        Player player = event.getPlayer();
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        Material material = Material.matchMaterial(instance.getConfig().getString("Hide Players Item"));
        if( material == null) material = Material.WATCH;
        if(world.equals(currentworld)) {
            ItemStack item = null;
            if( event.hasItem() ) item = event.getItem();
            if( item != null ){
                Material itemmaterial = item.getType();
                if( material == itemmaterial){
                    ArrayList hidden = (ArrayList) instance.getConfig().getList("PlayersHiding");
                    ClockCountdown d = new ClockCountdown();
                    event.setCancelled(true);
                    if (!cantUseClock.contains(player)) {
                        if (hidden.contains(player.getName())) {
                            hidden.remove(player.getName());
                            instance.getConfig().set("PlayersHiding", hidden);
                            instance.saveConfig();
                            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                player.showPlayer(online);
                            }
                            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Players no longer hidden!");
                            ItemStack inHand = player.getItemInHand();
                            ItemMeta imeta = inHand.getItemMeta();
                            imeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Hide Players!");
                            inHand.setItemMeta(imeta);
                        } else {
                            hidden.add(player.getName());
                            instance.getConfig().set("PlayersHiding", hidden);
                            instance.saveConfig();
                            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                player.hidePlayer(online);
                            }
                            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Players hidden!");
                            ItemStack inHand = player.getItemInHand();
                            ItemMeta imeta = inHand.getItemMeta();
                            imeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Show Players!");
                            inHand.setItemMeta(imeta);
                        }

                        cantUseClock.add(player);
                        d.setList(cantUseClock);
                        d.setPlayer(player);
                        new Thread(d).start();
                    } else {
                        Integer time = instance.getConfig().getInt("Hide Players Cooldown");
                        String seconds;
                        if (time == 1) {
                            seconds = "second";
                        } else {
                            seconds = "seconds";
                        }
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Please wait " + time + " " + seconds + "!");
                    }
                }
            }
        }
    }

    /* Old clock function was here, No Longer Used! */

    /* Moved Text Color to Global.UsefulFunctions */

    public class ClockCountdown implements Runnable {
        public Player player1 = null;
        public List<Player> cantUseClock1 = new ArrayList<Player>();
        Plugin instance = HubPlus.getInstance();

        public void setPlayer(Player player){
            player1 = player;
        }

        public void setList(List<Player> list){
            cantUseClock1 = list;
        }

        public List<Player> getList() {
            return cantUseClock1;
        }

        public void run(){
            try{
                Integer time = instance.getConfig().getInt("Hide Players Cooldown");
                Integer fulltime = time * 1000;
                Thread.sleep(fulltime);
                cantUseClock1.remove(player1);
            } catch (Exception ignored){
            }
        }
    }
}
