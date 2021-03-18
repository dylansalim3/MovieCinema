package com.dylansalim.moviecinema.utils

import java.text.ParseException
import java.text.SimpleDateFormat

class DateTimeUtil() {

    fun getAbbreviatedFromDateTime(
        dateTime: String,
        dateFormat: String,
        field: String
    ): String {
        val input = SimpleDateFormat(dateFormat)
        val output = SimpleDateFormat(field)
        try {
            val getAbbreviate = input.parse(dateTime)    // parse input
            return output.format(getAbbreviate)    // format output
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }
}
