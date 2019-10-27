package th.ac.up.se.takingbear

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory

import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        video_back.setOnClickListener {
            finish()
        }
        val bundle = intent.extras!!
        val url = bundle.getString("URL")!!

        initializePlayer(url)



    }



    private fun initializePlayer(url: String) {
        Log.e("VIDEOs",url)

        val uri = Uri.parse(url)
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(
                    DefaultRenderersFactory(this),
                    DefaultTrackSelector(),
                    DefaultLoadControl())
            //playerView?.player = player
            player?.playWhenReady = true
            //player.seekTo(currentWindow, playbackPosition)
        }
        val mediaSource = buildMediaSource(uri)

        if( mediaSource != null){
            player?.prepare(mediaSource, true, false)
        }else {
            //Log.e("SO",mediaSource)

            finish()
        }

        player_activity_video_view.player = player

    }

    private fun buildMediaSource(uri: Uri): MediaSource? {

        val userAgent = "exoplayer-codelab"

        /*if (uri.lastPathSegment.contains("mp3") || uri.lastPathSegment.contains("mp4")) {
            return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri)
        } else if (uri.lastPathSegment.contains("m3u8")) {
            return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri)
        } else {

            return null
        }
        */

        return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            player?.release()
            player = null
        }
    }

    override fun onStop() {
        super.onStop()

        player?.release()
        player = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        /*
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUiFullScreen()
        } else {
            hideSystemUi()
        }
        */
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUiFullScreen() {
        player_activity_video_view!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    }


    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        player_activity_video_view!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }


}
