package me.hammer86gn.chloeslimeworld.api.slime.serialization;

import me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld;
import me.hammer86gn.chloeslimeworld.api.slime.exception.SlimeWorldSerializationFailedException;
import me.hammer86gn.chloeslimeworld.api.slime.options.SlimeWorldOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface WorldSerializer {

    /**
     * Loads a {@link me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld} into memory
     *
     * @param worldName The name of the {@link me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld}
     * @param readOnly If <code>true</code> any edits made to the world will be discarded after being unloaded
     * @param convert If <code>true</code> older worlds will be converted to new worlds
     * @param options The options for the {@link me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld}
     * @return The {@link me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld} if the loading was successful, otherwise <code>null</code>
     */
    @Nullable SlimeWorld loadSlimeWorld(@NotNull String worldName, boolean readOnly, boolean convert, @NotNull SlimeWorldOptions options);

    /**
     * Saves a {@link me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld}
     *
     * @param world The {@link me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld} to save
     * @return <code>true</code> if the {@link me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld} was successfully saved
     * @throws SlimeWorldSerializationFailedException If the {@link me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld} is <code>readOnly</code>
     */
    boolean saveSlimeWorld(@NotNull SlimeWorld world) throws SlimeWorldSerializationFailedException;

}
