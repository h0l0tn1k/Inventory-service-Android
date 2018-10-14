package android.inventory.siemens.cz.siemensinventory.api.entity

import android.content.Context
import android.graphics.drawable.Drawable
import android.inventory.siemens.cz.siemensinventory.R
import android.inventory.siemens.cz.siemensinventory.tools.DateParser
import android.support.v4.content.ContextCompat
import java.util.*
import java.util.concurrent.TimeUnit

class DeviceCalibration(
        var id: Long,
        var calibrationInterval: Int? = 0,
        var lastCalibrationDateString: String = ""
) {

    fun getDaysLeft(): Int? {
        if (lastCalibrationDateString.isEmpty() || calibrationInterval == null) {
            return null
        } else {
            val lastCalDate = DateParser.fromString(lastCalibrationDateString)
            if (lastCalDate != null) {
                val calendar = Calendar.getInstance()
                calendar.time = lastCalDate
                calendar.add(Calendar.YEAR, calibrationInterval!!)
                val diff = calendar.time.time.minus(Date().time)
                return Math.max(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt(), 0)
            }
        }
        return null
    }

    fun getDaysLeftColor(context: Context): Int {
        val daysLeft = getDaysLeft()
                ?: return ContextCompat.getColor(context, android.R.color.holo_orange_dark)
        return if (daysLeft > 0) {
            ContextCompat.getColor(context, android.R.color.holo_green_dark)
        } else {
            ContextCompat.getColor(context, android.R.color.holo_red_dark)
        }
    }

    fun getDaysLeftIcon(context: Context): Drawable? {
        val daysLeft = getDaysLeft()
                ?: return ContextCompat.getDrawable(context, R.drawable.ic_help_yellow_800_24dp)
        return if (daysLeft > 0) {
            ContextCompat.getDrawable(context, R.drawable.ic_check_green_a700_24dp)
        } else {
            ContextCompat.getDrawable(context, R.drawable.ic_close_red_800_24dp)
        }
    }

    fun getPeriodString(): String {
        return if (calibrationInterval == null) {
            "Not Defined"
        } else {
            calibrationInterval.toString() + " year(s)"
        }
    }
}