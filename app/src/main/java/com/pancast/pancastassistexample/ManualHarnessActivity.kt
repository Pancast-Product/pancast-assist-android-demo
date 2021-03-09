package com.pancast.pancastassistexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import com.google.ads.interactivemedia.v3.internal.id
import com.pancast.assistsdk.PancastAssist
import com.pancast.assistsdk.events.PAAdAction
import com.pancast.assistsdk.events.PAAdEvent
import com.pancast.assistsdk.events.PAVideoAction
import com.pancast.assistsdk.events.PAVideoEvent
import com.pancast.assistsdk.meta.PAAd
import com.pancast.assistsdk.meta.PAVideo

class ManualHarnessActivity : AppCompatActivity(), PancastAssist.DebugListener {

    private lateinit var pancastAssist: PancastAssist
    private lateinit var debugTextView: TextView

    private val videoMeta = PAVideo().apply {
        id = "videoId"
        title = "videoTitle"
        duration = 99.0
        owner = "videoOwner"
        channel = "videoChannel"
        tags = listOf("tag1", "tag2")
        country = "AU"
        language = "en"
        mode = "vod"
    }

    private val adMeta = PAAd().apply {
        id = "adId"
        serve_id = "adServeId"
        title = "adTitle"
        system = "adSystem"
        advertiser = "adAdvertiser"
        advertiser_id = "adAdvertiserId"
        creative_id = "adCreativeId"
        description = "adDescription"
        duration = 10.0
        type = "linear"
        universal_ad_id = "adUniversalId"
        viewable = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_harness)

        debugTextView = findViewById(R.id.debugTextView)
        debugTextView.movementMethod = ScrollingMovementMethod()

        findViewById<Button>(R.id.startButton).setOnClickListener{
            pancastAssist = PancastAssist(this, "pancast-mobile-manual-harness", "tv-pancast-prod2.mini.snplow.net", this)
        }

        findViewById<Button>(R.id.setUserIdButton).setOnClickListener{
            if (::pancastAssist.isInitialized) {
                pancastAssist.setUserId("sample_user_1234")
            }
        }

        findViewById<Button>(R.id.videoStartButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.Start))
        }

        findViewById<Button>(R.id.videoPlayButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.Play))
        }

        findViewById<Button>(R.id.videoPauseButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.Pause))
        }

        findViewById<Button>(R.id.videoProgressButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.Progress))
        }

        findViewById<Button>(R.id.videoVolumeChangeButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.VolumeChange))
        }

        findViewById<Button>(R.id.videoFullscreenButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.FullScreen))
        }

        findViewById<Button>(R.id.videoChangeButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.Change))
        }

        findViewById<Button>(R.id.videoErrorButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.Error))
        }

        findViewById<Button>(R.id.videoQualityChangeButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.QualityChange))
        }

        findViewById<Button>(R.id.videoLoadedMetadataButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.LoadedMetadata))
        }

        findViewById<Button>(R.id.videoEndButton).setOnClickListener{
            sendVideoEvent(createVideoEventForAction(PAVideoAction.End))
        }

        findViewById<Button>(R.id.adStartButton).setOnClickListener{
            sendAdEvent(createAdEventForAction(PAAdAction.Start))
        }

        findViewById<Button>(R.id.adChangeButton).setOnClickListener{
            sendAdEvent(createAdEventForAction(PAAdAction.Change))
        }

        findViewById<Button>(R.id.adPlayButton).setOnClickListener{
            sendAdEvent(createAdEventForAction(PAAdAction.Play))
        }

        findViewById<Button>(R.id.adPauseButton).setOnClickListener{
            sendAdEvent(createAdEventForAction(PAAdAction.Pause))
        }

        findViewById<Button>(R.id.adProgressButton).setOnClickListener{
            sendAdEvent(createAdEventForAction(PAAdAction.Progress))
        }

        findViewById<Button>(R.id.adErrorButton).setOnClickListener{
            sendAdEvent(createAdEventForAction(PAAdAction.Error))
        }

        findViewById<Button>(R.id.adEndButton).setOnClickListener{
            sendAdEvent(createAdEventForAction(PAAdAction.End))
        }

    }

    private fun sendVideoEvent(event: PAVideoEvent) {
        if (::pancastAssist.isInitialized) {
            pancastAssist.sendVideoEvent(event, videoMeta)
        }
    }

    private fun sendAdEvent(event: PAAdEvent) {
        if (::pancastAssist.isInitialized) {
            pancastAssist.sendAdEvent(event, adMeta)
        }
    }

    override fun PancastAssistEventLog(output: String?) {
        debugTextView.append("$output  \n")
    }

    private fun createVideoEventForAction(videoAction: PAVideoAction): PAVideoEvent {
        return PAVideoEvent().apply {
            vid = videoMeta.id
            pid = "Manual Test Harness"
            action = videoAction
            ctime = 5.0
            pp = 2.0
            vd = 15.0
        }
    }

    private fun createAdEventForAction(adAction: PAAdAction): PAAdEvent {
        return PAAdEvent().apply {
            pid = "Manual Test Harness"
            vid = videoMeta.id
            action = adAction
            ctime = 1.0
        }
    }
}
