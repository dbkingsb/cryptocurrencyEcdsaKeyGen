# Cryptocurrency Key Generator

Generate ECDSA keys, and their various encodings, for common cryptocurrencies.

This program exists to satisfy a desire for long-term storage with the fewest dependencies on software wallets implementations.

Produce a pair of CSV files:

1. contains private keys (HEX & WIF) and associated public addresses
2. contains only public addresses

Encrypt the file that contains the private keys and store away; keep the public-only file available for deposits and tracking.

**Private and Public Headers**

```
Crypto-currency Type	Date Generated	Private Key (Hex)	Private Key (WIF)	Address
...
```
**Public-only Headers**

```
Crypto-currency Type	Date Generated	Address
...
```

## How to build and run

Required: Java 1.8 (or higher) and Gradle

```
./gradlew build
java -jar build/libs/cryptocurrencyKeyGen-all.jar
```

## Supported Types
- Bitcoin
- Dash
- Doge
- Litecoin
- Reddcoin
- Vertcoin

## Example method of encryption

The following is an example of how the private file could be encrypted. This example uses Open PGP.

`gpg --armor --symmetric --cipher-algo AES256 --s2k-digest-algo SHA512 --s2k-count 65011712 [path to file]`

The following is the command one could issue to decrypt the result of the above command: `gpg -d`.



