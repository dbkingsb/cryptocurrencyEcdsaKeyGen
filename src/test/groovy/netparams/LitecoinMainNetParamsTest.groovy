package com.ubrgk.crypto.netparams

import com.ubrgk.crypto.netparams.LitecoinMainNetParams
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Utils
import spock.lang.Specification
import spock.lang.Subject
/**
 *
 */
class LitecoinMainNetParamsTest extends Specification {
    @Subject
    LitecoinMainNetParams netParams

    void setup() {
        netParams = LitecoinMainNetParams.get()
    }

    def "Verify net params produce valid private WIF and public address"() {
        given: 'known values'
        String privateKeyHex = "F107412EA368C33B95A0FD10E3C965197A960479935007C0E29BC2D05936261A"
        String privateWif = "TB8WEEFaFyfsbcq6wfZtnHmgvfHweWAhFGfcHbEgrrJHL73uSyrN"
        String publicAddress = "LP89Aw3WSgZMhtm4Ph9Sn7nzse7GXV9eSa"

        when: 'generate an EC key from known private key'
        def privateKeyBytes = Utils.HEX.decode(privateKeyHex.toLowerCase())
        def key = ECKey.fromPrivate(privateKeyBytes)

        then: 'known wif and address are as expected'
        key.getPrivateKeyAsWiF(netParams) == privateWif
        key.toAddress(netParams).toString() == publicAddress
    }
}
