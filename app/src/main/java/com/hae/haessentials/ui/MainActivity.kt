package com.hae.haessentials.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseActivity
import com.hae.haessentials.databinding.ActivityMainBinding
import com.hae.haessentials.utility.CheckNumberNull
import com.hae.haessentials.utility.FirebaseDataLinker
import com.hae.haessentials.utility.FirebaseDataLinker.USER_DETAIL
import com.hae.haessentials.utility.UserSharedPref
import com.hae.haessentials.utility.checkNullString
import com.hae.haessentials.viewmodels.MainViewModel

class MainActivity : BaseActivity(), View.OnClickListener {
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
    private var userExist: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setStatusBarColor(R.color.white)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.main_frag_view) as NavHostFragment
        navController = navHostFragment.navController
        initViewModelAndBinding()
        binding.lbnHome.setOnClickListener(this)
        binding.lbnCart.setOnClickListener(this)
        binding.lbnOrders.setOnClickListener(this)
        binding.lbnUser.setOnClickListener(this)
        handleNavigation()
    }

     fun handleNavigation() {
        if(!UserSharedPref.isLoggedIn()){
            replaceFragment(R.id.login,null)
        }
        /*else if(!UserSharedPref.getFirstName().isNullOrEmpty()){
            userExist = true
            binding.viewHome.setBackgroundResource(R.drawable.bg_header_home)
            binding.btNavHome.setTextColor(ContextCompat.getColor(this@MainActivity,R.color.green_186049))
            replaceFragment(R.id.homeFragment,null)
        }
        else {
            val database = Firebase.database(FirebaseDataLinker.FIREBASE_DB).reference
            val users = database.child(USER_DETAIL).child(UserSharedPref.getUserUniqueId().toString())
            if(!CheckNumberNull(UserSharedPref.getUserUniqueId())){
                logout()
                return
            }
            users.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        //for(i in snapshot.children){
                            if(snapshot.exists()){
                                userExist = true
                                val name = snapshot.child("fullName").getValue<String>()!!.split(" ")
                                UserSharedPref.setFirstName(name[0])
                                binding.viewHome.setBackgroundResource(R.drawable.bg_header_home)
                                binding.btNavHome.setTextColor(ContextCompat.getColor(this@MainActivity,R.color.green_186049))
                                replaceFragment(R.id.homeFragment,null)
                             *//*   break
                            }*//*
                        }else*//*
                        if(!userExist)*//*{
                            replaceFragment(R.id.onBoardingFormFrag,null)
                        }
                    }else {
                        replaceFragment(R.id.onBoardingFormFrag,null)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w("firebaseerror", "Failed to read value.", error.toException())
                }

            })

        }*/
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

    when(navController.currentDestination?.id){
        R.id.login, R.id.onBoardingFormFrag,R.id.homeFragment,R.id.blankFragment ->{
            finish()
        }
        R.id.otpFragment ->{
            super.onBackPressed()
        }
        else->{
            binding.viewHome.setBackgroundResource(R.drawable.bg_header_home)
            binding.btNavHome.setTextColor(ContextCompat.getColor(this,R.color.green_186049))
            replaceFragment(R.id.homeFragment,null)
        }
    }
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
     fun showBottomBar(boolean: Boolean){
        binding.bottombar.visibility = if(boolean) View.VISIBLE else View.GONE
    }

    override fun onClick(p0: View?) {
        //setting other views top background null
        binding.viewHome.setBackgroundResource(0)
        binding.viewOrder.setBackgroundResource(0)
        binding.viewCart.setBackgroundResource(0)
        binding.viewUser.setBackgroundResource(0)
//setting other views text color to normal
        binding.btNavHome.setTextColor(ContextCompat.getColor(this,R.color.darker_grey_3E3D3D))
        binding.btNavOrder.setTextColor(ContextCompat.getColor(this,R.color.darker_grey_3E3D3D))
        binding.btNavCart.setTextColor(ContextCompat.getColor(this,R.color.darker_grey_3E3D3D))
        binding.btNavUser.setTextColor(ContextCompat.getColor(this,R.color.darker_grey_3E3D3D))
       // if(p0)
        when(p0?.id){
            R.id.lbn_home->{
                binding.viewHome.setBackgroundResource(R.drawable.bg_header_home)
                binding.btNavHome.setTextColor(ContextCompat.getColor(this,R.color.green_186049))
                replaceFragment(R.id.homeFragment,null)
            }
            R.id.lbn_orders->{
                binding.viewOrder.setBackgroundResource(R.drawable.bg_header_home)
                binding.btNavOrder.setTextColor(ContextCompat.getColor(this,R.color.green_186049))
                replaceFragment(R.id.ordersFragment,null)
            }
            R.id.lbn_cart->{
                binding.viewCart.setBackgroundResource(R.drawable.bg_header_home)
                binding.btNavCart.setTextColor(ContextCompat.getColor(this,R.color.green_186049))
                replaceFragment(R.id.cartFragment,null)
            }
            R.id.lbn_user->{
                binding.viewUser.setBackgroundResource(R.drawable.bg_header_home)
                binding.btNavUser.setTextColor(ContextCompat.getColor(this,R.color.green_186049))
                replaceFragment(R.id.accountFragment,null)
            }
        }
    }
    fun logout(){
        UserSharedPref.setMobile(null)
        UserSharedPref.setUserUniqueId(null)
        UserSharedPref.setFirstName(null)
        UserSharedPref.setLogin(false)
        handleNavigation()

    }


}