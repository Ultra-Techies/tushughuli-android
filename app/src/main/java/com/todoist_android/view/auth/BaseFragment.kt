package com.todoist_android.view.auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.todoist_android.view.auth.repository.BaseRepo

abstract class BaseFragment<
        viewModel: ViewModel,
        viewBinding: ViewBinding,
        baseRepository: BaseRepo> : Fragment() {

     protected lateinit var binding: viewBinding

     override fun onCreateView(
         inflater: android.view.LayoutInflater,
         container: android.view.ViewGroup?,
         savedInstanceState: android.os.Bundle?
     ): View? {
         binding = getFragmentBinding(inflater, container)
         return binding.root
     }
        abstract fun getViewModel(): Class<viewModel>

        abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) : viewBinding

        abstract fun getRepository(): baseRepository
        }