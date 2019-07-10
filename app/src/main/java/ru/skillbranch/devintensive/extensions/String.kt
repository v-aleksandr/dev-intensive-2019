package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16): String {
    val line = this.trim()
    return if (line.length<=count) line else line.substring(0,count).trim()+"..."
}
fun String.stripHtml(): String {
//    s/<(.*?)>//g
    var result =this.replace("&nbsp;"," ")
    val noTags = """<[^>]*>""".toRegex()
    val noSpaces = """\s+""".toRegex()
    val noEscapes = """&amp;|&gt;|&lt;""".toRegex()
    result= noTags.replace(result," ")
    result= noEscapes.replace(result,"")
    return noSpaces.replace(result," ").trim()
}
