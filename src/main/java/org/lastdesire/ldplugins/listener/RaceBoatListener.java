package org.lastdesire.ldplugins.listener;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.lastdesire.ldplugins.LDPlugins;

import java.util.HashMap;
import java.util.Date;
import java.util.List;

import static org.lastdesire.ldplugins.utils.StaticInfo.*;

public class RaceBoatListener implements Listener, CommandExecutor {

    private static final String[] defaultEndPoint = {"","-4429","-4419","-596","-596"};

    private class EndPoint{
        private int startX;
        private int startZ;
        private int endX;
        private int endZ;

        private EndPoint(String args[]) {
            this.startX = Integer.valueOf(args[1]);
            this.endX = Integer.valueOf(args[2]);
            this.startZ = Integer.valueOf(args[3]);
            this.endZ = Integer.valueOf(args[4]);
        }

        private int getStartX() {
            return startX;
        }

        private int getStartZ() {
            return startZ;
        }

        private int getEndX() {
            return endX;
        }

        private int getEndZ() {
            return endZ;
        }
    }

    private LDPlugins plugin;

    private EndPoint endPoint;

    private HashMap<String,Player> players;

    //private HashSet<Player> watchers;

    private HashMap<String,Date> playerStartTime;

    private void setEndPoint(String[] args) {
        this.endPoint = new EndPoint(args);
    }

    private boolean inEndPoint(Location location){
        int x = location.getBlockX();
        int z = location.getBlockZ();
        return (x <= endPoint.getEndX() &&
                x >= endPoint.getStartX() &&
                z <= endPoint.getEndZ() &&
                z >= endPoint.getStartZ());
    }

    private String raceTime(Date start,Date end){
        int time = (int) (end.getTime() - start.getTime());
        int second = time / 1000;
        int minute = second / 60;
        second = second % 60;
        int millisecond = time % 1000;
        return minute+"\'"+second+"\""+millisecond;
    }

    public RaceBoatListener(LDPlugins plugin){
        this.plugin = plugin;
        setEndPoint(defaultEndPoint);
        players = new HashMap<String, Player>();
        playerStartTime = new HashMap<String, Date>();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("dejavu").setExecutor(this);
    }

    @EventHandler
    public void onBoatMove(VehicleMoveEvent event){
        Vehicle vehicle = event.getVehicle();
        List<Entity> passengers = vehicle.getPassengers();
        if(passengers == null || passengers.size() == 0)
            return;
        Entity entity = passengers.get(0);
        if(entity == null)
            return;

        if((entity instanceof Player) &&
                players.containsKey(((Player) entity).getPlayerListName()) &&
                vehicle.getType() == EntityType.BOAT &&
                inEndPoint(vehicle.getLocation())){
            Player player = (Player) entity;
            String playerName = player.getPlayerListName();
            Date endTime = new Date();
            player.sendMessage(raceTime(playerStartTime.get(playerName),endTime));
            players.remove(playerName);
            playerStartTime.remove(playerName);
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage(ERR_NAP);
            return false;
        }

        final Player player = (Player)sender;
        if(args.length == 0)
            return false;
        String subCommand = args[0];

        if(subCommand.equals("setEndPoint")) {
            if (args.length != 5)
                return false;
            else{
                setEndPoint(args);
            }
        }
        else if(subCommand.equals("start")) {
            final String playerName = player.getPlayerListName();
            players.put(playerName,player);

            new BukkitRunnable(){
                int countdown = 3;
                public void run(){
                    if(countdown == 0){
                        player.sendMessage(PREFIX+"Go!");
                        Date startTime = new Date();
                        playerStartTime.put(playerName,startTime);
                        cancel();
                    }
                    else {
                        player.sendMessage(PREFIX + "Race begins in " + countdown-- + "...");
                    }
                }
            }.runTaskTimer(plugin,0,20);



        }
        else
            return false;
        return true;
    }
}
