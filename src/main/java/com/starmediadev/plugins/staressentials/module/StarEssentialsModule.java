package com.starmediadev.plugins.staressentials.module;

import com.starmediadev.plugins.staressentials.StarEssentials;
import com.starmediadev.plugins.starmcutils.module.StarModule;

public abstract class StarEssentialsModule extends StarModule<StarEssentials> {
    public StarEssentialsModule(StarEssentials plugin, String name) {
        super(plugin, "modules", name);
    }
}
