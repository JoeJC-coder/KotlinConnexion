package com.example.kotlinconnexion

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

private lateinit var usernameEditText: EditText
private lateinit var emailEditText: EditText
private lateinit var passwordEditText: EditText
private lateinit var phoneEditText: EditText
private lateinit var signupButton: Button

private val SMS_PERMISSION_REQUEST_CODE = 1

class createAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_account)

        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        phoneEditText = findViewById(R.id.phone)
        signupButton = findViewById(R.id.signup_button)


        signupButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val phone = phoneEditText.text.toString()

            val db = MyDatabaseHelper(this).readableDatabase
            val projection = arrayOf("_id", "username", "email", "password")
            val selection = "username = ? OR email = ?"
            val selectionArgs = arrayOf(username, email)
            val cursor = db.query("users", projection, selection, selectionArgs, null, null, null)
            if (isValidEmail(email)) {
                if (cursor.moveToFirst()) {
                    // User already exists in database
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // Insert user into SQLite database
                    val values = ContentValues().apply {
                        put("username", username)
                        put("email", email)
                        put("password", password)
                        put("phone", phone)
                    }
                    val code = generateRandomCode()
                    sendVerificationCode(phone, code)

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("Phone Verification")

                    val input = EditText(this)
                    input.setHint("Enter code verification")
                    input.inputType = InputType.TYPE_CLASS_NUMBER
                    builder.setView(input)

                    builder.setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, which ->
                        // Here you get get input text from the Edittext
                        var enteredCode = input.text.toString()
                        val validtionCode = code
                        if (validtionCode.equals(enteredCode)) {
                            Toast.makeText(this, "Numéro de téléphone vérifié avec succès.", Toast.LENGTH_SHORT).show()
                            db.insert("users", null, values)

                            Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()

                            // Rediriger l'utilisateur vers la page suivante
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else {
                            Toast.makeText(this, "Code de vérification incorrect.", Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }
                    })
                    builder.show()
                }
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }

            cursor.close()
        }
    }

    fun isValidEmail(email: String): Boolean {
        val regex = Regex(pattern = "^([a-zA-Z0-9._-]+)@([a-zA-Z0-9._-]+\\.[a-zA-Z]{2,5})$")
        return regex.matches(email)
    }

    fun showdialog(code : String) : Boolean{
        var result = false

        return result
    }

    private fun sendVerificationCode(phone : String, code : String) {
        val message = "Votre code de vérification est $code"
        try {
       /* val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phone")
            putExtra("sms_body", message)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Impossible d'envoyer le message.", Toast.LENGTH_SHORT).show()
        }*/

            val smsManager: SmsManager
            if (Build.VERSION.SDK_INT >= 23) {
                //if SDK is greater that or equal to 23 then
                //this is how we will initialize the SmsManager
                smsManager = this.getSystemService(SmsManager::class.java)
            } else {
                //if user's SDK is less than 23 then
                //SmsManager will be initialized like this
                smsManager = SmsManager.getDefault()
            }

            // on below line we are sending text message.
            smsManager.sendTextMessage(phone, null, message, null, null)
            Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {

            // on catch block we are displaying toast message for error.
            Toast.makeText(applicationContext, "Please enter all the data.."+e.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun generateRandomCode(): String {
        var code = ((100000..999999).random()).toString()
        return code
    }

}