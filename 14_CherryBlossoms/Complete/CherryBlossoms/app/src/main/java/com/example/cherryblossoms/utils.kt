package com.example.cherryblossoms

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


// Kotlinでは、java.timeパッケージのZonedDateTimeクラスを使用して
// ISO 8601形式の時刻表現を日本時間（JST）に変換することができます。以下にその手順を示します。
fun convertToJST(iso8601Time: String): String {
  val inputTime = ZonedDateTime.parse(iso8601Time)
  val jstTime = inputTime.withZoneSameInstant(ZoneId.of("Asia/Tokyo"))
  return jstTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
}