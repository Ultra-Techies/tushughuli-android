package com.todoist_android.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.todoist_android.di.TodoRemoteDataSource
import com.todoist_android.data.network.repository.BaseRepo

abstract class BaseFragment<
        viewModel: ViewModel,
        viewBinding: ViewBinding,
        baseRepository: BaseRepo> : Fragment() {

     protected lateinit var binding: viewBinding
     protected val remoteSource = TodoRemoteDataSource()
    protected lateinit var viewModel: viewModel

     override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: android.os.Bundle?
     ): View? {
         binding = getFragmentBinding(inflater, container)

         //we need our viewmodel generator instance to create a new viewmodel
         val generator = ViewModelGenerator(getRepository())
         viewModel = ViewModelProvider(this, generator).get(getViewModel())

         return binding.root
     }
        abstract fun getViewModel(): Class<viewModel>

        abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) : viewBinding

        abstract fun getRepository(): baseRepository
        }