# ShopCheckout

Small Android/Compose app demoing a real Google Pay API checkout flow — the API for paying at checkout on physical goods, not Play Billing (that's for in-app digital content).

Runs against Google's `ENVIRONMENT_TEST` wallet environment the whole time, so nothing is ever actually charged.

## What's in it

- Product catalog → cart → checkout
- Google Pay button, `isReadyToPay` check, payment sheet launched via the Activity Result API

## Stack

Kotlin, Compose, MVVM, `play-services-wallet`

## Running it

```
./gradlew assembleDebug
./gradlew test
```

Needs an emulator/device with Play Services and a signed-in Google account to actually see the payment sheet.
