package com.hae.haessentials.ui

import android.os.CountDownTimer
import android.text.Editable
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseViewModel

class LoginViewModel : BaseViewModel() {
        val continueClick: MutableLiveData<Boolean> = MutableLiveData()
        var mobileNumber: MutableLiveData<Boolean> = MutableLiveData()
    private var timer : CountDownTimer?=null
    private lateinit var auth: FirebaseAuth
    private val RESENDTIME:Int = 30

    fun onEnterMobile(e:Editable){
        mobileNumber.value = e.isNotEmpty()
    }
    fun onClick(v: View){
        continueClick.value = true
    }
    private fun initTimer() {
       // binding.lfResend.text = RESENDTIME.toString()
        timer = object : CountDownTimer(RESENDTIME.toLong()*1000, 1000){
            override fun onTick(p0: Long) {
             //   binding.lfResend.text = getString(R.string.resend_otp_in,(p0/1000).toString())
            }

            override fun onFinish() {
              //  binding.lfResend.text = getString(R.string.resend_otp)
            //    binding.lfResend.isClickable = true
            }
        }
        timer?.start()
    }
}