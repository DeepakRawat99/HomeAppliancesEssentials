package com.hae.haessentials.utility

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.hae.haessentials.R
import com.hae.haessentials.databinding.ConfirmDialogBinding

class DialogClass {
     fun showConfirmedDialog(activity: Activity) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val dialogBinding: ConfirmDialogBinding = DataBindingUtil.inflate(dialog.layoutInflater,R.layout.confirm_dialog,null,false)
         dialog.setContentView(dialogBinding.root)
         dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
         //dialog.window!!.
         dialog.window!!.setGravity(Gravity.CENTER)

         dialogBinding.confirmLayout.visibility = View.GONE
         dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        /*val confirmLayout =  dialog.findViewById<LinearLayout>(R.id.confirm_layout)
        val cdOk = dialog.findViewById<Button>(R.id.cd_ok)*/

        /*dialogBinding.cdOk.setOnClickListener {
            dialog.dismiss()
        }*/

            dialog.show()
    }

     fun showConfirmDialog(activity: Activity, listener: ConfirmClick,subtitle: String) {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
         val dialogBinding: ConfirmDialogBinding = DataBindingUtil.inflate(dialog.layoutInflater,R.layout.confirm_dialog,null,false)
         dialog.setContentView(dialogBinding.root)
         dialog.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            //dialog.window!!.
         dialog.window!!.setGravity(Gravity.CENTER)
         dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogBinding.nDialogSubtitle.text = subtitle
        dialogBinding.confirmedLayout.visibility = View.GONE
        dialogBinding.cdNo.setOnClickListener {
            listener.onNo()
            dialog.dismiss()
        }
        dialogBinding.cdYes.setOnClickListener {
            listener.onYes()
            dialog.dismiss()
        }

        dialog.show()
    }

    interface ConfirmClick{
        fun onNo()
        fun onYes()
    }

}