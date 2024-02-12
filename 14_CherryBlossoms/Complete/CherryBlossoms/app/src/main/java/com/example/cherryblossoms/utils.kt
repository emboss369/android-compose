package com.example.cherryblossoms

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun convertToJST(iso8601Time: String): String {
  val inputTime = ZonedDateTime.parse(iso8601Time)
  val jstTime = inputTime.withZoneSameInstant(ZoneId.of("Asia/Tokyo"))
  return jstTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
}