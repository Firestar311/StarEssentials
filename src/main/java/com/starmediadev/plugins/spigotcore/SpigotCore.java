package com.starmediadev.plugins.spigotcore;

import org.bukkit.plugin.java.JavaPlugin;

public class SpigotCore extends JavaPlugin {

    /*
    Add the following commands/features
    
    Broadcast Command
    ClearInv command
    Feed Command
    Fly Command
    flyspeed command
    walkspeed command
    gamemode command (With specific shortcuts)
    god command
    heal command
    kill command
    messaging commands (eventually add support for detecting StarChat)
    Nick command (Just the name)
    spawn features (spawn itself, setting spawn, teleporting players on first login to spawn, teleporting players to spawn always (configurable)
    tp commands (tp, tpall, tphere, tpa, tpahere)
     */
    
    public void onEnable() {
        this.saveDefaultConfig();
    }
}