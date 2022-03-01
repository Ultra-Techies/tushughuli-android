package com.todoist_android.ui.auth

import android.os.Bundle
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
import androidx.navigation.findNavController
import com.todoist_android.R
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
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            //TODO: add validation
            viewModel.login(email, password)
        }

        binding.textViewRegister.setOnClickListener {
            it.findNavController().navigate( R.id.action_loginFragment_to_signupFragment )
        }
    }

}
