package org.lastdesire.ldplugins.utils;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.lastdesire.ldplugins.utils.StringBigraph;

import java.util.HashMap;
import java.util.HashSet;



public class LocationUtils {

    private static final String UNDEFINED = "UNDEFINED";

    public static String getInventoryLocationString(Inventory inventory){
        return getInventoryLocationString(inventory,4);
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

    public static StringBuilder getTracksString(String locationString, StringBigraph playerInventoryInfo){
        StringBuilder builder = new StringBuilder();
        for(String playerName : playerInventoryInfo.getMappingToLeft(locationString)){
            builder.append(" ");
            builder.append(playerName);
        }
        return builder;
    }
}
