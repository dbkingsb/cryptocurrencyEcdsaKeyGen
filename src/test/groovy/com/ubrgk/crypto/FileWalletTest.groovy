package com.ubrgk.crypto

import spock.lang.Specification
/**
 *
 */
class FileWalletTest extends Specification {
    def "GenerateKeys"() {
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
}
