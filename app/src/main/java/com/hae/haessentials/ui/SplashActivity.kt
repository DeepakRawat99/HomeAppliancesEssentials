package com.hae.haessentials.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.hae.haessentials.R
import com.hae.haessentials.base.BaseActivity
import com.hae.haessentials.utility.UserSharedPref
import com.hae.haessentials.viewmodels.SplashViewModel


/*
* Created by Deepak Rawat on 09/01/2022
* launcher activity
* */
class SplashActivity : BaseActivity() {
    private var activityvisible = true
    private lateinit var textview: TextView
    private lateinit var textview1: TextView
    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return SplashViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        UserSharedPref.init(applicationContext)
       // setStatusBarColor(R.color.darker_grey_3E3D3D)
            textview = findViewById(R.id.splash_ha)
            textview1 = findViewById(R.id.splash_essentials)
            goToNextActivity()
    }

    private fun goToNextActivity() {
        val fadeanim = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        textview.startAnimation(fadeanim)
        textview1.startAnimation(fadeanim)
        fadeanim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                fadeanim.fillAfter
                handleNavigation()
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
    }

    private fun handleNavigation() {
           val intent = Intent(this, MainActivity::class.java)
           startActivity(intent)
           finish()
    }

    override fun onResume() {
        super.onResume()
        activityvisible = true
    }

    //when user leaves
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        textview.animate().cancel()
        textview1.animate().cancel()
        activityvisible = false
    }

}