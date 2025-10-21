package com.example.demoappusinglogincomponent.utils

import java.text.NumberFormat
import java.util.Locale

object StringFormatUtils {

    fun formatCurrency(value: Int): String {
        // Dùng NumberFormat theo locale "vi-VN"
        val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        return formatter.format(value) + " VNĐ"
    }

    fun formatSale(sale: Int): String {
        return "-$sale%"
    }
}
