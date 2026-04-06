package com.example.taskapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private val VALID_EMAIL = "admin@taskapp.com"
    private val VALID_PASSWORD = "123456"
    
    private val PREFS_NAME = "LoginPrefs"
    private val KEY_EMAIL = "saved_email"
    private val KEY_REMEMBER = "remember_me"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail    = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val cbRemember = findViewById<CheckBox>(R.id.cbRememberEmail)
        val btnLogin   = findViewById<Button>(R.id.btnLogin)
        val tvError    = findViewById<TextView>(R.id.tvError)

        val sharedPrefs: SharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // 1. Carregar e-mail salvo, se existir
        val savedEmail = sharedPrefs.getString(KEY_EMAIL, "")
        val isRemembered = sharedPrefs.getBoolean(KEY_REMEMBER, false)
        
        if (isRemembered && !savedEmail.isNullOrEmpty()) {
            etEmail.setText(savedEmail)
            cbRemember.isChecked = true
            etPassword.requestFocus() // Foca na senha, já que o e-mail está preenchido
        }

        btnLogin.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email == VALID_EMAIL && password == VALID_PASSWORD) {
                // 2. Salvar ou limpar e-mail no SharedPreferences
                val editor = sharedPrefs.edit()
                if (cbRemember.isChecked) {
                    editor.putString(KEY_EMAIL, email)
                    editor.putBoolean(KEY_REMEMBER, true)
                } else {
                    editor.remove(KEY_EMAIL)
                    editor.putBoolean(KEY_REMEMBER, false)
                }
                editor.apply()

                // Login OK
                tvError.visibility = View.GONE
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                tvError.visibility = View.VISIBLE
            }
        }
    }
}
