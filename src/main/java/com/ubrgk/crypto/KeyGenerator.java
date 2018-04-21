package com.ubrgk.crypto;

import org.bitcoinj.core.ECKey;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
final class KeyGenerator {
    private KeyGenerator() {}

    public static List<ECKey> generateKeys(final int numberOfAddress) {
        final SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }

        final List<ECKey> ecKeys = new ArrayList<>();
        for (int i=0; i<numberOfAddress; i++) {
            final ECKey ecKey = new ECKey(secureRandom);
            ecKeys.add(ecKey);
        }

        return ecKeys;
    }
}
