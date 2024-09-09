package com.quoinsight.tv

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/*
  Loads [MainFragment]
  A Fragment represents a reusable portion of your app's UI. Fragments introduce modularity and reusability
  into your activity's UI by letting you divide the UI into discrete chunks...
*/
class MainActivity : androidx.fragment.app.FragmentActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(
			    R.id.main_browse_fragment, MainFragment()
			).commitNow()
        }

        // android.app.Application.applicationContext
        //android.widget.Toast.makeText(this, "testing ...", android.widget.Toast.LENGTH_LONG).show() 
        //commonGui.writeMessage(this, "MainActivity", "onCreate")

        // do one simple task
        //sysUtil.launchUrl(this@MainActivity, "https://sooka.my/en/watch/channel/aec-hd/1497824")
        //sysUtil.launchUrl(this@MainActivity, "https://d25tgymtnqzu8s.cloudfront.net/smil:tv2/playlist.m3u8?id=2")
        //finish() // all done and exit/quit !

    }
} 
