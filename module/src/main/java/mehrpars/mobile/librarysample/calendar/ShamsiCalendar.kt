package mehrpars.mobile.librarysample.calendar

import java.util.*

class ShamsiCalendar {

    inner class SolarCalendar {

        var strWeekDay = ""
        var strMonth = ""

        var date: Int = 0
        var month: Int = 0
        var year: Int = 0

        constructor() {
            val miladiDate = Date()
            calcSolarCalendar(miladiDate)
        }

        constructor(miladiDate: Date) {
            calcSolarCalendar(miladiDate)
        }

        private fun calcSolarCalendar(MiladiDate: Date) {

            val ld: Int

            val miladiYear = MiladiDate.year + 1900
            val miladiMonth = MiladiDate.month + 1
            val miladiDate = MiladiDate.date
            val WeekDay = MiladiDate.day

            val buf1 = IntArray(12)
            val buf2 = IntArray(12)

            buf1[0] = 0
            buf1[1] = 31
            buf1[2] = 59
            buf1[3] = 90
            buf1[4] = 120
            buf1[5] = 151
            buf1[6] = 181
            buf1[7] = 212
            buf1[8] = 243
            buf1[9] = 273
            buf1[10] = 304
            buf1[11] = 334

            buf2[0] = 0
            buf2[1] = 31
            buf2[2] = 60
            buf2[3] = 91
            buf2[4] = 121
            buf2[5] = 152
            buf2[6] = 182
            buf2[7] = 213
            buf2[8] = 244
            buf2[9] = 274
            buf2[10] = 305
            buf2[11] = 335

            if (miladiYear % 4 != 0) {
                date = buf1[miladiMonth - 1] + miladiDate

                if (date > 79) {
                    date = date - 79
                    if (date <= 186) {
                        when (date % 31) {
                            0 -> {
                                month = date / 31
                                date = 31
                            }
                            else -> {
                                month = date / 31 + 1
                                date = date % 31
                            }
                        }
                        year = miladiYear - 621
                    } else {
                        date = date - 186

                        when (date % 30) {
                            0 -> {
                                month = date / 30 + 6
                                date = 30
                            }
                            else -> {
                                month = date / 30 + 7
                                date = date % 30
                            }
                        }
                        year = miladiYear - 621
                    }
                } else {
                    if (miladiYear > 1996 && miladiYear % 4 == 1) {
                        ld = 11
                    } else {
                        ld = 10
                    }
                    date = date + ld

                    when (date % 30) {
                        0 -> {
                            month = date / 30 + 9
                            date = 30
                        }
                        else -> {
                            month = date / 30 + 10
                            date = date % 30
                        }
                    }
                    year = miladiYear - 622
                }
            } else {
                date = buf2[miladiMonth - 1] + miladiDate

                if (miladiYear >= 1996) {
                    ld = 79
                } else {
                    ld = 80
                }
                if (date > ld) {
                    date = date - ld

                    if (date <= 186) {
                        when (date % 31) {
                            0 -> {
                                month = date / 31
                                date = 31
                            }
                            else -> {
                                month = date / 31 + 1
                                date = date % 31
                            }
                        }
                        year = miladiYear - 621
                    } else {
                        date = date - 186

                        when (date % 30) {
                            0 -> {
                                month = date / 30 + 6
                                date = 30
                            }
                            else -> {
                                month = date / 30 + 7
                                date = date % 30
                            }
                        }
                        year = miladiYear - 621
                    }
                } else {
                    date = date + 10

                    when (date % 30) {
                        0 -> {
                            month = date / 30 + 9
                            date = 30
                        }
                        else -> {
                            month = date / 30 + 10
                            date = date % 30
                        }
                    }
                    year = miladiYear - 622
                }

            }

            when (month) {
                1 -> strMonth = "فروردین"
                2 -> strMonth = "اردیبهشت"
                3 -> strMonth = "خرداد"
                4 -> strMonth = "تیر"
                5 -> strMonth = "مرداد"
                6 -> strMonth = "شهریور"
                7 -> strMonth = "مهر"
                8 -> strMonth = "آبان"
                9 -> strMonth = "آذر"
                10 -> strMonth = "دی"
                11 -> strMonth = "بهمن"
                12 -> strMonth = "اسفند"
            }

            when (WeekDay) {

                0 -> strWeekDay = "شنبه"
                1 -> strWeekDay = "یکشنبه"
                2 -> strWeekDay = "دوشنبه"
                3 -> strWeekDay = "سه شنبه"
                4 -> strWeekDay = "چهارشنبه"
                5 -> strWeekDay = "پنج شنبه"
                6 -> strWeekDay = "جمعه"
            }

        }

    }

    companion object {

        val currentShamsiDate: String
            get() {
                val loc = Locale("en_US")
                val util = ShamsiCalendar()
                val sc = util.SolarCalendar()
                return sc.year.toString() + "/" + String.format(
                    loc, "%02d",
                    sc.month
                ) + "/" + String.format(loc, "%02d", sc.date)
            }
    }
}