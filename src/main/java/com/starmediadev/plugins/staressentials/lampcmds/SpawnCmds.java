package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.SpawnModule;
import com.starmediadev.plugins.staressentials.util.SEUtils;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import com.starmediadev.utils.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class SpawnCmds {
    
    @Dependency
    private SpawnModule spawnModule;
    
    @Command("spawn")
    @Description("Go to the spawnpoint")
    public void spawnTeleport(CommandSender sender, @Named("player") @Optional Player player) {
        Pair<Player, Boolean> pair = SEUtils.getPlayerTarget(sender, player, "staressentials.spawn");
        Player target = pair.getValue1();
        boolean self = pair.getValue2();
        
        target.teleport(spawnModule.getSpawn());
        SEUtils.sendActionMessage(spawnModule, target, self, sender, "messages");
    }
    
    @Command("setspawn")
    @Description("Sets the spawnpoint of the server")
    @CommandPermission("staressentials.command.setspawn")
    public void setSpawn(Player player) {
        spawnModule.setSpawn(player.getLocation());
        player.sendMessage(MCUtils.color(spawnModule.getConfig().getConfiguration().getString("messages.set")));
    }
}
