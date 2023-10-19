package io.github.acedroidx.shark7

import java.time.Duration

object Utils {
    fun formatMilliseconds(milliseconds: Long): String {
        val duration = Duration.ofMillis(milliseconds)
        val minutes = duration.toMinutes()
        val seconds = duration.seconds % 60

        return if (minutes > 0) {
            String.format("%dm%02ds", minutes, seconds)
        } else {
            String.format("%ds", seconds)
        }
    }
}