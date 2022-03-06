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
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseActivity
import com.hae.haessentials.databinding.ActivityMainBinding
import com.hae.haessentials.utility.FirebaseDataLinker
import com.hae.haessentials.utility.UserSharedPref
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

    private fun handleNavigation() {
        if(!UserSharedPref.isLoggedIn()){
            replaceFragment(R.id.login,null)
        }
        else if(UserSharedPref.getFirstName().isNullOrEmpty()){
            replaceFragment(R.id.onBoardingFormFrag,null)
        }
        else {
           /* val databse = Firebase.database("https://ha-essentials-default-rtdb.asia-southeast1.firebasedatabase.app")
            val pincodes = databse.getReference("pincodes")
            pincodes.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue<Any>()
                    Log.d("firebase value", "Value is: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("firebaseerror", "Failed to read value.", error.toException())
                }

            })*/
            binding.viewHome.setBackgroundResource(R.drawable.bg_header_home)
            binding.btNavHome.setTextColor(ContextCompat.getColor(this,R.color.green_186049))
            replaceFragment(R.id.homeFragment,null)
            /*Log.d("-----Firebase", FirebaseDataLinker.getString(FirebaseDataLinker.ENCRYPTION_KEY))
            Toast.makeText(this, UserSharedPref.getMobileNumber().toString(),Toast.LENGTH_SHORT).show()*/
            //s
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
                supportFragmentManager.fragments[index] is HomeFragment ||
                supportFragmentManager.fragments[index] is Login
                    ){
                finish()
                return
            }else super.onBackPressed()
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
            }
        }
    }


}