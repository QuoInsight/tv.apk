package com.quoinsight.tv

object MovieList {
    val MOVIE_CATEGORY = arrayOf(
        "Category Zero",
        "Category One",
        "m3u8" /*"Category Two",
        "Category Three",
        "Category Four",
        "Category Five"*/
    )

    //val list: List<Movie> by lazy {
	//val list: MutableList by lazy {
	val list: ArrayList<Movie> by lazy {
        setupMovies()
    }

    //private var count: Long = 0
	private var count: Int = 0

    //private fun setupMovies(): List<Movie> {
	//private fun setupMovies(): MutableList<Movie> {
	private fun setupMovies(): ArrayList<Movie> {
        val title = arrayOf(
            //"Zeitgeist 2010_ Year in Review",
            "Google Demo Slam_ 20ft Search",
            "8TV","TV2","CNA"/*,"AEC","Introducing Gmail Blue",
            "Introducing Google Fiber to the Pole",
            "Introducing Google Nose"*/
        )

        val description = "<description...>"
        val studio = arrayOf(
            "Studio Zero",
            "Studio One",
            "Studio Two",
            "Studio Three",
            "Studio Four"
        )
        val videoUrl = arrayOf(
            //"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search.mp4",
            "https://tonton-live-ssai.akamaized.net/live/a884c33b-6b11-4433-8bf9-a8899939e224/live.isml/.m3u8",
            "https://d25tgymtnqzu8s.cloudfront.net/smil:tv2/playlist.m3u8?id=2",
            "https://d2e1asnsl7br7b.cloudfront.net/7782e205e72f43aeb4a48ec97f66ebbe/index.m3u8"
            /*"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue.mp4",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole.mp4",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose.mp4"*/
        )
        val bgImageUrl = arrayOf(
            //"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
            "https://www.xtra.com.my/wp-content/themes/xtra_revamp/assets/img/tv8.png",
            "https://www.livenewsmag.com/wp-content/uploads/2017/02/RTM-TV-2.jpg",
            "https://i.imgur.com/xWglicB.png"
            /*"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/bg.jpg",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg"*/
        )
        val cardImageUrl = arrayOf(
            //"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/card.jpg",
            "https://www.xtra.com.my/wp-content/themes/xtra_revamp/assets/img/tv8.png",
            "https://www.livenewsmag.com/wp-content/uploads/2017/02/RTM-TV-2.jpg",
            "https://i.imgur.com/xWglicB.png"
            /*"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/card.jpg",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/card.jpg",
            "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/card.jpg"*/
        )

        var list = ArrayList( title.indices.map {
            buildMovieInfo(
                title[it],
                description,
                "<studio...>", //studio[it],
                videoUrl[it],
                cardImageUrl[it],
                bgImageUrl[it]
            )
        } )

        // https://stackoverflow.com/questions/55488291/how-to-add-an-item-to-a-list-in-kotlin
		// list.toMutableList().add(item) || list.add(item)
		
		var movie = Movie()
		  movie.id = count++
		  movie.title = "AEC"
		  movie.description = "<description...>"
		  movie.studio = "<studio...>"
		  movie.cardImageUrl = "https://dj7fdt04hl8tv.cloudfront.net/acm/media/njoi/astro-aec-656x388.jpg"
		  movie.backgroundImageUrl = movie.cardImageUrl
		  movie.videoUrl = "https://sooka.my/en/watch/channel/aec-hd/1497824"
		list.add(movie)

        movie = Movie()
		  movie.id = count++
		  movie.title = "CCTV4"
		  movie.description = "<description...>"
		  movie.studio = "<studio...>"
		  movie.cardImageUrl = "https://i.imgur.com/ovUSVEQ.png"
		  movie.backgroundImageUrl = movie.cardImageUrl
		  movie.videoUrl = "https://tv.cctv.com/live/cctv4/m/index.shtml"
		list.add(movie)

        // https://kotlinlang.org/docs/serialization.html#serialize-and-deserialize-json

		/*try {
			movie = kotlinx.serialization.json.Json.decodeFromString<Movie>("""{
			  "id": 99,
			  "title": "NHK",
			  "description": "<description...>",
			  "studio": "<studio...>",
			  "cardImageUrl": "https://i.imgur.com/TDCuUDs.png",
			  "backgroundImageUrl": "https://i.imgur.com/TDCuUDs.png",
			  "videoUrl": "https://nhkwlive-ojp.akamaized.net/hls/live/2003459/nhkwlive-ojp-en/index.m3u8"
			}""")
			list.add(movie)
		} catch(e: Exception) {
			//commonGui.writeMessage(
			//  	android.content.Context.getApplicationContext(),
			//	"MovieList.setupMovies()", e.message.toString()
			//)
		}*/

        return list
    }

    private fun buildMovieInfo(
        title: String,
        description: String,
        studio: String,
        videoUrl: String,
        cardImageUrl: String,
        backgroundImageUrl: String
    ): Movie {
        val movie = Movie()
        movie.id = count++
        movie.title = title
        movie.description = description
        movie.studio = studio
        movie.cardImageUrl = cardImageUrl
        movie.backgroundImageUrl = backgroundImageUrl
        movie.videoUrl = videoUrl
        return movie
    }
}
