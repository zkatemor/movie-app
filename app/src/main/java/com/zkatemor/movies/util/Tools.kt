package com.zkatemor.movies.util

import java.text.DateFormatSymbols

/**
 * преобразование данных в нужный формат
 */
class Tools {
    companion object {
        fun convertDate(sDate: String?): String {
            var result = ""

            var month = ""
            var day = ""
            var year = ""


            if (sDate != "") {
                month += sDate!!.substring(5, 7)
                day += sDate!!.substring(8)
                year += sDate!!.substring(0, 4)
            }

            if (sDate != "") {
                result += day.toInt().toString()
                result += " " + DateFormatSymbols().getMonths()[month.toInt() - 1]
                result += " " + year.toInt().toString()
            }

            return result
        }

        fun buildImageURL(url: String?): String{
            return "https://image.tmdb.org/t/p/w500/" + url
        }
    }
}