package org.lastdesire.ldplugins.listener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.lastdesire.ldplugins.LDPlugins;
import org.lastdesire.ldplugins.utils.LocationUtils;
import org.lastdesire.ldplugins.utils.StringBigraph;

import javax.print.DocFlavor;


public class InventoryOpenListener implements Listener, CommandExecutor {

    private StringBigraph playerInventoryInfo;

    private static final String PREFIX = ChatColor.BOLD + "[" + ChatColor.AQUA + "LDPlugins" + ChatColor.WHITE + ChatColor.BOLD + "] " + ChatColor.RESET;

    private static final String ERR_NAP = PREFIX + ChatColor.RED + "You must be a player!";

    private static final String ERR_ARGU = PREFIX + ChatColor.RED + "Too many arguments!";

    public InventoryOpenListener(LDPlugins plugin) {
        playerInventoryInfo = new StringBigraph();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("listen").setExecutor(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event){
        if(event == null || event.isCancelled()){
            return;
        }
        Inventory inventory = event.getInventory();
        InventoryType type = inventory.getType();
        if(type != InventoryType.CRAFTING &&
                type != InventoryType.PLAYER &&
                type != InventoryType.WORKBENCH
                ) {
            String playerName = event.getPlayer().getName();
            String location = LocationUtils.getInventoryLocationString(inventory);
            playerInventoryInfo.addToMap(playerName,location);
        }
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
        String location = LocationUtils.getLocationString(player.getLocation());
        sender.sendMessage(PREFIX + ChatColor.YELLOW + "You're searching for tracks near "
                + ChatColor.RESET + location);

        if(playerInventoryInfo.containsRightKey(location)) {
            StringBuilder builder = new StringBuilder();
            for(String playerName : playerInventoryInfo.getMappingToLeft(location)){
                builder.append(" ");
                builder.append(playerName);
            }
            sender.sendMessage(PREFIX + ChatColor.YELLOW + "You've found tracks of:"
                    + ChatColor.RESET + builder);
        }
        else{
            sender.sendMessage(PREFIX + ChatColor.YELLOW + "No tracks were found.");
        }
        return true;
    }
}
