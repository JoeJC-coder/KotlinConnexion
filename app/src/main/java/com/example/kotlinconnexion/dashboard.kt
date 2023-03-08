package com.example.kotlinconnexion

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private lateinit var logoutButton: Button

class dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)


        logoutButton = findViewById(R.id.logout)
        val sessionManager = SessionManager(this)

        logoutButton.setOnClickListener {
            val sessionManager = SessionManager(this)
            sessionManager.logout()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            if(sessionManager.isLoggedOut()) {
                Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show()
            }
        }

        if (sessionManager.isLoggedIn()) {
            // L'utilisateur est connecté
            val username = sessionManager.getUsername()
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Connection Message")
            alertDialogBuilder.setMessage("Welcome $username")
            alertDialogBuilder.setPositiveButton(android.R.string.yes) { _,_ ->
                Toast.makeText(applicationContext,
                    android.R.string.yes, Toast.LENGTH_SHORT).show()
            }
            alertDialogBuilder.show()

        }
    }
}