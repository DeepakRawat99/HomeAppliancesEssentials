package com.hae.haessentials.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.LoginFragmentBinding
import com.hae.haessentials.utility.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class Login : BaseFragment() {

    companion object {
        fun newInstance() = Login()
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var phoneNumber:String
    private lateinit var otpCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun getLayoutId(): Int {
      return R.layout.login_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return LoginViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
       return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
//onviewcreated is called because sometimes on create doesn't initialize everything

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as LoginFragmentBinding
        viewModel = getViewModel() as LoginViewModel
        binding.viewModel = viewModel

        try {
            auth = FirebaseAuth.getInstance()
        }
        catch (e:Exception){
            Log.d("FirebaseException", e.toString())
        }

        otpCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
               signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                        hideLoading()
                    showToast("Invalid Request Please Try after Sometime",requireContext())
                } else if (p0 is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                        hideLoading()
                    showToast("Too many Requests Please Try after Sometime",requireContext())
                }
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                showToast( "Otp Sent to this number",requireContext())
                val bundle = Bundle()
                bundle.putString(VERIFICATION_ID,p0)
                bundle.putParcelable(TOKEN, p1)
                bundle.putString(MOBILE_NUMBER, phoneNumber)
                replaceFragment(R.id.otpFragment,bundle)
            }

        }

        viewModel.continueClick.observe(this, Observer {
                if(it){
                    //checking if otp or mobile
                    if(binding.lfTillogin.visibility == View.VISIBLE){
                    if(validMobileNumber(binding.lfEtmobile.text.toString())){
                        binding.lfTillogin.error = null
                        showLoading()

                        //hideLoading is done in baseFragment's onResume
                        phoneNumber = "+91"+binding.lfEtmobile.text.toString()
                        hideKeyboard(this.requireActivity())
                        callOtp()

                    }else {
                        binding.lfTillogin.error = getString(R.string.valid_mobile)
                    }
                }
                }
        })

        viewModel.mobileNumber.observe(this, Observer {
            if(it){
                binding.lfTillogin.error = null
            }
        })
    }

    private fun callOtp() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(otpCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    UserSharedPref.setUserUniqueId(auth.currentUser?.uid.toString())
                    UserSharedPref.setMobile(phoneNumber)
                    UserSharedPref.setLogin(true)
                    // Sign in success, update UI with the signed-in user's information
                    handleNavigation()
                //replaceFragment(R.id.onBoardingFormFrag,null)
                    //val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                        hideLoading()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        showToast("Invalid Otp Please Try again",requireContext())
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    override fun onResume() {
        super.onResume()
        binding.lfTillogin.visibility = View.VISIBLE
        binding.lfTilotp.visibility = View.GONE
        binding.lfResend.visibility = View.GONE
        binding.lfEntermobtv.text = getString(R.string.enter_your_mobile_number)
        showBottomBar(false)
    }

}