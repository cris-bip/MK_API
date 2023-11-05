package mx.org.bm.gamesrf.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import mx.org.bm.gamesrf.R
import mx.org.bm.gamesrf.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var contrasenia = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnRegistrarse.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //Registrando al usuario
            firebaseAuth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener { authResult->
                if(authResult.isSuccessful){
                    //Enviar correo para verificación de email
                    var user_fb = firebaseAuth.currentUser
                    user_fb?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this, getString(R.string.email_verify_msg), Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener {
                        Toast.makeText(this, getString(R.string.varify_email_erro_msg), Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(this, getString(R.string.usr_created_confirm_msg), Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    binding.progressBar.visibility = View.GONE
                    manejaErrores(authResult)
                }
            }
        }

        binding.btnLogin.setOnClickListener{
            if(!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            autenticaUsuario(email, contrasenia)
        }

        binding.tvRestablecerPassword.setOnClickListener {
            val resetMail = EditText(it.context)
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            val passwordResetDialog = AlertDialog.Builder(it.context)
                .setTitle(getString(R.string.forgot_password_msg))
                .setMessage(getString(R.string.email_redo_msg))
                .setView(resetMail)
                .setPositiveButton(getString(R.string.send_btn_msg)) { _, _ ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                getString(R.string.redo_email_confirm_msg),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "El enlace no se ha podido enviar: ${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show() //it tiene la excepción
                        }
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.email_error_msg),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.setNegativeButton(getString(R.string.cancel_btn_label)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun autenticaUsuario(usr: String, psw: String){
        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener {resultTask->
            if(resultTask.isSuccessful){
                Toast.makeText(this, getString(R.string.auth_succed_msg), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                finish()
            }else{
                binding.progressBar.visibility = View.GONE
                manejaErrores(task = resultTask)
            }

        }

    }

    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(this, getString(R.string.email_format_error_msg), Toast.LENGTH_SHORT).show()
                binding.tietEmail.error = getString(R.string.email_format_error_msg)
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(this, getString(R.string.password_error_msg), Toast.LENGTH_SHORT).show()
                binding.tietContrasenia.error = getString(R.string.password_error_msg)
                binding.tietContrasenia.requestFocus()
                binding.tietContrasenia.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, getString(R.string.unconsistent_error_msg), Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(this, getString(R.string.email_used_error_msg), Toast.LENGTH_LONG).show()
                binding.tietEmail.error = getString(R.string.email_used_error_msg)
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(this, getString(R.string.session_expiration_error_msg), Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(this, getString(R.string.user_not_found_error_msg), Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(this, getString(R.string.password_incorrect_error_msg), Toast.LENGTH_LONG).show()
                binding.tietContrasenia.error = getString(R.string.password_incorrect_error_msg)
                binding.tietContrasenia.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(this, getString(R.string.unestable_conn_error_msg), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, getString(R.string.auth_unsucced_msg), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validaCampos(): Boolean{
        email = binding.tietEmail.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.tietContrasenia.text.toString().trim()

        if(email.isEmpty()){
            binding.tietEmail.error = getString(R.string.email_req_msg)
            binding.tietEmail.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.tietContrasenia.error = getString(R.string.password_requirements_msg)
            binding.tietContrasenia.requestFocus()
            return false
        }

        return true
    }
}