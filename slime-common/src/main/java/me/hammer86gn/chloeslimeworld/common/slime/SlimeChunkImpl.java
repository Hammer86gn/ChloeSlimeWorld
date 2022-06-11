package me.hammer86gn.chloeslimeworld.common.slime;

import dev.dewy.nbt.tags.collection.CompoundTag;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunk;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunkSection;

import java.util.ArrayList;
import java.util.List;

public class SlimeChunkImpl implements SlimeChunk {

    private int x;
    private int z;

    private CompoundTag heightMap;
    private int[] biomes = new int[]{};

    private int minSectionY;
    private int maxSectionY;

    List<SlimeChunkSection> sections = new ArrayList<>();

    public SlimeChunkImpl(int x, int z) {
        this.x = x;
        this.z = z;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public List<SlimeChunkSection> getChunkSections() {
        return this.sections;
    }

    public void setHeightMap(CompoundTag heightMap) {
        this.heightMap = heightMap;
    }

    public void setBiomes(int[] biomes) {
        this.biomes = biomes;
    }

    public void setMinSectionY(int minSectionY) {
        this.minSectionY = minSectionY;
    }

    public void setMaxSectionY(int maxSectionY) {
        this.maxSectionY = maxSectionY;
    }

    public void setSections(List<SlimeChunkSection> sections) {
        this.sections = sections;
    }
}
