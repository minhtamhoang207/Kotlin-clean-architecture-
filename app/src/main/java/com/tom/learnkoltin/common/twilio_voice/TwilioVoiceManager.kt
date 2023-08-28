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

    fun init() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val fcmToken = task.result
            this.accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTS2U2Y2E4YzA0ZDRhODZlNGMwMWIxOWMxZTkzZjU0YzEzLTE2OTMxOTIzOTAiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJ0b21wZWkiLCJ2b2ljZSI6eyJpbmNvbWluZyI6eyJhbGxvdyI6dHJ1ZX0sIm91dGdvaW5nIjp7ImFwcGxpY2F0aW9uX3NpZCI6IkFQMDA5NjY3OTI1OWRiMmFlNmQ2ZDVjN2I3YTMyMjIwMmMifSwicHVzaF9jcmVkZW50aWFsX3NpZCI6IkNSM2RiZTNlMjhjNGY3ZmYzZmUxZTdjYWJmOWM1NTNkOGEifX0sImlhdCI6MTY5MzE5MjM5MCwiZXhwIjoxNjkzMTk1OTkwLCJpc3MiOiJTS2U2Y2E4YzA0ZDRhODZlNGMwMWIxOWMxZTkzZjU0YzEzIiwic3ViIjoiQUM4NGJmMzU2N2JlNjMxMDJmMDlmODQ0Njc5NDc3NWFmYSJ9.tfVjAO_Xj6YJ_hoARL9ZswSCUUMS4qCMHUOx24MtXxU"
//            this.accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTS2U2Y2E4YzA0ZDRhODZlNGMwMWIxOWMxZTkzZjU0YzEzLTE2OTMxOTIzOTAiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJ0b21wZWkiLCJ2b2ljZSI6eyJpbmNvbWluZyI6eyJhbGxvdyI6dHJ1ZX0sIm91dGdvaW5nIjp7ImFwcGxpY2F0aW9uX3NpZCI6IkFQMDA5NjY3OTI1OWRiMmFlNmQ2ZDVjN2I3YTMyMjIwMmMifSwicHVzaF9jcmVkZW50aWFsX3NpZCI6IkNSM2RiZTNlMjhjNGY3ZmYzZmUxZTdjYWJmOWM1NTNkOGEifX0sImlhdCI6MTY5MzE5MjM5MCwiZXhwIjoxNjkzMTk1OTkwLCJpc3MiOiJTS2U2Y2E4YzA0ZDRhODZlNGMwMWIxOWMxZTkzZjU0YzEzIiwic3ViIjoiQUM4NGJmMzU2N2JlNjMxMDJmMDlmODQ0Njc5NDc3NWFmYSJ9.tfVjAO_Xj6YJ_hoARL9ZswSCUUMS4qCMHUOx24MtXxU"

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