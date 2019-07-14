package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.MILLISECOND -> value * 1L
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = date.time - this.time
    fun getPeriodInWords(period: Long): String {
        var result: String
        if (period > 26 * HOUR) {
            val res = period / DAY
            result = "$res "
            if (res % 100 in 11..20 || (res - 1) % 10 > 3) {
                result += "дней"
            } else if (res % 10 == 1L) {
                result += "день"
            } else {
                result += "дня"
            }
        } else if (period > 75 * MINUTE) {
            val res = period / HOUR
            result = "$res "
            if (res % 100 in 11..20 || (res - 1) % 10 > 3) {
                result += "часов"
            } else if (res % 10 == 1L) {
                result += "час"
            } else {
                result += "часа"
            }
        } else {
            val res = period / MINUTE
            result = "$res "
            if (res % 100 in 11..20 || (res - 1) % 10 > 3) {
                result += "минут"
            } else if (res % 10 == 1L) {
                result += "минуту"
            } else {
                result += "минуты"
            }
        }
        return result
    }
    return when (diff) {
        in 360 * DAY..this.time -> "более года назад"
        in 26 * HOUR..360 * DAY -> "${getPeriodInWords(diff)} назад"
        in 22 * HOUR..26 * HOUR -> "день назад"
        in 75 * MINUTE..22 * HOUR -> "${getPeriodInWords(diff)} назад"
        in 45 * MINUTE..75 * MINUTE -> "час назад"
        in 75 * SECOND..45 * MINUTE -> "${getPeriodInWords(diff)} назад"
        in 45 * SECOND..75 * SECOND -> "минуту назад"
        in SECOND..45 * SECOND -> "несколько секунд назад"
        in -SECOND..SECOND -> "только что"
        in -45 * SECOND..-SECOND -> "через несколько секунд"
        in -75 * SECOND..-45 * SECOND -> "через минуту"
        in -45 * MINUTE..-75 * SECOND -> "через ${getPeriodInWords(-diff)}"
        in -75 * MINUTE..-45 * MINUTE -> "через час"
        in -22 * HOUR..-75 * MINUTE -> "через ${getPeriodInWords(-diff)}"
        in -26 * HOUR..-22 * HOUR -> "через день"
        in -360 * DAY..-26 * HOUR -> "через ${getPeriodInWords(-diff)}"
        else -> "более чем через год"
    }
}

enum class TimeUnits {
    MILLISECOND {
        override fun plural(value:Int): String {
            return "$value " + when {
            value % 100 in 11..20 || (value - 1) % 10 > 3 -> "миллисекунд"
            value % 10 == 1 -> "миллисекунду"
            else -> "миллисекунды"
            }
        }
    },
    SECOND{
        override fun plural(value:Int): String {
            return "$value " + when {
                value % 100 in 11..20 || (value - 1) % 10 > 3 -> "секунд"
                value % 10 == 1 -> "секунду"
                else -> "секунды"
            }
        }
    },
    MINUTE{
        override fun plural(value:Int): String {
            return "$value " + when {
                value % 100 in 11..20 || (value - 1) % 10 > 3 -> "минут"
                value % 10 == 1 -> "минуту"
                else -> "минуты"
            }
        }
    },
    HOUR{
        override fun plural(value:Int): String {
            return "$value " + when {
                value % 100 in 11..20 || (value - 1) % 10 > 3 -> "часов"
                value % 10 == 1 -> "час"
                else -> "часа"
            }
        }
    },
    DAY{
        override fun plural(value:Int): String {
            return "$value " + when {
                value % 100 in 11..20 || (value - 1) % 10 > 3 -> "дней"
                value % 10 == 1 -> "день"
                else -> "дня"
            }
        }
    };

    abstract fun plural(value:Int): String
}

