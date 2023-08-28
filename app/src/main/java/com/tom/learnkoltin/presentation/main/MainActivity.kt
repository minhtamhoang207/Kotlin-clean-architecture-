package com.tom.learnkoltin.presentation.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tom.learnkoltin.common.twilio_voice.TwilioVoiceManager
import com.tom.learnkoltin.databinding.ActivityMainBinding
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
        twilioVoiceManager.init()
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