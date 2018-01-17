package com.ubrgk.crypto.netparams

import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Utils
import spock.lang.Specification
import spock.lang.Subject
/**
 *
 */
class DashMainNetParamsTest extends Specification {
    @Subject
    DashMainNetParams netParams

    void setup() {
        netParams = DashMainNetParams.get()
    }

    def "Verify net params produce valid private WIF and public address"() {
        given: 'known values'
        String privateKeyHex = "8BC342F788E735F2813EE40AB72C45AD229F0F29CDB69727A62C0F6008720334"
        String privateWif = "XFyK794gFPa65V4zb1eK1wJpqFZmKyF8jxWCnFjpVrd8BGNb5B99"
        String publicAddress = "XuvGHApc5kToRXjdGc3HUoTc8xw1vK1qma"

        when: 'generate an EC key from known private key'
        def privateKeyBytes = Utils.HEX.decode(privateKeyHex.toLowerCase())
        def key = ECKey.fromPrivate(privateKeyBytes)

        then: 'known wif and address are as expected'
        key.getPrivateKeyAsWiF(netParams) == privateWif
        key.toAddress(netParams).toString() == publicAddress
    }
}
