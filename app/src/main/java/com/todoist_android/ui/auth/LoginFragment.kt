package com.todoist_android.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.todoist_android.R
import com.todoist_android.data.network.APIResource
import com.todoist_android.databinding.FragmentLoginBinding
import com.todoist_android.ui.home.MainActivity
import com.todoist_android.view.handleApiError
import com.todoist_android.view.validateEmail
import dagger.hilt.android.AndroidEntryPoint
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
                           binding.progressbar.visibility = GONE

                           val userId = it.value.id

                           //if it.value.valid is true redirect to home
                           //else show error message
                           it.value.valid?.let {
                               if (it) {
                                   //save user token or id
                                   /**
                                    * viewModel.saveAuthToken(it.value.accessToken)
                                    * This would be the best approach once backend is able
                                    * to give as an access token on successful login
                                    */
                                   viewModel.saveAuthToken(userId.toString())

                                   //Redirect to Home pass user id to home activity
                                   Intent(requireContext(), MainActivity::class.java).also {
                                       it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                       it.putExtra("userId", userId)
                                       startActivity(it)
                                   }
                               } else {
                                   binding.buttonLogin.isEnabled = true
                                   Snackbar.make(binding.root, "Account does not exist", Snackbar.LENGTH_LONG).show()
                               }
                           }
                       }
                       is APIResource.Error -> {
                           binding.root.handleApiError(it)
                           binding.progressbar.visibility = GONE
                           binding.buttonLogin.isEnabled = true
                       }
                       is APIResource.Loading -> {
                           binding.progressbar.visibility = VISIBLE
                           binding.buttonLogin.isEnabled = false
                       }
                   }
               }
            }

        }

        binding.progressbar.visibility = GONE
        binding.buttonLogin.setOnClickListener {
            binding.progressbar.visibility = VISIBLE
            binding.buttonLogin.isEnabled = false

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
            it.findNavController().navigate( R.id.action_loginFragment_to_signupFragment )
        }

    }
}
