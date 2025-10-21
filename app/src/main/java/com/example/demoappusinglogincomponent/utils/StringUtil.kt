package com.example.demoappusinglogincomponent.utils

object StringUtil {

    fun isEmpty(input: String?): Boolean {
        return input == null || input.isEmpty() || "" == input.trim { it <= ' ' }
    }
}