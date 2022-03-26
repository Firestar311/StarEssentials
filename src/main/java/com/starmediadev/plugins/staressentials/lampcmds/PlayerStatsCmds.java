package com.starmediadev.plugins.staressentials.lampcmds;

import com.starmediadev.plugins.staressentials.module.PlayerStatsModule;
import com.starmediadev.plugins.staressentials.util.SEUtils;
import com.starmediadev.utils.Pair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.starmediadev.plugins.staressentials.util.SEUtils.sendActionMessage;
import static com.starmediadev.plugins.staressentials.util.SEUtils.sendActionMessageValue;

public class PlayerStatsCmds {
    
    @Dependency
    private PlayerStatsModule module;
    
    private static final String FEED_PERM = "staressentials.command.feed";
    
    @Command("feed")
    @CommandPermission(FEED_PERM)
    public void feed(CommandSender sender, @Named("target") @Optional Player player) {
        Pair<Player, Boolean> pair = SEUtils.getPlayerTarget(sender, player, FEED_PERM);
        Player target = pair.getValue1();
        boolean self = pair.getValue2();
        
        target.setFoodLevel(20);
        target.setSaturation(10);
        sendActionMessage(module, target, self, sender, "settings.feed");
    }
    
    private static final String FLY_PERM = "staressentials.command.fly";
    
    @Command("fly")
    @CommandPermission(FLY_PERM)
    public void fly(CommandSender sender, @Named("target") @Optional Player player) {
        Pair<Player, Boolean> pair = SEUtils.getPlayerTarget(sender, player, FLY_PERM);
        Player target = pair.getValue1();
        boolean self = pair.getValue2();
        
        target.setAllowFlight(!target.getAllowFlight());
        sendActionMessageValue(module, target, self, sender, "settings.fly", target.getAllowFlight());
    }
    
    private static final String HEAL_PERM = "staressentials.command.heal";
    
    @Command("heal")
    @CommandPermission(HEAL_PERM)
    public void heal(CommandSender sender, @Named("target") @Optional Player player) {
        Pair<Player, Boolean> pair = SEUtils.getPlayerTarget(sender, player, HEAL_PERM);
        Player target = pair.getValue1();
        boolean self = pair.getValue2();
        
        target.setHealth(20);
        sendActionMessage(module, target, self, sender, "settings.heal");
    }
    
    private static final String GOD_PERM = "staressentials.command.god";
    
    @Command("god")
    @CommandPermission(GOD_PERM)
    public void god(CommandSender sender, @Named("target") @Optional Player player) {
        Pair<Player, Boolean> pair = SEUtils.getPlayerTarget(sender, player, GOD_PERM);
        Player target = pair.getValue1();
        boolean self = pair.getValue2();
        
        boolean value = module.toggleGod(target);
        sendActionMessageValue(module, target, self, sender, "settings.god", value);
    }
    
    private static final String WORKBENCH_PERM = "staressentials.command.workbench";
    
    @Command({"workbench", "wb"})
    @CommandPermission(WORKBENCH_PERM)
    public void workbench(Player player) {
        player.openWorkbench(player.getLocation(), true);
    }
}
