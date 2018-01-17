package com.ubrgk.crypto.netparams

import com.ubrgk.crypto.netparams.VertcoinMainNetParams
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Utils
import spock.lang.Specification
import spock.lang.Subject
/**
 *
 */
class VertcoinMainNetParamsTest extends Specification {
    public static final String privateKeyHex = "CA539DA1DBE3A7C6A6E604E0EB9B0F5E133C41706587175DC80562EF71F8E4C1"

    @Subject
    VertcoinMainNetParams netParams

    void setup() {
        netParams = VertcoinMainNetParams.get()
    }

    def "Verify net params produce valid private WIF and public address"() {
        given: 'known values'
        String privateWif = "L411P9PPkLC6XBs46R18PEMNKym1HbnERRnJfFkRf21nDCrFPVrx"
        String publicAddress = "VedMZvX1AdJfKDpG7nZ4Tf5WbrNstFeN6t"

        when: 'generate an EC key from known private key'
        def privateKeyBytes = Utils.HEX.decode(privateKeyHex.toLowerCase())
        def key = ECKey.fromPrivate(privateKeyBytes)

        then: 'known wif and address are as expected'
        key.getPrivateKeyAsWiF(netParams) == privateWif
        key.toAddress(netParams).toString() == publicAddress
    }

    def "Verify net params produce valid private WIF (uncompressed) and public address (uncompressed)"() {
        given: 'known values'
        String privateWif = "5KMPk5ctNbi2hpmsYaz87PxpRh3ikdt2gX5pxLew55m8XUGRa7q"
        String publicAddress = "Vg8dyQdFZRUNa634c75hmXA2V7xTvUG32E"

        when: 'generate an EC key from known private key'
        def privateKeyBytes = Utils.HEX.decode(privateKeyHex.toLowerCase())
        def key = ECKey.fromPrivate(privateKeyBytes, false)

        then: 'known wif and address are as expected'
        key.getPrivateKeyAsWiF(netParams) == privateWif
        key.toAddress(netParams).toString() == publicAddress
    }
}
