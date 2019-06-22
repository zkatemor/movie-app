package com.zkatemor.movies.util

import java.text.DateFormatSymbols

class Tools {
    companion object {
        fun convertDate(sDate: String?): String {
            var result = ""

            var sMonth = ""
            var sDay = ""

            var eMonth = ""
            var eDay = ""

            if (sDate != null) {
                sMonth += sDate!!.substring(5, 7)
                sDay += sDate!!.substring(8)
            }

            if (sDate != null) {
                result += sDay.toInt().toString()

                if (!sMonth.equals(eMonth))
                    result += " " + DateFormatSymbols().getMonths()[sMonth.toInt() - 1]
            }

            return result
        }
    }
}