package com.starmediadev.plugins.staressentials;

import com.starmediadev.plugins.staressentials.cmds.*;
import com.starmediadev.plugins.staressentials.module.*;
import com.starmediadev.plugins.starmcutils.module.StarModule;
import com.starmediadev.plugins.starmcutils.util.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static com.starmediadev.plugins.staressentials.util.SEUtils.sendActionMessage;

public class StarEssentials extends JavaPlugin {
    private Map<String, StarModule<?>> modules = new HashMap<>();
    private Config modulesConfig;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        modulesConfig = new Config(this, "modules.yml");
        modulesConfig.setup();
        
        getCommand("killall").setExecutor(new KillAllCmd(this));
        
        getCommand("inventorysee").setExecutor(new InventoryOpenCmd(this, "staressentials.command.invsee") {
            public Inventory getInventory(Player player) {
                return player.getInventory();
            }
        });
        
        getCommand("enderchest").setExecutor(new InventoryOpenCmd(this, "staressentials.command.echest") {
            public Inventory getInventory(Player player) {
                return player.getEnderChest();
            }
        });
        
        getCommand("workbench").setExecutor(new InventoryOpenCmd(this, "staressentials.command.workbench") {
            public Inventory getInventory(Player player) {
                MCUtils.debugSender(player, "Note: this command currently does not work properly");
                return Bukkit.createInventory(null, InventoryType.WORKBENCH);
            }
        });
        
        getCommand("trash").setExecutor(new InventoryOpenCmd(this, "staressentials.command.trash") {
            public Inventory getInventory(Player player) {
                return Bukkit.createInventory(null, 54, "Trash Can");
            }
        });
        
        getCommand("top").setExecutor(new PlayerActionCmd(this, "staressentials.command.top", (target, self, sender, args) -> {
            Block highestBlock = target.getWorld().getHighestBlockAt(target.getLocation());
            Location location = highestBlock.getLocation().clone();
            location.setY(location.getY() + 1);
            target.teleport(location);
            sendActionMessage(this, target, self, sender, "top");
        }));
        
        getCommand("spawnmob").setExecutor(new SpawnmobCmd(this));
        getCommand("weather").setExecutor(new WeatherCmd(this));
        getCommand("time").setExecutor(new TimeCmd(this));
        getCommand("spawner").setExecutor(new SpawnerCmd(this));
        
        //Modules
        registerModule(new SpawnModule(this));
        registerModule(new BroadcastModule(this));
        registerModule(new HomeModule(this));
        registerModule(new PlayerStatsModule(this));
        registerModule(new GamemodeModule(this));
        registerModule(new SignEditModule(this));
        registerModule(new KillModule(this));
        registerModule(new ClearInvModule(this));
        
        ConfigurationSection modulesSection = modulesConfig.getConfigurationSection("modules");
        if (modulesSection != null) {
            for (String moduleName : modulesSection.getKeys(false)) {
                boolean enabled = modulesSection.getBoolean(moduleName + ".enabled");
                StarModule<?> module = this.modules.get(moduleName);
                if (module != null) {
                    module.setEnabled(enabled);
                }
            }
        }
        
        for (StarModule<?> module : this.modules.values()) {
            module.init();
            if (module.isEnabled()) {
                module.registerCommands();
                module.registerListeners();
            }
        }
    }
    
    private void registerModule(StarEssentialsModule module) {
        this.modules.put(module.getName(), module);
    }
    
    @Override
    public void onDisable() {
        for (StarModule<?> module : this.modules.values()) {
            modulesConfig.set("modules." + module.getName() + ".enabled", module.isEnabled());
        }
        modulesConfig.save();
    
        for (StarModule<?> module : modules.values()) {
            module.save();
        }
    }
}