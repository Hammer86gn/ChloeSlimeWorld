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

class InternalSerializer1_18 {

    private InternalSerializer1_18() {}

    private static SlimeChunk loadChunk(byte[] chunkData, int x, int z, int refPos) {

        int heightmapSize = ByteUtil.byteToInteger(chunkData, refPos); refPos += 4;
        byte[] rawHeightmap = new byte[heightmapSize];
        System.arraycopy(chunkData, refPos, rawHeightmap, 0, heightmapSize); refPos += heightmapSize;
        CompoundTag heightmap = ByteUtil.byteToCompound(rawHeightmap);

        int biomesSize = ByteUtil.byteToInteger(chunkData, refPos); refPos += 4;
        int[] biomes = new int[biomesSize];

        for (int i = 0; i < biomesSize; i++) {
            biomes[i] = ByteUtil.byteToInteger(chunkData, refPos); refPos += 4;
        }

        int minSectionY = ByteUtil.byteToInteger(chunkData, refPos); refPos += 4;
        int maxSectionY = ByteUtil.byteToInteger(chunkData, refPos); refPos += 4;
        int sectionCount = ByteUtil.byteToInteger(chunkData, refPos); refPos += 4;

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

    private static SlimeChunkSection loadChunkSection(byte[] sectionData, int refPos) {


        int posY = ByteUtil.byteToInteger(sectionData, refPos);
        byte[] blockLight = new byte[2048];

        refPos += 4;

        if (ByteUtil.byteToBool(sectionData[refPos])) {
            System.arraycopy(sectionData, refPos + 1, blockLight, 0, 2048);
            refPos += 2049;
        }
        refPos += 1;

        int blockStatesByteSize = ByteUtil.byteToInteger(sectionData, refPos); refPos += 4;
        byte[] blockStates = new byte[blockStatesByteSize];
        System.arraycopy(sectionData, refPos, blockStates, 0, blockStatesByteSize); refPos += blockStatesByteSize;

        CompoundTag blockStatesNBT = ByteUtil.byteToCompound(blockStates); // If these are compressed, this will break... Only one way to find out

        int biomesByteSize = ByteUtil.byteToInteger(sectionData, refPos); refPos += 4;
        byte[] biomes = new byte[biomesByteSize];
        System.arraycopy(sectionData, refPos, biomes, 0, biomesByteSize); refPos += biomesByteSize;


        CompoundTag biomesNBT = ByteUtil.byteToCompound(biomes); // If these are compressed, this will break... Only one way to find out

        byte[] skylight = new byte[2048];
        if (ByteUtil.byteToBool(sectionData[refPos])) {
            refPos += 1;
            System.arraycopy(sectionData, refPos, skylight, 0, 2048); refPos += 2049;
        }

        SlimeChunkSectionImpl section = new SlimeChunkSectionImpl(posY);
        section.setBlockLight(blockLight);
        section.setBlockStatesTag(blockStatesNBT);
        section.setBiomeInfoTag(biomesNBT);
        section.setSkyLight(skylight);

        return section;
    }

    private static List<CompoundTag> loadTileEntities(byte[] data, int refPos) {
        List<CompoundTag> tileEntities = new ArrayList<>();

        int compressedSize = ByteUtil.byteToInteger(data, refPos); refPos += 4;
        int uncompressedSize = ByteUtil.byteToInteger(data, refPos); refPos += 4;

        byte[] rawData = new byte[compressedSize];
        for (int i = 0; i < compressedSize; i++) {
            rawData[i] = data[refPos + i];
        } refPos += compressedSize;

        rawData = ZstdUtil.decompress(rawData, uncompressedSize);
        CompoundTag globalTag = ByteUtil.byteToCompound(rawData);
        ListTag<CompoundTag> tileEntitiesNBTList = globalTag.getList("tiles");

        for (CompoundTag tag : tileEntitiesNBTList) {
            tileEntities.add(tag);
        }

        return tileEntities;
    }

}
