package com.starmediadev.plugins.staressentials.cmds;

import com.starmediadev.plugins.staressentials.StarEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public record DelHomeCmd(StarEssentials plugin) implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return true;
    }
}
