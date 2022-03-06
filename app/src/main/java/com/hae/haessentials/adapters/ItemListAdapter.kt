package com.hae.haessentials.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hae.haessentials.R
import org.json.JSONArray

class ItemListAdapter(
    val listener: ItemAdapterListener,
    val item: JSONArray
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_bottom_navbar, parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is ViewHolder){
            holder.itemName.text = item.getJSONObject(position).getString("item_name")
            holder.itemPrice.text = item.getJSONObject(position).getString("item_price")
            holder.itemImage.setImageResource(R.drawable.ic_hae)
            holder.addToCart.setOnClickListener {
                notifyDataSetChanged()
                listener.onClick(position)
            }
            holder.itemCount.setOnClickListener{
                listener.onCountClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return item.length()
    }
    interface ItemAdapterListener{
        fun onClick(position: Int)
        fun onCountClick(position: Int)
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
val itemName: TextView = view.findViewById(R.id.item_name)
    val itemImage: ImageView = view.findViewById(R.id.item_img)
    val itemPrice: TextView = view.findViewById(R.id.item_price)
    val addToCart: Button = view.findViewById(R.id.il_add_to_cart)
    var itemCount: TextView = view.findViewById(R.id.item_count)
}
