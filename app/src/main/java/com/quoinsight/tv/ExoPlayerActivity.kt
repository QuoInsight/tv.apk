package com.quoinsight.tv

class ExoPlayerActivity : android.app.Activity() {

	public lateinit var exoPlayer : androidx.media3.exoplayer.ExoPlayer
	public var videoUrl : String? = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

	public var doneManualWA : Int = 0
	public var epochSec1 : Long = 0 // java.time.Instant.now().getEpochSecond()
	public lateinit var timeoutHandler : android.os.Handler

	//////////////////////////////////////////////////////////////////////

	fun playNext() {
		// https://github.com/donparapidos/ExoPlayer/blob/master/library/src/main/java/com/google/android/exoplayer2/ui/PlaybackControlView.java
		var currentTimeline = exoPlayer.getCurrentTimeline()
		if (currentTimeline.isEmpty()) {
			return
		}
		var currentWindowIndex = exoPlayer.getCurrentWindowIndex()
		if (currentWindowIndex < currentTimeline.getWindowCount() - 1) {
			currentWindowIndex = currentWindowIndex + 1
		} else {
			var currentWindow = androidx.media3.common.Timeline.Window();
			if (! currentTimeline.getWindow(currentWindowIndex, currentWindow, 0).isDynamic) {
				return
			}
		}
		exoPlayer.seekTo(currentWindowIndex, Long.MIN_VALUE + 1) // C.TIME_UNSET == Long.MIN_VALUE + 1 // https://github.com/donparapidos/ExoPlayer/blob/master/library/src/main/java/com/google/android/exoplayer2/C.java
	}

	//////////////////////////////////////////////////////////////////////

	public fun playNextOnTimeout(timeout: Long) {
		// MainActivity.stopOnTimeout
		// below seems to be crashing, need to debug further ...
		try {
			timeoutHandler.removeCallbacksAndMessages(null)
		} catch(e: Exception) {	}
		try {
			timeoutHandler = android.os.Handler()
			timeoutHandler.postDelayed(object : Runnable { override fun run(){ // !! must use this to avoid issue for UI thread !!
				try {
					playNext()
					//commonGui.writeMessage(this@ExoPlayerActivity, "playNextOnTimeout()", "done")					
				} catch(e: Exception) { 
					//commonGui.writeMessage(this@ExoPlayerActivity, "playNextOnTimeout()", e.message.toString())
				}
			}}, timeout)
			//commonGui.writeMessage(this@ExoPlayerActivity, "playNextOnTimeout()", "Stopping in next " + String.format("%.1f", (timeout/60000.0)) + " minute(s) ...")
		} catch(e: Exception) {
			//commonGui.writeMessage(getApplicationContext()!!, "playNextOnTimeout()", e.message.toString())
		}
		return
	}

	//////////////////////////////////////////////////////////////////////

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

		doneManualWA = 0
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
						// workaround to force playNext() to avoid m3u8 stop playing video after few seconds
						// https://stackoverflow.com/questions/62199914/video-stream-freezes-after-few-seconds-but-sound-works-fine
						// https://github.com/jellyfin/jellyfin-androidtv/issues/2248					
						if (
							doneManualWA==0 && isPlaying
							// && exoPlayer.isCommandAvailable(androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT
						) {
							// not working here
							//exoPlayer.seekToNextMediaItem() // .next() deprecated.
							//exoPlayer.play()

							// not working here although seems to be OK from the control buttons
							// exoPlayer.pause() // Equivalent to setPlayWhenReady(false)
							// exoPlayer.play() // Equivalent to setPlayWhenReady(true)

							// !! tested working OK, although this is somewhat a very ugly workaround,  !!
							playNextOnTimeout((7.5*1000).toLong())
							commonGui.writeMessage(getApplicationContext()!!, "done w/a", "1")
							doneManualWA = 1
							epochSec1 = java.time.Instant.now().getEpochSecond()
						}
                    }
					override fun onPositionDiscontinuity(
						oldPosition: androidx.media3.common.Player.PositionInfo,
						newPosition: androidx.media3.common.Player.PositionInfo,
						reason: Int // @Player.DiscontinuityReason
					) {
						super.onPositionDiscontinuity(oldPosition, newPosition, reason)
						commonGui.writeMessage(getApplicationContext()!!, "onPositionDiscontinuity()", reason.toString())
						// if (reason == SimpleExoPlayer.DISCONTINUITY_REASON_PERIOD_TRANSITION) { ... }
						// -- DISCONTINUITY_REASON_SEEK is not triggered before the initial video stopped, hence this does not help
						// if (
						//		doneManualWA==0 && reason==androidx.media3.common.Player.DISCONTINUITY_REASON_SEEK
						//		&& exoPlayer.isCommandAvailable(androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT)
						//	) {
					}
					override fun onTracksChanged(tracks: androidx.media3.common.Tracks) {
						super.onTracksChanged(tracks)
						commonGui.writeMessage(getApplicationContext()!!, "onTracksChanged()", "")
					}
					override fun onPlaybackStateChanged(playbackState: Int) { // @Player.State
						// onPlayerStateChanged() deprecated
						super.onPlaybackStateChanged(playbackState)
						commonGui.writeMessage(getApplicationContext()!!, "onPlaybackStateChanged()", playbackState.toString())
						/*
						if (
							//playbackState==androidx.media3.common.Player.STATE_BUFFERING ||
							playbackState==androidx.media3.common.Player.STATE_READY 
						) {
							// workaround to force playNext() to avoid m3u8 stop playing video after few seconds
							// https://stackoverflow.com/questions/62199914/video-stream-freezes-after-few-seconds-but-sound-works-fine
							// https://github.com/jellyfin/jellyfin-androidtv/issues/2248
							// !! this is somewhat an ugly workaround, tested working OK !!
							if ( doneManualWA==0 && exoPlayer.isCommandAvailable(androidx.media3.common.Player.COMMAND_SEEK_TO_NEXT) ) {
								exoPlayer.play()
								playNextOnTimeout((7.5*1000).toLong())
								commonGui.writeMessage(getApplicationContext()!!, "done w/a", "4")
								doneManualWA = 1
								epochSec1 = java.time.Instant.now().getEpochSecond()
							}
						}
						*/
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
			  //if above is commented out, we may manually trigger exoPlayer.play() onPlaybackStateChanged() when androidx.media3.common.Player.STATE_READY 
		} catch(e: Exception) {
			commonGui.writeMessage(getApplicationContext()!!, "ExoPlayerActivity.onCreate()#"+i, e.message.toString())
		}
    }

    override fun onPause() {
		// exoPlayer.pause()   // 
		// https://developer.android.com/reference/androidx/media3/common/Player#stop()
		exoPlayer.stop()    // the player will release the loaded media and resources required for playback. release must still be called on the player if it's no longer required.
		// stop() does not clear the playlist, reset the playback position or the playback error.
		exoPlayer.release() // This method must be called when the player is no longer required. The player must not be used after calling this method.
    }

}