package com.zkatemor.movies.util

import java.text.DateFormatSymbols

class Tools {
    companion object {
        fun convertDate(sDate: String?): String {
            var result = ""

            var month = ""
            var day = ""
            var year = ""


            if (sDate != null) {
                month += sDate!!.substring(5, 7)
                day += sDate!!.substring(8)
                year += sDate!!.substring(0, 4)
            }

            if (sDate != null) {
                result += day.toInt().toString()
                result += " " + DateFormatSymbols().getMonths()[month.toInt() - 1]
                result += " " + year.toInt().toString()
            }

            return result
        }
    }
}