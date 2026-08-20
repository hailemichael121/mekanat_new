package com.example.mekanat_new.data.util

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class EthDate(
    val year: Int,
    val month: Int, // 1 to 13
    val day: Int    // 1 to 30 (or 1 to 6 for Pagumen)
) {
    val monthName: String
        get() = EthiopianCalendar.monthNames.getOrElse(month - 1) { "Month $month" }

    val monthNameAmharic: String
        get() = EthiopianCalendar.monthNamesAmharic.getOrElse(month - 1) { "$month" }

    fun formatEnglish(): String = "$monthName $day, $year"
    fun formatAmharic(): String = "$monthNameAmharic $day ቀን $year"
    fun formatShort(): String = "$monthName $day"
}

object EthiopianCalendar {
    val monthNames = listOf(
        "Mäskäräm", "Ṭəqəmt", "Ḫədar", "Taḫśaś", "Ṭərr", "Yäkatit",
        "Mägabit", "Miyazya", "Gənbot", "Säne", "Ḥamle", "Nähase", "Ṗagumen"
    )

    val monthNamesAmharic = listOf(
        "መስከረም", "ጥቅምት", "ኅዳር", "ታኅሣሥ", "ጥር", "የካቲት",
        "መጋቢት", "ሚያዝያ", "ግንቦት", "ሰኔ", "ሐምሌ", "ነሐሴ", "ጳጉሜን"
    )

    // JDN of Ethiopian epoch: August 29, 8 CE (Julian) -> JDN 1724220.5
    // For Gregorian conversion calculations, we use fixed JDN arithmetic:
    private const val ETHIOPIC_EPOCH = 1723856

    private fun gregorianToJdn(year: Int, month: Int, day: Int): Int {
        val a = (14 - month) / 12
        val y = year + 4800 - a
        val m = month + 12 * a - 3
        return day + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045
    }

    private fun jdnToGregorian(jdn: Int): LocalDate {
        val a = jdn + 32044
        val b = (4 * a + 3) / 146097
        val c = a - (146097 * b) / 4
        val d = (4 * c + 3) / 1461
        val e = c - (1461 * d) / 4
        val m = (5 * e + 2) / 153
        val day = e - (153 * m + 2) / 5 + 1
        val month = m + 3 - 12 * (m / 10)
        val year = 100 * b + d - 4800 + m / 10
        return LocalDate.of(year, month, day)
    }

    fun fromGregorian(date: LocalDate): EthDate {
        val jdn = gregorianToJdn(date.year, date.monthValue, date.dayOfMonth)
        val r = (jdn - ETHIOPIC_EPOCH) % 1461
        val n = (r % 365) + 365 * (r / 1460)
        val ethYear = 4 * ((jdn - ETHIOPIC_EPOCH) / 1461) + (r / 365) - (r / 1460)
        val ethMonth = (n / 30) + 1
        val ethDay = (n % 30) + 1
        return EthDate(ethYear, ethMonth, ethDay)
    }

    fun toGregorian(eth: EthDate): LocalDate {
        val jdn = (ETHIOPIC_EPOCH + 365 * (eth.year - 1) + (eth.year / 4) + 30 * (eth.month - 1) + eth.day - 1)
        return jdnToGregorian(jdn)
    }

    fun todayEth(): EthDate = fromGregorian(LocalDate.now())
}
