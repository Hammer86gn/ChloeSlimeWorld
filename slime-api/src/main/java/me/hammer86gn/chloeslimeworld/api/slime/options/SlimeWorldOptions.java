package me.hammer86gn.chloeslimeworld.api.slime.options;

import dev.dewy.nbt.tags.collection.CompoundTag;

public class SlimeWorldOptions implements Cloneable {

    public static final SlimeWorldOptions DEFAULT = new SlimeWorldOptions();

    private CompoundTag options = new CompoundTag();

    public SlimeWorldOptions() {

    }

}
