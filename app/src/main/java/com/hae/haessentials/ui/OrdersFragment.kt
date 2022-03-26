package com.hae.haessentials.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hae.haessentials.R
import com.hae.haessentials.adapters.OrderListAdapter
import com.hae.haessentials.backend.ItemList
import com.hae.haessentials.backend.OrderDataResult
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.ConfirmDialogBinding
import com.hae.haessentials.databinding.OrdersFragmentBinding
import com.hae.haessentials.utility.CheckNumberNull
import com.hae.haessentials.utility.DialogClass
import com.hae.haessentials.utility.FirebaseDataLinker
import com.hae.haessentials.utility.UserSharedPref

class OrdersFragment : BaseFragment() {

    companion object {
        fun newInstance() = OrdersFragment()
    }

    private lateinit var viewModel: OrdersViewModel
    private lateinit var binding: OrdersFragmentBinding
    private lateinit var adapter: OrderListAdapter
    private var orderItemsList = ArrayList<ItemList>()
    private var orderList = OrderDataResult()
    private var orders = ArrayList<OrderDataResult>()
    private var cartItems = ItemList()
    private lateinit var userOrders: DatabaseReference
    private lateinit var orderDetails:DatabaseReference

    override fun getLayoutId(): Int {
        return R.layout.orders_fragment
    }

    override fun getViewModelClass(): Class<out ViewModel> {
        return OrdersViewModel::class.java
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
        if(!CheckNumberNull(UserSharedPref.getUserUniqueId())){
            showToast("Authentication Error",this.requireActivity())
            logout()
            return
        }
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as OrdersFragmentBinding
        viewModel = getViewModel() as OrdersViewModel
        binding.viewModel = viewModel
        showLoading()
        getOrders()
    }

    private fun getOrders() {

        orderDetails = Firebase.database(FirebaseDataLinker.FIREBASE_DB).reference
                        .child(FirebaseDataLinker.USER_ORDERS)
        userOrders = orderDetails.child(UserSharedPref.getUserUniqueId().toString())


        userOrders.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //setting list empty before call
                orders = arrayListOf()
                if(snapshot.exists()/* && snapshot.hasChild(UserSharedPref.getUserUniqueId().toString()*/){
                    for(dataKey in snapshot.children ){
                        val key = dataKey.key!!
                        orders.add(snapshot.child(key).getValue<OrderDataResult>()!!)
                    }
                }
                orderListEmptyCheck()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("firebaseError", "Failed to read value.", error.toException())
            }

        })
    }

    private fun populateRecyclerView(){
        adapter = OrderListAdapter(
            object :OrderListAdapter.ItemAdapterListener{

                override fun onClick(position: Int) {
                    val key = orders[position].orderID

                    val dialog = DialogClass()
                    dialog.showConfirmDialog(activity!!,object :DialogClass.ConfirmClick{
                        override fun onNo() {
                        }

                        override fun onYes() {
                            userOrders.child(key!!).child("status").setValue(OrderStatus.CANCELLED.value)
                            showToast("Order Cancelled", activity!!)
                        }

                    },getString(R.string.remove_items))

                }
            },
            orders
        )
        binding.rvOrderList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrderList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    /*private fun showConfirmDialog(key: String) {
        val dialog = Dialog(this.requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.confirm_dialog)

        //dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val subtitle = dialog.findViewById<TextView>(R.id.n_dialog_subtitle)
        val layout =  dialog.findViewById<LinearLayout>(R.id.confirmed_layout)
        val cdNo = dialog.findViewById<Button>(R.id.cd_no)
        val cdYes = dialog.findViewById<Button>(R.id.cd_yes)
        subtitle.text = getString(R.string.remove_items)
        layout.visibility = View.GONE
        cdNo.setOnClickListener {
            dialog.dismiss()
        }
        cdYes.setOnClickListener {
            userOrders.child(key).child("status").setValue(OrderStatus.CANCELLED.value)
            showToast("Order Cancelled", this.requireActivity())
            dialog.dismiss()
        }

        if(isAdded)
        dialog.show()
    }*/

    private fun orderListEmptyCheck() {

        if(orders.isEmpty()){
            binding.ofEmpty.visibility = View.VISIBLE
            hideLoading()
        }
        else{
            binding.ofEmpty.visibility = View.GONE
            populateRecyclerView()
        }
    }

}