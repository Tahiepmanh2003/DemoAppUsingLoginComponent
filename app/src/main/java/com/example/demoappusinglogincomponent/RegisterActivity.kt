package com.example.demoappusinglogincomponent

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.demoappusinglogincomponent.databinding.ActivityRegisterBinding
import com.example.registercomponent.component.RegisterCallback
import com.example.registercomponent.component.RegisterComponent
import com.example.registercomponent.component.RegisterConfig
import com.example.registercomponent.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var registerComponent: RegisterComponent
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().reference
        registerComponent = RegisterComponent(
            context = this,
            config = RegisterConfig.createDefault()
        )
        //gọi componet đã đóng gói ra sử dụng
        registerComponent.attachTo(binding.main)
        registerComponent.setCallback(object : RegisterCallback {
            override fun onRegisterClicked(user: User) {
                val userId = database.child("users").push().key
                user.id = userId
                database.child("users").child(userId.toString()).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Đăng kí tài khoản thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Đăng ký thất bại: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onBackToLoginCLicked() {
                finish()
            }
        })
    }
}