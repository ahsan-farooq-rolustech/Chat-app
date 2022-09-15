package com.example.chatapplication.utilities.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtil {

    fun dateFromUTC(date: Date): Date {
        return Date(date.time + Calendar.getInstance().timeZone.getOffset(date.time))
    }

    fun dateToUTC(date: Date): Date {
        return Date(date.time - Calendar.getInstance().timeZone.getOffset(date.time))
    }

    fun getDateInUtc(OurDate: String): String {
        var OurDate = OurDate
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(OurDate)
            OurDate = formatter.format(dateToUTC(value))
        } catch (e: Exception) {
            OurDate = "00-00-0000 00:00"
        }

        return OurDate
    }

    fun SendDateToDate(OurDate: String): String {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm a")
            val value = formatter.parse(OurDate)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") //this format changeable
            val formattedDate = dateFormatter.format(value)
            formattedDate
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") // val format = SimpleDateFormat("HH:mm:ss")
        return format.format(date)
    }

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        var mDate = getLocalDate(date)
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var startDate = df.parse(mDate)
        val strCurrentDate = df.format(Calendar.getInstance().time)
        var currentDate = df.parse(strCurrentDate)
        val diff = currentDate.time!! - startDate.time!!
        return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun convertDate(startDate: String, endDate: String): String {
        var finalDate: String? = null
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val currentDate = df.format(Calendar.getInstance().time)
        val splitedStartDate = startDate.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val splitedEndDate = endDate.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val subSplitedndDate = splitedEndDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val time1 = convertIn12HourFormat(startDate)
        val time2 = convertIn12HourFormat(endDate)
        if (splitedStartDate[0].equals(splitedEndDate[0], ignoreCase = true)) {
            finalDate = when {
                isToday(startDate) -> "Today $time1 to $time2"
                isYesterday(startDate) -> "Yesterday $time1 to $time2"
                isTomorrow(startDate) -> "Tomorrow $time1 to $time2"
                getDaysDifference(startDate, currentDate) in 0..5 -> getDayName(startDate) + ", " + time1 + " to " + time2
                else -> getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0] + " - " + time1 + " Between " + time2
            }
        } else {
            when {
                isToday(startDate) -> {
                    finalDate = "Today $time1"
                    finalDate = when {
                        isTomorrow(endDate) -> "$finalDate - Tomorrow $time2"
                        getDaysDifference(endDate, currentDate) in 0..5 -> finalDate + " - " + getDayName(endDate) + " " + time2
                        else -> finalDate + " - " + getMonthNameByValue(Integer.parseInt(subSplitedndDate[1])) + " " + subSplitedndDate[2] + ", " + subSplitedndDate[0] + "  " + time1
                    }
                }
                isTomorrow(startDate) -> {
                    finalDate = "Tomorrow $time1"
                    finalDate = when {
                        isTomorrow(endDate) -> "$finalDate - Tomorrow $time2"
                        getDaysDifference(endDate, currentDate) in 0..5 -> finalDate + " - " + getDayName(endDate) + " " + time2
                        else -> finalDate + " - " + getMonthNameByValue(Integer.parseInt(subSplitedndDate[1])) + " " + subSplitedndDate[2] + ", " + subSplitedndDate[0] + "  " + time1
                    }
                }
                isYesterday(startDate) -> {
                    finalDate = "Yesterday $time1"
                    finalDate = when {
                        isToday(endDate) -> "$finalDate - Today $time2"
                        isTomorrow(endDate) -> "$finalDate - Tomorrow $time2"
                        getDaysDifference(endDate, currentDate) in 0..5 -> finalDate + " - " + getDayName(endDate) + " " + time2
                        else -> finalDate + " - " + getMonthNameByValue(Integer.parseInt(subSplitedndDate[1])) + " " + subSplitedndDate[2] + ", " + subSplitedndDate[0] + "  " + time1
                    }
                }
                getDaysDifference(startDate, endDate) in 0..5 -> finalDate = getDayName(startDate) + " " + time1 + " - " + getDayName(endDate) + " " + time2
                else -> finalDate = getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0] + "  " + time1 + " - " + getMonthNameByValue(Integer.parseInt(subSplitedndDate[1])) + " " + subSplitedndDate[2] + ", " + subSplitedndDate[0] + " " + time2
            }
        }
        return finalDate
    }

    fun convertSingleDateAndTime(startDate: String?): String {
        return if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = df.format(Calendar.getInstance().time)
            getMinutesDifference(getLocalDate(startDate), currentDate)
            val splitedStartDate = getLocalDate(startDate).split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val time1 = convertIn12HourFormatWithMinute(getLocalDate(startDate))
            getDayName(startDate) + ", " + getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0] + " \u00B7 " + time1
        } else {
            " "
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun Date.getMinutesAgoLogicDate(): String {
        val messageDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(this)
        var finalDate: String? = null
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val currentDate = df.format(Calendar.getInstance().time)
        getMinutesDifference(messageDate, currentDate)
        val splitedStartDate = messageDate.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val time1 = convertIn12HourFormatWithMinute(messageDate)
        finalDate = when {
            isToday(messageDate) -> when {
                getMinutesDifference(messageDate, currentDate) < 1 -> "Just Now"
                getMinutesDifference(messageDate, currentDate) < 60 -> "${getMinutesDifference(messageDate, currentDate)}m ago"
                else -> "${getHoursDifference(messageDate, currentDate)}h ago"
            }
            isYesterday(messageDate) -> "Yesterday $time1"
            isTomorrow(messageDate) -> "Tomorrow $time1"
            getDaysDifference(messageDate, currentDate) in 0..7 -> getDaysDifference(messageDate, currentDate).toString() + "d ago"
            else -> getDayName(messageDate) + ", " + getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0] + " \u00B7 " + time1
        }
        return finalDate
    }

    fun convertSingleDate(startDate: String?): String {
        return if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = df.format(Calendar.getInstance().time)

            getMinutesDifference(getLocalDate(startDate), currentDate)
            val splitedStartDate = getLocalDate(startDate).split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val time1 = convertIn12HourFormatWithMinute(getLocalDate(startDate))
            getMonthFullNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0]
        } else {
            " "
        }
    }

    fun convertSingleDateWithoutYear(startDate: String?): String {
        return if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = df.format(Calendar.getInstance().time)

            getMinutesDifference(getLocalDate(startDate), currentDate)
            val splitedStartDate = getLocalDate(startDate).split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val time1 = convertIn12HourFormatWithMinute(getLocalDate(startDate))
            getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2]
        } else {
            " "
        }
    }


    fun convertSingleDateWithYear(startDate: String?): String {
        return if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = df.format(Calendar.getInstance().time)
            getMinutesDifference(getLocalDate(startDate), currentDate)
            val splitedStartDate = getLocalDate(startDate).split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val time1 = convertIn12HourFormatWithMinute(getLocalDate(startDate))
            getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0]
        } else {
            " "
        }
    }


    fun getOnlyDate(startDate: String?): String {

        return if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = df.format(Calendar.getInstance().time)
            getMinutesDifference(startDate, currentDate)
            val splitedStartDate = startDate.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val time1 = convertIn12HourFormatWithMinute(startDate)
            getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " - " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0]
        } else {
            " "
        }
    }

    fun getViewDate(date: String): String {
        return if (date != null) {
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val splitStartDate = date.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitStartDate = splitStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            getMonthNameByValue(Integer.parseInt(subSplitStartDate[1])) + " " + subSplitStartDate[2] + ", " + subSplitStartDate[0]
        } else {
            " "
        }
    }

    fun convertSingleTime(startDate: String?): String {
        return if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = df.format(Calendar.getInstance().time)
            getMinutesDifference(getLocalDate(startDate), currentDate)
            val time1 = convertIn12HourFormatWithMinute(getLocalDate(startDate))
            time1
        } else {
            " "
        }
    }

    private fun getMonthNameByValue(position: Int): String {
        return when (position - 1) {
            Calendar.JANUARY -> "Jan"
            Calendar.FEBRUARY -> "Feb"
            Calendar.MARCH -> "Mar"
            Calendar.APRIL -> "Apr"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "Jun"
            Calendar.JULY -> "Jul"
            Calendar.AUGUST -> "Aug"
            Calendar.SEPTEMBER -> "Sep"
            Calendar.OCTOBER -> "Oct"
            Calendar.NOVEMBER -> "Nov"
            Calendar.DECEMBER -> "Dec"
            else -> ""
        }
    }


    private fun getMonthFullNameByValue(position: Int): String {
        return when (position - 1) {
            Calendar.JANUARY -> "January"
            Calendar.FEBRUARY -> "February"
            Calendar.MARCH -> "March"
            Calendar.APRIL -> "April"
            Calendar.MAY -> "May"
            Calendar.JUNE -> "June"
            Calendar.JULY -> "July"
            Calendar.AUGUST -> "August"
            Calendar.SEPTEMBER -> "September"
            Calendar.OCTOBER -> "October"
            Calendar.NOVEMBER -> "November"
            Calendar.DECEMBER -> "December"
            else -> ""
        }
    }

    fun convertSingleDateDoubleLine(startDate: String?): String {
        if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = df.format(Calendar.getInstance().time)
            getMinutesDifference(getLocalDate(startDate), currentDate)
            val splitedStartDate = getLocalDate(startDate).split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val time1 = convertIn12HourFormatWithMinute(getLocalDate(startDate))
            finalDate = when {
                isToday(getLocalDate(startDate)) -> when {
                    getMinutesDifference(getLocalDate(startDate), currentDate).toInt() == 0 -> "Just Now"
                    getMinutesDifference(getLocalDate(startDate), currentDate) < 60 -> "${
                        getMinutesDifference(getLocalDate(startDate), currentDate)
                    }min ago"
                    else -> "${getHoursDifference(getLocalDate(startDate), currentDate)}h ago"
                }
                isYesterday(getLocalDate(startDate)) -> "Yesterday $time1"
                isTomorrow(getLocalDate(startDate)) -> "Tomorrow $time1"/* else if (getDaysDifference(startDate, currentDate) in 0..5) {
                    getDayName(startDate) + ", " + time1
                }*/
                else -> time1 + "\n" + getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0]
            }
            return finalDate
        } else {
            return " "
        }

    }

    fun convertMeetingDate(startDate: String?): String {
        if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val splitedStartDate = startDate.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val time1 = convertIn12HourFormatWithMinute(startDate)
            finalDate = getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " - " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0] + " @ " + time1
            return finalDate
        } else {
            return " "
        }

    }

    fun checkIfEqualAfterDate(startDate: String, endDate: String): Boolean {
        val sDate = startDate.split("T")[0]
        val eDate = endDate.split("T")[0]

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val dt1: Date = sdf.parse(sDate)
        val dt2: Date = sdf.parse(eDate)

        return dt1.after(dt2) || dt1 == dt2
    }

    fun checkIfBeforeDate(startDate: String, endDate: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dt1: Date = sdf.parse(startDate)
        val dt2: Date = sdf.parse(endDate)
        return dt1.before(dt2)
    }

    fun checkIfAfterDate(startDate: String, endDate: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val dt1: Date = sdf.parse(startDate)
        val dt2: Date = sdf.parse(endDate)
        return dt1.after(dt2)
    }

    fun checkIfTimeEqualOrAfter(startTime: String, endTime: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm")
        val dt1: Date = sdf.parse(startTime)
        val dt2: Date = sdf.parse(endTime)
        return dt1.after(dt2) || dt1 == dt2
    }

    fun checkIfTimeEqualOrBefore(startTime: String, endTime: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm")
        val dt1: Date = sdf.parse(startTime)
        val dt2: Date = sdf.parse(endTime)
        return dt1.before(dt2) || dt1 == dt2
    }

    fun getTimeOnly(date: String): String {
        var formattedTime = date
        val splitedStartDate = formattedTime.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val time = splitedStartDate[1]

        val sdf = SimpleDateFormat("HH:mm:ss")
        val sdfs = SimpleDateFormat("HH:mm")
        val dt: Date
        try {
            dt = sdf.parse(time)
            formattedTime = sdfs.format(dt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedTime
    }

    fun checkIfTimeBefore(startTime: String, endTime: String): Boolean {
        val pattern = "HH:mm"
        val sdf = SimpleDateFormat(pattern)
        val date1: Date = sdf.parse(startTime)
        val date2: Date = sdf.parse(endTime)
        return date1.before(date2)
    }

    fun checkIfTenMinutesEarlier(startTime: String, endTime: String): Boolean {
        val pattern = "HH:mm"
        val sdf = SimpleDateFormat(pattern)
        val date1: Date = sdf.parse(startTime)
        val date2: Date = sdf.parse(endTime)
        val diff = date1.time - date2.time
        val minDiff = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
        return minDiff <= 1032
    }

    fun getTimeIn12HoursViewMeeting(startTime: String): String {
        var startTime = startTime
        val splitedStartDate = startTime.split("T")
        val date = splitedStartDate[0]
        val time = splitedStartDate[1]

        val sdf = SimpleDateFormat("HH:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date
        try {
            dt = sdf.parse(time)
            startTime = sdfs.format(dt)
        } catch (e: ParseException) { // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return startTime.replace("am", "AM").replace("pm", "PM")
    }

    fun convertIn12HourFormatWithMinute(startTime: String): String {
        var startTime = startTime
        val splitedStartDate = startTime.split("T")
        val date = splitedStartDate[0]
        val time = splitedStartDate[1]

        val sdf = SimpleDateFormat("hh:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date
        try {
            dt = sdf.parse(time)
            startTime = sdfs.format(dt)
        } catch (e: ParseException) { // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return startTime.replace("am", "AM").replace("pm", "PM")
    }

    private fun convertIn12HourFormat(startTime: String): String {
        var startTime = startTime
        val splitedStartDate = startTime.split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val date = splitedStartDate[0]
        val time = splitedStartDate[1]

        val sdf = SimpleDateFormat("HH:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date
        try {
            dt = sdf.parse(time)
            startTime = sdfs.format(dt)
        } catch (e: ParseException) { // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return startTime
    }

    fun getDateDaysDiff(startDate: String, currentDate: String, isAllDay: Boolean, durationInMin: Int): Long {
        if (!isAllDay) {
            val actualDif = getMinutesDifference(currentDate, startDate)
            val diff = stringToDate(startDate)?.time!! - stringToDate(currentDate)?.time!!
            var days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            val daysMin = days * 1440
            if ((daysMin + durationInMin) < actualDif) days += 1
            return days
        } else {
            val onlyDate1 = startDate.split("T")[0]
            val onlyDate2 = currentDate.split("T")[0]
            val date1 = "${onlyDate1}T00:00:00"
            val date2 = "${onlyDate2}T00:00:00"
            val diff = stringToDate(date1)?.time!! - stringToDate(date2)?.time!!
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        }
    }

    fun getDaysDifference(startDate: String, currentDate: String): Long {
        val diff = stringToDate(startDate)?.time!! - stringToDate(currentDate)?.time!!
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }


    fun getMinutesDifference(beforeDate: String, afterDate: String): Long {
        val diff = stringToDate(afterDate)?.time!! - stringToDate(beforeDate)?.time!!
        return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun getHoursDifference(startDate: String, currentDate: String): Long {
        val diff = stringToDate(currentDate)?.time!! - stringToDate(startDate)?.time!!
        return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
    }

    private fun isToday(date: String): Boolean {
        val d = stringToDate(date)
        return android.text.format.DateUtils.isToday(d!!.time)
    }

    private fun isYesterday(date: String): Boolean {
        val d = stringToDate(date)
        return android.text.format.DateUtils.isToday(d!!.time + android.text.format.DateUtils.DAY_IN_MILLIS)
    }

    private fun isTomorrow(date: String): Boolean {
        val d = stringToDate(date)
        return android.text.format.DateUtils.isToday(d!!.time - android.text.format.DateUtils.DAY_IN_MILLIS)
    }

    fun stringToDate(dtString: String): Date? {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var date: Date? = null
        try {
            date = format.parse(dtString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }

    private fun getDayName(date: String): String? {
        var name: String? = null
        val outFormat = SimpleDateFormat("EEE")
        name = outFormat.format(stringToDate(date))
        return name
    }

    fun localToGMT(): String {
        val date = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    fun getDateForPastFromDays(days: Int): String {
        var calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)
        var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return dateFormat.format(dateToUTC(calendar.time))
    }

    fun getDateForPastFromDays(days: Int, ourDate: String): String {
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val value = formatter.parse(ourDate)
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = value
        calendar.add(Calendar.DAY_OF_YEAR, days)
        var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        return dateFormat.format(calendar.time)
    }

    fun getCurrentDate(): String? {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val currentDate = df.format(Calendar.getInstance().time)
        return currentDate
    }

    fun getDateWithoutTimeOfParticularDay(day: Int): String? {
        val df = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, day) // 14 days date
        return df.format(cal.time)
    }

    fun getLocalDate(OurDate: String): String {
        return try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val date = simpleDateFormat.parse(OurDate)
            simpleDateFormat.format(dateFromUTC(date))
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun getDateDaysHoursDifference(date: String): String {
        var days = getDaysDifferenceComming(getLocalDate(date), getCurrentDate()!!)
        var hours = getHoursDifferenceComming(getLocalDate(date), getCurrentDate()!!) - (days * 24)
        var min = getMinutesDifferenceComming(getLocalDate(date), getCurrentDate()!!) - (((days * 24) + hours) * 60)

        if (days > 0) {
            return "$days Days, $hours Hours"
        } else if (hours > 0) {
            return "$hours Hours, $min Minutes"
        } else {
            return "$min Minutes"
        }

    }

    fun getDaysDifferenceComming(startDate: String, currentDate: String): Long {
        val diff = stringToDate(startDate)?.time!! - stringToDate(currentDate)?.time!!
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }


    fun getMinutesDifferenceComming(startDate: String, currentDate: String): Long {
        val diff = stringToDate(startDate)?.time!! - stringToDate(currentDate)?.time!!
        return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun getHoursDifferenceComming(startDate: String, currentDate: String): Long {
        val diff = stringToDate(startDate)?.time!! - stringToDate(currentDate)?.time!!
        return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
    }


    fun convertSeconds(seconds: Int): String {
        return String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60)
    }

    fun getDateofBirthWithoutT(dob: String): String {
        val split = dob.split("T")
        return split[0]
    }

    private fun getWeeksDates(weekNumber: Int, year: String): ArrayList<String> {
        val daysList = ArrayList<String>()
        val c = GregorianCalendar(Locale.getDefault())
        c.set(Calendar.WEEK_OF_YEAR, weekNumber)
        c.set(Calendar.YEAR, year.toInt())
        val firstDayOfWeek = c.firstDayOfWeek
        for (i in firstDayOfWeek until firstDayOfWeek + 7) {
            c.set(Calendar.DAY_OF_WEEK, i)
            daysList.add(SimpleDateFormat("yyyy-MM-dd").format(c.time))
        }
        return daysList
    }

    fun getChartFilterStartDate(ourDate: String, year: String): String? {
        val c = Calendar.getInstance()
        val ourDate = ourDate + " $year"
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val value = formatter.parse(ourDate)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        return dateFormatter.format(value)
    }

    fun getChartFilterEndDate(ourDate: String, year: String): String? {
        val c = Calendar.getInstance()
        val ourDate = ourDate + " $year"
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val value = formatter.parse(ourDate)
        c.time = value
        c.add(Calendar.DATE, 6)
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        return dateFormatter.format(c.time)
    }

    fun getChartFilterDateData(ourDate: String, year: String): ArrayList<String> {
        var list = ArrayList<String>()
        var ourDate = ourDate + " $year"
        if (getDaysDifference(getDateForPastFromDays(7, ourDate), SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Calendar.getInstance().time)) > 0) {
            ourDate = SimpleDateFormat("dd MMM yyyy").format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(getDateForPastFromDays(-getDaysDifference(getDateForPastFromDays(7, ourDate), SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Calendar.getInstance().time)).toInt(), ourDate)))
        }

        val formatter = SimpleDateFormat("dd MMM yyyy")
        val value = formatter.parse(ourDate)
        for (i in 0 until 7) {
            val c = Calendar.getInstance()
            c.time = value
            c.add(Calendar.DATE, i)
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
            list.add(dateFormatter.format(c.time))
        }
        return list
    }

    fun getWeeksBetween(a: Date, b: Date): Int {
        var a = a
        var b = b
        if (b.before(a)) {
            return -getWeeksBetween(b, a)
        }
        a = resetTime(a)
        b = resetTime(b)

        val cal = GregorianCalendar()
        cal.time = a
        var weeks = 0
        while (cal.time.before(b)) {

            cal.add(Calendar.WEEK_OF_YEAR, 1)
            weeks++
        }
        return weeks
    }

    private fun resetTime(d: Date): Date {
        val cal = GregorianCalendar()
        cal.time = d
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    fun convertSingleDateChat(startDate: String?): String {
        if (startDate != null) {
            var finalDate: String? = null
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = df.format(Calendar.getInstance().time)
            getMinutesDifference(getLocalDate(startDate), currentDate)
            val splitedStartDate = getLocalDate(startDate).split("T".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val subSplitedStartDate = splitedStartDate[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val time1 = convertIn12HourFormatWithMinute(getLocalDate(startDate))
            finalDate = getMonthNameByValue(Integer.parseInt(subSplitedStartDate[1])) + " " + subSplitedStartDate[2] + ", " + subSplitedStartDate[0]
            return finalDate
        } else {
            return " "
        }

    }

    fun checkBefore(date: String): Boolean {
        var temp2 = date
        if (!temp2.contains("T")) {
            temp2 = "${temp2}T00:00:00.000Z"
        }
        val strDate = stringToDate(temp2)
        return Date().after(strDate)
    }

    fun checkIsToday(date: String): Boolean {
        var myDate = date
        var currentDate = getCurrentDate()
        if (currentDate!!.contains("T")) currentDate = currentDate.split("T")[0]

        if (myDate.contains("T")) myDate = myDate.split("T")[0]

        return myDate == currentDate
    }

    fun setAppFormatDate(date: String): String {
        var temp = date
        if (temp.contains("T")) temp = temp.split("T")[0]

        val sp2 = temp.split("-")
        val year = sp2[0]
        val month = (sp2[1]).toInt()
        val day = sp2[2]
        if (month < 10) return "0$month-$day-$year"
        else return "$month-$day-$year"
    }

    @SuppressLint("SimpleDateFormat")
    fun addDurationAndReturnDate(durationInMin: Int, localDate: String): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        calendar.timeInMillis = stringToDate(localDate)!!.time
        calendar.add(Calendar.MINUTE, durationInMin)
        return format.format(calendar.time)
    }

    fun formatDate(dtString: String, useFormat: Boolean): String {
        var dtString = dtString
        var format = if (useFormat) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        else SimpleDateFormat("yyyy-MM-dd hh:mm a")
        val targetFormat = SimpleDateFormat("MMM dd, yyyy • hh:mm a")

        var date: Date? = null
        try {
            date = format.parse(dtString)
            dtString = targetFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        return dtString
    }

    fun formatMeetingListDate(dtString: String, useFormat: Boolean): String {
        var dtString = dtString
        var format = if (useFormat) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        else SimpleDateFormat("yyyy-MM-dd hh:mm a")
        val targetFormat = SimpleDateFormat("MMM dd, yyyy @ h:mm a")

        var date: Date? = null
        try {
            date = format.parse(dtString)
            dtString = targetFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        return dtString.replace("am", "AM").replace("pm", "PM")
    }

    fun formatEventDate(date: String): String {
        val mainDate = date.split(" ")[0]
        val time = "${date.split(" ")[1]} ${date.split(" ")[2]}"
        var conTime = ""
        val sdf = SimpleDateFormat("HH:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm a")
        val dt: Date
        try {
            dt = sdfs.parse(time)
            conTime = sdf.format(dt) //            println("Time Display: " + sdfs.format(dt)) // <-- I got result here
            Log.e("Time", conTime)

            return "${mainDate}T${conTime}"
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun formatLocally(finalDob: String?): String {
        val sp2 = finalDob!!.split("-")
        val year = sp2[0]
        val month = (sp2[1]).toInt()
        val day = sp2[2]
        if (month < 10) return "0$month-$day-$year"
        else return "$month-$day-$year"
    }

    fun splitAndGetDate(date: String): String {
        return formatDate(date, true).split("•")[0]
    }
}