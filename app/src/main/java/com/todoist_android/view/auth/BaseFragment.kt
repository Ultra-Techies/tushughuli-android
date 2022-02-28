package com.todoist_android.view.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.todoist_android.view.auth.networkapi.TodoRemoteSource
import com.todoist_android.view.auth.repository.BaseRepo
import com.todoist_android.viewmodel.AuthenticationViewModel
import com.todoist_android.viewmodel.ViewModelGenerator

abstract class BaseFragment<
        viewModel: ViewModel,
        viewBinding: ViewBinding,
        baseRepository: BaseRepo> : Fragment() {

     protected lateinit var binding: viewBinding
     protected val remoteSource = TodoRemoteSource()
    protected lateinit var viewModel: viewModel

     override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: android.os.Bundle?
     ): View? {
         binding = getFragmentBinding(inflater, container)

         //we need our viewmodelgenerator instance to create a new viewmodel
         val generator = ViewModelGenerator(getRepository())
         viewModel = ViewModelProvider(this, generator).get(getViewModel())

         return binding.root
     }
        abstract fun getViewModel(): Class<viewModel>

        abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) : viewBinding

        abstract fun getRepository(): baseRepository
        }