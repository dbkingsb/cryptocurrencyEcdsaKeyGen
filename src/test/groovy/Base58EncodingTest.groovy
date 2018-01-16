import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Utils
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.script.Script
import spock.lang.Specification
/**
 *
 */
class Base58EncodingTest extends Specification {

    def "Verify base58 encoding of WIF and Addresses using bitcoin core test vectors"() {
        given: "the base58_keys_valid.json test vectors from https://github.com/bitcoin/bitcoin"
        def fileName = getClass().getClassLoader().getResource("base58_keys_valid.json").getFile()
        def file = new File(fileName)

        when: "an ECKey is established with the given key"
        def objectMapper = new ObjectMapper()
        def parentArray = objectMapper.readValue(file, ArrayNode.class)
        def mainNetParams = MainNetParams.get()

        then: "the ECKey encoded private key (WIF) or the ECKey address match the given value"
        int counter = 0
        for (int i=0; i<parentArray.size(); i++) {
            def testVectors = (ArrayNode) parentArray.get(i)
            def encoded = testVectors.get(0).asText()
            def keyText = testVectors.get(1).asText()
            def keyBytes = Utils.HEX.decode(keyText)
            def info = objectMapper.treeToValue(testVectors.get(2), Base58VectorInfo.class)

            if (info.chain == "main") {
                if (info.isPrivkey) {
                    def ecKey = ECKey.fromPrivate(keyBytes, info.isCompressed)
                    assert ecKey.getPrivateKeyAsWiF(mainNetParams) == encoded
                    counter++
                } else {
                    def script = new Script(keyBytes)
                    if (info.tryCaseFlip) {
                        def address
                        try {
                            address = script.getToAddress(mainNetParams)
                        }
                        catch (org.bitcoinj.script.ScriptException ignored) {
                            // Exception expected when tryCaseFlip is true
                            continue
                        }
                        assert address.toString() != encoded
                    }
                    else {
                        def address = script.getToAddress(mainNetParams)
                        assert address.toString() == encoded
                    }
                }
            }
        }

        System.out.println("Base58EncodingTest made " + counter + " assertions.")
    }

    static class Base58VectorInfo {
        boolean isCompressed
        boolean isPrivkey
        String chain
        boolean tryCaseFlip
    }
}
