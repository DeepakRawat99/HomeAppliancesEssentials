package com.hae.haessentials.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.LoginFragmentBinding
import com.hae.haessentials.utility.validMobileNumber
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class Login : BaseFragment() {

    companion object {
        fun newInstance() = Login()
        const val LOGIN = "login"
        const val OTP = "otp"
        var isOTPScreenShown = false
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth:FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
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

       /* try {
            auth = FirebaseAuth.getInstance()
        }
        catch (e:Exception){
            Log.d("FirebaseException", e.toString())
        }*/
        //changing layout to login
        if(!isOTPScreenShown) changeLayout(LOGIN)

        otpCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
               signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    showToast("Invalid Request Please Try after Sometime",requireContext())
                } else if (p0 is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    showToast("Too many Requests Please Try after Sometime",requireContext())
                }
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                storedVerificationId = p0
                resendToken = p1
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
                        showToast( "Otp Sent to this number",requireContext())
                       // callOtp(binding.lfEtmobile.text.toString())

                        changeLayout(OTP)
                    }else {
                        binding.lfTillogin.error = getString(R.string.valid_mobile)
                    }
                }else   if(binding.lfTilotp.visibility == View.VISIBLE){
                        val otp = binding.lfEtotp1.text.toString() + binding.lfEtotp2.text.toString() + binding.lfEtotp3.text.toString() + binding.lfEtotp4.text.toString()
                        if(otp.isEmpty() || otp.length!=4){
                            showToast( "Invalid Otp",requireContext())

                        }
                        else{
                            showLoading()
                            isOTPScreenShown = false
                            replaceFragment(R.id.onBoardingFormFrag,null)
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

    private fun callOtp(mobile: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobile)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(otpCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
    }


    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(otpCallback)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information


                    replaceFragment(R.id.onBoardingFormFrag,null)
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        showToast("Invalid Otp Please Try again",requireContext())
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


    private fun changeLayout(type:String) {
            hideLoading()
            if(type == LOGIN){
            //using same layout for otp also so need to change while resuming
            binding.lfTillogin.visibility = View.VISIBLE
            binding.lfTilotp.visibility = View.GONE
            binding.lfEntermobtv.text = getString(R.string.enter_your_mobile_number)
            isOTPScreenShown = true
    }
        else {
            binding.lfTillogin.visibility = View.GONE
            binding.lfTilotp.visibility = View.VISIBLE
            binding.lfEntermobtv.text = getString(R.string.otp_enter)

                //text watcher for otp
                binding.lfEtotp1.addTextChangedListener(
                     OtpTextWatcher(
                        binding.lfEtotp1,
                        binding.lfEtotp2
                    )
                )
                binding.lfEtotp2.addTextChangedListener(
                     OtpTextWatcher(
                        binding.lfEtotp2,
                        binding.lfEtotp3
                    )
                )
                binding.lfEtotp3.addTextChangedListener(
                     OtpTextWatcher(
                        binding.lfEtotp3,
                        binding.lfEtotp4
                    )
                )
                binding.lfEtotp4.addTextChangedListener(
                     OtpTextWatcher(
                        binding.lfEtotp4,
                        null
                    )
                )

                //key listener for backspace backspace will work reverse so starting with null
                binding.lfEtotp1.setOnKeyListener( OtpKeyEvent(binding.lfEtotp1, null))
                binding.lfEtotp2.setOnKeyListener(
                     OtpKeyEvent(
                        binding.lfEtotp2,
                        binding.lfEtotp1
                    )
                )
                binding.lfEtotp3.setOnKeyListener(
                     OtpKeyEvent(
                        binding.lfEtotp3,
                        binding.lfEtotp2
                    )
                )
                binding.lfEtotp4.setOnKeyListener(
                     OtpKeyEvent(
                        binding.lfEtotp4,
                        binding.lfEtotp3
                    )
                )
            isOTPScreenShown = false
        }
    }
    class OtpKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
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

    }

}