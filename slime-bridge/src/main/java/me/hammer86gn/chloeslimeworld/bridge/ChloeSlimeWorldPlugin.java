package me.hammer86gn.chloeslimeworld.bridge;

import me.hammer86gn.chloeslimeworld.api.SlimeBridge;
import org.bukkit.plugin.java.JavaPlugin;

public class ChloeSlimeWorldPlugin extends JavaPlugin implements SlimeBridge {
    @Override
    public void onEnable() {
        this.initSlime();
    }
}
