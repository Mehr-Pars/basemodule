package mehrpars.mobile.librarysample

import mehrpars.mobile.librarysample.calendar.CalendarTool
import mehrpars.mobile.librarysample.calendar.ShamsiCalendar
import java.util.*

object DateUtils {
    private const val dateFormat = "yyyy/MM/dd HH:mm:ss"

    fun getPersianDateFromTimeStamp(timeStamp: Long, outputFormat: String = dateFormat): String? {
        val timeMillis = timeStamp * 1000
        return getPersianDate(
            timeMillis,
            outputFormat
        )
    }

    fun getTimeStampFromPersianDate(
        stringDate: String,
        format: String = dateFormat
    ): Long {
        try {
            val simpleDate =
                SimpleDate.getSimpleDate(
                    stringDate,
                    format
                )
            val calendarTool = CalendarTool()
            calendarTool.setIranianDate(simpleDate.year, simpleDate.month, simpleDate.date)
            simpleDate.year = calendarTool.gregorianYear
            simpleDate.month = calendarTool.gregorianMonth
            simpleDate.date = calendarTool.gregorianDay

            return simpleDate.getDate().time / 1000
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    fun getPersianDate(timeMillis: Long, outputFormat: String = dateFormat): String? {
        val date = Date(timeMillis)
        return getPersianDate(
            date,
            outputFormat
        )
    }

    fun getPersianDate(
        stringDate: String, inputFormat: String = dateFormat, outputFormat: String = dateFormat
    ): String? {
        val simpleDate =
            SimpleDate.getSimpleDate(
                stringDate,
                inputFormat
            )
        return getPersianDate(
            simpleDate.getDate(),
            outputFormat
        )
    }

    fun getPersianDate(date: Date, outputFormat: String = dateFormat): String? {
        try {
            val simpleDate =
                SimpleDate.getSimpleDate(
                    date
                )
            val solarCalendar = ShamsiCalendar().SolarCalendar(date)

            simpleDate.year = solarCalendar.year
            simpleDate.month = solarCalendar.month
            simpleDate.date = solarCalendar.date
            return simpleDate.getStringDate(outputFormat)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getGregorianDate(
        stringDate: String,
        inputFormat: String = dateFormat,
        outputFormat: String = dateFormat
    ): String? {
        try {
            val simpleDate =
                SimpleDate.getSimpleDate(
                    stringDate,
                    inputFormat
                )
            val calendarTool = CalendarTool()
            calendarTool.setIranianDate(simpleDate.year, simpleDate.month, simpleDate.date)

            simpleDate.year = calendarTool.gregorianYear
            simpleDate.month = calendarTool.gregorianMonth
            simpleDate.date = calendarTool.gregorianDay
            return simpleDate.getStringDate(outputFormat)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getTimeString(millis: Int): String {
        val second = (millis / 1000 % 60).toLong()
        val minute = (millis / (1000 * 60) % 60).toLong()
        val hour = (millis / (1000 * 60 * 60) % 24).toLong()
        return if (hour > 0) String.format("%02d:%02d:%02d", hour, minute, second)
        else String.format("%02d:%02d", minute, second)
    }

    class SimpleDate(
        var year: Int = 0,
        var month: Int = 0,
        var date: Int = 0,
        var hours: Int = 0,
        var minutes: Int = 0,
        var seconds: Int = 0
    ) {
        fun getStringDate(format: String = dateFormat): String {
            return format.replace("yyyy", "$year")
                .replace("MM", "%02d".format(month))
                .replace("dd", "%02d".format(date))
                .replace("HH", "%02d".format(hours))
                .replace("mm", "%02d".format(minutes))
                .replace("ss", "%02d".format(seconds))
        }

        fun getDate(): Date {
            return Date().apply {
                year = this@SimpleDate.year - 1900
                month = this@SimpleDate.month - 1
                date = this@SimpleDate.date
                hours = this@SimpleDate.hours
                minutes = this@SimpleDate.minutes
                seconds = this@SimpleDate.seconds
            }
        }

        override fun toString(): String {
            return getStringDate()
        }

        private fun getTimeNumber(s: String): String {
            var number = ""
            var i = 0
            while (i in s.indices && s[i].isDigit()) number += s[i++]

            return number
        }

        fun parse(date: String, format: String) {
            if (date.isEmpty() || format.isEmpty()) return
            val f = format.first()
            val number = getTimeNumber(date)
            when (f) {
                'y' -> year = if (number.isEmpty()) 0 else number.toInt()
                'M' -> month = if (number.isEmpty()) 0 else number.toInt()
                'd' -> this.date = if (number.isEmpty()) 0 else number.toInt()
                'H' -> hours = if (number.isEmpty()) 0 else number.toInt()
                'm' -> minutes = if (number.isEmpty()) 0 else number.toInt()
                's' -> seconds = if (number.isEmpty()) 0 else number.toInt()
                else -> {
                    parse(date.removeRange(0, 1), format.removeRange(0, 1))
                    return
                }
            }

            parse(date.replace(number, ""), format.replace(f.toString(), ""))
        }

        companion object {

            fun getSimpleDate(date: String, format: String = dateFormat): SimpleDate {
                return SimpleDate().apply {
                    parse(date, format)
                }
            }

            fun getSimpleDate(date: Date): SimpleDate {
                return SimpleDate(
                    date.year,
                    date.month,
                    date.date,
                    date.hours,
                    date.minutes,
                    date.seconds
                )
            }
        }
    }
}