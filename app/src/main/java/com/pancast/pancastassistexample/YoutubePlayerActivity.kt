package com.pancast.pancastassistexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import com.pancast.assistsdk.PancastAssist
import com.pancast.assistsdk.events.PAVideoAction
import com.pancast.assistsdk.events.PAVideoEvent
import com.pancast.assistsdk.meta.PAVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YoutubePlayerActivity : AppCompatActivity(), YouTubePlayerListener, YouTubePlayerCallback, PancastAssist.DebugListener {

    private lateinit var pancastAssist: PancastAssist
    private lateinit var mYoutubePlayerView: YouTubePlayerView
    private lateinit var mYoutubePlayer: YouTubePlayer
    private lateinit var debugTextView: TextView

    private var currentTime = 0.0

    private val videoMeta = PAVideo().apply {
        id = "9uyyOHIidMA"
        title = "videoTitle"
        duration = 99.0
        owner = "videoOwner"
        channel = "videoChannel"
        tags = listOf("tag1", "tag2")
        country = "AU"
        language = "en"
        mode = "vod"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        debugTextView = findViewById(R.id.debugTextView)
        debugTextView.movementMethod = ScrollingMovementMethod()

        pancastAssist = PancastAssist(this, "pancast-mobile-youtube-player", "tv-pancast-prod2.mini.snplow.net", this)

        mYoutubePlayerView = findViewById(R.id.youtubePlayerView)
        lifecycle.addObserver(mYoutubePlayerView)
        mYoutubePlayerView.getYouTubePlayerWhenReady(this)
    }

    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
        mYoutubePlayer = youTubePlayer
        mYoutubePlayer.addListener(this)
        mYoutubePlayer.loadVideo("9uyyOHIidMA", 0.0f)
    }

    override fun onApiChange(youTubePlayer: YouTubePlayer) {
        //
    }

    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        currentTime = second.toDouble()
    }

    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
        pancastAssist.sendVideoEvent(createVideoEventForAction(PAVideoAction.Error), videoMeta)
    }

    override fun onPlaybackQualityChange(
        youTubePlayer: YouTubePlayer,
        playbackQuality: PlayerConstants.PlaybackQuality
    ) {
        //
    }

    override fun onPlaybackRateChange(
        youTubePlayer: YouTubePlayer,
        playbackRate: PlayerConstants.PlaybackRate
    ) {
        //
    }

    override fun onReady(youTubePlayer: YouTubePlayer) {
        //
    }

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        when (state) {
            PlayerConstants.PlayerState.PLAYING -> pancastAssist.sendVideoEvent(createVideoEventForAction(PAVideoAction.Play), videoMeta)
            PlayerConstants.PlayerState.PAUSED -> pancastAssist.sendVideoEvent(createVideoEventForAction(PAVideoAction.Pause), videoMeta)
            PlayerConstants.PlayerState.ENDED -> pancastAssist.sendVideoEvent(createVideoEventForAction(PAVideoAction.End), videoMeta)
            else -> return
        }

    }

    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
        videoMeta.duration = duration.toDouble()
    }

    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
        videoMeta.id = videoId
    }

    override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {
        //
    }

    override fun PancastAssistEventLog(output: String?) {
        debugTextView.append("$output  \n")
    }

    private fun createVideoEventForAction(videoAction: PAVideoAction): PAVideoEvent {
        return PAVideoEvent().apply {
            vid = videoMeta.id
            pid = "YouTube Player Test Harness"
            action = videoAction
            ctime = currentTime
            pp = currentTime / videoMeta.duration
            vd = videoMeta.duration
        }
    }
}
