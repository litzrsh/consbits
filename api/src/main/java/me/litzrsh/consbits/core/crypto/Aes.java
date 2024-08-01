package me.litzrsh.consbits.core.crypto;

import lombok.Getter;

public abstract class Aes {

    public static final String AES_INSTANCE = "AES/ECB/PKCS5Padding";

    @Getter
    private static final AesEncoder encoder = new AesEncoder();
    @Getter
    private static final AesDecoder decoder = new AesDecoder();
}
