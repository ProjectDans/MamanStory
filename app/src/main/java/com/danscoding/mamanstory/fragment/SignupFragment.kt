package com.danscoding.mamanstory.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.danscoding.mamanstory.R
import com.danscoding.mamanstory.databinding.FragmentSignupBinding
import com.danscoding.mamanstory.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnRegister.setOnClickListener {
                registerAuth()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container,false)
        return binding.root
    }

    private fun registerAuth() {
        showLoading(true)
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        viewModel.viewModelScope.launch {
            viewModel.userRegister(name, email, password).collect{ result ->
                result.onSuccess {
                    findNavController().navigate(R.id.action_signupFragment_to_signinFragment)
                    Toast.makeText(requireContext(), "Daftar Berhasil, Silahkan Login Akun Anda!", Toast.LENGTH_SHORT).show()
                }
                when {
                    name.isEmpty() -> {
                        result.onFailure {
                            Toast.makeText(requireContext(), "Please Fill Your Name", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                    email.isEmpty() -> {
                        result.onFailure {
                            Toast.makeText(requireContext(), "Please Fill Your Email", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                    password.isEmpty() -> {
                        result.onFailure {
                            Toast.makeText(requireContext(), "Please Fill Your Password", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                    else -> {
                        result.onFailure {
                            Toast.makeText(requireContext(), "Please Fill Your Data Correctly", Toast.LENGTH_SHORT).show()
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