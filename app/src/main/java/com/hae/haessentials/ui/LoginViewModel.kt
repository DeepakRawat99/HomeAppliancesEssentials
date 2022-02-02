package com.hae.haessentials.ui

import android.text.Editable
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hae.haessentials.base.BaseViewModel

class LoginViewModel : BaseViewModel() {
        val continueClick: MutableLiveData<Boolean> = MutableLiveData()
        var mobileNumber: MutableLiveData<Boolean> = MutableLiveData()
    fun onEnterMobile(e:Editable){
        mobileNumber.value = e.isNotEmpty()
    }
    fun onClick(v: View){
        continueClick.value = true
    }
}