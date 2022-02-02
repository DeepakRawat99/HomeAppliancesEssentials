package com.hae.haessentials.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseApp
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseActivity
import com.hae.haessentials.databinding.ActivityMainBinding
import com.hae.haessentials.utility.UserSharedPref
import com.hae.haessentials.viewmodels.MainViewModel

class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return MainViewModel::class.java
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setStatusBarColor(R.color.white)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_frag_view) as NavHostFragment
        navController = navHostFragment.navController
        initViewModelAndBinding()
        handleNavigation()
    }

    private fun handleNavigation() {
        if(!UserSharedPref.isLoggedIn()){
            replaceFragment(R.id.login,null)
        }
    }

    private fun initViewModelAndBinding() {
        binding = getBinding() as ActivityMainBinding
        viewModel = getViewModel() as MainViewModel
        FirebaseApp.initializeApp(this)
    }

    fun replaceFragment(fragmentLayout: Int, data:Bundle?) {
        navController.navigate(fragmentLayout,data)
    }

//on back pressed method to close app from fragments from where going back is not needed
    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size>0){
            val index= supportFragmentManager.fragments.size
            if(
                supportFragmentManager.fragments[index] is BlankFragment ||
                supportFragmentManager.fragments[index] is OnBoardingFormFrag ||
                supportFragmentManager.fragments[index] is HomeFragment
                    ){
                finish()
                return
            }else if(supportFragmentManager.fragments[index] is Login && Login.isOTPScreenShown){
                //condition added when user will press back from otp screen
                    Login.isOTPScreenShown = false
                    replaceFragment(R.id.login, null)
            }else
                super.onBackPressed()
        }
        else if (supportFragmentManager.fragments.size==0) {
            finish()
            return
        }
        super.onBackPressed()
    }
    fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.mainFragView.visibility = View.GONE
    }
    fun hideLoading(){
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            binding.progressBar.visibility = View.GONE
            binding.mainFragView.visibility = View.VISIBLE
        },500)

    }
}