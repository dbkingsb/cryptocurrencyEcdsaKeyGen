package com.ubrgk.crypto.netparams;

import org.bitcoinj.params.AbstractBitcoinNetParams;

/**
 * Parameters for the main production network on which people trade goods and services.
 */
public class DogeMainNetParams extends AbstractBitcoinNetParams {
    private static DogeMainNetParams instance;

    public DogeMainNetParams() {
        super();
        dumpedPrivateKeyHeader = 158;
        addressHeader = 30;
        p2shHeader = 22;
        bip32HeaderPriv = 0x2FAC398; //The 4 byte header that serializes in base58 to "dgpv"
    }

    @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
    public static synchronized DogeMainNetParams get() {
        if (instance == null) {
            instance = new DogeMainNetParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }
}
