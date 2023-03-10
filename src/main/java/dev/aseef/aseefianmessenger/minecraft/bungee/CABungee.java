package dev.aseef.aseefianmessenger.minecraft.bungee;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public class CABungee extends Plugin {

    @Getter
    private static CABungee instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {

    }

}
