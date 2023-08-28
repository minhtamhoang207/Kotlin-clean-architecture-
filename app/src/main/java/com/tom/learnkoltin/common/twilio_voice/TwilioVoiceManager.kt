package com.tom.learnkoltin.common.twilio_voice

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.twilio.voice.Call
import com.twilio.voice.CallException
import com.twilio.voice.ConnectOptions
import com.twilio.voice.RegistrationException
import com.twilio.voice.RegistrationListener
import com.twilio.voice.Voice

class TwilioVoiceManager(private val context: Context) {

    private lateinit var accessToken: String
    val tag: String = "Twilio call Tompei"

    fun init(accessToken: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val fcmToken = task.result
            this.accessToken = accessToken

            Voice.register(
                accessToken,
                Voice.RegistrationChannel.FCM,
                fcmToken,
                registrationListener()
            )
        })
    }

    private fun registrationListener(): RegistrationListener {
        return object : RegistrationListener {
            override fun onRegistered(accessToken: String, fcmToken: String) {
                Log.d(tag,"Successfully registered FCM $fcmToken")
            }

            override fun onError(
                registrationException: RegistrationException,
                accessToken: String,
                fcmToken: String
            ) {
                val message = String.format(
                    "Registration Error:" +
                            " ${registrationException.message}" +
                            " ${registrationException.errorCode}"
                )
                Log.d(tag, message)
            }
        }
    }

    fun makeCall() {

        val params = mutableMapOf("to" to "tompei")
        val connectOptions: ConnectOptions = ConnectOptions.Builder(accessToken)
            .params(params)
            .build()

        Voice.connect(context, connectOptions, callListener())
    }

    private fun callListener(): Call.Listener {
        return object : Call.Listener {
            override fun onRinging(call: Call) {
                Log.d(tag, "Ringing")
            }

            override fun onConnectFailure(call: Call, error: CallException) {
                Log.d(tag, "Connect failure")
            }

            override fun onConnected(call: Call) {
                Log.d(tag,"Connected")
            }

            override fun onReconnecting(call: Call, callException: CallException) {
                Log.d(tag,"Reconnecting")
            }

            override fun onReconnected(call: Call) {
                Log.d(tag, "Reconnected")
            }

            override fun onDisconnected(call: Call, error: CallException?) {
                Log.d(tag, "Disconnected")
            }
        }
    }

}