package com.rahul.otpfetcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SMSBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {

            val extras = intent.extras
            var status: Status? = null
            if (extras != null) {
                status = extras[SmsRetriever.EXTRA_STATUS] as Status?
            }

            if (status != null) {
                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val message = extras!![SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                        createBroadcastIntent(context, message, true)
                    }
                    CommonStatusCodes.TIMEOUT -> createBroadcastIntent(context, null, false)
                }
            }
        }
    }

    /**
     * Sending a broadcast to capture the SMS received
     */
    private fun createBroadcastIntent(context: Context?, data: String?, isSuccess: Boolean) {
        val intent = Intent(SmsListener.SMS_INTENT_ACTION)
        intent.putExtra(SmsListener.TAG_MESSAGE, data)
        intent.putExtra(SmsListener.TAG_SUCCESS, isSuccess)
        context?.sendBroadcast(intent)
    }
}
