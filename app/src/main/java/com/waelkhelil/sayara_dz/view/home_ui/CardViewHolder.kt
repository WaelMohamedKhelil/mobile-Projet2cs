package com.waelkhelil.sayara_dz.view.home_ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.waelkhelil.sayara_dz.model.Brand
import com.waelkhelil.sayara_dz.R
import com.waelkhelil.sayara_dz.view.MainActivity
import com.waelkhelil.sayara_dz.view.models_ui.ModelFragment

class CardViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.horizontal_list_item, parent, false)), View.OnClickListener  {
    //TODO : convert this class to a generic class
    private var mBrand: Brand? = null
    private var mTitleView: TextView? = null


    init {
        mTitleView = itemView.findViewById(R.id.text_card_header)
        this.itemView.setOnClickListener(this)
    }

    fun bind(brand: Brand) {
        mTitleView?.text = brand.name
        mBrand = brand
    }

    override fun onClick(view: View?) {
        Log.d(toString(), "onClick")

        val fragmentContainer = view?.findViewById<View>(R.id.nav_main_host_fragment)
        val navController = fragmentContainer?.let { Navigation.findNavController(it)}
        mBrand?.id?.let {
            navController?.navigate(R.id.action_home_fragment_to_modelFragment)
        }

    }
}