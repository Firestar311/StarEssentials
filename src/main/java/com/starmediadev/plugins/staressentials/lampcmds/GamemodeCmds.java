package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.GamemodeModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;

public class GamemodeCmds {
    @Dependency
    private GamemodeModule gamemodeModule;
    
    public void gamemode(CommandSender sender, @Named("gamemode") String gamemode, @Named("target") @Optional Player player) {
        
    }
}
