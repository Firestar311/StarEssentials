package com.starmediadev.core.spigot;

import com.starmediadev.data.Context;
import com.starmediadev.data.StarData;
import com.starmediadev.data.manager.DatabaseManager;
import com.starmediadev.data.properties.SqlProperties;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotCore extends JavaPlugin {

    private DatabaseManager databaseManager;
    
    public void onEnable() {
        this.saveDefaultConfig();

        this.databaseManager = new StarData(Context.SINGLE, getLogger()).getDatabaseManager();
        this.databaseManager.createDatabase(new SqlProperties().setDatabase(getConfig().getString("mysql.database")).setHost(getConfig().getString("mysql.host"))
        .setPassword(getConfig().getString("mysql.password")).setPort(getConfig().getInt("mysql.port")).setUsername(getConfig().getString("mysql.username")));
        Bukkit.getServicesManager().register(DatabaseManager.class, this.databaseManager, this, ServicePriority.Highest);
    }
}