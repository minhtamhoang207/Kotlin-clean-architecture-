package com.tom.learnkoltin.common.twilio_voice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.tom.learnkoltin.R

class IncomingCallActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call)
        registerViewsEvent()
    }

    private fun registerViewsEvent() {
        findViewById<AppCompatButton>(R.id.btnRejectCall).setOnClickListener {
            val callingService = Intent(this, CallingService::class.java)
            stopService(callingService)
        }

        findViewById<AppCompatButton>(R.id.btnAcceptCall).setOnClickListener {
            // TODO start calling screen
        }
    }
}
