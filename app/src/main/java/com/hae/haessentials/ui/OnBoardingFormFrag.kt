package com.hae.haessentials.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.OnBoardingFormFragmentBinding
import com.hae.haessentials.utility.*

class OnBoardingFormFrag : BaseFragment() {

    companion object {
        fun newInstance() = OnBoardingFormFrag()
    }

    private lateinit var viewModel: OnBoardingFormViewModel
    private lateinit var binding:OnBoardingFormFragmentBinding
    override fun getLayoutId(): Int {
        return R.layout.on_boarding_form_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return OnBoardingFormViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return  super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as OnBoardingFormFragmentBinding
        viewModel = getViewModel() as OnBoardingFormViewModel
        binding.viewModel = viewModel

        //initializing observer's
        viewModel.onClickValue.observe(this, Observer {
            if(it==1){
                //not using on text change listener for getting et values because api's
            // will return value and user might not change them will through error
                val firstName= binding.obffFirstnameet.text.toString()
                val lastName= binding.obffLastnameet.text.toString()
                val building= binding.obffHouseet.text.toString()
                val area= binding.obffLocalityet.text.toString()
                val landmark= binding.obffLandmarket.text.toString()
                val pincode= binding.obffPincodeet.text.toString()
                if(!validText(firstName)) binding.obfftilFirstname.error = getString(R.string.valid_firstname)
                else if(!validText(lastName)) binding.obfftilLastname.error = getString(R.string.valid_lastname)
                else if(!validText(building)) binding.obfftilHouse.error = getString(R.string.valid_building)
                else if(!validText(area)) binding.obfftilLocality.error = getString(R.string.valid_locality)
                else if(!validPinCode(pincode)) binding.obfftilPincode.error = getString(R.string.valid_pincode)
                else if(!servicePinCode(pincode)) binding.obfftilPincode.error = getString(R.string.serviceable_text)
                else {
                    //if everything seems to be fine move to next frag
                    replaceFragment(R.id.homeFragment,null)

                }
            }
        })

        viewModel.onErrorListener.observe(this, Observer {
            binding.obfftilFirstname.error = null
            binding.obfftilLastname.error = null
            binding.obfftilHouse.error = null
            binding.obfftilLocality.error = null
            binding.obfftilPincode.error = null
        })
    }

}