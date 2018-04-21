package com.ubrgk.crypto

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import spock.lang.Specification

/**
 *
 */
@SuppressFBWarnings("SE_NO_SERIALVERSIONID")
class KeyGeneratorTest extends Specification {

    def "Generate keys"() {
        given: "a desired number of keys to generate"
        def numKeysToGen = 256

        when: "the keys are generated"
        def keys = KeyGenerator.generateKeys(numKeysToGen)

        then: "an equal number of ECDSA keys are returned"
        keys.size() == numKeysToGen

        then: "the public keys are compressed"
        keys.each {
            it.isCompressed()
        }
    }
}
