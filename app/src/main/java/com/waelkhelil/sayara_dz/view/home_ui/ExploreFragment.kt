package com.waelkhelil.sayara_dz.view.home_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.waelkhelil.sayara_dz.R
import com.waelkhelil.sayara_dz.database.model.AdResponse
import kotlinx.android.synthetic.main.fragment_explore.*


class ExploreFragment : Fragment() {

    companion object {
        fun newInstance() = ExploreFragment()
    }
    private  lateinit var viewModel:HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.getAlAds().observe(this, Observer<List<AdResponse>> {

            if (it.isNotEmpty()) {
                rv_listing_list.apply {adapter = ListingListItemAdapter(it)}
            }

        })

    }
}
