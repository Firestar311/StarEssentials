package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.KillModule;
import com.starmediadev.plugins.staressentials.util.SEUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

public class KillCmd {
    
    @Dependency
    private KillModule module;
    
    @Command("kill")
    @CommandPermission("staressentials.command.kill")
    public void kill(CommandSender sender, @Named("target") @Optional Player target) {
        if (sender instanceof ConsoleCommandSender && target == null) {
            throw new CommandErrorException("You must provide a player to kill as the console");
        }
        
        Player player;
        boolean self = false;
        if (sender instanceof Player playerSender) {
            if (target != null) {
                player = target;
            } else {
                player = playerSender;
                self = true;
            }
        } else if (sender instanceof ConsoleCommandSender) {
            player = target;
        } else {
            throw new CommandErrorException("Only the console and players can use that command.");
        }
        
        player.setHealth(0);
        SEUtils.sendActionMessage(module, player, self, sender, "messages");
    }
}
