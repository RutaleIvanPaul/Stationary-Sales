package io.ramani.ramaniStationary.domain.datetime

import android.annotation.SuppressLint
import io.ramani.ramaniStationary.domain.datetime.DateFormatter.Companion.CALENDAR_FORMAT
import io.ramani.ramaniStationary.domain.datetime.DateFormatter.Companion.DATE_WITH_DASHES_1
import io.ramani.ramaniStationary.domain.datetime.DateFormatter.Companion.SERVER_RECEIVE_DATE_FORMAT
import io.ramani.ramaniStationary.domain.datetime.DateFormatter.Companion.TIME_FORMAT_24_hours
import io.ramani.ramaniStationary.domain.datetime.DateFormatter.Companion.TIME_FORMAT_AM_PM
import io.ramani.ramaniStationary.domainCore.datetime.IDateFormatter
import io.ramani.ramaniStationary.domainCore.datetime.IDateTimeManager
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by raede on 27/10/2017.
 */
@SuppressLint("SimpleDateFormat")
class DateFormatter(private val dateTimeManager: IDateTimeManager) : IDateFormatter {

    companion object {
        const val DAY_OF_WEEK_FORMAT = "EEEE"
        const val DAY_OF_WEEK_SHORT_FORMAT = "EEE"
        const val DAY_FORMAT = "dd"
        const val MONTH_FORMAT = "MMM"
        const val FULL_MONTH_FORMAT = "MMMM"
        const val FULL_MONTH_YEAR_FORMAT = "MMMM yyyy"
        const val SERVER_SEND_DATE_FORMAT = "dd/MM/yyyy"
        const val DURATION_TIME_FORMAT = "mm:ss"
        const val SERVER_RECEIVE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val VIEW_DISPLAY_DATE_FORMAT = "hh:mm a • dd/MM, yyyy"
        const val SERVER_RECEIVE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
        const val CALENDAR_FORMAT = "dd MMM yyyy"
        const val UTC_TIME_ZONE = "UTC"
        const val MEDIA_DOWNLOAD_FORMAT = "yyyyMMdd"
        const val TIME_FORMAT_AM_PM = "hh:mm a"
        const val TIME_FORMAT_24_hours = "HH:mm"
        const val NEWS_FFED_DATE_FORMAT = "dd MMMM"
        const val ACTIVITY_DEADLINE_FORMAT = "EEE, dd MMM yy"
        const val DATE_WITH_DASHES = "dd-MM-yyyy"
        const val DATE_WITH_DASHES_1 = "yyyy-MM-dd"
        const val DATE_WITH_DASHES_FULL = "yyyy-MM-dd'T'HH:mm:ss"
        const val DATE_WITH_FULL = "EEE, dd MMM, yyyy"
    }


