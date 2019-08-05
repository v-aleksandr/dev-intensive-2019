package ru.skillbranch.devintensive.utils

import java.lang.StringBuilder

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = when (fullName) {
            " ", "" -> null
            else -> fullName
        }?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
//        return Pair(firstName,lastName) //the same to next
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        val chars: CharArray = payload.toCharArray()
        var transChars = ""
        for (i in chars.indices) {
            transChars += transliterated(chars[i], divider)
        }
        return transChars
    }

    fun transliterated(ch: Char, divider: String): String {
        val isUpperCase = ch.isUpperCase()
        var ret: String? = when (ch.toLowerCase()) {
            in 'a' .. 'z' -> ch.toString()
            in '0' .. '9' -> ch.toString()
           'а' -> "a"
           'б' -> "b"
           'в' -> "v"
           'г' -> "g"
           'д' -> "d"
           'е' -> "e"
           'ё' -> "e"
           'ж' -> "zh"
           'з' -> "z"
           'и', 'й', 'і' -> "i"
           'ї' -> "yi"
           'к' -> "k"
           'л' -> "l"
           'м' -> "m"
           'н' -> "n"
           'о' -> "o"
           'п' -> "p"
           'р' -> "r"
           'с' -> "s"
           'т' -> "t"
           'у' -> "u"
           'ф' -> "f"
           'х' -> "h"
           'ц' -> "c"
           'ч' -> "ch"
           'ш' -> "sh"
           'щ' -> "sh'"
           'ъ','ь' -> ""
           'ы' -> "y"
           'э' -> "e"
           'є' -> "ye"
           'ю' -> "yu"
           'я' -> "ya"
            else -> null
        }
        if (isUpperCase && ret != null) {
            ret=ret[0].toUpperCase()+ret.substring(1)
        }
        return when(ret) {
            null -> divider
            else -> ret
        }
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        return when (val initials: String = getInitial(firstName) + getInitial(lastName)) {
            "" -> null
            else -> initials
        }
    }

    fun getInitial(name: String?): String {
        return when (name) {
            " ", "", null -> ""
            else -> name.substring(0, 1).toUpperCase()
        }
    }
}