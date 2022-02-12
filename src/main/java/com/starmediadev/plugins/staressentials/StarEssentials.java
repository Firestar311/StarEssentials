package com.starmediadev.plugins.staressentials;

import com.starmediadev.plugins.staressentials.cmds.*;
import com.starmediadev.plugins.staressentials.listeners.GodListener;
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
    private Config godmodeConfig, modulesConfig;
    
    private Set<UUID> playersInGodMode = new HashSet<>();
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        godmodeConfig = new Config(this, "godmode.yml");
        godmodeConfig.setup();
        
        modulesConfig = new Config(this, "modules.yml");
        modulesConfig.setup();
        
        List<String> rawPlayersInGodMode = godmodeConfig.getConfiguration().getStringList("players");
        for (String entry : rawPlayersInGodMode) {
            this.playersInGodMode.add(UUID.fromString(entry));
        }
        
        //Non Module commands
        getCommand("broadcast").setExecutor(new BroadcastCmd(this));
        getCommand("feed").setExecutor(new PlayerActionCmd(this, "staressentials.command.feed", (target, self, sender) -> {
            target.setFoodLevel(20);
            target.setSaturation(10);
            sendActionMessage(this, target, self, sender, "feed");
        }));
        
        getCommand("fly").setExecutor(new PlayerActionCmd(this, "staressentials.command.fly", (target, self, sender) -> {
            target.setAllowFlight(!target.getAllowFlight());
            sendActionMessageValue(this, target, self, sender, "fly", target.getAllowFlight());
        }));
        
        getCommand("heal").setExecutor(new PlayerActionCmd(this, "staressentials.command.heal", (target, self, sender) -> {
            target.setHealth(20);
            sendActionMessage(this, target, self, sender, "heal");
        }));
        
        getCommand("kill").setExecutor(new PlayerActionCmd(this, "staressentials.command.kill", (target, self, sender) -> {
            target.setHealth(0);
            sendActionMessage(this, target, self, sender, "kill");
        }));
        
        getCommand("clearinv").setExecutor(new PlayerActionCmd(this, "staressentials.command.clearinv", (target, self, sender) -> {
            target.getInventory().setContents(new ItemStack[0]);
            target.getInventory().setArmorContents(new ItemStack[0]);
            target.getInventory().setExtraContents(new ItemStack[0]);
            sendActionMessage(this, target, self, sender, "clearinv");
        }));
        
        getCommand("god").setExecutor(new PlayerActionCmd(this, "staressentials.command.god", (target, self, sender) -> {
            if (playersInGodMode.contains(target.getUniqueId())) {
                playersInGodMode.remove(target.getUniqueId());
            } else {
                playersInGodMode.add(target.getUniqueId());
            }
            
            sendActionMessageValue(this, target, self, sender, "god", playersInGodMode.contains(target.getUniqueId()));
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
        
        getCommand("top").setExecutor(new PlayerActionCmd(this, "staressentials.command.top", (target, self, sender) -> {
            Block highestBlock = target.getWorld().getHighestBlockAt(target.getLocation());
            Location location = highestBlock.getLocation().clone();
            location.setY(location.getY() + 1);
            target.teleport(location);
            sendActionMessage(this, target, self, sender, "top");
        }));
        
        getCommand("gmc").setExecutor(new PlayerActionCmd(this, "staressentials.command.gamemode.creative", (target, self, sender) -> {
            target.setGameMode(GameMode.CREATIVE);
            sendActionMessageValue(this, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(target.getGameMode().name()));
        }));
        
        getCommand("gms").setExecutor(new PlayerActionCmd(this, "staressentials.command.gamemode.survival", (target, self, sender) -> {
            target.setGameMode(GameMode.SURVIVAL);
            sendActionMessageValue(this, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(target.getGameMode().name()));
        }));
        
        getCommand("gmsp").setExecutor(new PlayerActionCmd(this, "staressentials.command.gamemode.spectator", (target, self, sender) -> {
            target.setGameMode(GameMode.SPECTATOR);
            sendActionMessageValue(this, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(target.getGameMode().name()));
        }));
        
        getCommand("gma").setExecutor(new PlayerActionCmd(this, "staressentials.command.gamemode.adventure", (target, self, sender) -> {
            target.setGameMode(GameMode.ADVENTURE);
            sendActionMessageValue(this, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(target.getGameMode().name()));
        }));
        
        getCommand("gamemode").setExecutor(new GamemodeCommand(this));
        getCommand("spawnmob").setExecutor(new SpawnmobCmd(this));
        getCommand("weather").setExecutor(new WeatherCmd(this));
        getCommand("time").setExecutor(new TimeCmd(this));
        getCommand("spawner").setExecutor(new SpawnerCmd(this));
        
        //Modules
        SpawnModule spawnModule = new SpawnModule(this);
        this.modules.put(spawnModule.getName(), spawnModule);
        
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
        
        getServer().getPluginManager().registerEvents(new GodListener(this), this);
    }
    
    public SpawnModule getSpawnModule() {
        return (SpawnModule) modules.get("spawn");
    }
    
    @Override
    public void onDisable() {
        List<String> rawPlayersInGodMode = new ArrayList<>();
        this.playersInGodMode.forEach(uuid -> rawPlayersInGodMode.add(uuid.toString()));
        godmodeConfig.getConfiguration().set("players", rawPlayersInGodMode);
        godmodeConfig.save();
        
        for (StarModule<?> module : this.modules.values()) {
            modulesConfig.getConfiguration().set("modules." + module.getName() + ".enabled", module.isEnabled());
        }
        modulesConfig.save();
    
        for (StarModule<?> module : modules.values()) {
            module.save();
        }
    }
    
    public boolean isPlayerInGodMode(Player player) {
        return this.playersInGodMode.contains(player.getUniqueId());
    }
}