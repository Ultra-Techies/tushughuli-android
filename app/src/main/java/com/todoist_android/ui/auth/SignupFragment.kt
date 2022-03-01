package com.todoist_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.todoist_android.R
import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.databinding.FragmentSignupBinding
import com.todoist_android.ui.base.BaseFragment
import com.todoist_android.view.validateEmail

class SignupFragment : BaseFragment<AuthenticationViewModel, FragmentSignupBinding, AuthRepo>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.signUpResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is APIResource.Success -> {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }
                is APIResource.Error -> {
                    Toast.makeText(requireContext(), "Signup failed!", Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.btnSignup.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordConfirm = binding.etConfirmPassword.text.toString()
            val username = binding.etUsername.text.toString()
            Toast.makeText(context, "clicked!", Toast.LENGTH_SHORT).show()

            if (binding.etUsername.text.isNullOrEmpty()){
                binding.etUsername.error = "Please Enter Your User Name"
                return@setOnClickListener
            }
            if (binding.etUsername.text.toString().trim().length<= 2){
                binding.etUsername.error ="Enter a Name with more than two words"
                return@setOnClickListener
            }
            if (binding.etEmail.text.isNullOrEmpty()){
                binding.etEmail.error ="Please Enter your Email"
                return@setOnClickListener
            }
            if (!validateEmail(binding.etEmail.text.toString())){
                binding.etEmail.error = "Please Enter a valid Email"
                return@setOnClickListener
            }
            if (binding.etPassword.text.isNullOrEmpty()){
                binding.etPassword.error ="Please enter your password"
                return@setOnClickListener
            }

            if (binding.etConfirmPassword.text.isNullOrEmpty()){
                binding.etConfirmPassword.error ="Confirm your Password"
                return@setOnClickListener

            }
            if(binding.etPassword.text.toString().trim() != binding.etConfirmPassword.text.toString().trim()){

                binding.etConfirmPassword.error ="Passwords do not match"
                return@setOnClickListener
            }

            //TODO: add validation
            viewModel.signup(username, email, password)
        }

        binding.textViewLogin.setOnClickListener {
            Navigation.findNavController(view!!).navigate(R.id.loginFragment)
        }
    }
    override fun getViewModel() = AuthenticationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignupBinding.inflate(inflater, container, false)

    override fun getRepository() = AuthRepo(remoteSource.createRetrofitClient(APIAuthentication::class.java))
}
