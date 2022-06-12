package me.hammer86gn.chloeslimeworld.common.slime;

import dev.dewy.nbt.tags.collection.CompoundTag;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunk;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld;
import me.hammer86gn.chloeslimeworld.api.slime.options.SlimeWorldOptions;
import me.hammer86gn.chloeslimeworld.api.utils.SlimeUtil;
import me.hammer86gn.chloeslimeworld.common.manager.WorldManager;

import java.util.ArrayList;
import java.util.List;

public class SlimeWorldImpl implements SlimeWorld {

    private String name;
    private SlimeUtil.WorldVersion worldVersion;
    private boolean readOnly = false;
    private SlimeWorldOptions worldOptions;

    private List<SlimeChunk> chunks = new ArrayList<>();
    private List<CompoundTag> tileEntities = new ArrayList<>();
    private List<CompoundTag> entities = new ArrayList<>();

    private CompoundTag extraNBT;

    private List<CompoundTag> worldMaps = new ArrayList<>();


    public SlimeWorldImpl(String name, SlimeUtil.WorldVersion version, List<SlimeChunk> chunks, List<CompoundTag> tileEntities,
                          List<CompoundTag> entities, CompoundTag extra, List<CompoundTag> worldMaps,
                          SlimeWorldOptions options, boolean readOnly) {
        this.name = name;
        this.worldVersion = version;
        this.chunks = chunks;
        this.tileEntities = tileEntities;
        this.entities = entities;
        this.extraNBT = extra;
        this.worldMaps = worldMaps;
        this.worldOptions = options;
        this.readOnly = readOnly;

        WorldManager.getInstance().load(this);
    }

    public SlimeWorldImpl(String name, SlimeUtil.WorldVersion version, List<SlimeChunk> chunks, List<CompoundTag> tileEntities,
                          List<CompoundTag> entities, CompoundTag extra, List<CompoundTag> worldMaps,
                          SlimeWorldOptions options) {
        this(name, version, chunks, tileEntities, entities, extra, worldMaps, options, false);
    }

    public SlimeWorldImpl(String name, SlimeUtil.WorldVersion version, List<SlimeChunk> chunks,
                          List<CompoundTag> tileEntities, List<CompoundTag> entities, CompoundTag extra,
                          List<CompoundTag> worldMaps) {
        this(name, version, chunks, tileEntities, entities, extra, worldMaps, SlimeWorldOptions.DEFAULT);
    }

    @Override
    public String getWorldName() {
        return this.name;
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override
    public SlimeUtil.WorldVersion getWorldVersion() {
        return this.worldVersion;
    }

    @Override
    public List<SlimeChunk> getChunks() {
        return this.chunks;
    }

    @Override
    public List<CompoundTag> getTileEntities() {
        return this.tileEntities;
    }

    @Override
    public List<CompoundTag> getEntities() {
        return this.entities;
    }

    @Override
    public List<CompoundTag> getWorldMaps() {
        return this.worldMaps;
    }

    @Override
    public CompoundTag getExtra() {
        return this.extraNBT;
    }
}
