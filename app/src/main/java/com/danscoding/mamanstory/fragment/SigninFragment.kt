package com.danscoding.mamanstory.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.R
import com.danscoding.mamanstory.SnapStoryActivity
import com.danscoding.mamanstory.SnapStoryActivity.Companion.EXTRA_TOKEN
import com.danscoding.mamanstory.databinding.FragmentSigninBinding
import com.danscoding.mamanstory.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class SigninFragment : Fragment() {

    private var _binding: FragmentSigninBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSignIn.setOnClickListener {
                loginAuth()
            }
            btnRegister.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_signinFragment_to_signupFragment)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loginAuth() {
        showLoading(true)
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        viewModel.viewModelScope.launch {
            viewModel.userLogin(email, password).collect { result ->
                result.onSuccess { credentials ->
                    credentials.loginResult.token.let { token ->
                        viewModel.saveAuthToken(token)
                        Intent(requireContext(), SnapStoryActivity::class.java).also { intent ->
                            intent.putExtra(EXTRA_TOKEN, token)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }
                when {
                    email.isEmpty() -> {
                        result.onFailure {
                            Toast.makeText(
                                requireContext(), "Please Fill Your Email", Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
                    password.isEmpty() -> {
                        result.onFailure {
                            Toast.makeText(
                                requireContext(), "Please Fill Your Password", Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
                    else -> {
                        result.onFailure {
                            Toast.makeText(
                                requireContext(), "Please Fill Your Data Correctly", Toast.LENGTH_SHORT
                            ).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

        private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}