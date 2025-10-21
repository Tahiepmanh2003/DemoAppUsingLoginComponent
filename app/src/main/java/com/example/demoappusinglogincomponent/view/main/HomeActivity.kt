package com.example.demoappusinglogincomponent.view.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.demoappusinglogincomponent.databinding.ActivityHomeBinding
import com.example.demoappusinglogincomponent.fragments.HomeFragment
import com.library.homecomponent.component.HomeCallback
import com.library.homecomponent.component.HomeComponent
import com.library.homecomponent.component.HomeConfig

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeComponent: HomeComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gọi hàm để thiết lập toàn bộ component
        setupHomeComponent()

        // Sau khi setup, bạn có thể gọi API để lấy dữ liệu và cập nhật lên Drawer
        fetchUserData()
    }

    /**
     * Hàm chính để thiết lập và khởi tạo HomeComponent.
     */
    private fun setupHomeComponent() {
        // --- BƯỚC 1: TÙY BIẾN - Cung cấp danh sách các Fragment ---
        // Đây là bước bắt buộc vì component của bạn không có hành vi mặc định.
        val fragments = listOf(
            HomeFragment(),
        )

        // Tạo đối tượng cấu hình
        val homeConfig = HomeConfig(
            fragments = fragments as List<Fragment>,
            initialPage = 0, // Bắt đầu ở trang Home
            offscreenPageLimit = 2 // Giữ 2 trang trong bộ nhớ để chuyển đổi mượt hơn
        )

        // --- BƯỚC 2: KHỞI TẠO - Tạo instance của HomeComponent ---
        homeComponent = HomeComponent(this, homeConfig)

        // --- BƯỚC 3: KIỂM SOÁT - Đăng ký Callback để xử lý sự kiện ---
        homeComponent.setCallback(object : HomeCallback {
            // Xử lý sự kiện từ Navigation Drawer
            override fun onAccountInfoClicked() {
                // Chuyển đến trang AccountInfo (vị trí 5)
                homeComponent.navigateToPage(5)
                // Đặt lại tiêu đề cho trang mới
                homeComponent.setToolbarTitle("Thông tin tài khoản")
                // Hiển thị nút "Back" thay cho nút "Menu"
                homeComponent.showBackButton(true)
                // Đóng drawer sau khi nhấn
                homeComponent.closeDrawer()
            }

            override fun onLogoutClicked() {
                Log.d("HomeActivity", "User clicked Logout")
                Toast.makeText(this@HomeActivity, "Đang xử lý đăng xuất...", Toast.LENGTH_SHORT).show()
                // Ví dụ: Xóa dữ liệu user và chuyển về màn hình Login
                // finish()
            }

            // Xử lý sự kiện từ Toolbar
            override fun onToolbarBackClicked() {
                // Khi người dùng nhấn nút "Back" trên toolbar, quay về trang chủ
                homeComponent.navigateToPage(0)
                // Hiện lại nút "Menu"
                homeComponent.showBackButton(false)
            }

            override fun onToolbarCartClicked() {
                Log.d("HomeActivity", "User clicked Cart icon on Toolbar")
                // Chuyển đến trang giỏ hàng (vị trí 1)
                homeComponent.navigateToPage(1)
            }

            // Implement các callback còn lại (có thể để trống nếu không dùng)
            override fun onToolbarMenuClicked() {
                Log.d("HomeActivity", "Menu icon clicked, drawer is opening.")
            }

            override fun onToolbarNotificationClicked() {
                TODO("Not yet implemented")
            }

            override fun onLocationClicked() {}
            override fun onChangePasswordClicked() {}
            override fun onPrivacyPolicyClicked() {}
        })

        // --- BƯỚC 4: HIỂN THỊ - Gắn component vào container trong layout ---
        homeComponent.attachTo(binding.homeContainer)
    }

    /**
     * Hàm giả lập việc lấy dữ liệu người dùng và cập nhật lên Drawer.
     */
    private fun fetchUserData() {
        // Trong dự án thực tế, bạn sẽ gọi API hoặc lấy từ SharedPreferences ở đây
        val userName = "Demo User"
        val userPhone = "0909090909"
        val userEmail = "demo.user@email.com"

        // Gọi hàm public của component để cập nhật UI
        homeComponent.updateUserInfo(userName, userPhone, userEmail)
    }

    /**
     * Rất quan trọng: Dọn dẹp component khi Activity bị hủy để tránh rò rỉ bộ nhớ.
     */
    override fun onDestroy() {
        super.onDestroy()
        homeComponent.destroy()
    }
}