package me.hammer86gn.chloeslimeworld.bridge.serialization;

import me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld;
import me.hammer86gn.chloeslimeworld.api.slime.exception.SlimeWorldSerializationFailedException;
import me.hammer86gn.chloeslimeworld.api.slime.options.SlimeWorldOptions;
import me.hammer86gn.chloeslimeworld.api.slime.serialization.WorldSerializer;
import me.hammer86gn.chloeslimeworld.api.utils.SlimeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class SlimeWorldSerializerImpl implements WorldSerializer {

    private static final String SLIME_WORLD_DIRECTORY = "SlimeWorlds/";

    @Override
    public @Nullable SlimeWorld loadSlimeWorld(@NotNull String worldName, boolean readOnly, boolean convert, @NotNull SlimeWorldOptions options) {
        SlimeWorld slimeWorld = null;
        File worldFile = new File(SLIME_WORLD_DIRECTORY + worldName + ".slime");
        byte[] worldData = SlimeUtil.isValidSlimeFile(worldFile);

        SlimeUtil.WorldVersion worldVersion;

        if (!SlimeUtil.isCurrentSlimeVersion(worldData)) {
            // TODO handle this
            System.err.println("Legacy SlimeWorld format has not been implemented");
            return null;
        }
        worldVersion = SlimeUtil.getWorldVersion(worldData);

        switch(worldVersion) {
            case V1_18 -> {
                slimeWorld = InternalSerializer1_18.load(worldData, worldName, options, readOnly, new AtomicInteger(4));
            }
            default -> System.out.println("Support for legacy worlds has not been Implemented");
        }



        return slimeWorld;
    }

    @Override
    public boolean saveSlimeWorld(@NotNull SlimeWorld world) throws SlimeWorldSerializationFailedException {
        return false;
    }
}
