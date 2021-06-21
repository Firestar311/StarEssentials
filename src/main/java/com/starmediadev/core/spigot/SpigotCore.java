package com.starmediadev.core.spigot;

import com.starmediadev.data.manager.DatabaseManager;
import com.starmediadev.data.manager.SingleDatabaseManager;
import com.starmediadev.data.properties.SqlProperties;
import com.starmediadev.data.registries.DataObjectRegistry;
import com.starmediadev.data.registries.TypeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotCore extends JavaPlugin {

    private DatabaseManager databaseManager;
    
    public void onEnable() {
        this.saveDefaultConfig();

        TypeRegistry typeRegistry = TypeRegistry.createInstance(getLogger());
        DataObjectRegistry dataObjectRegistry = DataObjectRegistry.createInstance(getLogger(), typeRegistry);
        this.databaseManager = new SingleDatabaseManager(getLogger(), dataObjectRegistry, typeRegistry);
        this.databaseManager.createDatabase(new SqlProperties().setDatabase(getConfig().getString("mysql.database")).setHost(getConfig().getString("mysql.host"))
        .setPassword(getConfig().getString("mysql.password")).setPort(getConfig().getInt("mysql.port")).setUsername(getConfig().getString("mysql.username")));
        Bukkit.getServicesManager().register(DatabaseManager.class, this.databaseManager, this, ServicePriority.Highest);
    }
}
