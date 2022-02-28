package com.todoist_android.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.todoist_android.databinding.FragmentLoginBinding
import com.todoist_android.view.auth.networkapi.APIAuthentication
import com.todoist_android.view.auth.repository.AuthRepo
import com.todoist_android.viewmodel.AuthenticationViewModel

class LoginFragment : BaseFragment<AuthenticationViewModel, FragmentLoginBinding, AuthRepo>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    override fun getViewModel() = AuthenticationViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getRepository() = AuthRepo(remoteSource.createRetrofitClient(APIAuthentication::class.java))
}
