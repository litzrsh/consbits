package me.litzrsh.consbits.core.crypto;

import lombok.Getter;

public abstract class Rsa {

    public static final String RSA_INSTANCE = "RSA/ECB/PKCS1Padding";
    public static final String SIGN_INSTANCE = "RSA/ECB/PKCS1Padding";

    @Getter
    private static final RsaEncoder encoder = new RsaEncoder();
    @Getter
    private static final RsaDecoder decoder = new RsaDecoder();
}
