package com.pancast.pancastassistexample

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.pancast.assistsdk.PancastAssist
import com.pancast.assistsdk.events.PAAdAction
import com.pancast.assistsdk.events.PAAdEvent
import com.pancast.assistsdk.events.PAVideoAction
import com.pancast.assistsdk.events.PAVideoEvent
import com.pancast.assistsdk.meta.PAAd
import com.pancast.assistsdk.meta.PAVideo


class ExoplayerAdsActivity: AppCompatActivity(), PancastAssist.DebugListener, Player.EventListener, AdEvent.AdEventListener {

    private lateinit var pancastAssist: PancastAssist
    private lateinit var debugTextView: TextView
    private lateinit var playerView: StyledPlayerView
    private lateinit var player: SimpleExoPlayer
    private lateinit var adsLoader: ImaAdsLoader
    private val videoUri = "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd"
    private val adTagUri = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonlybumper&cmsid=496&vid=short_onecue&correlator="

    private val videoMeta = PAVideo().apply {
        id = "sample"
        title = "Tears"
        duration = 99.0
        owner = "google"
        channel = "google"
        tags = listOf("tag1", "tag2")
        country = "AU"
        language = "en"
        mode = "vod"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exoplayer_ads)

        playerView = findViewById(R.id.playerView)

        debugTextView = findViewById(R.id.debugTextView)
        debugTextView.movementMethod = ScrollingMovementMethod()

        pancastAssist = PancastAssist(
            this,
            "pancast-mobile-exoplayer-ads",
            "tv-pancast-prod2.mini.snplow.net",
            this
        )

    }

    override fun onResume() {
        super.onResume()
        setupPlayer()
        playerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        playerView.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        playerView.onPause()
        releasePlayer()
    }

    private fun setupPlayer() {

        val adsProvider : DefaultMediaSourceFactory.AdsLoaderProvider = DefaultMediaSourceFactory.AdsLoaderProvider {
            getAdsLoaderProvider()
        }
        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(this)
            .setAdsLoaderProvider(adsProvider)
            .setAdViewProvider(playerView)
        player = SimpleExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
        playerView.player = player

        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri(videoUri)
            .setAdTagUri(adTagUri)
            .build()

        player.addListener(this)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    private fun releasePlayer() {
        player.release()
        adsLoader.release()
    }

    private fun getAdsLoaderProvider() : ImaAdsLoader {
        adsLoader = ImaAdsLoader.Builder(this)
            .setAdEventListener(this)
            .build()
        adsLoader.setPlayer(player)
        return adsLoader
    }

    override fun PancastAssistEventLog(output: String?) {
        debugTextView.append("$output  \n")
    }

    override fun onAdEvent(adEvent: AdEvent?) {
        when (adEvent?.type) {
            AdEvent.AdEventType.STARTED -> pancastAssist.sendAdEvent(createAdEventForAction(PAAdAction.Play), adMetaForAd(adEvent))
            AdEvent.AdEventType.COMPLETED -> pancastAssist.sendAdEvent(createAdEventForAction(PAAdAction.End), adMetaForAd(adEvent))
            else -> return
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if (isPlaying) {
            pancastAssist.sendVideoEvent(createVideoEventForAction(PAVideoAction.Play), videoMeta)
        } else {
            pancastAssist.sendVideoEvent(createVideoEventForAction(PAVideoAction.Pause), videoMeta)
        }
    }

    private fun createVideoEventForAction(videoAction: PAVideoAction): PAVideoEvent {
        return PAVideoEvent().apply {
            vid = videoMeta.id
            pid = "ExoPlayer Test Harness"
            action = videoAction
            ctime = player.currentPosition.toDouble()
            pp = player.currentPosition.toDouble() / player.duration.toDouble()
            vd = player.duration.toDouble()
        }
    }

    private fun createAdEventForAction(adAction: PAAdAction): PAAdEvent {
        return PAAdEvent().apply {
            pid = "ExoPlayer Test Harness"
            vid = videoMeta.id
            action = adAction
            ctime = player.currentPosition.toDouble()
        }
    }

    private fun adMetaForAd(adEvent: AdEvent): PAAd {
        val ad = adEvent.ad
        return PAAd().apply {
            id = ad.adId
            title = ad.title
            system = ad.adSystem
            advertiser = ad.advertiserName
            creative_id = ad.creativeId
            description = ad.description
            duration = ad.duration
            type = "linear"
            universal_ad_id = ad.universalAdIdValue
            viewable = true
        }
    }

}