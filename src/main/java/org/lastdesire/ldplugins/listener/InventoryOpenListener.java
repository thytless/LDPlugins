package org.lastdesire.ldplugins.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.lastdesire.ldplugins.LDPlugins;
import org.lastdesire.ldplugins.utils.LocationUtils;
import org.lastdesire.ldplugins.utils.StringBigraph;

import java.util.*;


public class InventoryOpenListener implements Listener, CommandExecutor {

    private static final String PREFIX = ChatColor.BOLD + "[" + ChatColor.AQUA + "LDPlugins" + ChatColor.WHITE + ChatColor.BOLD + "] " + ChatColor.RESET;

    private static final String ERR_NAP = PREFIX + ChatColor.RED + "You must be a player!";

    private static final String ERR_ARGU = PREFIX + ChatColor.RED + "Too many arguments!";

    private static final String INFO_LOCATION = PREFIX + ChatColor.YELLOW + "You're searching for tracks near "
            + ChatColor.RESET;

    private static final String INFO_FOUND = PREFIX + ChatColor.YELLOW + "You've found tracks of:"
            + ChatColor.RESET;

    private static final String INFO_NOT_FOUND = PREFIX + ChatColor.YELLOW + "No tracks were found.";

    private static final InventoryType[] ALLOWED_TYPE = {
            InventoryType.ANVIL,InventoryType.CHEST,InventoryType.ENCHANTING,
            InventoryType.HOPPER,InventoryType.MERCHANT,InventoryType.SHULKER_BOX
    } ;

    private StringBigraph playerInventoryInfo;

    private ArrayList<InventoryType> allowedType;

    private void initPlayerInventoryInfo(){
        playerInventoryInfo = new StringBigraph();
    }

    private void initAllowedType() {
        allowedType = new ArrayList<InventoryType>();
        allowedType.addAll(Arrays.asList(ALLOWED_TYPE));
    }



    public InventoryOpenListener(LDPlugins plugin) {
        initPlayerInventoryInfo();
        initAllowedType();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("listen").setExecutor(this);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event){

        Inventory inventory;
        HumanEntity player;
        Location location;

        if(event == null || event.isCancelled()) return;

        if((inventory = event.getInventory()) == null ||
                (player = event.getPlayer()) == null ||
                (location = inventory.getLocation()) == null) return;

        if(!allowedType.contains(inventory.getType())) return;

        String playerName = player.getName();
        String locationString = LocationUtils.getInventoryLocationString(inventory);
        playerInventoryInfo.addToMap(playerName,locationString);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ERR_NAP);
            return false;
        }

        if(args.length != 0){
            sender.sendMessage(ERR_ARGU);
            return false;
        }

        Player player = (Player)sender;
        String locationString = LocationUtils.getLocationString(player.getLocation());

        sender.sendMessage(INFO_LOCATION + locationString);

        if(playerInventoryInfo.containsRightKey(locationString))
            sender.sendMessage(INFO_FOUND + LocationUtils.getTracksString(locationString, playerInventoryInfo));
        else
            sender.sendMessage(INFO_NOT_FOUND);
        return true;
    }
}
