package com.ubrgk.crypto

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.bitcoinj.core.ECKey
import spock.lang.Specification

/**
 *
 */
@SuppressFBWarnings("SE_NO_SERIALVERSIONID")
class CsvBuilderTest extends Specification {

    def "create lines of fields"() {
        given: "a list of EC keys"
        def ecKeys = [new ECKey(), new ECKey()]

        when: "the list is provided from which to create file lines of fields for Bitcoin"
        def linesOfFields = CsvBuilder.createLinesOfFields(ecKeys, CryptoCurrencyType.BITCOIN)

        then: "a header is the first line"
        linesOfFields.get(0).get(0).is CsvBuilder.HEADER_CRYPTO_CURRENCY_TYPE
        linesOfFields.get(0).get(1).is CsvBuilder.HEADER_DATE_GENERATED
        linesOfFields.get(0).get(2).is CsvBuilder.HEADER_PRIVATE_KEY_HEX
        linesOfFields.get(0).get(3).is CsvBuilder.HEADER_PRIVATE_KEY_WIF
        linesOfFields.get(0).get(4).is CsvBuilder.HEADER_ADDRESS

        then: "an equal number of data lines to EC keys are generated"
        linesOfFields.size() - 1 == ecKeys.size()
    }
}
