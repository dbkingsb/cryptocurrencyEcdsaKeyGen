package com.ubrgk.crypto.netparams

import com.ubrgk.crypto.netparams.ReddcoinMainNetParams
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Utils
import spock.lang.Specification
import spock.lang.Subject
/**
 *
 */
class ReddcoinMainNetParamsTest extends Specification {
    public static final String privateKeyHex = "C496DA1E0DBC62B934C1771F89D06A14940F7F587FE43BCFFF04FF7A83A78918"

    @Subject
    ReddcoinMainNetParams netParams

    void setup() {
        netParams = ReddcoinMainNetParams.get()
    }

    def "Verify net params produce valid private WIF and public address"() {
        given: 'known values'
        String privateWif = "V5BKQPfx1jffaKC18rARME8iEr1cNVitHwbrd6R8wSWDapAKPFR7"
        String publicAddress = "Rsx6S5syhzsR8DFetBBYeZS1AL5qLv14Lr"

        when: 'generate an EC key from known private key'
        def privateKeyBytes = Utils.HEX.decode(privateKeyHex.toLowerCase())
        def key = ECKey.fromPrivate(privateKeyBytes)

        then: 'known wif and address are as expected'
        key.getPrivateKeyAsWiF(netParams) == privateWif
        key.toAddress(netParams).toString() == publicAddress
    }

    def "Verify net params produce valid private WIF (uncompressed) and public address (uncompressed)"() {
        given: 'known values'
        String privateWif = "7MtGx9JtS68rXCpohkJzdJUaMouFMjJeT3QJb1KcL8bG63Mm4fH"
        String publicAddress = "RnMvqMQTxFxxq48z5uhzi8aGqZWdSbGKWz"

        when: 'generate an EC key from known private key'
        def privateKeyBytes = Utils.HEX.decode(privateKeyHex.toLowerCase())
        def key = ECKey.fromPrivate(privateKeyBytes, false)

        then: 'known wif and address are as expected'
        key.getPrivateKeyAsWiF(netParams) == privateWif
        key.toAddress(netParams).toString() == publicAddress
    }
}
