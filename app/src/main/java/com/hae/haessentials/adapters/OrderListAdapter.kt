package com.hae.haessentials.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hae.haessentials.R
import com.hae.haessentials.backend.OrderDataResult
import com.hae.haessentials.ui.OrderStatus
import com.hae.haessentials.ui.OrderStatus.OUT_FOR_DELIVERY

class OrderListAdapter (val listener: ItemAdapterListener,
private val orders: ArrayList<OrderDataResult>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.order_item_layout, parent,false
        )
        context = parent.context
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is OrderViewHolder){
            holder.orderId.text = orders[position].orderID
            holder.totalPrice.text = context.getString(R.string.price,orders[position].totalAmount.toString())
            holder.orderId.text = context.getString(R.string.order_id,orders[position].orderID)

            holder.date.text = context.getString(R.string.order_date,orders[position].orderedDate)
            val childItemAdapter = ChildItemAdapter(orders[position].items!!)
            holder.orderList.layoutManager = LinearLayoutManager(context)
            holder.orderList.adapter = childItemAdapter

            when(orders[position].status){
                OUT_FOR_DELIVERY.value, OrderStatus.DELIVERED.value -> {
                    holder.cancelBtn.visibility = View.GONE
                    holder.status.text = context.getString(R.string.order_status,orders[position].status)
                }
                OrderStatus.CANCELLED.value ->{
                    holder.cancelBtn.visibility = View.GONE
                    holder.status.text = context.getString(R.string.cancelled)
                }
            }
            holder.cancelBtn.setOnClickListener {
                listener.onClick(position)
            }

        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }
    interface ItemAdapterListener{
        fun onClick(position: Int)
    }
}

class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val orderId: TextView = view.findViewById(R.id.oil_oid)
    val date: TextView = view.findViewById(R.id.oil_date)
    val totalPrice: TextView = view.findViewById(R.id.oil_price)
    val cancelBtn: Button = view.findViewById(R.id.oil_cancel)
    val status: TextView = view.findViewById(R.id.oil_status)
    var orderList: RecyclerView = view.findViewById(R.id.oil_order_list)
}
