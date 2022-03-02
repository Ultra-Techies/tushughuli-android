package com.todoist_android.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.todoist_android.R
import com.todoist_android.data.network.APIResource
import com.todoist_android.databinding.FragmentSignupBinding
import com.todoist_android.view.validateEmail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private val viewModel: AuthenticationViewModel by viewModels()
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signupResponse.collect {
                    when(it){
                        is APIResource.Success->{
                            binding.progressbarTwo.visibility = View.GONE
                            view.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                        }
                        is APIResource.Error ->{
                            binding.progressbarTwo.visibility = View.GONE
                            Toast.makeText(requireContext(),it.errorBody.toString(),Toast.LENGTH_LONG).show()
                        }
                        is APIResource.Loading -> {
                            binding.progressbarTwo.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        binding.progressbarTwo.visibility = View.GONE
        binding.btnSignup.setOnClickListener {

            binding.progressbarTwo.visibility = View.VISIBLE

            val userName = binding.etUsername.text.toString()
            val userEmail = binding.etEmail.text.toString()
            val userPassword = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()


            if (binding.etUsername.text.isNullOrEmpty()){
                binding.etUsername.error = "Please Enter Your User Name"
                binding.progressbarTwo.visibility = View.GONE
                return@setOnClickListener
            }
            if (userName.trim().length<= 2){
                binding.etUsername.error ="Enter a Name with more than two words"
                binding.progressbarTwo.visibility = View.GONE
                return@setOnClickListener
            }
            if (binding.etEmail.text.isNullOrEmpty()){
                binding.etEmail.error ="Please Enter your Email"
                binding.progressbarTwo.visibility = View.GONE
                return@setOnClickListener
            }
            if (!validateEmail(binding.etEmail.text.toString())){
                binding.etEmail.error = "Please Enter a valid Email"
                binding.progressbarTwo.visibility = View.GONE
                return@setOnClickListener
            }
            if (binding.etPassword.text.isNullOrEmpty()){
                binding.etPassword.error ="Please enter your password"
                binding.progressbarTwo.visibility = View.GONE
                return@setOnClickListener
            }

            if (binding.etConfirmPassword.text.isNullOrEmpty()){
                binding.etConfirmPassword.error ="Confirm your Password"
                binding.progressbarTwo.visibility = View.GONE
                return@setOnClickListener

            }
            if(userPassword.trim() != confirmPassword.trim()){

                binding.etConfirmPassword.error ="Passwords do not match"
                binding.progressbarTwo.visibility = View.GONE
                return@setOnClickListener
            }

            viewModel.signUp(
                userName,userEmail,userPassword,
            )
        }

        binding.textViewLogin.setOnClickListener {
            it.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

}
