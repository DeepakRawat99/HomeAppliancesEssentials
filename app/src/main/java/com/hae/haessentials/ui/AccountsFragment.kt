package com.hae.haessentials.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hae.haessentials.R
import com.hae.haessentials.backend.UserData
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.UserAccountFragmentBinding
import com.hae.haessentials.utility.*
import com.hae.haessentials.viewmodels.AccountsViewModel

class AccountsFragment: BaseFragment() {
    override fun getLayoutId(): Int {
       return R.layout.user_account_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return AccountsViewModel::class.java
    }
    private lateinit var viewModel: AccountsViewModel
    private lateinit var binding: UserAccountFragmentBinding
    private lateinit var user: DatabaseReference
    private lateinit var userDetails: DatabaseReference
    private var userData= UserData()

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as UserAccountFragmentBinding
        viewModel = getViewModel() as AccountsViewModel
        user = Firebase.database(FirebaseDataLinker.FIREBASE_DB).reference
            .child(FirebaseDataLinker.USER_DETAIL)
        if(!CheckNumberNull(UserSharedPref.getUserUniqueId())){
            showToast("Authentication Error",this.requireActivity())
            logout()
            return
        }
        userDetails = user.child(UserSharedPref.getUserUniqueId().toString())

        userDetails.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    userData = snapshot.getValue<UserData>()!!
                    updateUserData()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebaseError", "Failed to read value.", error.toException())
            }

        })
        binding.uafEmailConnect.setOnClickListener {
        openEmail(binding.uafEmailConnect.text.toString(), this.requireContext())
        }
        binding.uafMobileConnect.setOnClickListener {
            openPhone(binding.uafMobileConnect.text.toString(),this.requireContext())
        }
        binding.uafMobConnect.setOnClickListener {
            openPhone(binding.uafMobConnect.text.toString(),this.requireContext())
        }
        binding.logout.setOnClickListener {
            val dialog = DialogClass()
            dialog.showConfirmDialog(activity!!,object : DialogClass.ConfirmClick{
                override fun onNo() {

                }

                override fun onYes() {
                    logout()
                }

            },getString(R.string.logout_confirm))

        }
    }

    private fun updateUserData() {
        binding.uafNameMob.text = getString(R.string.name,userData.fullName,userData.mobileNumber,"Delhi, India")
        binding.uafAddress.text = userData.fullAddress
        hideLoading()
    }
}