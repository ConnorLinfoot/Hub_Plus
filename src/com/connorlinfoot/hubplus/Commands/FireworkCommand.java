package com.connorlinfoot.hubplus.Commands;

import com.connorlinfoot.hubplus.Global.Messages;
import com.connorlinfoot.hubplus.HubPlus;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FireworkCommand implements CommandExecutor {

    private Plugin instance = HubPlus.getInstance();
    private List<Player> cantUseFw = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if( label.equalsIgnoreCase( "fw" ) ){
            if( !(sender instanceof Player) ) {
                sender.sendMessage("Please use this command as a player!");
            } else {
                Player player = (Player) sender;
                if (player.hasPermission("hubplus.fw:")) {
                    FwCountdown d = new FwCountdown();
                    if (!cantUseFw.contains(player)) {
                        cantUseFw.add(player);
                        d.setList(cantUseFw);
                        d.setPlayer(player);
                        new Thread(d).start();
                        shootFirework(player);
                        String launch = ChatColor.GREEN + "You launched a firework!";
                        player.sendMessage(formatVariables(launch, player));
                    } else {
                        Integer cooldown = instance.getConfig().getInt("Firework Cooldown");
                        String cooldownstring = "";
                        if( cooldown >= 2 ){
                            cooldownstring = "seconds";
                        } else {
                            cooldownstring = "second";
                        }
                        player.sendMessage(ChatColor.GREEN + "You must wait " + cooldown +  " " + cooldownstring + " between fireworks!");
                    }
                } else {
                    Messages.noPerms(player);
                }
            }
        }

        return false;
    }

    public String formatVariables( String string, Player player ) {
        int cdt = 1;
        return ChatColor.translateAlternateColorCodes("&".charAt(0), string).replace("%time", String.valueOf(cdt));
    }

    private void shootFirework( Player player ){
        Firework firework = ( Firework )player.getWorld().spawnEntity( player.getLocation(), EntityType.FIREWORK );
        FireworkMeta fm = firework.getFireworkMeta();
        Random r = new Random();
        FireworkEffect.Type type = null;
        int fType = r.nextInt(5) + 1;
        switch ( fType ) {
            case 1:
            default:
                type = FireworkEffect.Type.BALL;
                break;
            case 2:
                type = FireworkEffect.Type.BALL_LARGE;
                break;
            case 3:
                type = FireworkEffect.Type.BURST;
                break;
            case 4:
                type = FireworkEffect.Type.CREEPER;
                break;
            case 5:
                type = FireworkEffect.Type.STAR;
        }
        int c1i = r.nextInt(16) + 1;
        int c2i = r.nextInt(16) + 1;
        Color c1 = getColor(c1i);
        Color c2 = getColor(c2i);
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
        fm.addEffect(effect);
        int power = r.nextInt(2) + 1;
        fm.setPower(power);
        firework.setFireworkMeta(fm);
    }

    public Color getColor( int c ) {
        switch ( c ) {
            case 1:
            default:
                return Color.AQUA;
            case 2:
                return Color.BLACK;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.FUCHSIA;
            case 5:
                return Color.GRAY;
            case 6:
                return Color.GREEN;
            case 7:
                return Color.LIME;
            case 8:
                return Color.MAROON;
            case 9:
                return Color.NAVY;
            case 10:
                return Color.OLIVE;
            case 11:
                return Color.ORANGE;
            case 12:
                return Color.PURPLE;
            case 13:
                return Color.RED;
            case 14:
                return Color.SILVER;
            case 15:
                return Color.TEAL;
            case 16:
                return Color.WHITE;
        }
        //return Color.YELLOW;
    }

    public class FwCountdown implements Runnable {
        public Player player1 = null;
        public List<Player> cantUseFw1 = new ArrayList<Player>();
        Plugin instance = HubPlus.getInstance();

        public void setPlayer(Player player){
            player1 = player;
        }

        public void setList(List<Player> list){
            cantUseFw1 = list;
        }

        public List<Player> getList() {
            return cantUseFw1;
        }

        public void run(){
            try{
                Integer time = instance.getConfig().getInt("Firework Cooldown");
                Integer fulltime = time * 1000;
                Thread.sleep(fulltime);
                cantUseFw1.remove(player1);
            } catch (Exception ignored){
            }
        }
    }
}
