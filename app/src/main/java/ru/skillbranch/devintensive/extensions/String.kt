package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16): String {
    val line = this.trim()
    return if (line.length<=count) line else line.substring(0,count).trim()+"..."
}