package com.starmediadev.plugins.spigotcore;

import com.starmediadev.data.manager.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotCore extends JavaPlugin {

    private DatabaseManager databaseManager;
    
    public void onEnable() {
        this.saveDefaultConfig();
    }
}