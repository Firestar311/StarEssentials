package com.starmediadev.plugins.staressentials;

import com.starmediadev.plugins.staressentials.cmds.*;
import com.starmediadev.plugins.staressentials.listeners.SignListener;
import com.starmediadev.plugins.staressentials.module.*;
import com.starmediadev.plugins.starmcutils.module.StarModule;
import com.starmediadev.plugins.starmcutils.util.Config;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.HashMap;
import java.util.Map;

import static com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd.sendActionMessage;

public class StarEssentials extends JavaPlugin {
    private Map<String, StarModule<?>> modules = new HashMap<>();
    private Config modulesConfig;
    
    @Override
    public void onEnable() {
        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        
        
        this.saveDefaultConfig();
        modulesConfig = new Config(this, "modules.yml");
        modulesConfig.setup();
        
        getCommand("kill").setExecutor(new PlayerActionCmd(this, "staressentials.command.kill", (target, self, sender, args) -> {
            target.setHealth(0);
            sendActionMessage(this, target, self, sender, "kill");
        }));
        
        getCommand("clearinv").setExecutor(new PlayerActionCmd(this, "staressentials.command.clearinv", (target, self, sender, args) -> {
            target.getInventory().setContents(new ItemStack[0]);
            target.getInventory().setArmorContents(new ItemStack[0]);
            target.getInventory().setExtraContents(new ItemStack[0]);
            sendActionMessage(this, target, self, sender, "clearinv");
        }));
        
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
        getCommand("signedit").setExecutor(new SignEditCmd(this));
        
        //Modules
        SpawnModule spawnModule = new SpawnModule(this);
        this.modules.put(spawnModule.getName(), spawnModule);
    
        BroadcastModule broadcastModule = new BroadcastModule(this);
        this.modules.put(broadcastModule.getName(), broadcastModule);
    
        HomeModule homeModule = new HomeModule(this);
        this.modules.put(homeModule.getName(), homeModule);
        
        PlayerStatsModule playerStatsModule = new PlayerStatsModule(this);
        this.modules.put(playerStatsModule.getName(), playerStatsModule);
    
        GamemodeModule gamemodeModule = new GamemodeModule(this);
        this.modules.put(gamemodeModule.getName(), gamemodeModule);
        
        ConfigurationSection modulesSection = modulesConfig.getConfiguration().getConfigurationSection("modules");
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
            module.init(commandHandler);
            if (module.isEnabled()) {
                module.registerCommands();
                module.registerListeners();
            }
        }
        
        commandHandler.registerBrigadier();
        
        getServer().getPluginManager().registerEvents(new SignListener(), this);
    }
    
    @Override
    public void onDisable() {
        for (StarModule<?> module : this.modules.values()) {
            modulesConfig.getConfiguration().set("modules." + module.getName() + ".enabled", module.isEnabled());
        }
        modulesConfig.save();
    
        for (StarModule<?> module : modules.values()) {
            module.save();
        }
    }
}