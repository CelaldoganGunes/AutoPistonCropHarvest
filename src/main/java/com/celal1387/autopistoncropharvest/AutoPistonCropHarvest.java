package com.celal1387.autopistoncropharvest;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class AutoPistonCropHarvest extends JavaPlugin {

    private static AutoPistonCropHarvest plugin;
    @Override
    public void onEnable() {

        plugin = this;

        Bukkit.getLogger().log(Level.INFO, "AutoPistonCropHarvest activated.");
        getConfig().options().copyDefaults(true);
        saveConfig();

        Bukkit.getPluginManager().registerEvents(new CropGrowListener(),plugin);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "AutoPistonCropHarvest deactivated.");
        saveConfig();

    }

    public static AutoPistonCropHarvest getPlugin(){
        return plugin;
    }
}
