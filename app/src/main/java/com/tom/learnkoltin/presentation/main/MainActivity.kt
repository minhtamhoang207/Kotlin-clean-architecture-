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
        twilioVoiceManager.init(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTS2U2Y2E4YzA0ZDRhODZlNGMwMWIxOWMxZTkzZjU0YzEzLTE2OTI5NTkwNjAiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJhbGljZSIsInZvaWNlIjp7ImluY29taW5nIjp7ImFsbG93Ijp0cnVlfSwib3V0Z29pbmciOnsiYXBwbGljYXRpb25fc2lkIjoiQVAwMDk2Njc5MjU5ZGIyYWU2ZDZkNWM3YjdhMzIyMjAyYyJ9LCJwdXNoX2NyZWRlbnRpYWxfc2lkIjoiQ1IzZGJlM2UyOGM0ZjdmZjNmZTFlN2NhYmY5YzU1M2Q4YSJ9fSwiaWF0IjoxNjkyOTU5MDYwLCJleHAiOjE2OTI5NjI2NjAsImlzcyI6IlNLZTZjYThjMDRkNGE4NmU0YzAxYjE5YzFlOTNmNTRjMTMiLCJzdWIiOiJBQzg0YmYzNTY3YmU2MzEwMmYwOWY4NDQ2Nzk0Nzc1YWZhIn0.pmBmJvsk9boN4rFthsJo1SmhL5ACm_b5pFgD2PZrZ6o"
        )

//        twilioVoiceManager.init(
//           "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTS2U2Y2E4YzA0ZDRhODZlNGMwMWIxOWMxZTkzZjU0YzEzLTE2OTI5NTkxMDIiLCJncmFudHMiOnsiaWRlbnRpdHkiOiJ0b21wZWkiLCJ2b2ljZSI6eyJpbmNvbWluZyI6eyJhbGxvdyI6dHJ1ZX0sIm91dGdvaW5nIjp7ImFwcGxpY2F0aW9uX3NpZCI6IkFQMDA5NjY3OTI1OWRiMmFlNmQ2ZDVjN2I3YTMyMjIwMmMifSwicHVzaF9jcmVkZW50aWFsX3NpZCI6IkNSM2RiZTNlMjhjNGY3ZmYzZmUxZTdjYWJmOWM1NTNkOGEifX0sImlhdCI6MTY5Mjk1OTEwMiwiZXhwIjoxNjkyOTYyNzAyLCJpc3MiOiJTS2U2Y2E4YzA0ZDRhODZlNGMwMWIxOWMxZTkzZjU0YzEzIiwic3ViIjoiQUM4NGJmMzU2N2JlNjMxMDJmMDlmODQ0Njc5NDc3NWFmYSJ9.XQBv4LMZREWHLyhMKF5m-kZZzjxr7JOOZfmMik6w6hk"
//        )
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