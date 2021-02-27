package com.pancast.pancastassistexample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manualButton = findViewById<Button>(R.id.manualHarnessButton)
        manualButton.setOnClickListener{
            val intent = Intent(this, ManualHarnessActivity::class.java)
            startActivity(intent)
        }

        val youtubeButton = findViewById<Button>(R.id.youtubePlayerButton)
        youtubeButton.setOnClickListener{
            val intent = Intent(this, YoutubePlayerActivity::class.java)
            startActivity(intent)
        }

        val exoplayerButton = findViewById<Button>(R.id.exoplayerAdsButton)
        exoplayerButton.setOnClickListener{
            val intent = Intent(this, ExoplayerAdsActivity::class.java)
            startActivity(intent)
        }
    }
}