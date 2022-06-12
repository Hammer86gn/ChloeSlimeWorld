package me.hammer86gn.chloeslimeworld.bridge.serialization;


import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunk;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunkSection;
import me.hammer86gn.chloeslimeworld.api.utils.ByteUtil;
import me.hammer86gn.chloeslimeworld.api.utils.ZstdUtil;
import me.hammer86gn.chloeslimeworld.common.slime.SlimeChunkImpl;
import me.hammer86gn.chloeslimeworld.common.slime.SlimeChunkSectionImpl;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class InternalSerializer1_18 {

    private InternalSerializer1_18() {}

    private static SlimeChunk loadChunk(byte[] chunkData, int x, int z, AtomicInteger refPos) {

        int heightmapSize = ByteUtil.byteToInteger(chunkData, refPos.get()); refPos.set(refPos.get() + 4);
        byte[] rawHeightmap = new byte[heightmapSize];
        System.arraycopy(chunkData, refPos.get(), rawHeightmap, 0, heightmapSize); refPos.set(refPos.get() + heightmapSize);
        CompoundTag heightmap = ByteUtil.byteToCompound(rawHeightmap);

        int biomesSize = ByteUtil.byteToInteger(chunkData, refPos.get()); refPos.set(refPos.get() + 4);
        int[] biomes = new int[biomesSize];

        for (int i = 0; i < biomesSize; i++) {
            biomes[i] = ByteUtil.byteToInteger(chunkData, refPos.get()); refPos.set(refPos.get() + 4);
        }

        int minSectionY = ByteUtil.byteToInteger(chunkData, refPos.get()); refPos.set(refPos.get() + 4);
        int maxSectionY = ByteUtil.byteToInteger(chunkData, refPos.get()); refPos.set(refPos.get() + 4);
        int sectionCount = ByteUtil.byteToInteger(chunkData, refPos.get()); refPos.set(refPos.get() + 4);

        List<SlimeChunkSection> chunkSections = new ArrayList<>();

        for (int i = 0; i < sectionCount; i++) {
            chunkSections.add(InternalSerializer1_18.loadChunkSection(chunkData, refPos));
        }

        SlimeChunkImpl slimeChunk = new SlimeChunkImpl(x, z);
        slimeChunk.setHeightMap(heightmap);
        slimeChunk.setBiomes(biomes);
        slimeChunk.setMinSectionY(minSectionY);
        slimeChunk.setMaxSectionY(maxSectionY);
        slimeChunk.setSections(chunkSections);

        return slimeChunk;
    }

    private static SlimeChunkSection loadChunkSection(byte[] sectionData, AtomicInteger refPos) {


        int posY = ByteUtil.byteToInteger(sectionData, refPos.get());
        byte[] blockLight = new byte[2048];

        refPos.set(refPos.get() + 4);

        if (ByteUtil.byteToBool(sectionData[refPos.get()])) {
            System.arraycopy(sectionData, refPos.getAndIncrement(), blockLight, 0, 2048);
            refPos.set(refPos.get() + 2049);
        }
        refPos.set(refPos.get() + 1);

        int blockStatesByteSize = ByteUtil.byteToInteger(sectionData, refPos.get()); refPos.set(refPos.get() + 4);
        byte[] blockStates = new byte[blockStatesByteSize];
        System.arraycopy(sectionData, refPos.get(), blockStates, 0, blockStatesByteSize); refPos.set(refPos.get() + blockStatesByteSize);

        CompoundTag blockStatesNBT = ByteUtil.byteToCompound(blockStates); // If these are compressed, this will break... Only one way to find out

        int biomesByteSize = ByteUtil.byteToInteger(sectionData, refPos.get()); refPos.set(refPos.get() + 4);
        byte[] biomes = new byte[biomesByteSize];
        System.arraycopy(sectionData, refPos.get(), biomes, 0, biomesByteSize); refPos.set(refPos.get() + biomesByteSize);


        CompoundTag biomesNBT = ByteUtil.byteToCompound(biomes); // If these are compressed, this will break... Only one way to find out

        byte[] skylight = new byte[2048];
        if (ByteUtil.byteToBool(sectionData[refPos.get()])) {
            refPos.set(refPos.get() + 1);
            System.arraycopy(sectionData, refPos.get(), skylight, 0, 2048); refPos.set(refPos.get() + 2049);
        }

        SlimeChunkSectionImpl section = new SlimeChunkSectionImpl(posY);
        section.setBlockLight(blockLight);
        section.setBlockStatesTag(blockStatesNBT);
        section.setBiomeInfoTag(biomesNBT);
        section.setSkyLight(skylight);

        return section;
    }

    private static List<CompoundTag> loadTileEntities(byte[] data, AtomicInteger refPos) {
        List<CompoundTag> tileEntities = new ArrayList<>();

        int compressedSize = ByteUtil.byteToInteger(data, refPos.get()); refPos.set(refPos.get() + 4);
        int uncompressedSize = ByteUtil.byteToInteger(data, refPos.get()); refPos.set(refPos.get() + 4);

        byte[] rawData = new byte[compressedSize];
        System.arraycopy(data, refPos.get(), rawData, 0, compressedSize);
        refPos.set(refPos.get() + compressedSize);

        rawData = ZstdUtil.decompress(rawData, uncompressedSize);
        CompoundTag globalTag = ByteUtil.byteToCompound(rawData);
        ListTag<CompoundTag> tileEntitiesNBTList = globalTag.getList("tiles");

        for (CompoundTag tag : tileEntitiesNBTList) {
            tileEntities.add(tag);
        }

        return tileEntities;
    }

}
