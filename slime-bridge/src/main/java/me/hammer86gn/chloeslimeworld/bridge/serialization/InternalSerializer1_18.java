package me.hammer86gn.chloeslimeworld.bridge.serialization;


import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunkSection;
import me.hammer86gn.chloeslimeworld.api.utils.ByteUtil;
import me.hammer86gn.chloeslimeworld.common.slime.SlimeChunkSectionImpl;
import dev.dewy.nbt.tags.collection.CompoundTag;

class InternalSerializer1_18 {

    private InternalSerializer1_18() {}


    private static SlimeChunkSection loadChunkSection(byte[] sectionData) {
        int posY = ByteUtil.byteToInteger(sectionData, 0);
        byte[] blockLight = new byte[2048];

        int refPos = 4;

        if (ByteUtil.byteToBool(sectionData[4])) {
            System.arraycopy(sectionData, 4, blockLight, 0, 2048);
            refPos += 2049;
        }

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

}
