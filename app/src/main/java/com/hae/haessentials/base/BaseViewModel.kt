package com.hae.haessentials.base

import androidx.lifecycle.ViewModel
import com.hae.haessentials.backend.APICalls

abstract class BaseViewModel:ViewModel() {
    lateinit var apiCalls: APICalls
}