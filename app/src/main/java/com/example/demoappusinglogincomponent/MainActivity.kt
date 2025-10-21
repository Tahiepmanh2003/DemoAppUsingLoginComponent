package com.example.demoappusinglogincomponent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.demoappusinglogincomponent.view.main.HomeActivity
import com.library.logincomponent.LoginCallback
import com.library.logincomponent.LoginComponent
import com.library.logincomponent.LoginConfig
import com.library.logincomponent.databinding.ActivityMainBinding
import com.library.logincomponent.model.User

/**
 * Demo Activity sử dụng LoginComponent đã đóng gói
 *
 * CHỈ CẦN:
 * 1. Khởi tạo LoginComponent với config
 * 2. Attach vào view
 * 3. Set callback để xử lý events
 *
 * KHÔNG CẦN viết lại code login từ đầu!
 */
class MainActivity : AppCompatActivity() {

    private lateinit var loginComponent: LoginComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // CÁCH 1: Sử dụng config mặc định
        setupLoginComponentDefault()

        // CÁCH 2: Sử dụng config tùy chỉnh (uncomment để dùng)
        // setupLoginComponentCustom()
    }

    /**
     * CÁCH 1: Sử dụng LoginComponent với config mặc định
     */
    private fun setupLoginComponentDefault() {
        // Bước 1: Khởi tạo component với config mặc định
        loginComponent = LoginComponent(
            context = this,
            config = LoginConfig.createDefault()
        )

        // Bước 2: Attach component vào container
        val container = findViewById<FrameLayout>(R.id.login_container)
        loginComponent.attachToView(container)

        // Bước 3: Set callback để xử lý các sự kiện
        loginComponent.setCallback(object : LoginCallback {
            override fun onLoginSuccess(user: User) {
                // Xử lý khi đăng nhập thành công
                Toast.makeText(
                    this@MainActivity,
                    "Chào mừng ${user.fullName ?: user.phone}!",
                    Toast.LENGTH_LONG
                ).show()

                // Lưu user vào SharedPreferences
                saveUserToPreferences(user)

                // Navigate đến màn hình chính
                navigateToHome(user)
            }

            override fun onLoginFailure(errorMessage: String) {
                // Xử lý khi đăng nhập thất bại
                // Component đã tự show toast, có thể log thêm
                android.util.Log.e("LoginDemo", "Login failed: $errorMessage")
            }

            override fun onNavigateToRegister() {
                // Navigate đến màn hình đăng ký
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            override fun onForgotPassword() {
                // Xử lý quên mật khẩu
                Toast.makeText(
                    this@MainActivity,
                    "Tính năng đang phát triển",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCallHotline(phoneNumber: String) {
                // Mở dialer để gọi hotline
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(intent)
            }
        })
    }

    /**
     * CÁCH 2: Sử dụng LoginComponent với config tùy chỉnh
     */
    private fun setupLoginComponentCustom() {
        // Custom config để phù hợp với yêu cầu dự án
        val customConfig = LoginConfig(
            allowRegistration = true,           // Cho phép đăng ký
            showForgotPassword = true,          // Hiển thị quên mật khẩu
            minPasswordLength = 8,              // Mật khẩu tối thiểu 8 ký tự
            phoneLength = 10,                   // Số điện thoại 10 số
            maxLoginAttempts = 3,               // Tối đa 3 lần nhập sai
            blockTimeMinutes = 5,               // Khóa 5 phút
            hotlineNumber = "1900xxxx",         // Số hotline tùy chỉnh
            firebasePath = "users",             // Path trên Firebase
            logoResId = R.drawable.ppend,     // Logo tùy chỉnh
            backgroundResId = R.drawable.applogin  // Background tùy chỉnh
        )

        loginComponent = LoginComponent(this, customConfig)

        val container = findViewById<FrameLayout>(R.id.login_container)
        loginComponent.attachToView(container)

        loginComponent.setCallback(object : LoginCallback {
            override fun onLoginSuccess(user: User) {
                saveUserToPreferences(user)
                navigateToHome(user)
            }

            override fun onLoginFailure(errorMessage: String) {
                android.util.Log.e("LoginDemo", errorMessage)
            }

            override fun onNavigateToRegister() {
                startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
            }

            override fun onForgotPassword() {
                Toast.makeText(this@MainActivity, "Forgot password", Toast.LENGTH_SHORT).show()
            }

            override fun onCallHotline(phoneNumber: String) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(intent)
            }
        })
    }

    /**
     * Lưu thông tin user vào SharedPreferences
     */
    private fun saveUserToPreferences(user: User) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit().apply {
            putString("user_id", user.id)
            putString("user_name", user.fullName)
            putString("user_phone", user.phone)
            putString("user_email", user.email)
            apply()
        }
    }

    /**
     * Navigate đến màn hình chính sau khi login thành công
     */
    private fun navigateToHome(user: User) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("user", user)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Dọn dẹp resources của component
        loginComponent.destroy()
    }
}