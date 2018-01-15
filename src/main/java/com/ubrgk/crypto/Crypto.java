package com.ubrgk.crypto;

import com.ubrgk.crypto.netparams.DashMainNetParams;
import com.ubrgk.crypto.netparams.DogeMainNetParams;
import com.ubrgk.crypto.netparams.LitecoinMainNetParams;
import com.ubrgk.crypto.netparams.ReddcoinMainNetParams;
import com.ubrgk.crypto.netparams.VertcoinMainNetParams;
import org.bitcoinj.params.AbstractBitcoinNetParams;
import org.bitcoinj.params.MainNetParams;

/**
 *
 */
public enum Crypto {
    BITCOIN("BTC", MainNetParams.get()),
    LITECOIN("LTC", LitecoinMainNetParams.get()),
    DASH("DASH", DashMainNetParams.get()),
    VERTCOIN("VTC", VertcoinMainNetParams.get()),
    REDDCOIN("RDD", ReddcoinMainNetParams.get()),
    DOGECOIN("DOGE", DogeMainNetParams.get());

    private String abbreviation;
    private AbstractBitcoinNetParams netParams;

    Crypto(final String abbreviation, final AbstractBitcoinNetParams netParams) {
        this.abbreviation = abbreviation;
        this.netParams = netParams;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public AbstractBitcoinNetParams getNetParams() {
        return netParams;
    }
}
