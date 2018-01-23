package com.ubrgk.crypto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 *
 */
final public class App {

    private App() {
    }

    public static void main (final String[] args) throws IOException {
        final Guide guide = new Guide();
        final Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8.displayName());
        guide.doDisclaimer(sc);
        final CryptoCurrencyType crypto = guide.doSelectCryptoType(sc);
        final String filePath;
        filePath = guide.doGenerateFiles(sc, crypto);
        guide.doEncryptionExample(filePath);
    }
}
