package com.anaserastudio.bancheesemobile.tool

import android.annotation.SuppressLint
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StringControl {

    val DATE_FORMAT_FIREBASE = "E MMM dd HH:mm:ss yyyy"
    val DATE_FORMAT = "dd/MM/yyyy HH:mm"
    val DATE_FORMAT_SQL = "yyyy-MM-dd HH:mm"

    var bulan = arrayOf(
        "Januari",
        "Februari",
        "Maret",
        "April",
        "Mei",
        "Juni",
        "Juli",
        "Agustus",
        "September",
        "Oktober",
        "November",
        "Desember"
    )
        internal set

    val currDate: Date
        get() {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")


            return Date()

        }

    fun getNumberFormat(number: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale.CANADA)
        format.maximumFractionDigits = 0
        format.minimumFractionDigits = 0
        format.roundingMode = RoundingMode.UP
        var currency = format.format(number.toLong())
        currency = currency.replace("$", "Rp. ")
        return currency
    }
    fun getNumberFormatNoCurr(number: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale.CANADA)
        format.maximumFractionDigits = 0
        format.minimumFractionDigits = 0
        format.roundingMode = RoundingMode.UP
        var currency = format.format(number.toLong())
        currency = currency.replace("$", "")
        return currency
    }

    fun getNamaBulan(number_bulan: Int): String {
        return bulan[number_bulan - 1]
    }

    fun getTanggal(tanggal: String, jenis: String): String {
        var result = ""
        var date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        try {
            if (!tanggal.equals("now", ignoreCase = true)) {
                val parsedTimeStamp = dateFormat.parse(tanggal)
                val timestamp = java.sql.Timestamp(parsedTimeStamp.time)
                date = java.util.Date(timestamp.time)

            }

            val cal = Calendar.getInstance()
            cal.time = date
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val year = cal.get(Calendar.YEAR)
            var minute: String
            var hour: String
            var second: String
            minute = "" + cal.get(Calendar.MINUTE)
            hour = "" + cal.get(Calendar.HOUR_OF_DAY)
            second = "" + cal.get(Calendar.SECOND)

            if (cal.get(Calendar.MINUTE) < 10) {
                minute = "0$minute"
            }
            if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
                hour = "0$hour"
            }
            if (cal.get(Calendar.SECOND) < 10) {
                second = "0$second"
            }

            if (jenis.equals("tanggal_full", ignoreCase = true)) {
                result = day.toString() + " " + getNamaBulan(month) + " " + year
            } else if (jenis.equals("tanggal", ignoreCase = true)) {
                result = "$year-$month-$day"
            } else if (jenis.equals("waktu", ignoreCase = true)) {
                result = "$hour:$minute"
            } else if (jenis.equals("timestamp", ignoreCase = true)) {
                val str = "$year/$month/$day"
                val df = SimpleDateFormat("yyyy/MM/dd")
                val date2 = df.parse(str)
                val epoch = date2.time / 1000L

                result = epoch.toString() + ""
            }


        } catch (e: Exception) {

        }

        return result
    }


    fun getDate(tanggal: Date): String {

        val cal = Calendar.getInstance()
        cal.time = tanggal
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        var strDay = day.toString() + ""
        var strHour = hour.toString() + ""
        var strMinute = minute.toString() + ""

        if (day < 10)
            strDay = "0$day"

        if (hour < 10)
            strHour = "0$hour"
        if (minute < 10)
            strMinute = "0$minute"

        return strDay + " " + bulan[month] + " " + year + " " + strHour + ":" + strMinute

    }
    fun getDateNow(): String {

        val tgl = Date()
        tgl.time
        return tgl.time.toString()

    }

    @SuppressLint("SimpleDateFormat")
    fun dateNow() : String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        val date = Date()
        return dateFormat.format(date)
    }

    fun roundedNumber(number : String) : Int{
        return Math.round(number.toDouble()).toInt()
    }

}
