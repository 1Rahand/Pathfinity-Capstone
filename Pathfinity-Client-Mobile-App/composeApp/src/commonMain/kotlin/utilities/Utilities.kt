package utilities

import domain.Lang
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

inline fun <T1, T2, T3, T4, T5, T6, R> combine(
   flow: Flow<T1>,
   flow2: Flow<T2>,
   flow3: Flow<T3>,
   flow4: Flow<T4>,
   flow5: Flow<T5>,
   flow6: Flow<T6>,
   crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> {
   return combine(flow, flow2, flow3, flow4, flow5, flow6) { args: Array<*> ->
      @Suppress("UNCHECKED_CAST")
      transform(
         args[0] as T1,
         args[1] as T2,
         args[2] as T3,
         args[3] as T4,
         args[4] as T5,
         args[5] as T6,
      )
   }
}

fun Float.roundToOneDecimal(): Float {
   val scale = 10f
   // This simple version handles only non-negative values:
   return ((this * scale) + 0.5f).toInt() / scale
}


fun String.wordCount(): Int {
   return this.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
}

fun <T> List<T>.lastIndexOf(predicate: (T) -> Boolean): T? {
   for (i in this.indices.reversed()) {
      if (predicate(this[i])) {
         return this[i]
      }
   }
   return null
}


fun LocalDate.toMillis(): Long {
   return this.toEpochDays().days.inWholeMilliseconds
}

fun LocalDate.Companion.fromMillis(millis: Long): LocalDate {
   return LocalDate.fromEpochDays(millis.milliseconds.inWholeDays.toInt())
}

fun String.replaceDollarSignsWithLatex(): String {
   // Replace double dollar signs with square brackets while keeping the original
   val doubleDollarRegex = Regex("\\$\\$(.*?)\\$\\$")
   val withSquareBrackets = doubleDollarRegex.replace(this) { matchResult ->
      "\\[${matchResult.groupValues[1]}\\]"
   }

   // Replace single dollar signs with LaTeX delimiters
   val singleDollarRegex = Regex("\\$(.*?)\\$")
   val withLatexDelimiters = singleDollarRegex.replace(withSquareBrackets) { matchResult ->
      "\\(${matchResult.groupValues[1]}\\)"
   }

   return withLatexDelimiters
}

fun String.replaceLastChar(target: Char, replacement: Char): String {
   if (isEmpty()) return this
   return if (this[lastIndex] == target) {
      substring(0, lastIndex) + replacement
   } else {
      this
   }
}

fun String.toKurdishNumber(): String {
   return this.replace('0', '٠')
      .replace('1', '١')
      .replace('2', '٢')
      .replace('3', '٣')
      .replace('4', '٤')
      .replace('5', '٥')
      .replace('6', '٦')
      .replace('7', '٧')
      .replace('8', '٨')
      .replace('9', '٩')
}

fun Duration.formatPretty(lang: Lang): String {
   val totalSeconds = inWholeSeconds

   return when {
      // 1) Less than a minute (under 60 seconds)
      totalSeconds < 60 -> {
         when (lang) {
            Lang.Eng -> "$totalSeconds second${if (totalSeconds == 1L) "" else "s"}"
            Lang.Krd -> "$totalSeconds چركه"
         }
      }

      // 2) 1 minute to under 1 hour (60-3599 seconds)
      totalSeconds < 3600 -> {
         val minutes = totalSeconds / 60
         val leftoverSeconds = totalSeconds % 60

         when (lang) {
            Lang.Eng -> {
               val minLabel = if (minutes == 1L) "minute" else "minutes"
               val secLabel = if (leftoverSeconds == 1L) "second" else "seconds"
               "$minutes $minLabel $leftoverSeconds $secLabel"
            }

            Lang.Krd -> {
               // Adjust words as preferred
               "$minutes خولەک $leftoverSeconds چركه"
            }
         }
      }

      // 3) One hour or more -> numeric HH:mm:ss for both languages
      else -> {
         val hours = totalSeconds / 3600
         val leftoverMinutes = (totalSeconds % 3600) / 60
         val leftoverSeconds = totalSeconds % 60

         // Format HH:mm:ss the same for both languages
         "${hours.toTwoDigits()}:${leftoverMinutes.toTwoDigits()}:${leftoverSeconds.toTwoDigits()}"
      }
   }
}

private fun Long.toTwoDigits(): String = toString().padStart(2, '0')

/**
 * Extension function on kotlinx.datetime.Instant that formats the timestamp
 * similarly to Instagram's messaging style.
 *
 * Rules:
 * - If the timestamp is from today, show the time (e.g., "9:41 AM").
 * - If it's from 1–6 days ago, show the number of days (e.g., "1d", "2d", etc.).
 * - If it's 7–364 days ago, show the number of weeks (e.g., "1w", "2w", etc.).
 * - If it's a year or older, show the full date as "MMM d, yyyy" (e.g., "Dec 31, 2022").
 */
fun Instant.toInstagramTime(): String {
   // Use the system default time zone.
   val timeZone = TimeZone.currentSystemDefault()

   // Convert the current moment and the message's Instant to local date-time.
   val now = Clock.System.now().toLocalDateTime(timeZone)
   val messageDateTime = this.toLocalDateTime(timeZone)

   // Create LocalDate instances for easy date-only comparison.
   val currentDate = LocalDate(now.year, now.monthNumber, now.dayOfMonth)
   val messageDate = LocalDate(messageDateTime.year, messageDateTime.monthNumber, messageDateTime.dayOfMonth)

   // If the message was sent today, format and return the time (e.g., "9:41 AM").
   if (messageDate == currentDate) {
      val hour24 = messageDateTime.hour
      val minute = messageDateTime.minute
      val period = if (hour24 < 12) "AM" else "PM"
      val hour12 = when {
         hour24 == 0 -> 12
         hour24 > 12 -> hour24 - 12
         else -> hour24
      }
      return "$hour12:${minute.toString().padStart(2, '0')} $period"
   }

   // Compute the difference in days between the current date and the message date.
   val daysDiff = currentDate.toEpochDays() - messageDate.toEpochDays()

   // If the difference is less than 7 days, display the day difference (e.g., "1d", "2d", etc.).
   if (daysDiff < 7) {
      return "${daysDiff}d"
   }

   // For differences from 7 days up to (but not including) a year, display the number of weeks.
   // Use integer division (floor) so that 7 to 13 days become "1w", 14 to 20 days become "2w", etc.
   if (daysDiff < 365) {
      val weeks = daysDiff / 7
      return "${weeks}w"
   }

   // For messages a year or older, manually format the full date as "MMM d, yyyy".
   val monthAbbreviations = arrayOf(
      "Jan", "Feb", "Mar", "Apr", "May", "Jun",
      "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
   )
   val monthAbbrev = monthAbbreviations[messageDateTime.monthNumber - 1]
   return "$monthAbbrev ${messageDateTime.dayOfMonth}, ${messageDateTime.year}"
}
