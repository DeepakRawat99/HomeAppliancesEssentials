package com.hae.haessentials.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hae.haessentials.R

abstract class BaseActivity: AppCompatActivity() {
    private lateinit var binding: ViewDataBinding
    private lateinit var viewModel: ViewModel

    protected abstract fun getLayoutId(): Int
    protected abstract fun getViewModelClass(): Class<out ViewModel>


    protected fun getViewModel():ViewModel{
        return viewModel
    }
    protected fun getBinding():ViewDataBinding{
        return binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,getLayoutId())
        viewModel = ViewModelProvider(this).get(getViewModelClass())
    }

    fun setStatusBarColor(color: Int){
    val window = window
   /*     window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
   */     window.statusBarColor = ContextCompat.getColor(this, if(color == 0) R.color.colorPrimary else color)
        window.navigationBarColor= ContextCompat.getColor(this, if(color == 0) R.color.colorPrimary else color)
    }
}