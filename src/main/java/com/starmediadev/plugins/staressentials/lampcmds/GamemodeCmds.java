package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.GamemodeModule;
import com.starmediadev.plugins.staressentials.util.SEUtils;
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
    
        changeGameMode(sender, player, gameMode);
    }
    
    @Command("gmc")
    @CommandPermission(GAMEMODE_PERM + ".creative")
    public void gmc(CommandSender sender, @Named("target") @Optional Player player) {
        changeGameMode(sender, player, GameMode.CREATIVE);
    }
    
    @Command("gms")
    @CommandPermission(GAMEMODE_PERM + ".survival")
    public void gms(CommandSender sender, @Named("target") @Optional Player player) {
        changeGameMode(sender, player, GameMode.SURVIVAL);
    }
    
    @Command("gma")
    @CommandPermission(GAMEMODE_PERM + ".adventure")
    public void gma(CommandSender sender, @Named("target") @Optional Player player) {
        changeGameMode(sender, player, GameMode.ADVENTURE);
    }
    
    @Command("gmsp")
    @CommandPermission(GAMEMODE_PERM + ".spectator")
    public void gmsp(CommandSender sender, @Named("target") @Optional Player player) {
        changeGameMode(sender, player, GameMode.SPECTATOR);
    }
    
    private void changeGameMode(CommandSender sender, Player player, GameMode gameMode) {
        Player target = null;
        boolean self = false;
        if (sender instanceof ConsoleCommandSender) {
            if (player == null) {
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
        SEUtils.sendActionMessageValue(gamemodeModule, target, self, sender, "messages", StringHelper.capitalizeEveryWord(gameMode.name()));
    }
}
