package com.danscoding.mamanstory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danscoding.mamanstory.api.ApiConfiguration
import com.danscoding.mamanstory.databinding.ActivityRegisterBinding
import com.danscoding.mamanstory.model.ModelRegister
import com.danscoding.mamanstory.response.ResponseRegisterStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        textField()
    }




    private fun textField() {
        binding.btnRegister.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.nameEditText.error = "Harap isi form nama"
                }
                email.isEmpty() -> {
                    binding.emailEditText.error = "Harap isi form email"
                }
                password.isEmpty() -> {
                    binding.emailEditText.error = "Harap isi password"
                }
                else -> {
                    binding.nameEditText.error = null
                    binding.emailEditText.error = textValidate()
                }
            }
        }
    }

    private fun textValidate(): String? {
        val valid = binding.emailEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(valid).matches()) {
            return "Invalid E-mail"
        } else {
            showLoading(true)
            val registerIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(registerIntent)
            finish()
            makeAccountStory()
        }
        return null
    }

    private fun makeAccountStory() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        showLoading(true)

        ApiConfiguration().getApiService().storyRegister(ModelRegister(
            name,
            email,
            password))
            .enqueue(object : Callback<ResponseRegisterStory> {
                override fun onResponse(
                    call: Call<ResponseRegisterStory>,
                    response: Response<ResponseRegisterStory>
                ) {
                    if (response.code() == 201){
                        showLoading(false)
                        Toast.makeText(applicationContext, "Register Berhasil", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        showLoading(false)
                        Toast.makeText(applicationContext, "Register tidak berhasil, coba lakukan registrasi ulang", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseRegisterStory>, t: Throwable) {
                    showLoading(false)
                    Log.d("failure: ", t.message.toString())
                }

            })
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}