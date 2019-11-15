package com.example.handlercancellation

import java.io.BufferedReader
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

//in Kotlin this is a Singleton implementation
object Network{
    fun initNetwork(url: String) : String{
        var mUrl = URL(url)
        val connection = mUrl.openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 15000
        connection.requestMethod = "GET"
        connection.doInput = true
        connection.connect()
        return parseJsonResponse(
            connection.inputStream)
    }

    fun parseJsonResponse(inputStream: InputStream): String{
        var builder= StringBuilder()
        val reader= BufferedReader(inputStream.reader())

        reader.use { reader ->
            var line = reader.readLine()
            while(line != null){
                builder.append("$line \n")
                line = reader.readLine()
            }
        }
        return builder.toString()
    }


}