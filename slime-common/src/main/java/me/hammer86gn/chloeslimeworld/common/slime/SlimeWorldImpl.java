package me.hammer86gn.chloeslimeworld.common.slime;

import dev.dewy.nbt.tags.collection.CompoundTag;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunk;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld;
import me.hammer86gn.chloeslimeworld.api.utils.SlimeUtil;

import java.util.ArrayList;
import java.util.List;

public class SlimeWorldImpl implements SlimeWorld {

    private SlimeUtil.WorldVersion worldVersion;

    private int width;
    private int depth;
    private int lowestX;
    private int lowestZ;

    private List<SlimeChunk> chunks = new ArrayList<>();
    private List<CompoundTag> tileEntities = new ArrayList<>();
    private List<CompoundTag> entities = new ArrayList<>();

    private CompoundTag extraNBT;

    private List<CompoundTag> worldMaps = new ArrayList<>();

    @Override
    public List<SlimeChunk> getChunks() {
        return null;
    }
}
