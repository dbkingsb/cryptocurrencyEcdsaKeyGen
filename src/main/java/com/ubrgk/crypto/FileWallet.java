package com.ubrgk.crypto;

import com.google.common.io.BaseEncoding;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.AbstractBitcoinNetParams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 */
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
final class FileWallet {

    private static final String CSV_DELIMITER = ",";

    static final String LABEL_PUBLIC_ADDRESSES_ONLY = "-public_addresses_only";

    private FileWallet() {
    }

    public static List<ECKey> generateKeys(final int numberOfAddress) {
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

    public static void saveKeys(final String filePath, final List<ECKey> ecKeys, final AbstractBitcoinNetParams netParams, final Crypto crypto, final Scanner sc) throws IOException {
        final List<String> headerRow = new ArrayList<>();
        headerRow.add("Crypto-currency Type");
        headerRow.add("Date Generated");
        headerRow.add("Private Key (Hex)");
        headerRow.add("Private Key (WIF)");
        headerRow.add("Address");
        final List<List<String>> fullRows = new ArrayList<>();
        fullRows.add(headerRow);
        final List<List<String>> addressOnlyRows = new ArrayList<>();
        addressOnlyRows.add(headerRow.subList(4,5));

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
            fullRow.add(ecKey.getPrivateKeyAsWiF(netParams));
            fullRow.add(ecKey.toAddress(netParams).toString());
            fullRows.add(fullRow);
            final List<String> addressOnlyRow = new ArrayList<>();
            addressOnlyRow.add(ecKey.toAddress(netParams).toString());
            addressOnlyRows.add(addressOnlyRow);
        }

        /*
         * Create full file
         */

        final String fullContent = toCsv(fullRows);
        createFile(filePath, fullContent, sc);

        /*
         * Create public address only file
         */

        final String addressOnlyContent = toCsv(addressOnlyRows);
        final String addressOnlyFilePath = filePath.substring(0, filePath.length() - Guide.CSV_EXTENTION.length())
                + LABEL_PUBLIC_ADDRESSES_ONLY + Guide.CSV_EXTENTION;
        createFile(addressOnlyFilePath, addressOnlyContent, sc);
    }

    private static void createFile(final String filePath, final String content, final Scanner sc) throws IOException {
        final Path path = Paths.get(filePath);
        println("Saving file " + path + "...");
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            print("File already exists, overwrite? (y/N) ");
            final String overwriteAnswer = sc.next();
            if ("y".equals(overwriteAnswer)) {
                println("Overwriting file...");
            } else {
                println("Aborted.");
                System.exit(0);
            }
        }
        final byte[] strToBytes = content.getBytes(StandardCharsets.UTF_8);
        Files.write(path, strToBytes);
        println("... saved.");
    }

    private static String toCsv(final List<List<String>> rows) {
        final StringBuilder contentBuilder = new StringBuilder();
        for (final List<String> row : rows) {
            for (int i=0; i<row.size(); i++) {
                row.set(i, "\"" + row.get(i) + "\"");
            }
            contentBuilder.append(String.join(",", row));
            contentBuilder.append("\r\n");
        }
        return contentBuilder.toString();
    }

    public static void verify(final String filePath, final AbstractBitcoinNetParams netParams) throws IOException {
        println("Reading file for verification...");
        final Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path);
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
                Files.move(path, path.resolveSibling(filePath + "-failed_verification"));
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
