package org.lastdesire.ldplugins.utils;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

public class LocationUtils {

    public static String getInventoryLocationString(Inventory inventory){
        return getLocationString(inventory.getLocation());
    }

    public static String getInventoryLocationString(Inventory inventory, int scale){
        return getLocationString(inventory.getLocation(),scale);
    }

    public static String getLocationString(Location location){
        return getLocationString(location,4);
    }

    public static String getLocationString(Location location, int scale){
        return (location.getBlockX()&(-1L<<scale)) + "," + (location.getBlockZ()&(-1L<<scale));
    }
}
