package com.ubrgk.crypto

import org.bitcoinj.core.ECKey
import spock.lang.Specification
/**
 *
 */
class FileWalletTest extends Specification {
    def "generate keys"() {
        given: "a desired number of keys to generate"
        def numKeysToGen = 5

        when: "the keys are generated"
        def keys = FileWallet.generateKeys(numKeysToGen)

        then: "an equal number of ECDSA keys are returned"
        keys.size() == numKeysToGen

        then: "the public keys are compressed"
        keys.each {
            it.isCompressed()
        }
    }

    def "create lines of fields"() {
        given: "a list of EC keys"
        def ecKeys = [new ECKey(), new ECKey()]

        when: "the list is provided from which to create file lines of fields for Bitcoin"
        def linesOfFields = FileWallet.createLinesOfFields(ecKeys, CryptoCurrencyType.BITCOIN)

        then: "a header is the first line"
        linesOfFields.get(0).get(0).is FileWallet.HEADER_CRYPTO_CURRENCY_TYPE
        linesOfFields.get(0).get(1).is FileWallet.HEADER_DATE_GENERATED
        linesOfFields.get(0).get(2).is FileWallet.HEADER_PRIVATE_KEY_HEX
        linesOfFields.get(0).get(3).is FileWallet.HEADER_PRIVATE_KEY_WIF
        linesOfFields.get(0).get(4).is FileWallet.HEADER_ADDRESS

        then: "an equal number of data lines to EC keys are generated"
        linesOfFields.size() - 1 == ecKeys.size()
    }
}
