package com.starmediadev.plugins.staressentials;

import com.starmediadev.plugins.staressentials.cmds.*;
import com.starmediadev.plugins.staressentials.listeners.SignListener;
import com.starmediadev.plugins.staressentials.module.BroadcastModule;
import com.starmediadev.plugins.staressentials.module.PlayerStatsModule;
import com.starmediadev.plugins.staressentials.module.SpawnModule;
import com.starmediadev.plugins.starmcutils.module.StarModule;
import com.starmediadev.plugins.starmcutils.util.Config;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.utils.helper.StringHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd.sendActionMessage;
import static com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd.sendActionMessageValue;

public class StarEssentials extends JavaPlugin {
    private Map<String, StarModule<?>> modules = new HashMap<>();
    private Config modulesConfig;
    
    @Override
    public void onEnable() {
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
        
        getCommand("gmc").setExecutor(new PlayerActionCmd(this, "staressentials.command.gamemode.creative", (target, self, sender, args) -> {
            target.setGameMode(GameMode.CREATIVE);
            sendActionMessageValue(this, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(target.getGameMode().name()));
        }));
        
        getCommand("gms").setExecutor(new PlayerActionCmd(this, "staressentials.command.gamemode.survival", (target, self, sender, args) -> {
            target.setGameMode(GameMode.SURVIVAL);
            sendActionMessageValue(this, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(target.getGameMode().name()));
        }));
        
        getCommand("gmsp").setExecutor(new PlayerActionCmd(this, "staressentials.command.gamemode.spectator", (target, self, sender, args) -> {
            target.setGameMode(GameMode.SPECTATOR);
            sendActionMessageValue(this, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(target.getGameMode().name()));
        }));
        
        getCommand("gma").setExecutor(new PlayerActionCmd(this, "staressentials.command.gamemode.adventure", (target, self, sender, args) -> {
            target.setGameMode(GameMode.ADVENTURE);
            sendActionMessageValue(this, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(target.getGameMode().name()));
        }));
        
        getCommand("gamemode").setExecutor(new GamemodeCommand(this));
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
            module.init();
            if (module.isEnabled()) {
                module.registerCommands();
                module.registerListeners();
            }
        }
        
        getServer().getPluginManager().registerEvents(new SignListener(), this);
    }
    
    public SpawnModule getSpawnModule() {
        return (SpawnModule) modules.get("spawn");
    }
    
    public PlayerStatsModule getPlayerStatsModule() {
        return (PlayerStatsModule) modules.get("playerstats");
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
    
    public boolean isPlayerInGodMode(Player player) {
        PlayerStatsModule playerStatsModule = getPlayerStatsModule();
        if (playerStatsModule == null) {
            return false;
        }
        
        return playerStatsModule.isPlayerInGodMode(player);
    }
    
    public BroadcastModule getBroadcastModule() {
        return (BroadcastModule) modules.get("broadcast");
    }
}