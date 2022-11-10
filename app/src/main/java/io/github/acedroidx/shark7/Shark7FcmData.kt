package io.github.acedroidx.shark7

public class Shark7FcmData(map: Map<String, String>) {
    val event by map
    val is_show_notification by map.withDefault { "None" }
}