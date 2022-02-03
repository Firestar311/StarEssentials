package com.starmediadev.plugins.staressentials;

import com.starmediadev.plugins.staressentials.cmds.BroadcastCmd;
import com.starmediadev.plugins.staressentials.cmds.KillAllCmd;
import com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd;
import com.starmediadev.plugins.staressentials.listeners.GodListener;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd.sendActionMessage;
import static com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd.sendActionMessageValue;

public class StarEssentials extends JavaPlugin {
    /*
    Add the following commands/features
    
    flyspeed command - default .1
    walkspeed command - default .2
    gamemode command (With specific shortcuts)
    spawn features (spawn itself, setting spawn, teleporting players on first login to spawn, teleporting players to spawn always (configurable)
    editsign
    repair
    weather
    time
    homes
    warps
    - enderchest
    - trash
    - top (go to highest block at your location)
    spawnmob
    more (add more items based on what is holding)
    List (Add an API for this though and can make it based on the ranks)
    Item command (Spawn an item, will have support for StarItems, but does not replace this command)
    near
    enchant
    workbench
    Give
    Sudo (Add a hook for StarPerms/API for StarPerms to allow more control over this)
    Back
    
    Stuff to add here and add checks for the other more indepth plugins to disable
    spawner (change spawner type)
    invsee (and echest variant) will be in a moderator tools plugin eventually, this provides a very basic thing
    basic punishment commands (kick, warn, ban, tempban, mute, tempmute and kickall)
    Nicknames
    messaging commands (eventually add support for detecting StarChat), also toggle commands
    tp commands (tp, tpall, tphere, tpa, tpahere)
    Very basic economy system with Vault integration
    Ignore (StarChat overrides this)
    Socialspy
    Vanish
     */
    
    private Set<UUID> playersInGodMode = new HashSet<>();
    
    private File godmodeFile;
    private YamlConfiguration godmodeConfig;
    
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        godmodeFile = new File(getDataFolder(), "godmode.yml");
        Path godmodeFilePath = godmodeFile.toPath();
        if (Files.notExists(godmodeFilePath)) {
            try {
                Files.createFile(godmodeFilePath);
            } catch (IOException e) {
                getLogger().severe("Could not create godmode.yml: " + e.getMessage());
            }
        }
        
        godmodeConfig = YamlConfiguration.loadConfiguration(godmodeFile);
        
        List<String> rawPlayersInGodMode = godmodeConfig.getStringList("players");
        for (String entry : rawPlayersInGodMode) {
            this.playersInGodMode.add(UUID.fromString(entry));
        }
        
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
        
        getServer().getPluginManager().registerEvents(new GodListener(this), this);
    }
    
    @Override
    public void onDisable() {
        List<String> rawPlayersInGodMode = new ArrayList<>();
        this.playersInGodMode.forEach(uuid -> rawPlayersInGodMode.add(uuid.toString()));
        godmodeConfig.set("players", rawPlayersInGodMode);
        try {
            godmodeConfig.save(godmodeFile);
        } catch (IOException e) {
            getLogger().severe("Could not save godmode.yml: " + e.getMessage());
        }
    }
    
    public boolean isPlayerInGodMode(Player player) {
        return this.playersInGodMode.contains(player.getUniqueId());
    }
}