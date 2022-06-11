package me.hammer86gn.chloeslimeworld.api.utils;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ZstdUtil {

    private ZstdUtil() { }

    public static byte[] decompress(byte[] data, int originalSize) {
        return Zstd.decompress(data, originalSize);
    }

}
