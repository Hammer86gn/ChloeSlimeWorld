package me.hammer86gn.chloeslimeworld.common.manager;

import me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld;

import java.util.HashMap;
import java.util.Map;

public class WorldManager {
    private static WorldManager instance;
    private Map<String, SlimeWorld> loadedWorlds = new HashMap<>();

    public WorldManager() {
        instance = this;
    }

    public void load(SlimeWorld world) {
        this.loadedWorlds.put(world.getWorldName(), world);
    }

    public SlimeWorld getLoadedWorld(String name) {
        return this.loadedWorlds.get(name);
    }

    public void unload(String name) {
        this.loadedWorlds.remove(name);
        // TODO(Chloe) unload world
    }

    public static WorldManager getInstance() {
        return instance == null ? new WorldManager() : instance;
    }
}
