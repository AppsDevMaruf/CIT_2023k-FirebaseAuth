package com.maruf.firebaseauth.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    const val DummyImgLink = "https://avatars.githubusercontent.com/u/44240762?s=400&u=3d47d70fa5de9672024eebaea40d4fb80b6f157e&v=4"


}
fun convertTimestampToTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val date = Date(timestamp)
    return dateFormat.format(date)
}
