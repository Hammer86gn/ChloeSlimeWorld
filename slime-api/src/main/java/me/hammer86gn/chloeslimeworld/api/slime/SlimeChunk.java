package me.hammer86gn.chloeslimeworld.api.slime;

import java.util.List;

public interface SlimeChunk {

    int getX();
    int getZ();

    List<SlimeChunkSection> getChunkSections();

}
