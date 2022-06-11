package me.hammer86gn.chloeslimeworld.common.slime;

import dev.dewy.nbt.tags.collection.CompoundTag;
import me.hammer86gn.chloeslimeworld.api.slime.SlimeChunkSection;


public class SlimeChunkSectionImpl implements SlimeChunkSection {

    private int y;

    // I don't know if this works like this
    private byte[] blockLight;
    private byte[] skyLight;
    // end

    private CompoundTag blockStatesTag;
    private CompoundTag biomeInfoTag;

    public SlimeChunkSectionImpl(int y) {
        this.y =y;
    }

    @Override
    public int getY() {
        return 0;
    }


    public void setBlockLight(byte[] blockLight) {
        this.blockLight = blockLight;
    }

    public void setSkyLight(byte[] skyLight) {
        this.skyLight = skyLight;
    }

    public void setBlockStatesTag(CompoundTag blockStatesTag) {
        this.blockStatesTag = blockStatesTag;
    }

    public void setBiomeInfoTag(CompoundTag biomeInfoTag) {
        this.biomeInfoTag = biomeInfoTag;
    }
}
