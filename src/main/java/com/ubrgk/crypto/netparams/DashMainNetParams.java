package com.ubrgk.crypto.netparams;

import org.bitcoinj.params.AbstractBitcoinNetParams;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class DashMainNetParams extends AbstractBitcoinNetParams {
    private static DashMainNetParams instance;

    public DashMainNetParams() {
        super();
        dumpedPrivateKeyHeader = 204;
        addressHeader = 76;
        p2shHeader = 16;
        bip32HeaderPriv = 0x2FE52CC; //The 4 byte header that serializes in base58 to "drkp"
    }

    @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
    public static synchronized DashMainNetParams get() {
        if (instance == null) {
            instance = new DashMainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
