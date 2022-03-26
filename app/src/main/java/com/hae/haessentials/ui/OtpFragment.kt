package com.hae.haessentials.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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
import java.util.concurrent.TimeUnit

class OtpFragment:BaseFragment() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private lateinit var mobileNumber: String
    private lateinit var verificationID: String
    private lateinit var token: PhoneAuthProvider.ForceResendingToken
    private var timer : CountDownTimer?=null
    private lateinit var auth: FirebaseAuth
    private val RESENDTIME:Int = 30
    private lateinit var otpResendCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
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
        binding.lfEtotp1.addTextChangedListener(OtpTextWatcher(binding.lfEtotp1,binding.lfEtotp2))
        binding.lfEtotp2.addTextChangedListener(OtpTextWatcher(binding.lfEtotp2,binding.lfEtotp3))
        binding.lfEtotp3.addTextChangedListener(OtpTextWatcher(binding.lfEtotp3,binding.lfEtotp4))
        binding.lfEtotp4.addTextChangedListener(OtpTextWatcher(binding.lfEtotp4,binding.lfEtotp5))
        binding.lfEtotp5.addTextChangedListener(OtpTextWatcher(binding.lfEtotp5,binding.lfEtotp6))
        binding.lfEtotp6.addTextChangedListener(OtpTextWatcher(binding.lfEtotp6,null))

        //key listener for backspace backspace will work reverse so starting with null
        binding.lfEtotp1.setOnKeyListener(OtpKeyEvent(binding.lfEtotp1,null))
        binding.lfEtotp2.setOnKeyListener(OtpKeyEvent(binding.lfEtotp2,binding.lfEtotp1))
        binding.lfEtotp3.setOnKeyListener(OtpKeyEvent(binding.lfEtotp3,binding.lfEtotp2))
        binding.lfEtotp4.setOnKeyListener(OtpKeyEvent(binding.lfEtotp4,binding.lfEtotp3))
        binding.lfEtotp5.setOnKeyListener(OtpKeyEvent(binding.lfEtotp5,binding.lfEtotp4))
        binding.lfEtotp6.setOnKeyListener(OtpKeyEvent(binding.lfEtotp6,binding.lfEtotp5))
    }

    private fun initialize() {
        binding = getBinding() as LoginFragmentBinding
        viewModel = getViewModel() as LoginViewModel
        binding.viewModel = viewModel
        if(arguments!=null && arguments!!.containsKey(MOBILE_NUMBER)){
            mobileNumber = arguments!!.getString(MOBILE_NUMBER).toString()
        }
        if(arguments!=null && arguments!!.containsKey(VERIFICATION_ID)){
            verificationID = arguments!!.getString(VERIFICATION_ID).toString()
        }
        if(arguments!=null && arguments!!.containsKey(TOKEN)){
            token = arguments!!.getParcelable<PhoneAuthProvider.ForceResendingToken>(TOKEN)!!
        }
        try {
            auth = FirebaseAuth.getInstance()
        }
        catch (e: Exception){
            Log.d("FirebaseException", e.toString())
        }
        initTimer()
        binding.lfResend.setOnClickListener {
            resendVerificationCode(mobileNumber,token)
        }

        otpResendCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
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
                verificationID = p0
                token = p1
            }

        }

        viewModel.continueClick.observe(this, Observer {
            if(it){
                if(binding.lfTilotp.visibility == View.VISIBLE){
                    val otp = binding.lfEtotp1.text.toString() + binding.lfEtotp2.text.toString() + binding.lfEtotp3.text.toString() + binding.lfEtotp4.text.toString() + binding.lfEtotp5.text.toString() + binding.lfEtotp6.text.toString()
                    if(otp.isEmpty() || otp.length!=6){
                        Toast.makeText(requireContext(), "Invalid Otp Please Try again", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        showLoading()
                        verifyPhoneNumberWithCode(verificationID,otp)
                        //replaceFragment(R.id.onBoardingFormFrag,null)
                    }
                }
            }
        })
    }

    private fun initTimer() {
        binding.lfResend.text = RESENDTIME.toString()
        timer = object : CountDownTimer(RESENDTIME.toLong()*1000, 1000){
            override fun onTick(p0: Long) {
                if(isAdded)
                binding.lfResend.text = getString(R.string.resend_otp_in,(p0/1000).toString())
            }

            override fun onFinish() {
                binding.lfResend.text = getString(R.string.resend_otp)
                binding.lfResend.isClickable = true
            }
        }
        timer?.start()
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }


    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(otpResendCallback)          // OnVerificationStateChangedCallbacks
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

                    UserSharedPref.setUserUniqueId(auth.currentUser?.uid.toString())
                    UserSharedPref.setMobile(mobileNumber)
                    UserSharedPref.setLogin(true)
                    timer?.cancel()
                    handleNavigation()
                    //replaceFragment(R.id.onBoardingFormFrag,null)
                    //val user = task.result?.user
                } else {
                    hideLoading()
                    // Sign in failed, display a message and update the UI
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
        binding.lfTillogin.visibility = View.GONE
        binding.lfTilotp.visibility = View.VISIBLE
        binding.lfEntermobtv.text = getString(R.string.otp_enter)

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
                R.id.lf_etotp4 -> if (text.length == 1) nextView!!.requestFocus()
                R.id.lf_etotp5 -> if (text.length == 1) nextView!!.requestFocus()
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