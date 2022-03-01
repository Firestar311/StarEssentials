package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.SignEditModule;
import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

@Command("signedit")
@CommandPermission("staressentials.command.signedit")
public class SignEditCmds {
    
    @Dependency
    private SignEditModule module;
    
    @Subcommand({"setline", "sl"})
    @CommandPermission("staressentials.command.signedit.setline")
    public void setLine(Player player, @Named("line") @Range(min = 1, max = 4) int line, @Named("text") String text) {
        Sign sign = getSign(player);
        line -= 1;
    
        if (text.length() > 16) {
            throw new CommandErrorException("The text you provided is greater than 16 characters.");
        }
        
        sign.setLine(line, MCUtils.color(text));
        sign.update();
        if (text.isEmpty() || text.isBlank() || text.equals("")) {
            player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("messages.clearline").replace("{line}", (line + 1) + "")));
        } else {
            player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("messages.setline").replace("{line}", (line + 1) + "").replace("{text}", text)));
        }
    }
    
    @Subcommand({"setglowing", "sg"})
    @CommandPermission("staressentials.command.signedit.setglowing")
    public void setGlowing(Player player, @Named("value") boolean value) {
        Sign sign = getSign(player);
        sign.setGlowingText(value);
        sign.update();
        player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("messages.setglowing").replace("{value}", value + "")));
    }
    
    @Subcommand({"setcolor", "sc"})
    @CommandPermission("staressentials.command.signedit.setcolor")
    public void setColor(Player player, @Named("dyecolor") String color) {
        DyeColor dyeColor = DyeColor.valueOf(color.toUpperCase());
        Sign sign = getSign(player);
        sign.setColor(dyeColor);
        sign.update();
        player.sendMessage(MCUtils.color(module.getConfig().getConfiguration().getString("messages.setcolor").replace("{color}", dyeColor.name())));
    }
    
    private Sign getSign(Player player) {
        Block block = player.getTargetBlock(null, 50);
        if (block == null) {
            throw new CommandErrorException("You are not looking at a block.");
        }
    
        if (!(block.getState() instanceof Sign sign)) {
            throw new CommandErrorException("&cYou are not looking at a sign.");
        }
        return sign;
    }
}