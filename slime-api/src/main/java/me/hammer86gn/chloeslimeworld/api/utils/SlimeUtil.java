package me.hammer86gn.chloeslimeworld.api.utils;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class SlimeUtil {

    public static final byte[] SLIME_MAGIC = { -79, 11 };
    public static final byte   SLIME_VERSION = 0x09;

    private SlimeUtil() { }

    public static byte[] isValidSlimeFile(File file) {
        if (!file.exists())
            throw new RuntimeException("SlimeWorld does not exist");

        byte[] data = null;
        try {
             data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data == null)
            throw new RuntimeException("SlimeWorld has no data");
        if (data.length == 0)
            throw new RuntimeException("SlimeWorld has no data");


        byte[] magic = {data[0], data[1]};
        if (!Arrays.equals(magic, SlimeUtil.SLIME_MAGIC))
            throw new RuntimeException("SlimeWorld is not a valid SlimeWorld");

        return data;
    }

    public static byte[] isValidSlimeFile(String path) {
        return SlimeUtil.isValidSlimeFile(new File(path));
    }

    public static boolean isCurrentSlimeVersion(byte[] data) {
        return data[2] == SlimeUtil.SLIME_VERSION;
    }

    public static int getSlimeChunkArraySize(byte[] bitmask) {
        int count = 0;
        for (byte b : bitmask) {
            for (int i = 0; i < 8; i++) {
                if ((b >> i & 1) == 1) {
                    count += 1;
                }
            }
        }
        return count;
    }

    public static @Nullable WorldVersion getWorldVersion(byte[] data) {
        return WorldVersion.of(data[3]);
    }

    public enum WorldVersion {
        V1_18((byte) 0x08),
        V1_17((byte) 0x07),
        V1_16((byte) 0x06),
        V1_14((byte) 0x05),
        V1_13((byte) 0x04),
        V1_11((byte) 0x03),
        V1_9((byte) 0x02),
        V1_8((byte) 0x01),
        ;

        private final byte version;

        WorldVersion(byte version) {
            this.version = version;
        }

        public byte getVersionByte() {
            return this.version;
        }

        public static @Nullable WorldVersion of(byte b) {
            for (WorldVersion v : WorldVersion.values()) {
                if (v.version == b)
                    return v;
            }
            return null;
        }

        public static boolean isLatest(WorldVersion version) {
            return version == WorldVersion.V1_18;
        }

        public static boolean isLegacy(WorldVersion version) {
            return switch (version) {
                case V1_8, V1_9, V1_11, V1_13, V1_14, V1_16, V1_17 -> true;
                case V1_18 -> false;
            };
        }

    }

}
