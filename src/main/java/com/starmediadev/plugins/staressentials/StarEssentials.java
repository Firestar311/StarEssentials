package com.starmediadev.plugins.staressentials;

import com.starmediadev.plugins.cmds.BroadcastCmd;
import com.starmediadev.plugins.cmds.PlayerActionCmd;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class StarEssentials extends JavaPlugin {
    
    /*
    Add the following commands/features
    
    ClearInv command
    flyspeed command
    walkspeed command
    gamemode command (With specific shortcuts)
    god command
    - kill command
    - killall command
    spawn features (spawn itself, setting spawn, teleporting players on first login to spawn, teleporting players to spawn always (configurable)
    editsign
    repair
    weather
    time
    homes
    warps
    enderchest
    trash
    top (go to highest block at your location)
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
    
    public void onEnable() {
        this.saveDefaultConfig();
        
        getCommand("broadcast").setExecutor(new BroadcastCmd(this));
        getCommand("feed").setExecutor(new PlayerActionCmd(this, "staressentials.command.feed", (target, self, sender) -> {
            target.setFoodLevel(20);
            target.setSaturation(10);
            if (self) {
                target.sendMessage(MCUtils.color(getConfig().getString("feed.self")));
            } else {
                sender.sendMessage(MCUtils.color(getConfig().getString("feed.other").replace("{target}", target.getName())));
                target.sendMessage(MCUtils.color(getConfig().getString("feed.target").replace("{player}", sender.getName())));
            }
        }));
        
        getCommand("fly").setExecutor(new PlayerActionCmd(this, "staressentials.command.fly", (target, self, sender) -> {
            target.setAllowFlight(!target.getAllowFlight());
            if (self) {
                sender.sendMessage(MCUtils.color(getConfig().getString("fly.self").replace("{value}", target.getAllowFlight() + "")));
            } else {
                sender.sendMessage(MCUtils.color(getConfig().getString("fly.other").replace("{target}", target.getName()).replace("{value}", target.getAllowFlight() + "")));
                target.sendMessage(MCUtils.color(getConfig().getString("fly.target").replace("{player}", sender.getName()).replace("{value}", target.getAllowFlight() + "")));
            }
        }));
        
        getCommand("heal").setExecutor(new PlayerActionCmd(this, "staressentials.command.heal", (target, self, sender) -> {
            target.setHealth(20);
            if (self) {
                target.sendMessage(MCUtils.color(getConfig().getString("heal.self")));
            } else {
                sender.sendMessage(MCUtils.color(getConfig().getString("heal.other").replace("{target}", target.getName())));
                target.sendMessage(MCUtils.color(getConfig().getString("heal.target").replace("{player}", sender.getName())));
            }
        }));
    }
}