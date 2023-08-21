package com.tom.learnkoltin.presentation.main

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
import com.tom.learnkoltin.databinding.ActivityMainBinding
import com.twilio.voice.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    lateinit var twilioVoiceManager: TwilioVoiceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View binding
        Firebase.initialize(context = baseContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setOnClick()
    }

    private fun setOnClick() {
        binding.fab.setOnClickListener {
            if (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                twilioVoiceManager.makeCall()
            }
        }
    }

    private fun init(){
        twilioVoiceManager = TwilioVoiceManager(baseContext)
//        twilioVoiceManager.init(
//            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzNiODY2MzcxZTY5ODU5Y2ViY2I2MWRhNzRmNjNhNjA0LTE2OTI2NDI2NzMiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJUb20iLCJ2b2ljZSI6eyJvdXRnb2luZyI6eyJhcHBsaWNhdGlvbl9zaWQiOiJBUDExOGUwZDFhODMzYWFkNmY0NTU5MjgyYjQwMmE2NTNjIn19fSwiaWF0IjoxNjkyNjQyNjczLCJleHAiOjE2OTI2NDYyNzMsImlzcyI6IlNLM2I4NjYzNzFlNjk4NTljZWJjYjYxZGE3NGY2M2E2MDQiLCJzdWIiOiJBQzg0YmYzNTY3YmU2MzEwMmYwOWY4NDQ2Nzk0Nzc1YWZhIn0.e_1seWPBY13HszdbdUUspc6lbSUM1EPMJFD7mSJJEJI"
//        )
//
        twilioVoiceManager.init(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTSzNiODY2MzcxZTY5ODU5Y2ViY2I2MWRhNzRmNjNhNjA0LTE2OTI2NDI3MjAiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJUb20yIiwidm9pY2UiOnsib3V0Z29pbmciOnsiYXBwbGljYXRpb25fc2lkIjoiQVAxMThlMGQxYTgzM2FhZDZmNDU1OTI4MmI0MDJhNjUzYyJ9fX0sImlhdCI6MTY5MjY0MjcyMCwiZXhwIjoxNjkyNjQ2MzIwLCJpc3MiOiJTSzNiODY2MzcxZTY5ODU5Y2ViY2I2MWRhNzRmNjNhNjA0Iiwic3ViIjoiQUM4NGJmMzU2N2JlNjMxMDJmMDlmODQ0Njc5NDc3NWFmYSJ9.xbk9F4ethPqF_9LE8Y2DITWgSu62nNVZeCYEwJGTClI"
        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                twilioVoiceManager.makeCall()
            } else {
                Toast.makeText(baseContext, "PLease allow to make call", Toast.LENGTH_SHORT)
            }
        }
}

class TwilioVoiceManager(private val context: Context) {

    private lateinit var accessToken: String
    val tag: String = "Twilio call Tompei"

    fun init(accessToken: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val fcmToken = task.result
//CA927715f3b675287aa381744622350af8
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

        val params = mapOf("to" to "Tom2")
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