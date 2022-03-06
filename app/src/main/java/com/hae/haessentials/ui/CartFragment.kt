package com.hae.haessentials.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hae.haessentials.R
import com.hae.haessentials.adapters.ItemListAdapter
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.FragmentCartBinding
import com.hae.haessentials.databinding.HomeFragmentBinding
import com.hae.haessentials.utility.ITEM_JSON
import com.hae.haessentials.utility.UserSharedPref
import org.json.JSONObject

class CartFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentCartBinding
    private lateinit var adapter: ItemListAdapter
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
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as FragmentCartBinding
        viewModel = getViewModel() as HomeViewModel
        binding.viewModel = viewModel
        showBottomBar(true)

        val item_json = JSONObject(ITEM_JSON)
        val items = item_json.getJSONArray("items")

       /* adapter = ItemListAdapter(
            object :ItemListAdapter.ItemAdapterListener{
                override fun onClick(position: Int) {
                    showToast("good", requireContext())
                }
            },
            items,
            R.layout.order_layout
        )
        binding.rvCartList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartList.adapter = adapter
        adapter.notifyDataSetChanged()*/
    }

}