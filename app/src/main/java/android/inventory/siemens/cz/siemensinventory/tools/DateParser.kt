package android.inventory.siemens.cz.siemensinventory.tools

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateParser {

    companion object {

        const val datePattern = "yyyy-MM-dd"
        private val formatter = SimpleDateFormat(datePattern, Locale.getDefault())

        fun fromString(stringValue: String): Date? {
            return try {
                formatter.parse(stringValue)
            } catch (ex: ParseException) {
                null
            }
        }

        fun toString(date: Date): String? {
            return formatter.format(date)
        }
    }
}