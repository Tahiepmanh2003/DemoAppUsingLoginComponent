package com.example.demoappusinglogincomponent.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

object QRCodeUtils {

    /**
     * Generate QR code for bank transfer
     */
    fun generateQRCode(bankAccount: String, accountName: String, amount: Int, content: String): Bitmap? {
        return try {
            // Create QR content for Vietnamese banking standard
            val qrContent = createBankingQRContent(bankAccount, accountName, amount, content)

            val writer = QRCodeWriter()
            val bitMatrix: BitMatrix = writer.encode(qrContent, BarcodeFormat.QR_CODE, 300, 300)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Create banking QR content following Vietnamese banking standards
     */
    private fun createBankingQRContent(
        bankAccount: String,
        accountName: String,
        amount: Int,
        content: String
    ): String {
        // Simple format for demonstration
        // In real implementation, use VietQR format
        return buildString {
            append("Bank: MB Bank\n")
            append("Account: $bankAccount\n")
            append("Name: $accountName\n")
            append("Amount: $amount VND\n")
            append("Content: $content")
        }
    }

    /**
     * Generate simple QR code with custom text
     */
    fun generateSimpleQRCode(text: String, size: Int = 300): Bitmap? {
        return try {
            val writer = QRCodeWriter()
            val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}