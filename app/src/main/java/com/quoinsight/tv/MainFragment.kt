package com.quoinsight.tv

import java.util.Collections
import java.util.Timer
import java.util.TimerTask

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment() {

    private val mHandler = Handler(Looper.myLooper()!!)
    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var mBackgroundTimer: Timer? = null
    private var mBackgroundUri: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        prepareBackgroundManager()

        setupUIElements()

        loadRows()

        setupEventListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: " + mBackgroundTimer?.toString())
        mBackgroundTimer?.cancel()
    }

    private fun prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(activity!!.window)
        mDefaultBackground = ContextCompat.getDrawable(context!!, R.drawable.default_background)
        mMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        // over title
        headersState = BrowseSupportFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(context!!, R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(context!!, R.color.search_opaque)
    }

    private fun loadRows() {
        var list = MovieList.list
		var x: Int = 0
		try {
			val json = kotlinx.serialization.json.Json { isLenient=true; ignoreUnknownKeys=true; explicitNulls=false; prettyPrint=true }
			x = 1

		/*
			// below tested working fine for LocalTestDataClass
			val testData0 = LocalTestDataClass("a",0)
			x = 2
			// https://github.com/Kotlin/kotlinx.serialization/issues/1286
			//val testString0 = kotlinx.serialization.encodeToString(testData0)
			val testString0 = json.encodeToString(kotlinx.serialization.serializer(LocalTestDataClass::class.java), testData0) 
			x = 3
			commonGui.writeMessage(context!!, "#"+x, testString0)
		*/
		/*	// below tested working fine
			x = 4
			val testData1 = kotlinx.serialization.json.Json.decodeFromString<TestDataClass>("""{"s":"a","i":1}""")
			/*val testData1 = json.decodeFromString(
			  kotlinx.serialization.serializer(TestDataClass::class.java),
			  """{"s":"a","i":1}"""
			)*/
			x = 5
			val testData2 = json.decodeFromString<Array<TestDataClass>>("""[{"s":"a","i":1}]""")
			x = 6
		*/

			//#EXTINF:-1 tvg-id="TDM3.mo" tvg-logo="https://i.imgur.com/oFPUZ97.png" group-title="Macau",Ch.96
			//var movie = json.decodeFromString(kotlinx.serialization.serializer(Movie::class.java), """
			//var movie = json.decodeFromString<Movie>("""
			var movieArr = json.decodeFromString<Array<Movie>>("""[
			{
				"id"                 : 7,
				"title"              : "NHK",
				"description"        : "<description...>",
				"backgroundImageUrl" : "https://i.imgur.com/TDCuUDs.png",
				"cardImageUrl"       : "https://i.imgur.com/TDCuUDs.png",
				"videoUrl"           : "https://nhkwlive-ojp.akamaized.net/hls/live/2003459/nhkwlive-ojp-en/index.m3u8",
				"studio"             : "<studio...>"
			},{
				"id"                 : 99,
				"title"              : "TDM",
				"description"        : "",
				"backgroundImageUrl" : "https://i.imgur.com/oFPUZ97.png",
				"cardImageUrl"       : "https://i.imgur.com/oFPUZ97.png",
				"videoUrl"           : "https://live5.tdm.com.mo/ch3/ch3.live/playlist.m3u8",
				"studio"             : "<studio...>"
			}
			]""")
			x = 6

			for (m in movieArr) { 
				list.add(m as Movie)
			}
		} catch(e: Exception) {
			//commonGui.writeMessage(context!!, "MainFragment.loadRows()#"+x, e.message.toString())
			val errMsg = e.message.toString().replace("Serializer for class 'TestDataClass'","")
			commonGui0.msgBox(context!!, "#"+x, errMsg)
			(this@MainFragment).title = errMsg
		}

		val list_count = list.count()
		NUM_ROWS = MovieList.MOVIE_CATEGORY.count()
		//NUM_COLS = 5

        val rowsAdapter = androidx.leanback.widget.ArrayObjectAdapter(androidx.leanback.widget.ListRowPresenter())
        val cardPresenter = CardPresenter()

        for (i in 0 until NUM_ROWS) {
            if (i != 0) {
                Collections.shuffle(list)
            }
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            for (j in 0 until list_count) {
                listRowAdapter.add(list[j])
            }
            val header = HeaderItem(i.toLong(), "["+i.toString()+":"+list_count.toString()+"] "+MovieList.MOVIE_CATEGORY[i])
            rowsAdapter.add(ListRow(header, listRowAdapter))
        }

        val gridHeader = HeaderItem(NUM_ROWS.toLong(), "PREFERENCES")
			val gridRowAdapter = ArrayObjectAdapter(GridItemPresenter())
			gridRowAdapter.add(resources.getString(R.string.grid_view))
			gridRowAdapter.add(getString(R.string.error_fragment))
			gridRowAdapter.add(resources.getString(R.string.personal_settings))
            rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(context!!, "Implement your own in-app search", Toast.LENGTH_LONG)
                .show()
        }

        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {

            if (item is Movie) {
              Log.d(TAG, "Item: " + item.toString())

		      if ( item.title.toString() in arrayOf("AEC","CCTV4") ) {
                //launch web browser

			    sysUtil.launchUrl(context!!, item.videoUrl.toString())

		      //} else if (item.title.toString() == "TV2") {
		      } else if ( item.title.toString() in arrayOf("TV2","8TV","TDM") ) {
                
                /*
				// directly trigger the PlaybackActivity -> PlaybackVideoFragment 
				val intent = Intent(context!!, PlaybackActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item)
                startActivity(intent)
				*/

				// start exoPlayer instead
				try {
					//startActivity(android.content.Intent(context!!, ExoPlayerActivity::class.java))
					val intent = Intent(context!!, ExoPlayerActivity::class.java)
					intent.putExtra("videoUrl", item.videoUrl.toString())
					startActivity(intent)					
				} catch(e: Exception) {
					commonGui.writeMessage(context!!, "MainFragment.ItemViewClickedListener()", e.message.toString())
				}
				
		      } else {
                //launch DetailsActivity page

                val intent = Intent(context!!, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item)

                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    DetailsActivity.SHARED_ELEMENT_NAME
                ).toBundle()
                startActivity(intent, bundle)

              }
            } else if (item is String) {
                if (item.contains(getString(R.string.error_fragment))) {
                    val intent = Intent(context!!, BrowseErrorActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(context!!, item, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?, item: Any?,
            rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {
            if (item is Movie) {
                mBackgroundUri = item.backgroundImageUrl
                startBackgroundTimer()
            }
        }
    }

    private fun updateBackground(uri: String?) {
        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        Glide.with(context!!)
            .load(uri)
            .centerCrop()
            .error(mDefaultBackground)
            .into<SimpleTarget<Drawable>>(
                object : SimpleTarget<Drawable>(width, height) {
                    override fun onResourceReady(
                        drawable: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        mBackgroundManager.drawable = drawable
                    }
                })
        mBackgroundTimer?.cancel()
    }

    private fun startBackgroundTimer() {
        mBackgroundTimer?.cancel()
        mBackgroundTimer = Timer()
        mBackgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
    }

    private inner class UpdateBackgroundTask : TimerTask() {

        override fun run() {
            mHandler.post { updateBackground(mBackgroundUri) }
        }
    }

    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.default_background))
            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return Presenter.ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
            (viewHolder.view as TextView).text = item as String
        }

        override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {}
    }

    companion object {
        private val TAG = "MainFragment"

        private val BACKGROUND_UPDATE_DELAY = 300
        private val GRID_ITEM_WIDTH = 200
        private val GRID_ITEM_HEIGHT = 200

        //private val NUM_ROWS = 6
        //private val NUM_COLS = 15
		public var NUM_ROWS : Int = 6  
        public var NUM_COLS : Int = 15
    }
}

// declare the data class here locally instead of having that in TestDataClass.kt
@kotlinx.serialization.Serializable
data class LocalTestDataClass(val s: String, val i: Int)