    fun getDateInMillisFromServerDate(date: String): Long {
        return try {
            val simpleDateFormat = SimpleDateFormat(SERVER_RECEIVE_DATE_FORMAT)
            val time: Date = simpleDateFormat.parse(date)
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = time
            calendar.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    fun convertToServerDateFormat(date: Long): String = format(date, SERVER_RECEIVE_DATE_FORMAT)
    fun convertToDisplayDateFormat(date: Long): String = format(date, VIEW_DISPLAY_DATE_FORMAT)
    fun convertToDateWithDashes(date: Long): String = format(date, DATE_WITH_DASHES)
    fun convertToDateWithDashesInLocalTimeZone(date: Long): String =
        formatDisplay(date, DATE_WITH_DASHES)

    fun convertToDateWithDashesInLocalTimeZoneForFamoco(date: String): String =
        formatDisplay(getDateInMillisFromDate(date), SERVER_RECEIVE_DATE_FORMAT)

    fun convertToDateWithDashes1(date: Long): String = format(date, DATE_WITH_DASHES_1)
    fun convertToCalendarFormatDate(date: Long): String = format(date, CALENDAR_FORMAT)


    fun getDateInMillisFromDate(date: String): Long {
        return try {
            val simpleDateFormat = SimpleDateFormat(VIEW_DISPLAY_DATE_FORMAT)
            val time: Date = simpleDateFormat.parse(date)
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = time
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    fun getTimeInMillisFromSenderDate(date: String): Long {
        return try {
            val simpleDateFormat = SimpleDateFormat(SERVER_SEND_DATE_FORMAT)
            val time: Date = simpleDateFormat.parse(date)
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = time
            calendar.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    fun is24Hours(): Boolean {
        return dateTimeManager.is24Hours()
    }

    fun getCurrentYear(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.YEAR)
    }


    @Suppress("unused")
    fun getCurrentMonthNumber(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.MONTH)
    }

    @Suppress("unused")
    fun getWeekDay(date: String): Int {
        return try {
            val simpleDateFormat = SimpleDateFormat(SERVER_RECEIVE_DATE_FORMAT)
            val time: Date = simpleDateFormat.parse(date)
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = time
            calendar.get(Calendar.DAY_OF_WEEK)
        } catch (e: ParseException) {
            e.printStackTrace()
            Calendar.MONDAY
        }
    }

    fun getCalendarTimeString(date: Date): String {
        val simpleDateFormat = SimpleDateFormat(CALENDAR_FORMAT)
        return simpleDateFormat.format(date)
    }

    fun getServerTimeFromServerDate(createdAt: String?): String =
        //2021-12-22T16:31:03.823Z
        createdAt?.split("T")?.get(1)?.split(".")?.get(0) ?: ""

    fun getCalendarTimeWithDashes(date: Date): String {
        val simpleDateFormat = SimpleDateFormat(DATE_WITH_DASHES_1)
        return simpleDateFormat.format(date)
    }

    fun getCalendarTimeWithDashesFull(date: Date): String {
        val simpleDateFormat = SimpleDateFormat(DATE_WITH_DASHES_FULL)
        return simpleDateFormat.format(date)
    }

    fun getCalendarFullTime(date: Date): String {
        val simpleDateFormat = SimpleDateFormat(DATE_WITH_FULL)
        return simpleDateFormat.format(date)
    }

    fun getTimeWithFormmatter(date: Date, formatter: String): String {
        val simpleDateFormat = SimpleDateFormat(formatter)
        return simpleDateFormat.format(date)
    }
}


@Suppress("unused")
fun getTotalWeeksInYear(year: Int): Int {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, Calendar.DECEMBER)
    cal.set(Calendar.DAY_OF_MONTH, 31)
    val ordinalDay = cal.get(Calendar.DAY_OF_YEAR)
    val weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1 // Sunday = 0
    return (ordinalDay - weekDay + 10) / 7
}


fun isTodayInRange(startDate: Long, endDate: Long): Boolean {
    val start = DateTime.now().withMillis(startDate).toLocalDate()
    val end = DateTime.now().withMillis(endDate).toLocalDate()
    val today = DateTime.now().toLocalDate()

    return today.isAfter(start) || today == start && today.isBefore(end) || today == end
}

@SuppressLint("SimpleDateFormat")
private fun format(
    dateStr: String, fromFormat: String, toFormat: String, fromTimeZone: String,
    toTimeZone: String
): String {
    var formattedDate = ""
    try {
        val fromSdf = SimpleDateFormat(fromFormat)
        fromSdf.timeZone = TimeZone.getTimeZone(fromTimeZone)
        val toSdf = SimpleDateFormat(toFormat, Locale.ENGLISH)
        toSdf.timeZone = TimeZone.getTimeZone(toTimeZone)
        val date: Date = fromSdf.parse(dateStr)
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.SECOND, 0)
        formattedDate = toSdf.format(calendar.time)
//            if (fromSdf.timeZone.useDaylightTime()) {
//                formattedDate = toSdf.format(toSdf.parse(formattedDate).time + fromSdf.timeZone.dstSavings)
//            }
//
//            if (toSdf.timeZone.useDaylightTime()) {
//                formattedDate = toSdf.format(toSdf.parse(formattedDate).time - fromSdf.timeZone.dstSavings)
//            }
    } catch (e: ParseException) {
        e.printStackTrace()
        formattedDate = "0"
    } finally {
        return formattedDate
    }
}

@SuppressLint("SimpleDateFormat")
private fun format(dateStr: String, fromFormat: String, toFormat: String): String {
    var formattedDate = ""
    try {
        val fromSdf = SimpleDateFormat(fromFormat)
        val toSdf = SimpleDateFormat(toFormat, Locale.ENGLISH)
        val date: Date = fromSdf.parse(dateStr)
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        formattedDate = toSdf.format(calendar.time)
    } catch (e: ParseException) {
        e.printStackTrace()
        formattedDate = "0"
    } finally {
        return formattedDate
    }
}

@SuppressLint("SimpleDateFormat")
private fun format(date: Long, format: String): String {
    var formattedDate = ""
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        formattedDate = SimpleDateFormat(format).format(calendar.time)
    } catch (e: ParseException) {
        e.printStackTrace()
        formattedDate = "0"
    } finally {
        return formattedDate
    }
}

@SuppressLint("SimpleDateFormat")
private fun format(date: Long, timeZoneId: String, is24: Boolean): String {
    var formattedDate = ""
    try {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId))
        calendar.timeInMillis = date
        if (is24) {
            val sdf = SimpleDateFormat(TIME_FORMAT_24_hours)
            sdf.timeZone = TimeZone.getTimeZone(timeZoneId)
            formattedDate = sdf.format(calendar.time)
        } else {
            val sdf = SimpleDateFormat(TIME_FORMAT_AM_PM)
            sdf.timeZone = TimeZone.getTimeZone(timeZoneId)
            formattedDate = sdf.format(calendar.time)
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        formattedDate = "0"
    } finally {
        return formattedDate
    }
}

private fun formatDisplay(date: Long, format: String): String =
    try {
        val formatter = DateTimeFormat.forPattern(format)
        DateTime.now().withMillis(date).toString(formatter)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }

@Suppress("unused")
private fun parseDate(date: String, pattern: String) =
    DateTime.parse(date, dateTimeFormatter(pattern))

private fun dateTimeFormatter(pattern: String) =
    DateTimeFormatterBuilder().appendPattern(pattern).toFormatter()

fun getServerTimeFromServerDate(createdAt: String?): String =
    //2021-12-22T16:31:03.823Z
    createdAt?.split("T")?.get(1)?.split(".")?.get(0) ?: ""

fun formattedTimeZone(date: String?): String =
    format(
        date!!,
        SERVER_RECEIVE_DATE_FORMAT,
        SERVER_RECEIVE_DATE_FORMAT,
        "UTC",
        Calendar.getInstance().timeZone.id
    )

fun formatTimeStampFromServerToCalendarFormat(date: String?): String {
    val formattedDate = date?.split("T")?.get(0)
    return format(formattedDate!!, DATE_WITH_DASHES_1, CALENDAR_FORMAT)


}



