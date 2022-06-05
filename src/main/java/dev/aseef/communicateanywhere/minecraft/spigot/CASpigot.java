package dev.aseef.communicateanywhere.minecraft.spigot;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class CASpigot extends JavaPlugin {

    @Getter
    private static CASpigot instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
