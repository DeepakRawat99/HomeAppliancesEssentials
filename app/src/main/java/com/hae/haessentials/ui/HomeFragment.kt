package com.hae.haessentials.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hae.haessentials.R
import com.hae.haessentials.adapters.ItemListAdapter
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.HomeFragmentBinding
import com.hae.haessentials.utility.FirebaseDataLinker
import com.hae.haessentials.utility.FirebaseDataLinker.FIREBASE_DB_URL
import com.hae.haessentials.utility.FirebaseDataLinker.USER_CART
import com.hae.haessentials.utility.ITEM_JSON
import com.hae.haessentials.utility.UserSharedPref
import org.json.JSONObject

class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: ItemListAdapter
    private lateinit var cartItems: HashMap<String, String>
    override fun getLayoutId(): Int {
      return R.layout.home_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
       return HomeViewModel::class.java
    }

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
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as HomeFragmentBinding
        viewModel = getViewModel() as HomeViewModel
        binding.viewModel = viewModel
        showBottomBar(true)
        binding.hfUsername.text = getString(R.string.hello_user, UserSharedPref.getFirstName())

        val item_json = JSONObject(ITEM_JSON)
        val items = item_json.getJSONArray("items")

       adapter = ItemListAdapter(
            object :ItemListAdapter.ItemAdapterListener{
                override fun onClick(position: Int) {
                    /*showToast("good", requireContext())*/
                 val database = Firebase.database(FirebaseDataLinker.getString(FIREBASE_DB_URL))
                    val cart = database.getReference(USER_CART)
                    val userCart = cart.child(UserSharedPref.getMobileNumber().toString())
                    userCart.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val value = snapshot.getValue<Any>()


                           // Log.d("firebase value", "Value is: $value")
                        }

                        override fun onCancelled(error: DatabaseError) {
                            //Log.w("firebaseError", "Failed to read value.", error.toException())
                        }

                    })
                }
                override fun onCountClick(position: Int) {
                    showToast("good", requireContext())
                }
                /*
                *    phonenum=phoneSharedP.getMob();
            dbref=fid.getReference("matches");
            String un_key=dbref.push().getKey();
            final DatabaseReference matchref=dbref.child(un_key);
            match.put("creator",phonenum);
            match.put("date",currentDateString);
            match.put("time",t);
            match.put("type",matchtype);
            match.put("ground",groundname);
            match.put("tname",tourname);
            match.put("players",players_in_team);
                match.put("status","not finished");

                dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //check if match is already created

                for (DataSnapshot snapshot1:snapshot.getChildren()){
                       if(phoneSharedP.getMob().equals(snapshot1.child("creator").getValue())
                       && snapshot1.child("status").getValue().equals("not finished")){
                           temporarycount++;
                            break;
                       }

                * */
            },
            items
        )
        binding.rvItemList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemList.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}