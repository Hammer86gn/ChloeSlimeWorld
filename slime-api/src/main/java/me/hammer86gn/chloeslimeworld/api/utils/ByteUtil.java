package me.hammer86gn.chloeslimeworld.api.utils;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.IOException;

public class ByteUtil {

    private static final Nbt NBT = new Nbt();

    private ByteUtil() { }

    public static CompoundTag byteToCompound(byte[] bytes) {
        try {
            return NBT.fromByteArray(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CompoundTag();
    }

    public static long byteToLong(byte[] bytes, int start) {
        return (long) ((long) bytes[start] + (long) bytes[start + 1] + (long) bytes[start + 2] + (long) bytes[start + 3] + (long) bytes[start + 4] + (long) bytes[start + 5] + (long) bytes[start + 6] + (long) bytes[start + 7]);
    }

    public static int byteToInteger(byte[] bytes, int start) {
        return (int) ((int) bytes[start] + (int) bytes[start + 1] + (int) bytes[start + 2] + (int) bytes[start + 3]);
    }

    public static int byteToUShortAsInt(byte[] bytes, int start) {
        return (int) ((int) bytes[start] + (int) bytes[start + 1]);
    }

    public static short byteToShort(byte[] bytes, int start) {
        return (short) ((short) bytes[start] + (short) bytes[start + 1]);
    }

    public static boolean byteToBool(byte bytes) {
        return bytes != 0;
    }
}
