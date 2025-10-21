package com.example.demoappusinglogincomponent.constant

object Constant {
    const val TYPE_PAYMENT_CASH = 1
    const val TYPE_PAYMENT_BANK = 2

    // Existing constants...
    const val GENERIC_ERROR = "GENERIC_ERROR"
    const val FIREBASE_URL = "https://your-firebase-project.firebaseio.com/"

    // Thêm hằng số tiền tệ & phương thức thanh toán
    const val CURRENCY = " VNĐ"
    const val PAYMENT_METHOD_CASH = "Thanh toán khi nhận hàng (COD)"
    const val PAYMENT_METHOD_BANK = "Chuyển khoản ngân hàng"

    // Thêm key để truyền object Food qua Intent
    const val KEY_INTENT_FOOD_OBJECT = "KEY_INTENT_FOOD_OBJECT"

    fun getPaymentMethodText(paymentType: Int): String {
        return when (paymentType) {
            TYPE_PAYMENT_CASH -> PAYMENT_METHOD_CASH
            TYPE_PAYMENT_BANK -> PAYMENT_METHOD_BANK
            else -> PAYMENT_METHOD_CASH
        }
    }
}
