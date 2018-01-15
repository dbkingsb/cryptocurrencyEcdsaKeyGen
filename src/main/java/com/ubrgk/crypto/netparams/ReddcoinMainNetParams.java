package com.ubrgk.crypto.netparams;

import org.bitcoinj.params.AbstractBitcoinNetParams;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class ReddcoinMainNetParams extends AbstractBitcoinNetParams {
    private static ReddcoinMainNetParams instance;

    public ReddcoinMainNetParams() {
        super();
        dumpedPrivateKeyHeader = 189;
        addressHeader = 61;
        p2shHeader = 5;
        bip32HeaderPriv = 0x0488ADE4; //The 4 byte header that serializes in base58 to "xprv"
    }

    @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
    public static synchronized ReddcoinMainNetParams get() {
        if (instance == null) {
            instance = new ReddcoinMainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
