package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.cmds.PlayerActionCmd;
import com.starmediadev.plugins.staressentials.module.GamemodeModule;
import com.starmediadev.utils.helper.StringHelper;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

public class GamemodeCmds {
    @Dependency
    private GamemodeModule gamemodeModule;
    
    private static final String GAMEMODE_PERM = "staressentials.command.gamemode";
    
    @Command({"gamemode", "gm"})
    @CommandPermission(GAMEMODE_PERM)
    public void gamemode(CommandSender sender, @Named("gamemode") String gamemode, @Named("target") @Optional Player player) {
        GameMode gameMode = switch (gamemode.toLowerCase()) {
            case "creative", "c", "1" -> GameMode.CREATIVE;
            case "survival", "s", "0" -> GameMode.SURVIVAL;
            case "adventure", "a", "2" -> GameMode.ADVENTURE;
            case "spectator", "sp", "3" -> GameMode.SPECTATOR;
            default -> null;
        };
    
        if (gameMode == null) {
            throw new CommandErrorException("&cYou provided an invalid gamemode.");
        }
        
        if (!sender.hasPermission(GAMEMODE_PERM + "." + gameMode.name().toLowerCase())) {
            throw new CommandErrorException("You do not have permission to use that gamemode.");
        }
    
        Player target = null;
        boolean self = false;
        if (sender instanceof ConsoleCommandSender) {
            if (player != null) {
                throw new CommandErrorException("You must provide a player as the console");
            }
            target = player;
        } else if (sender instanceof Player p) {
            if (player == null) {
                target = p;
                self = true;
            } else {
                if (!sender.hasPermission("staressentials.command.gamemode." + gameMode.name().toLowerCase() + ".others")) {
                    throw new CommandErrorException("You do not have permission to set other player's gamemode to " + gameMode.name().toLowerCase());
                }
                target = player;
            }
        }
    
        if (target == null) {
            throw new CommandErrorException("You provided an invalid target.");
        }
    
        target.setGameMode(gameMode);
        PlayerActionCmd.sendActionMessageValue(gamemodeModule, target, self, sender, "gamemode", StringHelper.capitalizeEveryWord(gameMode.name()));
    }
}
