package com.hae.haessentials.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hae.haessentials.ui.MainActivity
import androidx.core.content.ContextCompat.getSystemService
import com.hae.haessentials.utility.UserSharedPref


abstract class BaseFragment: Fragment() {
    private lateinit var binding: ViewDataBinding
    private lateinit var viewModel: ViewModel
    private var mainActivity:MainActivity? = null

    protected abstract fun getLayoutId(): Int
    protected abstract fun getViewModelClass(): Class<out ViewModel>
    protected abstract fun getViewModelFactory(): ViewModelProvider.Factory?


    protected fun getViewModel(): ViewModel {
        return viewModel
    }
    protected fun getBinding(): ViewDataBinding {
        return binding
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false)
        viewModel = getViewModelFactory()?.let {
            ViewModelProvider(this,
                it
            ).get(getViewModelClass())
        } ?:run {
            ViewModelProvider(this)[getViewModelClass()]
        }
        return binding.root
    }

//attaching activity context
    override fun onAttach(context: Context) {
        super.onAttach(context)

        when(context){
            is MainActivity-> mainActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    protected fun replaceFragment(fragment: Int,data:Bundle?){
        mainActivity?.replaceFragment(fragment,data)
    }
    protected fun showLoading(){
        mainActivity?.showLoading()
    }
    protected fun hideLoading(){
        mainActivity?.hideLoading()
    }
    protected fun showToast(message:String, context: Context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
    protected fun hideKeyboard(activity: Activity){
        val imm: InputMethodManager? = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
    protected fun showBottomBar(boolean: Boolean){
        mainActivity?.showBottomBar(boolean)
    }

    protected fun handleNavigation(){
        mainActivity?.handleNavigation()
    }

    protected fun logout(){
      mainActivity?.logout()
    }

    override fun onResume() {
        super.onResume()
        mainActivity?.hideLoading()
    }
}