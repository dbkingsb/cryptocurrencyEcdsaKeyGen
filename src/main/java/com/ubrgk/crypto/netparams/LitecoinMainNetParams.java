package com.ubrgk.crypto.netparams;

import org.bitcoinj.params.AbstractBitcoinNetParams;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class LitecoinMainNetParams extends AbstractBitcoinNetParams {
    private static LitecoinMainNetParams instance;

    public LitecoinMainNetParams() {
        super();
        dumpedPrivateKeyHeader = 176;
        addressHeader = 48;
        p2shHeader = 5;
        bip32HeaderPriv = 0x0488ADE4; //The 4 byte header that serializes in base58 to "xprv"
    }

    @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
    public static synchronized LitecoinMainNetParams get() {
        if (instance == null) {
            instance = new LitecoinMainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
