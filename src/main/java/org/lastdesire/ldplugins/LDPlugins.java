package org.lastdesire.ldplugins;

import org.bukkit.plugin.java.JavaPlugin;
import org.lastdesire.ldplugins.listener.InventoryOpenListener;

public final class LDPlugins extends JavaPlugin {

    @Override
    public void onEnable() {
        InventoryOpenListener inventoryOpenListener = new InventoryOpenListener(this);

        getLogger().info("LDPlugins have been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("LDPlugins have been disabled.");
    }


}
