package com.ubrgk.crypto;

import org.bitcoinj.core.ECKey;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 *
 */
class Guide {
    public static final String CSV_EXTENTION = ".csv";

    void go() {
        final Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8.displayName());
        doDisclaimer(sc);
        final Crypto crypto = doSelectCryptoType(sc);
        final String filePath = doGenerateFile(sc, crypto);
        doEncryptionSuggestion(filePath);
    }

    void doDisclaimer(final Scanner sc) {
        println("-------------------------------------------------------------------");
        println("| This program is distributed in the hope that it will be useful, |");
        println("| but WITHOUT ANY WARRANTY; without even the implied warranty of  |");
        println("| MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.            |");
        println("-------------------------------------------------------------------");
        println("");
        println("***************************** WARNING *****************************");
        println("* This program produces unencrypted crypto-currency private keys. *");
        println("* Do not utilized the produced keys unless they are created in a  *");
        println("* secure environment and stored safely (encrypted, hidden).       *");
        println("*******************************************************************");
        println("");
        askForInput("Ready to proceed? (y/N)");
        final String answer = sc.next();
        if (!"y".equals(answer)) {
            println("Aborted.");
            System.exit(0);
        }
    }

    Crypto doSelectCryptoType(final Scanner sc) {
        final Crypto[] values = Crypto.values();
        Crypto value = null;
        while (value == null) {
            println("Supported crypto-currency types:");
            for (int i = 0; i < values.length; i++) {
                println(" " + i + ". " + values[i].getAbbreviation());
            }
            askForInput("Which crypto?");
            final int menuSelection = sc.nextInt();
            try {
                value = values[menuSelection];
                println("Selected " + value);
            } catch (ArrayIndexOutOfBoundsException e) {
                println(getInvalidSelectionResponse(menuSelection));
            }
        }
        return value;
    }

    private String doGenerateFile(final Scanner sc, Crypto crypto) {
        /*
         * Generate content
         */

        askForInput("How many addresses to generate?");
        final int addressCount = sc.nextInt();
        final List<ECKey> ecKeys = FileWallet.generateKeys(addressCount);

        /*
         * Save file
         */

        askForInput("Name (path) for file?");
        final String userFilePath = sc.next();
        final String filePath = userFilePath + CSV_EXTENTION;

        try {
            FileWallet.saveKeys(filePath, ecKeys, crypto.getNetParams(), crypto, sc);
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        /*
         * Verify file
         */

        try {
            FileWallet.verify(filePath, crypto.getNetParams());
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        /*
         * Create public addresses file
         */

        return filePath;
    }

    private void doEncryptionSuggestion(String filePath) {
        println("The following is an encryption suggestion WITHOUT ANY WARRANTY:");
        println("- Use an implementation of OpenPGP (e.g. PGP, GPG) to encrypt the generated file.");
        println("- The following is an example using GPG:");
        println("  gpg --armor --symmetric --cipher-algo AES256 --s2k-digest-algo SHA512 --s2k-count 65011712 " + filePath);
        println("- The above command will prompt you for a passphrase. The passphrase should be long and random.");
        println("- The following is an example of decrypting using GPG:");
        println("  gpg -d " + filePath);
    }

    private String getInvalidSelectionResponse(final int menuOptionNumber) {
        return "Selected " + menuOptionNumber + "; invalid selection.";
    }

    private void askForInput(final String s) {
        System.out.print(s + " ");
    }

    private void println(final String s) {
        System.out.println(s);
    }
}
