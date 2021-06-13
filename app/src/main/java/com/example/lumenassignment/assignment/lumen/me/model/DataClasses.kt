package com.example.lumenassignment.assignment.lumen.me.model

import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.lumenassignment.R
import com.google.gson.annotations.SerializedName


class BreathItem(
    @SerializedName("isValid")
    var isValid: Boolean,

    @SerializedName("date")
    var date: String? = null,

    @SerializedName("flow")
    var flow: ArrayList<Float> = arrayListOf()
) : IBreathItem {
    override fun getValidText(): Int {
        return if (isValid) R.string.home_page_breath_state_valid
        else R.string.home_page_breath_state_invalid
    }

    override fun getDateText(): String {
        return date ?: ""
    }
}

interface IBreathItem {
    @StringRes
    fun getValidText(): Int
    fun getDateText(): String
}

class GraphDetails(
    val xStepDp: Float = 0F,
    val xAxis: Float = 0F,
    var flowYPoints: List<Float> = arrayListOf(),
    val intervalGraphDraw: Int
)

sealed class MediaPlayerState {
    open var index = 0

    object Play : MediaPlayerState()

    object PlayFast : MediaPlayerState()

    object Pause : MediaPlayerState()

    object Rewind : MediaPlayerState()

    object None : MediaPlayerState()

}

sealed class GraphState {
    open var index = 0
    open val xfermode: PorterDuffXfermode? = null

    object DrawGraph : GraphState() {
        override val xfermode: PorterDuffXfermode? get() = null
    }

    object StopDraw : GraphState()

    object EraseGraph : GraphState(){
        override val xfermode: PorterDuffXfermode get() =  PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
}


