package com.hae.haessentials.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.HomeFragmentBinding
import com.hae.haessentials.databinding.OnBoardingFormFragmentBinding
import com.hae.haessentials.utility.UserSharedPref

class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    override fun getLayoutId(): Int {
      return R.layout.home_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
       return HomeViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as HomeFragmentBinding
        viewModel = getViewModel() as HomeViewModel
        binding.viewModel = viewModel
        showBottomBar(true)
        binding.hfUsername.text = getString(R.string.hello_user, UserSharedPref.getFirstName())
    }

}