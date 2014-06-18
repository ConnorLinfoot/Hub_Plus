package com.connorlinfoot.hubplus.Player;

import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/*
*
* New Item for preferences testing!
* May be removed!
*
*/
public class PlayerPreferences implements Listener {

    private Plugin instance = HubPlus.getInstance();

    @EventHandler
    public void onPlayerJoin( PlayerJoinEvent event){
        Player player = event.getPlayer();
        Material material = Material.matchMaterial(instance.getConfig().getString("Preferences Item"));
        if( material == null) material = Material.REDSTONE_COMPARATOR;
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        if(world.equals(currentworld)) {
            PlayerInventory inventory = player.getInventory();

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
                        imeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Preferences");
                        List<String> lore = new ArrayList<String>();
                        lore.add("Some new preferences tool?");
                        imeta.setLore(lore);
                        itm.setItemMeta(imeta);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPreferencesClick( PlayerInteractEvent event ){
        Plugin instance = HubPlus.getInstance();
        Player player = event.getPlayer();
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        Material material = Material.matchMaterial(instance.getConfig().getString("Preferences Item"));
        if( material == null) material = Material.REDSTONE_COMPARATOR;
        if(world.equals(currentworld)) {
            ItemStack item = null;
            if (event.hasItem()) item = event.getItem();
            if (item != null) {
                Material itemmaterial = item.getType();
                if (material == itemmaterial) {
                    Inventory menu = instance.getServer().createInventory(player,36, "Preferences");

                    // Item 1
                    ItemStack nitem = new ItemStack(Material.BLAZE_POWDER);
                    ItemMeta meta =  nitem.getItemMeta();
                    ArrayList stackerdisabled = (ArrayList) instance.getConfig().getList("PlayersStackerDisabled");
                    meta.setDisplayName("Disable Stacker!");
                    if(stackerdisabled.contains(player.getName())) meta.setDisplayName("Enable Stacker!");
                    nitem.setItemMeta(meta);
                    menu.setItem(14, nitem);

                    // Item 2
                    nitem = new ItemStack(Material.EYE_OF_ENDER);
                    meta =  nitem.getItemMeta();
                    meta.setDisplayName("Hide Players!");
                    nitem.setItemMeta(meta);
                    menu.setItem(12, nitem);





                    player.openInventory(menu);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInvDrag( InventoryClickEvent event ){
        Player player = (Player) event.getWhoClicked();
        String world =  player.getWorld().getName();
        String currentworld = instance.getConfig().getString("Hub World");
        ItemStack item = event.getCurrentItem();
        if( item != null ) {
            ItemMeta itemmeta = item.getItemMeta();
            if( itemmeta!= null ) {
                if( !String.valueOf(itemmeta).equals("UNSPECIFIC_META:{meta-type=UNSPECIFIC}") ) {
                    String itemname = itemmeta.getDisplayName();
                    if (world.equals(currentworld)) {
                        if (itemname.equals("Disable Stacker!")) {
                            disableStacker(player);
                            player.closeInventory();
                            event.setCancelled(true);
                        } else if (itemname.equals("Enable Stacker!")) {
                            enableStacker(player);
                            player.closeInventory();
                            event.setCancelled(true);
                        } else if (itemname.equals("Show All Players!")) {
                            showAllPlayers(player);
                            player.closeInventory();
                            event.setCancelled(true);
                        } else if (itemname.equals("Hide All Players!")) {
                            hideAllPlayers(player);
                            player.closeInventory();
                            event.setCancelled(true);
                        } else if (itemname.equals("Show Just Friends!")) {
                            player.closeInventory();
                            player.sendMessage("Coming Soon!");
                            event.setCancelled(true);
                        } else if (itemname.equals("Hide Players!")) {
                            Inventory menu = instance.getServer().createInventory(player, 36, "Preferences");
                            ArrayList hidden = (ArrayList) instance.getConfig().getList("PlayersHiding");
                            // Item 1
                            ItemStack nitem = new ItemStack(Material.EYE_OF_ENDER);
                            if( !hidden.contains(player.getName()) ) nitem = new ItemStack(Material.ENDER_PEARL);
                            ItemMeta meta = nitem.getItemMeta();
                            meta.setDisplayName("Show All Players!");
                            nitem.setItemMeta(meta);
                            menu.setItem(11, nitem);

                            // Item 2
                            nitem = new ItemStack(Material.EYE_OF_ENDER);
                            if( hidden.contains(player.getName()) ) nitem = new ItemStack(Material.ENDER_PEARL);
                            meta = nitem.getItemMeta();
                            meta.setDisplayName("Hide All Players!");
                            nitem.setItemMeta(meta);
                            menu.setItem(13, nitem);

                            // Item 3
                            nitem = new ItemStack(Material.EYE_OF_ENDER);
                            meta = nitem.getItemMeta();
                            meta.setDisplayName("Show Just Friends!");
                            nitem.setItemMeta(meta);
                            menu.setItem(15, nitem);
                            player.openInventory(menu);
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    private boolean showAllPlayers(Player player){
        ArrayList hidden = (ArrayList) instance.getConfig().getList("PlayersHiding");
        if (hidden.contains(player.getName())) {
            hidden.remove(player.getName());
            instance.getConfig().set("PlayersHiding", hidden);
            instance.saveConfig();
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                player.showPlayer(online);
            }
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Players no longer hidden!");
        } else {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                player.showPlayer(online);
            }
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Players no longer hidden!");
        }
        return false;
    }

    private boolean hideAllPlayers(Player player){
        ArrayList hidden = (ArrayList) instance.getConfig().getList("PlayersHiding");
        if (!hidden.contains(player.getName())) {
            hidden.add(player.getName());
            instance.getConfig().set("PlayersHiding", hidden);
            instance.saveConfig();
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                player.hidePlayer(online);
            }
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Players hidden!");
        } else {
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                player.hidePlayer(online);
            }
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Players hidden!");
        }
        return false;
    }

    private boolean disableStacker(Player player){
        ArrayList stackerdisabled = (ArrayList) instance.getConfig().getList("PlayersStackerDisabled");
        if (!stackerdisabled.contains(player.getName())) {
            stackerdisabled.add(player.getName());
            instance.getConfig().set("PlayersStackerDisabled", stackerdisabled);
            instance.saveConfig();
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Stacker Disabled!");
        }
        return false;
    }

    private boolean enableStacker(Player player){
        ArrayList stackerdisabled = (ArrayList) instance.getConfig().getList("PlayersStackerDisabled");
        if (stackerdisabled.contains(player.getName())) {
            stackerdisabled.remove(player.getName());
            instance.getConfig().set("PlayersStackerDisabled", stackerdisabled);
            instance.saveConfig();
            player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Stacker Enabled!");
        }
        return false;
    }
}
