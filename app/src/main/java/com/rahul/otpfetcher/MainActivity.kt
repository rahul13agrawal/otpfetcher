package com.rahul.otpfetcher

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SmsResponseHandler {

    lateinit var listener: SmsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This is used to get the hashcode. DO NOT INCLUDE THIS IN PRODUCTION.
        val helper = AppSignatureHelper(this)
        Log.d("MainActivity", helper.appSignatures[0]);

        listener = SmsListener(this, this, 4)
        listener.startService()
    }

    override fun failureToStartService(e: Exception) {
        Log.d("MainActivity", e.message)
    }

    override fun requestTimedOut() {
        Log.d("MainActivity", "timed out")
    }

    override fun otpResponse(otp: String) {
        Log.d("MainActivity", otp)
    }

    override fun smsResponse(retrievedText: String?) {
        Log.d("MainActivity", retrievedText)
    }

    override fun onDestroy() {
        super.onDestroy()
        listener.stopService()
    }
}
