package com.danscoding.mamanstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.danscoding.mamanstory.api.ApiConfig
import com.danscoding.mamanstory.databinding.ActivityRegisterBinding
import com.danscoding.mamanstory.response.AccountRegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)
        binding.btnRegister.setOnClickListener{
            showLoading(true)
            registerTask(binding.nameEditText.text.toString(), binding.emailEditText.text.toString(), binding. passwordEditText.text.toString())
        }
    }

    private fun registerTask(nama: String, email: String, password: String){
        val client = ApiConfig.getApiService().registerAccountTask(nama,email,password)
        client.enqueue(object: Callback<AccountRegisterResponse> {
            override fun onResponse(
                call: Call<AccountRegisterResponse>,
                response: Response<AccountRegisterResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    showToast(response.body()?.message.toString() + " Silakan Login")
                    finish()
                } else {
                    showToast(response.message())
                }
            }
            override fun onFailure(call: Call<AccountRegisterResponse>, t: Throwable) {
                showLoading(false)
                Log.e("FAILURE", "onFailure: ${t.message.toString()}")
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}