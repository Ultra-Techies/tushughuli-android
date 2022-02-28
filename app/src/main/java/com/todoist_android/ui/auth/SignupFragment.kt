package com.todoist_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.databinding.FragmentSignupBinding
import com.todoist_android.ui.base.BaseFragment

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

            //TODO: add validation
            viewModel.signup(username, email, password)
        }
    }
    override fun getViewModel() = AuthenticationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignupBinding.inflate(inflater, container, false)

    override fun getRepository() = AuthRepo(remoteSource.createRetrofitClient(APIAuthentication::class.java))
}
