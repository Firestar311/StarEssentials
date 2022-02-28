package com.starmediadev.plugins.staressentials.util;

import com.starmediadev.utils.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.exception.CommandErrorException;

public final class SEUtils {
    public static Pair<Player, Boolean> checkPlayerStatsCmdConditions(CommandSender sender, Player player, String permission) {
        if (sender instanceof ConsoleCommandSender && player == null) {
            throw new CommandErrorException("You must provide a player as the console");
        }
    
        Player target;
        boolean self = false;
        if (player == null) {
            target = (Player) sender;
            self = true;
        } else {
            target = player;
            if (!sender.hasPermission(permission + ".other")) {
                throw new CommandErrorException("You do not have permission to do that to other players.");
            }
        }
        return new Pair<>(target, self);
    }
}
