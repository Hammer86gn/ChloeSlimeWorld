package me.hammer86gn.chloeslimeworld.api;

import java.io.File;

public interface SlimeBridge {

    default void initSlime() {
        File file = new File("SlimeWorlds");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
