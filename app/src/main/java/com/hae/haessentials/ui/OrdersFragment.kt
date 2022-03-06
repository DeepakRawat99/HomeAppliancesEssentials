package com.hae.haessentials.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.hae.haessentials.R
import com.hae.haessentials.adapters.ItemListAdapter
import com.hae.haessentials.base.BaseFragment
import com.hae.haessentials.databinding.HomeFragmentBinding
import com.hae.haessentials.databinding.OrdersFragmentBinding
import com.hae.haessentials.utility.ITEM_JSON
import org.json.JSONObject

class OrdersFragment : BaseFragment() {

    companion object {
        fun newInstance() = OrdersFragment()
    }

    private lateinit var viewModel: OrdersViewModel
    private lateinit var binding: OrdersFragmentBinding
    private lateinit var adapter: ItemListAdapter
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
        initialize()
    }

    private fun initialize() {
        binding = getBinding() as OrdersFragmentBinding
        viewModel = getViewModel() as OrdersViewModel
        binding.viewModel = viewModel

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
        binding.rvOrderList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrderList.adapter = adapter
        adapter.notifyDataSetChanged()
    }*/


}
}