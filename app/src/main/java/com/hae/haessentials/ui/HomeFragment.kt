package com.hae.haessentials.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hae.haessentials.R
import com.hae.haessentials.adapters.ItemListAdapter
import com.hae.haessentials.backend.ItemList
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.HomeFragmentBinding
import com.hae.haessentials.databinding.ItemCountBottomSheetBinding
import com.hae.haessentials.utility.CheckNumberNull
import com.hae.haessentials.utility.FirebaseDataLinker
import com.hae.haessentials.utility.FirebaseDataLinker.USER_CART
import com.hae.haessentials.utility.ITEM_JSON
import com.hae.haessentials.utility.UserSharedPref
import org.json.JSONArray
import org.json.JSONObject
import java.text.FieldPosition

class HomeFragment : BaseFragment() {

    companion object;

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var adapter: ItemListAdapter
    private var cartItemsList = ArrayList<ItemList>()
    private var arrayList = ArrayList<ItemList>()
    private var cartItems = ItemList()
    private var itemCount = "1"
    private var isItemAdded: Boolean = false //item added to list or not
    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return HomeViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory? {
        return null
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
        binding = getBinding() as HomeFragmentBinding
        viewModel = getViewModel() as HomeViewModel
        binding.viewModel = viewModel
        showBottomBar(true)
        binding.hfUsername.text = getString(R.string.hello_user, UserSharedPref.getFirstName())


        val itemJson = FirebaseDataLinker.getString(FirebaseDataLinker.ITEM_DETAILS)
        val items: JSONArray = if(itemJson.isEmpty())
            JSONObject(ITEM_JSON).getJSONArray("items")
        else
            JSONObject(itemJson).getJSONArray("items")

        for(i in 0 until items.length()){
            //val itemMap = HashMap<String,String>()
            //cartItems.itemId = items.getJSONObject(i).getString("item_id")
            val cartItem = ItemList()
            cartItem.itemId = items.getJSONObject(i).getString("item_id")
            cartItem.itemName = items.getJSONObject(i).getString("item_name")
            cartItem.itemDesc = items.getJSONObject(i).getString("item_desc")
            cartItem.itemPrice = items.getJSONObject(i).getString("item_price")
            cartItem.itemImage = items.getJSONObject(i).getString("item_image")
            cartItem.itemCount = 1
            arrayList.add(cartItem)
           // cartItemMap = cartItemMap + listOf(cartItems)
        }
        adapter = ItemListAdapter(
            object :ItemListAdapter.ItemAdapterListener{
                override fun onClick(position: Int, totalItem:String) {
                    /*showToast("good", requireContext())*/
                    val database = Firebase.database(FirebaseDataLinker.FIREBASE_DB).reference
                    val cart = database.child(USER_CART)
                    val userCart = cart.child(UserSharedPref.getUserUniqueId().toString())
                    cartItems.itemId = arrayList[position].itemId
                    cartItems.itemName = arrayList[position].itemName
                    cartItems.itemDesc = arrayList[position].itemDesc
                    cartItems.itemPrice = arrayList[position].itemPrice
                    cartItems.itemImage = arrayList[position].itemImage
                    cartItems.itemCount = Integer.parseInt(totalItem)//items.getJSONObject(position).getString("item_ID")


                    //gets the items in the cart
                    cart.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            //val value = snapshot.getValue<Any>()
                            if(snapshot.exists() && snapshot.hasChild(UserSharedPref.getUserUniqueId().toString())){
                                cartItemsList = snapshot.child(UserSharedPref.getUserUniqueId().toString()).getValue<ArrayList<ItemList>>()!!

                                for(i in 0 until cartItemsList.size){
                                    if(cartItemsList[i].itemId!! == cartItems.itemId){
                                        isItemAdded = true
                                        break
                                    }
                                }


                            }
                            //adding cart
                            if(!isItemAdded){
                                cartItemsList.add(cartItems)
                                userCart.setValue(cartItemsList)
                                showToast("Item Added to Cart", requireActivity())
                                cartItemsList = arrayListOf()
                                cart.removeEventListener(this)
                            }else{
                                showToast("Item Already in Cart", requireActivity())
                                isItemAdded = false
                                cart.removeEventListener(this)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.e("firebaseError", "Failed to read value.", error.toException())
                        }
                    })


                }
                override fun onCountClick(position: Int) {
                    showItemCountBottomSheet(position)
                   }

            },
            arrayList
        )
        binding.rvItemList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItemList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showItemCountBottomSheet(position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this.requireActivity(), R.style.bottomSheetTheme)
        val countBinding: ItemCountBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.requireActivity()),
            R.layout.item_count_bottom_sheet,null,false)
            countBinding.icbsCta.setOnClickListener {

                itemCount = countBinding.icbsItemCount.text.toString().trim()
                if(itemCount.isEmpty() || itemCount == "0"){
                showToast("Enter valid number", this.requireContext())
                }
                else{
                arrayList[position].itemCount = Integer.parseInt(itemCount)
                adapter.notifyItemChanged(position)
                bottomSheetDialog.dismiss()
                }
            }
            bottomSheetDialog.setContentView(countBinding.root)
            if(isAdded)
            bottomSheetDialog.show()
    }
}