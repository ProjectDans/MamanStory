package com.danscoding.mamanstory

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.danscoding.mamanstory.api.ApiConfig
import com.danscoding.mamanstory.databinding.ActivityLoginBinding
import com.danscoding.mamanstory.preference.AccountTokenPreference
import com.danscoding.mamanstory.response.AccountLoginResponse
import com.danscoding.mamanstory.viewmodel.TokenViewModel
import com.danscoding.mamanstory.viewmodel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var tokenViewModel: TokenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userToken")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)

        //preferences untuk token
        val pref = AccountTokenPreference.getInstance(dataStore)
        tokenViewModel =ViewModelProvider(this, ViewModelFactory(pref))[TokenViewModel::class.java]
        binding.btnSignIn.setOnClickListener {
            showLoading(true)
            loginTask(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }

    }

    private fun loginTask(email: String, password: String){
        val client = ApiConfig.getApiService().loginAccountTask(email,password)
        client.enqueue(object: Callback<AccountLoginResponse> {
            override fun onResponse(
                call: Call<AccountLoginResponse>,
                response: Response<AccountLoginResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val token = response.body()?.loginResult?.token.toString()
                    tokenViewModel.saveTokens(token)
                    val intent = Intent(this@LoginActivity,SnapStoryActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast(response.message())
                }
            }
            override fun onFailure(call: Call<AccountLoginResponse>, t: Throwable) {
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