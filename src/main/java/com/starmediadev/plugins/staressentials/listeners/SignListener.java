package com.starmediadev.plugins.staressentials.listeners;

import com.starmediadev.plugins.starmcutils.util.MCUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        if (!e.getPlayer().hasPermission("staressentials.signcolor")) {
            return;
        }
        for (int i = 0; i < e.getLines().length; i++) {
            String line = e.getLine(i);
            e.setLine(i, MCUtils.color(line));
        }
    }
}
