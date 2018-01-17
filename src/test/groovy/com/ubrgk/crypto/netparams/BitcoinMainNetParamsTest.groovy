package com.ubrgk.crypto.netparams

import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Utils
import org.bitcoinj.params.MainNetParams
import spock.lang.Specification
import spock.lang.Subject
/**
 *
 */
class BitcoinMainNetParamsTest extends Specification {
    public static final String privateKeyHex = "B102CADB7A712D09C5144074262217F416450E163427769727835B26DC9DB247"

    @Subject
    MainNetParams netParams

    void setup() {
        netParams = MainNetParams.get()
    }

    def "Verify net params produce valid private WIF and public address"() {
        given: 'known values'
        String privateWif = "L39oBDDLUj27xNvQnNTpsCPJowDxkFyFnYymjeRRrfUggmTGEcpn"
        String publicAddress = "17x24dspoeTzfT1SuhU4JWoEwwZ6UUpYNU"

        when: 'generate an EC key from known private key'
        def privateKeyBytes = Utils.HEX.decode(privateKeyHex.toLowerCase())
        def key = ECKey.fromPrivate(privateKeyBytes)

        then: 'known wif and address are as expected'
        key.getPrivateKeyAsWiF(netParams) == privateWif
        key.toAddress(netParams).toString() == publicAddress
    }

    def "Verify net params produce valid private WIF (uncompressed) and public address (uncompressed)"() {
        given: 'known values'
        String privateWif = "5KAF62rvML34qoCeZUyNijTMM9fHFnXsUYG8RAPJQwV2HoCUafG"
        String publicAddress = "1C8wnabTidSjPx8HLkdTPrXhmpueGM1Vts"

        when: 'generate an EC key from known private key'
        def privateKeyBytes = Utils.HEX.decode(privateKeyHex.toLowerCase())
        def key = ECKey.fromPrivate(privateKeyBytes, false)

        then: 'known wif and address are as expected'
        key.getPrivateKeyAsWiF(netParams) == privateWif
        key.toAddress(netParams).toString() == publicAddress
    }
}
