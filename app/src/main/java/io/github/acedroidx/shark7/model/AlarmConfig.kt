package io.github.acedroidx.shark7.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AlarmConfig(
    val enableAudio: Boolean,
    val audioAttr: MyAudioAttributes,
    val headphoneOnly: Boolean,
    val enableGadgetCall: Boolean
) : Parcelable {}