@file:Suppress("Annotator")

package com.rahul.otpfetcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import com.google.android.gms.auth.api.phone.SmsRetriever
import java.util.regex.Pattern

class SmsListener(
    private val context: Context,
    private val handler: SmsResponseHandler,
    private val digits: Int
) {

    companion object {
        const val SMS_INTENT_ACTION = "SMS_INTENT_ACTION"
        const val TAG_MESSAGE = "TAG_MESSAGE"
        const val TAG_SUCCESS = "TAG_SUCCESS"
    }

    private var receiver: BroadcastReceiver? = null

    fun startService() {
        val client = SmsRetriever.getClient(context)

        val task = client.startSmsRetriever()

        task.addOnSuccessListener {
            //Registering the Broadcast receiver once the Task has started.
            registerBroadcastReceiver()
        }

        task.addOnFailureListener { e ->
            //If the Task could not start, will throw error.
            handler.failureToStartService(e)
        }
    }

    private fun registerBroadcastReceiver() {

        val intentFilter = IntentFilter(SMS_INTENT_ACTION)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.getBooleanExtra(TAG_SUCCESS, false)) {
                    val retrievedText =
                        intent.getStringExtra(TAG_MESSAGE)
                    handleResponse(retrievedText)
                    return
                }
                handler.requestTimedOut()
            }
        }
        context.registerReceiver(receiver, intentFilter)
    }

    private fun handleResponse(retrievedText: String?) {
        if (digits > 0) {
            val otp = getOtpFromSms(retrievedText)
            if (!TextUtils.isEmpty(otp)) {
                handler.otpResponse(otp)
            }
        }
        handler.smsResponse(retrievedText)
    }

    private fun getOtpFromSms(retrievedText: String?): String {

        val regex = "(\\d{$digits})"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(retrievedText)
        var value = ""
        if (matcher.find()) {
            value = matcher.group(0)
        }
        return value
    }

    /**
     * Call this method on onDestroy so that Broadcast Receiver can be unregistered
     */
    fun stopService() {
        context.unregisterReceiver(receiver)
    }
}
