# Cryptocurrency Key Generator

Generate ECDSA keys, and their various encodings, for common cryptocurrencies.

This program exists to satisfy a desire for long-term storage with the fewest dependencies on software wallets implmentations.

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
Crypto-currency Type	Date Generated						Address
BITCOIN					February 18, 2018 11:00:13 PM EST	1Cp9fD9y2WpBmdKYfXQTfDLrxKVyYCn4qd
BITCOIN					February 18, 2018 11:00:13 PM EST	1NQay6mDvRb4coSupDmeptV9ZSTfXphJ28
BITCOIN					February 18, 2018 11:00:13 PM EST	18MYEYgNqM31uid9USSTe3heaGWMPeDYWk
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




