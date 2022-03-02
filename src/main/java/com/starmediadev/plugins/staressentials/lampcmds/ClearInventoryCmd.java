package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.ClearInvModule;
import com.starmediadev.plugins.staressentials.util.SEUtils;
import com.starmediadev.utils.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class ClearInventoryCmd {
    
    @Dependency
    private ClearInvModule module;
    
    @Command({"clearinventory", "clearinv"})
    @CommandPermission("staressentials.command.clearinventory")
    public void clearInventory(CommandSender sender, @Named("target") @Optional Player target) {
        Pair<Player, Boolean> pair = SEUtils.getPlayerTarget(sender, target, "staressentials.command.clearinventory");
        Player player = pair.getValue1();
        player.getInventory().setContents(new ItemStack[0]);
        player.getInventory().setArmorContents(new ItemStack[0]);
        player.getInventory().setExtraContents(new ItemStack[0]);
        SEUtils.sendActionMessage(module, player, pair.getValue2(), sender, "messages");
    }
}
