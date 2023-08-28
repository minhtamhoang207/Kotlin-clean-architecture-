package com.tom.learnkoltin.common.twilio_voice

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.twilio.voice.Call
import com.twilio.voice.CallException
import com.twilio.voice.CallInvite
import com.twilio.voice.CancelledCallInvite
import com.twilio.voice.MessageListener
import com.twilio.voice.Voice

class VoiceFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "VoiceFCMService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(
            TAG,
            "Received onMessageReceived(): ${remoteMessage.from}"
        )
        Log.d(
            TAG,
            "Bundle data: " + remoteMessage.data
        )
        Log.d(
            TAG,
            "From: " + remoteMessage.from
        )

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            val valid = Voice.handleMessage(this, remoteMessage.data, object : MessageListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onCallInvite(callInvite: CallInvite) {
                    val notificationId = System.currentTimeMillis().toInt()
                    handleInvite(callInvite, notificationId)
                }

                override fun onCancelledCallInvite(
                    cancelledCallInvite: CancelledCallInvite,
                    callException: CallException?
                ) {
                    handleCanceledCallInvite(cancelledCallInvite)
                }
            })
            if (!valid) {
                Log.e(
                    TAG,
                    "The message was not a valid Twilio Voice SDK payload: " +
                            remoteMessage.data
                )
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        val intent = Intent(Constants.ACTION_FCM_TOKEN)
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleInvite(callInvite: CallInvite, notificationId: Int) {
        Log.d(
            "Firebase messaging",
            "handleInvite: $notificationId ${callInvite.from}"
        )

//        showIncomingCallPopup()
        callInvite.accept(baseContext, callListener())
    }

    private fun showIncomingCallPopup() {
        val intent = Intent(this, CallingService::class.java)
        startService(intent)
    }

    private fun callListener(): Call.Listener {
        val tag = "Twilio call Tompei"

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

    private fun handleCanceledCallInvite(cancelledCallInvite: CancelledCallInvite) {
        Log.d(
            "Firebase messaging",
            "handleCanceledCallInvite: ${cancelledCallInvite.from}"
        )
//        val intent = Intent(this, IncomingCallNotificationService::class.java)
//        intent.action = Constants.ACTION_CANCEL_CALL
//        intent.putExtra(Constants.CANCELLED_CALL_INVITE, cancelledCallInvite)
//        startService(intent)
    }
}