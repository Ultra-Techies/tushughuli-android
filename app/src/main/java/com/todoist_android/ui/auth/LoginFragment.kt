package com.todoist_android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.todoist_android.databinding.FragmentLoginBinding
import com.todoist_android.data.network.APIAuthentication
import com.todoist_android.data.network.APIResource
import com.todoist_android.data.repository.AuthRepo
import com.todoist_android.ui.base.BaseFragment

class LoginFragment : BaseFragment<AuthenticationViewModel, FragmentLoginBinding, AuthRepo>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is APIResource.Success -> {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }
                is APIResource.Error -> {
                    Toast.makeText(requireContext(), "Login failed!", Toast.LENGTH_LONG).show()
                }
            }
        })
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            //TODO: add validation
            viewModel.login(email, password)
        }
    }
    override fun getViewModel() = AuthenticationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getRepository() = AuthRepo(remoteSource.createRetrofitClient(APIAuthentication::class.java))
}
