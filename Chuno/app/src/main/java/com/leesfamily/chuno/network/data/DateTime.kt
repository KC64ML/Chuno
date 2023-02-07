package com.leesfamily.chuno.network.data

data class DateTime(
     var year: Int,
     var month: Int,
     var day: Int,
     var hour: Int,
     var minute: Int,

) {
     fun dateToString(): String {
          return "$year-$month-$day"
     }
}
