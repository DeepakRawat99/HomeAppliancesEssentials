package com.hae.haessentials.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hae.haessentials.R
import com.hae.haessentials.backend.ItemList

class CartListAdapter(
    val listener: ItemAdapterListener,
    private val item: ArrayList<ItemList>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_bottom_navbar, parent,false
        )
        context = parent.context
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is CartViewHolder){

            holder.itemName.text = item[position].itemName
            holder.itemDesc.text =  item[position].itemDesc
            holder.itemPrice.text = context.getString(R.string.price, item[position].itemPrice)
            holder.itemMrp.text = context.getString(R.string.price_mrp,item[position].itemMrp)
            holder.itemMrp.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemDisc.text = context.getString(R.string.price_disc,item[position].itemDisc)
            val context: Context = holder.itemImage.context
            val id: Int = context.resources
                .getIdentifier(item[position].itemImage, "drawable", context.packageName)
            holder.delFromCart.visibility = View.VISIBLE
            holder.itemImage.setImageResource(id)

            holder.itemCount.text = item[position].itemCount.toString()
            holder.delFromCart.setOnClickListener {
                notifyDataSetChanged()
                    listener.onClick(position, holder.itemCount.text.toString())
                }
            holder.itemCount.setOnClickListener{
                   listener.onCountClick(position)
                }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }
    interface ItemAdapterListener{
        fun onClick(position: Int, totalItem: String)
        fun onCountClick(position: Int)
    }
}

class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val itemName: TextView = view.findViewById(R.id.item_name)
    val itemImage: ImageView = view.findViewById(R.id.item_img)
    val itemPrice: TextView = view.findViewById(R.id.item_price)
    val delFromCart: Button = view.findViewById(R.id.il_delete_From_cart)
    var itemCount: TextView = view.findViewById(R.id.item_count)
    val itemDesc: TextView = view.findViewById(R.id.item_desc)
    val itemMrp: TextView = view.findViewById(R.id.item_mrp)
    val itemDisc: TextView = view.findViewById(R.id.item_disc)

}
