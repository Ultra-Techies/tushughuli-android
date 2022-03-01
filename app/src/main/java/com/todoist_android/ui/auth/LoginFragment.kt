package com.todoist_android.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.todoist_android.data.network.APIResource
import com.todoist_android.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: AuthenticationViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // use kotlin flows
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner. repeatOnLifecycle(Lifecycle.State.STARTED){
               viewModel.loginResponse.collect {
                   when (it) {
                       is APIResource.Success -> {
                           Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                           binding.progressbar.visibility = GONE
                       }
                       is APIResource.Error -> {
                           Toast.makeText(requireContext(), "Login failed!", Toast.LENGTH_LONG).show()
                           binding.progressbar.visibility = GONE
                       }
                   }
               }
            }

        }

        binding.progressbar.visibility = GONE
        binding.buttonLogin.setOnClickListener {
            binding.progressbar.visibility = VISIBLE
            val email = binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            //validate email and password
            if (binding.editTextTextEmailAddress.text.isNullOrEmpty()){
                binding.editTextTextEmailAddress.error ="Please Enter your Email"
                binding.progressbar.visibility = GONE
                binding.buttonLogin.isEnabled = true
                return@setOnClickListener
            }

            if (!validateEmail(binding.editTextTextEmailAddress.text.toString().trim())){
                binding.editTextTextEmailAddress.error = "Please Enter a valid Email"
                binding.progressbar.visibility = GONE
                binding.buttonLogin.isEnabled = true
                return@setOnClickListener
            }

            if (binding.editTextTextPassword.text.isNullOrEmpty()){
                binding.editTextTextPassword.error ="Please enter your password"
                binding.progressbar.visibility = GONE
                binding.buttonLogin.isEnabled = true
                return@setOnClickListener
            }

            if (email.isNotEmpty() && password.isNotEmpty()){
                viewModel.login(email, password)
            }
        }

        binding.textViewRegister.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.signupFragment)
        }

    }
}
