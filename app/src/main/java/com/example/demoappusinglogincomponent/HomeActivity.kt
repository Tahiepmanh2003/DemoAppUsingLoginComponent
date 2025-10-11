package com.example.demoappusinglogincomponent

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.demoappusinglogincomponent.databinding.ActivityHomeBinding
import com.library.logincomponent.model.User
import com.example.demoappusinglogincomponent.R

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Lấy user từ Intent
        val user = intent.getSerializableExtra("user") as? User

        // Hiển thị thông tin user
        binding.apply {
            tvWelcome.text = "Chào mừng, ${user?.fullName ?: "Người dùng"}!"
            tvPhone.text = "SĐT: ${user?.phone}"
            tvEmail.text = "Email: ${user?.email ?: "Chưa có"}"

            btnLogout.setOnClickListener {
                logout()
            }
        }
    binding.idMenu.setOnClickListener { showMenu() }
    }

    private fun logout() {
        // Xóa SharedPreferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit().clear().apply()

        // Xóa login attempts của component
        val loginPrefs = getSharedPreferences("login_component_prefs", MODE_PRIVATE)
        loginPrefs.edit().clear().apply()

        // Quay về màn hình login
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun showMenu(){
        val popup = PopupMenu(this, binding.idMenu)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_item, popup.menu)

        popup.setOnMenuItemClickListener { item: MenuItem ->
            when(item.itemId){
                R.id.item_info->{
                    Toast.makeText(this, "Thong tin ca nhan", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item_info->{
                    Toast.makeText(this, "Cai dat", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item_info->{
                    Toast.makeText(this, "Dang xuat thanh cong!", Toast.LENGTH_SHORT).show()
                    logout()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}