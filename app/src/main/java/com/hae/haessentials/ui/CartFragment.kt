package com.hae.haessentials.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hae.haessentials.R
import com.hae.haessentials.adapters.CartListAdapter
import com.hae.haessentials.adapters.ItemListAdapter
import com.hae.haessentials.backend.ItemList
import com.hae.haessentials.backend.OrderDataResult
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.ConfirmDialogBinding
import com.hae.haessentials.databinding.FragmentCartBinding
import com.hae.haessentials.databinding.HomeFragmentBinding
import com.hae.haessentials.databinding.ItemCountBottomSheetBinding
import com.hae.haessentials.utility.*
import org.json.JSONObject
import java.text.DateFormat
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CartFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: CartListAdapter
    private lateinit var cart:DatabaseReference
    private var orderList = OrderDataResult()
    private lateinit var order : DatabaseReference
    private var cartItemList = ArrayList<ItemList>()
    override fun getLayoutId(): Int {
        return R.layout.fragment_cart
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
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        initialize()
        initializeRecyclerView()

    }

    private fun initializeRecyclerView() {

        cart = Firebase.database(FirebaseDataLinker.FIREBASE_DB).reference
        .child(FirebaseDataLinker.USER_CART)

        //gets the items in the cart

        cart.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //val value = snapshot.getValue<Any>()
                if(snapshot.exists() && snapshot.hasChild(UserSharedPref.getUserUniqueId().toString())){
                    cartItemList = snapshot.child(UserSharedPref.getUserUniqueId().toString()).getValue<ArrayList<ItemList>>()!!

                }
                cartListEmptyCheck()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("firebaseError", "Failed to read value.", error.toException())
            }
        })

    }

    private fun cartListEmptyCheck() {

        if(cartItemList.isEmpty()){
            binding.fcEmpty.visibility = View.VISIBLE
            hideLoading()
        }
        else{
            populateRecyclerView()
            binding.fcEmpty.visibility = View.GONE
            binding.fcBuynow.visibility = View.VISIBLE
        }
    }

    private fun populateRecyclerView() {
        adapter = CartListAdapter(
            object :CartListAdapter.ItemAdapterListener{

                override fun onClick(position: Int, totalItem: String) {
                    cartItemList.removeAt(position)
                    cart.child(UserSharedPref.getUserUniqueId().toString()).setValue(cartItemList)
                    showToast("Item Removed from cart", requireActivity())
                }

                override fun onCountClick(position: Int) {
                showItemCountBottomSheet(position)
                }
            },
            cartItemList
        )
        binding.rvCartList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartList.adapter = adapter
        adapter.notifyDataSetChanged()
        hideLoading()
    }

    private fun initialize() {
        binding = getBinding() as FragmentCartBinding
        viewModel = getViewModel() as HomeViewModel
        binding.viewModel = viewModel
        showBottomBar(true)
        if(!CheckNumberNull(UserSharedPref.getUserUniqueId())){
            showToast("Authentication Error",this.requireActivity())
            logout()
            return
        }
        //placing Order
        binding.fcBuynow.setOnClickListener {
             order = Firebase.database(FirebaseDataLinker.FIREBASE_DB).reference
                .child(FirebaseDataLinker.USER_ORDERS)

            var amount:Int = 0
            for(i in 0 until cartItemList.size){
                amount += Integer.parseInt(cartItemList[i].itemPrice!!)
            }
            val orderId = order.push().key
                orderList.orderID = orderId
                orderList.orderedDate =  SimpleDateFormat( "dd-MMM-yyyy", Locale.getDefault()).format(Date())
                orderList.status = OrderStatus.ORDERED.value
                orderList.items = cartItemList
                orderList.totalAmount = amount
                orderList.genOrderId = 1


                order.child(UserSharedPref.getUserUniqueId().toString()).addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                orderList.genOrderId = snapshot.childrenCount + 1
                                order.removeEventListener(this)
                            }else
                                order.removeEventListener(this)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            order.child(UserSharedPref.getUserUniqueId().toString()).child(orderId.toString())
                .setValue(orderList)
            val dialog = DialogClass()
            dialog.showConfirmedDialog(activity!!)

            //cartItemList = arrayListOf()
            //cart.child(UserSharedPref.getUserUniqueId().toString()).setValue(cartItemList)
        }

    }

    private fun showItemCountBottomSheet(position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this.requireActivity(), R.style.bottomSheetTheme)
        val countBinding: ItemCountBottomSheetBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.requireActivity()),
            R.layout.item_count_bottom_sheet,null,false)
        countBinding.icbsCta.setOnClickListener {

            val userCart = cart.child(UserSharedPref.getUserUniqueId().toString())

            val itemCount = countBinding.icbsItemCount.text.toString()
            if(itemCount.isEmpty() || itemCount == "0" || Integer.parseInt(itemCount) > 5){
                showToast("Enter number between 0-5", this.requireContext())
            }
            else if(itemCount == "1"){
                bottomSheetDialog.dismiss()
            }else{
                cartItemList[position].itemCount = Integer.parseInt(itemCount)
                userCart.setValue(cartItemList)
                bottomSheetDialog.dismiss()
            }
        }
            bottomSheetDialog.setContentView(countBinding.root)
            if (isAdded)
                bottomSheetDialog.show()
        }



}