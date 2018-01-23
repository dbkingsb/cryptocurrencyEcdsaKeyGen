package com.ubrgk.crypto;

import com.google.common.io.BaseEncoding;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.AbstractBitcoinNetParams;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
final class FileWallet {

    private static final String CSV_DELIMITER = ",";
    static final String HEADER_CRYPTO_CURRENCY_TYPE = "Crypto-currency Type";
    static final String HEADER_DATE_GENERATED = "Date Generated";
    static final String HEADER_PRIVATE_KEY_HEX = "Private Key (Hex)";
    static final String HEADER_PRIVATE_KEY_WIF = "Private Key (WIF)";
    static final String HEADER_ADDRESS = "Address";

    private FileWallet() {
    }

    static List<ECKey> generateKeys(final int numberOfAddress) {
        println("Creating file content...");

        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }

        final List<ECKey> ecKeys = new ArrayList<>();
        for (int i=0; i<numberOfAddress; i++) {
            final ECKey ecKey = new ECKey(secureRandom);
            ecKeys.add(ecKey);
        }

        println("... generated " + ecKeys.size() + " addresses.");

        return ecKeys;
    }

    static List<List<String>> createLinesOfFields(List<ECKey> ecKeys, CryptoCurrencyType crypto) {
        final List<String> headerRow = new ArrayList<>();
        headerRow.add(HEADER_CRYPTO_CURRENCY_TYPE);
        headerRow.add(HEADER_DATE_GENERATED);
        headerRow.add(HEADER_PRIVATE_KEY_HEX);
        headerRow.add(HEADER_PRIVATE_KEY_WIF);
        headerRow.add(HEADER_ADDRESS);
        final List<List<String>> fullRows = new ArrayList<>();
        fullRows.add(headerRow);

        final DateFormat dateFormatter = DateFormat.getDateTimeInstance(
                DateFormat.LONG,
                DateFormat.LONG
        );
        final String date = dateFormatter.format(new Date());

        for (final ECKey ecKey : ecKeys) {
            final List<String> fullRow = new ArrayList<>();
            fullRow.add(crypto.name());
            fullRow.add(date);
            fullRow.add(ecKey.getPrivateKeyAsHex());
            fullRow.add(ecKey.getPrivateKeyAsWiF(crypto.getNetParams()));
            fullRow.add(ecKey.toAddress(crypto.getNetParams()).toString());
            fullRows.add(fullRow);
        }
        return fullRows;
    }

    static List<List<String>> getOnlyPubFields(List<List<String>> fullRows) {
        List<List<String>> onlyPubRows = new ArrayList<>();
        for (List<String> fullRow : fullRows) {
            List<String> onlyPubRow = new ArrayList<>();
            onlyPubRow.add(fullRow.get(0));
            onlyPubRow.add(fullRow.get(1));
            onlyPubRow.add(fullRow.get(4));
            onlyPubRows.add(onlyPubRow);
        }
        return onlyPubRows;
    }

    static String toCsv(final List<List<String>> rows) {
        final StringBuilder contentBuilder = new StringBuilder();
        for (final List<String> row : rows) {
            final ArrayList<String> toJoin = new ArrayList<>();
            for (String fields : row) {
                toJoin.add("\"" + fields + "\"");
            }
            contentBuilder.append(String.join(",", toJoin));
            contentBuilder.append("\r\n");
        }
        return contentBuilder.toString();
    }

    static void verify(List<String> lines, final AbstractBitcoinNetParams netParams) throws IOException {
        // Remove header
        lines = lines.subList(1, lines.size());
        println("... file read.");

        println("Verifying " + lines.size() + " addresses...");
        for (int i=0; i < lines.size(); i++) {
            final String[] fields = lines.get(i).split("\"" + CSV_DELIMITER + "\"");
            for (int j = 0; j < fields.length; j++) {
                /*
                 * Remove the double quotes that wrap the field
                 * PMD thinks this is a string concatenation operation using "+="
                 */
                fields[j] = fields[j].replaceAll("^\"|\"$", ""); //NOPMD
            }
            final String actualPrivateKeyHex = fields[2];
            final String actualPrivateKeyWif = fields[3];
            final String actualAddress = fields[4];
            final byte[] actualPrivateKey = BaseEncoding.base16().lowerCase().decode(actualPrivateKeyHex);

            final ECKey ecKey = ECKey.fromPrivate(actualPrivateKey);
            final String expectedPrivateKeyAsWiF = ecKey.getPrivateKeyAsWiF(netParams);
            final String expectedAddress = ecKey.toAddress(netParams).toString();

            if (!expectedPrivateKeyAsWiF.equals(actualPrivateKeyWif)
                    || !expectedAddress.equals(actualAddress)) {
                throw new AssertionError("Verification of address number " + i + " failed!");
            }
        }
        println("... addresses verified.");
    }

    private static void print(final String s) {
        System.out.print(s);
    }

    private static void println(final String s) {
        System.out.println(s);
    }
}
