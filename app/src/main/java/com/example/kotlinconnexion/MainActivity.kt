package com.example.kotlinconnexion

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

private lateinit var emailEditText: EditText
private lateinit var passwordEditText: EditText
private lateinit var loginButton: Button
private lateinit var createAccountTextView: TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)
        createAccountTextView = findViewById(R.id.createAccount)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val db = MyDatabaseHelper(this).readableDatabase
            val projection = arrayOf("_id", "email", "password")
            val selection = "email = ? AND password = ?"
            val selectionArgs = arrayOf(email, password)
            val cursor = db.query("users", projection, selection, selectionArgs, null, null, null)
            if (isValidEmail(email)) {
                if (cursor.moveToFirst()) {
                    // User is authenticated
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val sessionManager = SessionManager(this)
                    sessionManager.createLoginSession(email)
                    // Rediriger l'utilisateur vers la page suivante

                    val username = sessionManager.getUsername()
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    val intent = Intent(this, dashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Invalid credentials
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Invalid credentials
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        createAccountTextView.setOnClickListener {
            val intent = Intent(this, createAccount::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun isValidEmail(email: String): Boolean {
        val regex = Regex(pattern = "^([a-zA-Z0-9._-]+)@([a-zA-Z0-9._-]+\\.[a-zA-Z]{2,5})$")
        return regex.matches(email)
    }
}