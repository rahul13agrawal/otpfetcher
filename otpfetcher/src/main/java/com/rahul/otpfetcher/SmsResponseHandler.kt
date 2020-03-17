package com.rahul.otpfetcher

interface SmsResponseHandler {

    fun failureToStartService(e: Exception)

    fun requestTimedOut()

    fun otpResponse(otp: String)

    fun smsResponse(retrievedText: String?)
}
