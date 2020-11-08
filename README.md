# otpfetcher
This will make auto detecting OTP through SMS easier to implement.

Latest version : [![](https://jitpack.io/v/rahul13agrawal/otpfetcher.svg)](https://jitpack.io/#rahul13agrawal/otpfetcher)


## Setup:
Add below in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and Add the dependency:

```gradle
dependencies {
    implementation 'com.github.rahul13agrawal:otpfetcher:1.0.0'
}
```

## Implementation

```
val listener = SmsListener(Context, Listener, Otp Digit) //Default Otp Digit is 4
//This will start SMSRetrieverClient and Broadcast Receiver.
listener.startService()
```

### Callbacks:
```
    override fun failureToStartService(e: Exception) {
        //If error to start SMSRetrieverClient, will get it here.
    }

    override fun requestTimedOut() {
        //If OTP not fetched and Timeout response received in Broadcast receiver, This will be called.
    }

    override fun otpResponse(otp: String) {
         //Fetch otp from the SMS
    }

    override fun smsResponse(retrievedText: String?) {
        //The complete SMS will be provided here. Write logic to fetch OTP.
    }
```
Once you receive the SMS, do not forget to ***stopService***
```
   listener.stopService()
```
