package com.danscoding.mamanstory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.danscoding.mamanstory.api.ApiConfiguration
import com.danscoding.mamanstory.databinding.ActivityLoginBinding
import com.danscoding.mamanstory.model.ModelLogin
import com.danscoding.mamanstory.preference.PreferenceStoryAccount
import com.danscoding.mamanstory.response.LoginStoryResult
import com.danscoding.mamanstory.response.ResponseLoginStory
import com.danscoding.mamanstory.viewmodel.StoryViewModel
import com.danscoding.mamanstory.viewmodel.StoryViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val Context.dataStoreStoryApp: DataStore<Preferences> by preferencesDataStore(name= "storyAccount")
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var loginStoryResult: LoginStoryResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        showLoading(false)
        playAnimation()
        viewModelStory()
        loginStory()
        registerButton()

    }

    private fun playAnimation() {

        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextInput = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordTextInput = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val btnSignin = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val textView = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(email, emailTextInput, password, passwordTextInput)
        }

        AnimatorSet().apply {
            playSequentially(textView, together, btnSignin, btnRegister)
            start()
        }

    }
    private fun registerButton(){
        binding.btnRegister.setOnClickListener {
            val view = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(view)
        }
    }

    private fun viewModelStory(){
        val prefStory = PreferenceStoryAccount.getStoryApp(dataStoreStoryApp)
        storyViewModel = ViewModelProvider(
            this@LoginActivity,
            StoryViewModelFactory(prefStory)
        )[StoryViewModel::class.java]

        storyViewModel.getStoryAccount().observe(this){ login ->
            this.loginStoryResult = login
        }
    }

    private fun loginStory() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString().trim()
            showLoading(true)
            ApiConfiguration().getApiService()
                .storyLogin(ModelLogin(email, password))
                .enqueue(object : Callback<ResponseLoginStory> {
                    override fun onResponse(
                        call: Call<ResponseLoginStory>,
                        response: Response<ResponseLoginStory>
                    ) {
                        if (response.code() == 200){
                            val bodyUser = response.body()?.loginResult as LoginStoryResult
                            storyViewModel.saveStoryAccount(
                                LoginStoryResult(bodyUser.userId,
                                bodyUser.name,
                                bodyUser.token,
                                true)
                            )
                            showLoading(false)

                            Toast.makeText(applicationContext, "Login Berhasil",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, SnapStoryActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            showLoading(false)
                            Toast.makeText(applicationContext,
                                "E-mail or Password doesn't match with our credential",
                                Toast.LENGTH_SHORT).show()
                            Log.d(MainActivity::class.java.simpleName,
                                response.body()?.message.toString())
                        }
                    }

                    override fun onFailure(call: Call<ResponseLoginStory>, t: Throwable) {
                        Log.d("failure: ", t.message.toString())
                    }

                })
        }
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}