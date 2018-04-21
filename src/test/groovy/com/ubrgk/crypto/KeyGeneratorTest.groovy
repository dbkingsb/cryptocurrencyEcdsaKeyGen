package com.ubrgk.crypto

import spock.lang.Specification

/**
 *
 */
class KeyGeneratorTest extends Specification {

    def "GenerateKeys"() {
        given: 'the desire to make 256 keys'
        def numOfKeys = 256

        when: 'the keys are generated'
        def keys = KeyGenerator.generateKeys(numOfKeys)

        then: 'the number of generated key is 256'
        keys.size() == numOfKeys
    }
}
