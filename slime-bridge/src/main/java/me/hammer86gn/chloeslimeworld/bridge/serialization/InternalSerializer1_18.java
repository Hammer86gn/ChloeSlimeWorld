package me.hammer86gn.chloeslimeworld.bridge.serialization;


import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunk;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunkSection;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeWorld;
import me.hammer86gn.chloeslimeworld.api.slime.options.SlimeWorldOptions;
import me.hammer86gn.chloeslimeworld.api.utils.ByteUtil;
import me.hammer86gn.chloeslimeworld.api.utils.SlimeUtil;
import me.hammer86gn.chloeslimeworld.api.utils.ZstdUtil;
import me.hammer86gn.chloeslimeworld.common.slime.SlimeChunkImpl;
import me.hammer86gn.chloeslimeworld.common.slime.SlimeChunkSectionImpl;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;
import me.hammer86gn.chloeslimeworld.common.slime.SlimeWorldImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class InternalSerializer1_18 {

    private InternalSerializer1_18() {}

    static SlimeWorld load(byte[] worldData, String name, SlimeWorldOptions options, boolean readOnly, AtomicInteger refPos) {
        SlimeWorld world;
        List<SlimeChunk> chunks = new ArrayList<>();
        List<CompoundTag> tileEntities = new ArrayList<>();
        List<CompoundTag> entities = new ArrayList<>();
        CompoundTag extra;
        List<CompoundTag> worldMaps = new ArrayList<>();

        int lowestX, lowestZ;
        lowestX = ByteUtil.byteToShort(worldData, refPos.get()); refPos.set(refPos.get() + 2);
        lowestZ = ByteUtil.byteToShort(worldData, refPos.get()); refPos.set(refPos.get() + 2);

        int width, depth;
        width = ByteUtil.byteToUShortAsInt(worldData, refPos.get()); refPos.set(refPos.get() + 2);
        depth = ByteUtil.byteToUShortAsInt(worldData, refPos.get()); refPos.set(refPos.get() + 2);

        int bitmaskSize = (int) Math.ceil((float) (width * depth) / 8);
        byte[] bitmask = new byte[bitmaskSize];
        System.arraycopy(worldData, refPos.get(), bitmask, 0, bitmaskSize);
        refPos.set(refPos.get() + bitmaskSize);

        int uncompressed, compressed;
        uncompressed = ByteUtil.byteToShort(worldData, refPos.get()); refPos.set(refPos.get() + 2);
        compressed = ByteUtil.byteToShort(worldData, refPos.get()); refPos.set(refPos.get() + 2);

        byte[] compressedChunks = new byte[compressed];
        int chunkArraySize = SlimeUtil.getSlimeChunkArraySize(bitmask);
        System.arraycopy(worldData, refPos.get(), compressedChunks, 0, chunkArraySize);
        refPos.set(refPos.get() + chunkArraySize);

        byte[] chunkData = ZstdUtil.decompress(compressedChunks, uncompressed);

        int chunkX = lowestX;
        int chunkZ = lowestZ;

        for (int i = 0; i < chunkArraySize; i++) {
            int bitmaskIndex = chunkX + chunkZ * 8;

            if (SlimeUtil.slimeChunkDataEmpty(bitmask[bitmaskIndex], chunkX + chunkZ)) {
                chunks.add(InternalSerializer1_18.loadChunk(chunkData, chunkX, chunkZ, refPos));
            }  else {
                i -= 1;
            }
            chunkZ += 1;

            if (chunkZ == depth) {
                chunkZ = 0;
                chunkX += 1;
            }

        }

        tileEntities = InternalSerializer1_18.loadTileEntities(worldData, refPos);
        entities = InternalSerializer1_18.loadEntities(worldData, refPos);
        extra = InternalSerializer1_18.loadExtra(worldData, refPos);
        worldMaps = InternalSerializer1_18.loadWorldMaps(worldData, refPos);

        world = new SlimeWorldImpl(name, SlimeUtil.WorldVersion.V1_18, chunks, tileEntities, entities, extra, worldMaps, options, readOnly);
        return world;
    }

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

        byte[] decompressed = new byte[uncompressedSize];
        decompressed = ZstdUtil.decompress(rawData, uncompressedSize);
        CompoundTag globalTag = ByteUtil.byteToCompound(decompressed);
        ListTag<CompoundTag> tileEntitiesNBTList = globalTag.getList("tiles");

        for (CompoundTag tag : tileEntitiesNBTList) {
            tileEntities.add(tag);
        }

        return tileEntities;
    }

    private static List<CompoundTag> loadEntities(byte[] data, AtomicInteger refPos) {
        List<CompoundTag> entities = new ArrayList<>();

        int compressedSize = ByteUtil.byteToInteger(data, refPos.get()); refPos.set(refPos.get() + 4);
        int uncompressedSize = ByteUtil.byteToInteger(data, refPos.get()); refPos.set(refPos.get() + 4);

        byte[] rawEntity = new byte[compressedSize];
        System.arraycopy(data, refPos.get(), rawEntity, 0, compressedSize);
        refPos.set(refPos.get() + compressedSize);

        byte[] decompressed = ZstdUtil.decompress(rawEntity, uncompressedSize);
        CompoundTag globalNBT = ByteUtil.byteToCompound(decompressed);
        ListTag<CompoundTag> ents = globalNBT.getList("entities");

        for (CompoundTag tag : ents) {
            entities.add(tag);
        }

        return entities;
    }

    private static CompoundTag loadExtra(byte[] data, AtomicInteger refPos) {
        int compressedSize = ByteUtil.byteToInteger(data, refPos.get()); refPos.set(refPos.get() + 4);
        int uncompressedSize = ByteUtil.byteToInteger(data, refPos.get()); refPos.set(refPos.get() + 4);

        byte[] rawExtra = new byte[compressedSize];
        System.arraycopy(data, refPos.get(), rawExtra, 0, compressedSize);
        refPos.set(refPos.get() + compressedSize);

        byte[] decompress = ZstdUtil.decompress(rawExtra, uncompressedSize);

        return ByteUtil.byteToCompound(decompress);
    }

    private static List<CompoundTag> loadWorldMaps(byte[] data, AtomicInteger refPos) {
        List<CompoundTag> worldMapsNBT = new ArrayList<>();

        int compressedSize = ByteUtil.byteToInteger(data, refPos.get()); refPos.set(refPos.get() + 4);
        int uncompressedSize = ByteUtil.byteToInteger(data, refPos.get()); refPos.set(refPos.get() + 4);

        byte[] worldMap = new byte[compressedSize];
        System.arraycopy(data, refPos.get(), worldMap, 0, compressedSize);
        refPos.set(refPos.get() + compressedSize);

        byte[] decompressed = ZstdUtil.decompress(worldMap, uncompressedSize);
        CompoundTag globalTag = ByteUtil.byteToCompound(decompressed);

        ListTag<CompoundTag> maps = globalTag.getList("maps");
        for (CompoundTag tag : maps) {
            worldMapsNBT.add(tag);
        }

        return worldMapsNBT;
    }

}
