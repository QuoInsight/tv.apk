package com.quoinsight.tv

class ExoPlayerActivity : android.app.Activity() {

	public lateinit var exoPlayer : androidx.media3.exoplayer.ExoPlayer
	public var videoUrl : String? = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
	public var startedPlaying : Boolean = false

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
			//mSelectedMovie = activity!!.intent.getSerializableExtra(DetailsActivity.MOVIE) as Movie
			val extras : android.os.Bundle? = getIntent().getExtras()
			if (extras != null) {
				videoUrl= extras.getString("videoUrl")
			}
        } else {
			videoUrl = savedInstanceState.getSerializable("videoUrl") as String
		}
        setContentView(R.layout.exoplayer_activity)

		startedPlaying = false
		var i: Int = 0
		try {
			// https://stackoverflow.com/questions/68390773/create-a-simple-exoplayer-to-stream-video-from-url
			// https://developer.android.com/media/implement/playback-app
			exoPlayer = androidx.media3.exoplayer.ExoPlayer.Builder(this).build()
			i = 1
			//val mediaSession = androidx.media3.session.MediaSession.Builder(this, exoPlayer).build()
			// error Session ID
			i = 2
			var playerView = findViewById(R.id.player_view) as androidx.media3.ui.PlayerView // must match the android:id specified in R.layout.exoplayer_view
			i = 3
			  playerView.setPlayer(exoPlayer)
			i = 4
			/*
			  // !! ExoPlayer doesn't have an event called OnPrepared !!
			  mediaPlayer.setOnPreparedListener (
				object : android.media.MediaPlayer.OnPreparedListener {
                    override fun onPrepared(mediaPlayer: android.media.MediaPlayer) {
						mediaPlayer.start()
                    }
				}
			  )
			i = 5
			*/
			  playerView!!.player?.addListener(
				object : androidx.media3.common.Player.Listener {
					// https://developer.android.com/reference/androidx/media3/common/Player.Listener
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
						super.onIsPlayingChanged(isPlaying)
						commonGui.writeMessage(getApplicationContext()!!, "onIsPlayingChanged()", isPlaying.toString())
						//if (isPlaying) { ... }
                    }
					override fun onPositionDiscontinuity(
						oldPosition: androidx.media3.common.Player.PositionInfo,
						newPosition: androidx.media3.common.Player.PositionInfo,
						reason: Int // @Player.DiscontinuityReason
					) {
						super.onPositionDiscontinuity(oldPosition, newPosition, reason)
						commonGui.writeMessage(getApplicationContext()!!, "onPositionDiscontinuity()", reason.toString())
						// if (reason == SimpleExoPlayer.DISCONTINUITY_REASON_PERIOD_TRANSITION) { ... }
					}
					override fun onTracksChanged(tracks: androidx.media3.common.Tracks) {
						super.onTracksChanged(tracks)
						commonGui.writeMessage(getApplicationContext()!!, "onTracksChanged()", "")
					}
					override fun onPlaybackStateChanged(playbackState: Int) { // @Player.State
						// onPlayerStateChanged() deprecated
						super.onPlaybackStateChanged(playbackState)
						commonGui.writeMessage(getApplicationContext()!!, "onPlaybackStateChanged()", playbackState.toString())
						if ( playbackState == androidx.media3.common.Player.STATE_READY ) {
							if ( ! startedPlaying ) {
								// workaround to force playNext() to avoid m3u8 stop playing video after few seconds
								// https://stackoverflow.com/questions/62199914/video-stream-freezes-after-few-seconds-but-sound-works-fine
								// https://github.com/jellyfin/jellyfin-androidtv/issues/2248
								exoPlayer.next()
								commonGui.writeMessage(getApplicationContext()!!, "..", "exoPlayer.next()")
							}
							startedPlaying = true
						}
					}
					/*
                    override fun onVideoSizeChanged(videoSize: androidx.media3.common.VideoSize) {
                    }
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
						super.onPlayerError(error)
						Log.e(TAG, "PlaybackException $error")
                    }
					*/
				}
			  )
			i = 6

			var mediaItem = androidx.media3.common.MediaItem.fromUri(videoUrl.toString())
			i = 7
			  exoPlayer.addMediaItem(mediaItem)
			i = 8
			  exoPlayer.prepare()
			i = 9
			  exoPlayer.setPlayWhenReady(true) // required when you are playing a video from URL
		} catch(e: Exception) {
			commonGui.writeMessage(getApplicationContext()!!, "ExoPlayerActivity.onCreate()#"+i, e.message.toString())
		}
    }

    override fun onPause() {
		exoPlayer.stop()
    }

}