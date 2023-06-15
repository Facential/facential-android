package com.capestone.facential.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.capestone.facential.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()


        binding.btnRegisterSubmit.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                register(name, email, password)
                isLoading(true)
            } else if (name.isNullOrEmpty()) {
                Toast.makeText(this, "Please type your name", Toast.LENGTH_SHORT).show()
                isLoading(false)
            } else if (email.isNullOrEmpty()) {
                Toast.makeText(this, "Please type your email", Toast.LENGTH_SHORT).show()
                isLoading(false)
            } else {
                Toast.makeText(this, "Please type your password", Toast.LENGTH_SHORT).show()
                isLoading(false)
            }
        }

        binding.btnLoginNav.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    private fun isLoading(showLoading: Boolean) {
        if (showLoading) {
            binding.btnRegisterSubmit.visibility = View.INVISIBLE
            binding.pbLoading.visibility = View.VISIBLE
            binding.navLayout.visibility = View.INVISIBLE
        } else {
            binding.btnRegisterSubmit.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.INVISIBLE
            binding.navLayout.visibility = View.VISIBLE
        }
    }

    private fun register(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registrasi berhasil, lakukan tindakan selanjutnya atau tampilkan pesan sukses
                    val user: FirebaseUser? = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileUpdateTask ->
                            if (profileUpdateTask.isSuccessful) {
                                isLoading(false)
                                finish()
                            } else {
                                Toast.makeText(this, "Registrasi Gagal!", Toast.LENGTH_SHORT).show()
                                isLoading(false)
                            }
                        }
                } else {
                    Toast.makeText(this, "Registrasi Gagal!", Toast.LENGTH_SHORT).show()
                    isLoading(false)
                }
            }
    }
}