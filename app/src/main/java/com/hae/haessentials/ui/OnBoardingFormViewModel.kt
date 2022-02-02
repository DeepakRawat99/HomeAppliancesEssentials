package com.hae.haessentials.ui

import android.text.Editable
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnBoardingFormViewModel : ViewModel() {

    val onClickValue: MutableLiveData<Int> = MutableLiveData()
    val onErrorListener: MutableLiveData<Int> = MutableLiveData()
    fun onEnteradd(e: Editable){
        onErrorListener.value = 1
    }

    fun onClick(v: View){
        onClickValue.value = 1
    }
}