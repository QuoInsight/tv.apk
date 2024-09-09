package com.quoinsight.tv

import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow

/** Handles video playback with media controls. */
class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: androidx.leanback.media.PlaybackTransportControlGlue<MediaPlayerAdapter>
    private lateinit var glueHost : androidx.leanback.app.VideoSupportFragmentGlueHost
    private lateinit var playerAdapter :androidx.leanback.media.MediaPlayerAdapter

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        val (_, title, description, _, _, videoUrl) =
            activity?.intent?.getSerializableExtra(DetailsActivity.MOVIE) as Movie

        //val glueHost = androidx.leanback.app.VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        //val playerAdapter = androidx.leanback.media.MediaPlayerAdapter(context)
        glueHost = androidx.leanback.app.VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        playerAdapter = androidx.leanback.media.MediaPlayerAdapter(context)
        playerAdapter.setRepeatAction(androidx.leanback.widget.PlaybackControlsRow.RepeatAction.INDEX_NONE)

        mTransportControlGlue = androidx.leanback.media.PlaybackTransportControlGlue(getActivity(), playerAdapter)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.title = title
        mTransportControlGlue.subtitle = description
        mTransportControlGlue.playWhenPrepared()

        playerAdapter.setDataSource(android.net.Uri.parse(videoUrl))
    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }
}
