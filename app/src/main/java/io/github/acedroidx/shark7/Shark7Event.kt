package io.github.acedroidx.shark7

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// https://stackoverflow.com/questions/51898966/convert-a-map-to-a-data-class
@Parcelize
public class Shark7Event(val map: Map<String, String>) : Parcelable {
    val ts by map
    val name by map
    val scope by map
    val msg by map
    override fun toString(): String {
        return "<$name>($scope)\n$msg"
    }
}

//data class Shark7Event(
//    val ts: String,
//    val name: String,
//    val scope: String,
//    val msg: String,
//){
//    companion object{
//        fun fromMap(data: Map<String,String>){
//            keyList = listOf("ts","name","scope",)
//            if(data.containsKey("ts"))
//            Shark7Event(data["ts"],)
//        }
//    }
//}
