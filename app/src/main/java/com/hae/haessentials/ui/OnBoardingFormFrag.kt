package com.hae.haessentials.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hae.haessentials.R
import com.hae.haessentials.backend.ItemList
import com.hae.haessentials.backend.UserData
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.OnBoardingFormFragmentBinding
import com.hae.haessentials.utility.*

class OnBoardingFormFrag : BaseFragment() {

    companion object {
        fun newInstance() = OnBoardingFormFrag()
    }

    private lateinit var viewModel: OnBoardingFormViewModel
    private lateinit var binding:OnBoardingFormFragmentBinding
    private var userDetail = UserData()
    override fun getLayoutId(): Int {
        return R.layout.on_boarding_form_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return OnBoardingFormViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return  super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!CheckNumberNull(UserSharedPref.getUserUniqueId())){
            showToast("Authentication Error",this.requireActivity())
            logout()
            return
        }
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as OnBoardingFormFragmentBinding
        viewModel = getViewModel() as OnBoardingFormViewModel
        binding.viewModel = viewModel

        //initializing observer's
        viewModel.onClickValue.observe(this, Observer {
            if(it==1){
                //not using on text change listener for getting et values because api's
            // will return value and user might not change them will through error
                val firstName= binding.obffFirstnameet.text.toString()
                val lastName= binding.obffLastnameet.text.toString()
                val building= binding.obffHouseet.text.toString()
                val area= binding.obffLocalityet.text.toString()
                val landmark= binding.obffLandmarket.text.toString()
                val pincode= binding.obffPincodeet.text.toString()
                if(!validText(firstName.trim())) binding.obfftilFirstname.error = getString(R.string.valid_firstname)
                else if(!validText(lastName.trim())) binding.obfftilLastname.error = getString(R.string.valid_lastname)
                else if(!validText(building.trim())) binding.obfftilHouse.error = getString(R.string.valid_building)
                else if(!validText(area.trim())) binding.obfftilLocality.error = getString(R.string.valid_locality)
                else if(!validPinCode(pincode.trim())) binding.obfftilPincode.error = getString(R.string.valid_pincode)
                else if(!servicePinCode(pincode.trim())) binding.obfftilPincode.error = getString(R.string.serviceable_text)

                else {
                    //if everything seems to be fine move to next fragl
                    UserSharedPref.setFirstName(firstName)
                    userDetail.fullName = "$firstName $lastName"
                    userDetail.mobileNumber = UserSharedPref.getMobileNumber()
                    userDetail.fullAddress = "$building, $area, ${checkNullString(landmark.trim())}, $pincode"
                    userDetail.pinCode = pincode
                    saveDataToDB()
                }
            }
        })

        viewModel.onErrorListener.observe(this, Observer {
            binding.obfftilFirstname.error = null
            binding.obfftilLastname.error = null
            binding.obfftilHouse.error = null
            binding.obfftilLocality.error = null
            binding.obfftilPincode.error = null
        })
    }

    private fun saveDataToDB() {
        val database = Firebase.database(FirebaseDataLinker.FIREBASE_DB)
        val user = database.reference
        val userDetails = user.child(FirebaseDataLinker.USER_DETAIL).child(UserSharedPref.getUserUniqueId().toString())

        /*
        userDetails.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //val value = snapshot.getValue<Any>()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebaseError", "Failed to read value.", error.toException())
            }
        })*/

        //adding cart item
        userDetails.setValue(userDetail)
        handleNavigation()
    }

    override fun onResume() {
        super.onResume()
        showBottomBar(false)
    }

}