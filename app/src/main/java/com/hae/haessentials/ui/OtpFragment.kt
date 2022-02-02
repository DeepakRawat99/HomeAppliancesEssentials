package com.hae.haessentials.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.LoginFragmentBinding

class OtpFragment:BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private lateinit var mobileNumber: String
    private lateinit var otpValue : String
    override fun getLayoutId(): Int {
     return R.layout.login_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return LoginViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()

        //text watcher for otp
/*        binding.lfEtotp1.addTextChangedListener(OtpTextWatcher(binding.lfEtotp1,binding.lfEtotp2))
        binding.lfEtotp2.addTextChangedListener(OtpTextWatcher(binding.lfEtotp2,binding.lfEtotp3))
        binding.lfEtotp3.addTextChangedListener(OtpTextWatcher(binding.lfEtotp3,binding.lfEtotp4))
        binding.lfEtotp4.addTextChangedListener(OtpTextWatcher(binding.lfEtotp4,null))

        //key listener for backspace backspace will work reverse so starting with null
        binding.lfEtotp1.setOnKeyListener(OtpKeyEvent(binding.lfEtotp1,null))
        binding.lfEtotp2.setOnKeyListener(OtpKeyEvent(binding.lfEtotp2,binding.lfEtotp1))
        binding.lfEtotp3.setOnKeyListener(OtpKeyEvent(binding.lfEtotp3,binding.lfEtotp2))
        binding.lfEtotp4.setOnKeyListener(OtpKeyEvent(binding.lfEtotp4,binding.lfEtotp3))*/
    }

    private fun initialize() {
        binding = getBinding() as LoginFragmentBinding
        viewModel = getViewModel() as LoginViewModel
        binding.viewModel = viewModel
        if(arguments!=null && arguments!!.containsKey("mobileNumber")){
            mobileNumber = arguments!!.getString("mobileNumber").toString()
        }

        viewModel.continueClick.observe(this, Observer {
            if(it){
                if(binding.lfTilotp.visibility == View.VISIBLE){
                    val otp = binding.lfEtotp1.text.toString() + binding.lfEtotp2.text.toString() + binding.lfEtotp3.text.toString() + binding.lfEtotp4.text.toString()
                    if(otp.isEmpty() || otp.length!=4){
                        Toast.makeText(requireContext(), "Thank You", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        showLoading()
                        replaceFragment(R.id.onBoardingFormFrag,null)}
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.lfTillogin.visibility = View.GONE
        binding.lfTilotp.visibility = View.VISIBLE
        binding.lfEntermobtv.text = getString(R.string.otp_enter)
    }
  /*  class OtpKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.lf_etotp1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }


    }

    class OtpTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
        TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            val text = editable.toString()
            when (currentView.id) {
                R.id.lf_etotp1 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.lf_etotp2 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.lf_etotp3 -> if (text.length == 1) nextView!!.requestFocus()
                }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { //nothing
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { //nothing
        }

    }*/

}