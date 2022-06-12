package me.hammer86gn.chloeslimeworld.api.slime;

import dev.dewy.nbt.tags.collection.CompoundTag;
import me.hammer86gn.chloeslimeworld.api.utils.SlimeUtil;

import java.util.List;

public interface SlimeWorld {

    String getWorldName();
    boolean isReadOnly();
    SlimeUtil.WorldVersion getWorldVersion();

    List<SlimeChunk> getChunks();
    List<CompoundTag> getTileEntities();
    List<CompoundTag> getEntities();
    List<CompoundTag> getWorldMaps();

    CompoundTag getExtra();

}
