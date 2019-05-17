package com.waelkhelil.sayara_dz.view.model_ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ramotion.cardslider.CardSliderLayoutManager
import com.ramotion.cardslider.CardSnapHelper
import com.waelkhelil.sayara_dz.R
import com.waelkhelil.sayara_dz.database.model.Version
import com.waelkhelil.sayara_dz.view.model_ui.cards.SliderAdapter


class ModelVersionsFragment : Fragment() {

    companion object {
        fun newInstance() = ModelVersionsFragment()
    }


    private val versionsNames = arrayOf("Zen", "Intens", "RS Line")
    private val prices = arrayOf("2 300 K", "2 700 K", "3 200 K")
    private val list:List<Version> = listOf(
        Version(
            0,
            "Zen",
            "https://www.cdn.renault.com/content/dam/Renault/FR/personal-cars/clio/CLIO%20V/PackshotsVersions/New_Clio_Zen_Gris_Titanium.jpeg.ximg.l_12_m.smart.jpeg"
        ),
        Version(
            0,
            "Intens",
            "https://www.cdn.renault.com/content/dam/Renault/FR/personal-cars/clio/CLIO%20V/PackshotsVersions/New_Clio_Intens_Orange_Valencia_Jantes.jpg.ximg.l_12_m.smart.jpg"
        ),
        Version(
            0,
            "RS Line",
            "https://www.cdn.renault.com/content/dam/Renault/FR/personal-cars/clio/CLIO%20V/PackshotsVersions/New_Clio_RS_Line_Bleu_Iron.jpeg.ximg.l_12_m.smart.jpeg"
        )
    )
    private val sliderAdapter = SliderAdapter(list,  OnCardClickListener())

    private var layoutManager: CardSliderLayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var priceSwitcher: TextSwitcher? = null

    private var version1TextView: TextView? = null
    private var version2TextView: TextView? = null
    private var versionOffset1: Float = 0F
    private var versionOffset2: Float = 0F
    private var versionAnimDuration: Long = 0
    private var currentPosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_model_versions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = context?.let { CardSliderLayoutManager(it) }


        initRecyclerView()
        initVersionText()
        initSwitchers()
    }
    private fun initRecyclerView() {

        recyclerView = view?.findViewById(R.id.recycler_view_versions) as RecyclerView
        recyclerView?.adapter = sliderAdapter
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange()
                }
            }
        })

        layoutManager = recyclerView?.layoutManager as CardSliderLayoutManager?

        CardSnapHelper().attachToRecyclerView(recyclerView)
    }
    private fun initSwitchers() {
        priceSwitcher = view?.findViewById(R.id.ts_price)
        priceSwitcher?.setFactory(TextViewFactory(R.style.price_textView, true))
        priceSwitcher?.setCurrentText(prices[0])



    }

    private fun initVersionText() {
        versionAnimDuration = resources.getInteger(R.integer.labels_animation_duration).toLong()
        versionOffset1 = resources.getDimensionPixelSize(R.dimen.left_offset).toFloat()
        versionOffset2 = resources.getDimensionPixelSize(R.dimen.card_width).toFloat()

        version1TextView = view?.findViewById(R.id.tv_country_1) as TextView
        version2TextView = view?.findViewById(R.id.tv_country_2) as TextView

        version1TextView?.x = versionOffset1
        version2TextView?.x = versionOffset2

        version1TextView?.text = versionsNames[currentPosition]
        version2TextView?.alpha = 0f

    }


    private fun setVersionText(text: String, left2right: Boolean) {
        val invisibleText: TextView
        val visibleText: TextView
        if ((version2TextView != null) && (version1TextView != null)) {
            val alpha1 = (version1TextView as TextView).alpha
            val alpha2 = (version2TextView as TextView).alpha
            if (alpha1 > alpha2) {
                visibleText = version1TextView as TextView
                invisibleText = version2TextView as TextView
            } else {
                visibleText = version2TextView as TextView
                invisibleText = version1TextView as TextView
            }


            val vOffset: Float
            if (left2right) {
                invisibleText.x = 0f
                vOffset = versionOffset2
            } else {
                invisibleText.x = versionOffset2
                vOffset = 0f
            }
            invisibleText.text = text

            val iAlpha = ObjectAnimator.ofFloat(invisibleText, "alpha", 1f)
            val vAlpha = ObjectAnimator.ofFloat(visibleText, "alpha", 0f)
            val iX = ObjectAnimator.ofFloat(invisibleText, "x", versionOffset1)
            val vX = ObjectAnimator.ofFloat(visibleText, "x", vOffset)

            val animSet = AnimatorSet()
            animSet.playTogether(iAlpha, vAlpha, iX, vX)
            animSet.duration = versionAnimDuration
            animSet.start()
        }
    }

    private fun onActiveCardChange() {
        val pos = layoutManager?.activeCardPosition
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return
        }

        if (pos != null) {
            onActiveCardChange(pos)
        }
    }

    private fun onActiveCardChange(pos: Int) {
        val animH = intArrayOf(R.anim.slide_in_right, R.anim.slide_out_left)
        val animV = intArrayOf(R.anim.slide_in_top, R.anim.slide_out_bottom)

        val left2right = pos < currentPosition
        if (left2right) {
            animH[0] = R.anim.slide_in_left
            animH[1] = R.anim.slide_out_right

            animV[0] = R.anim.slide_in_bottom
            animV[1] = R.anim.slide_out_top
        }

        setVersionText(versionsNames[pos % versionsNames.size], left2right)

        priceSwitcher?.setInAnimation(context, animH[0])
        priceSwitcher?.setOutAnimation(context, animH[1])
        priceSwitcher?.setText(prices[pos % prices.size])



        currentPosition = pos
    }

  
//
    private inner class TextViewFactory internal constructor(
        @param:StyleRes @field:StyleRes
        internal val styleId: Int, internal val center: Boolean
    ) : ViewSwitcher.ViewFactory {

        override fun makeView(): View {
            val textView = TextView(context)

            if (center) {
                textView.gravity = Gravity.CENTER
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAppearance(styleId)
            }

            return textView
        }

    }

    private inner class OnCardClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val lm = recyclerView?.layoutManager as CardSliderLayoutManager?

            if (lm!!.isSmoothScrolling) {
                return
            }

            val activeCardPosition = lm.activeCardPosition
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return
            }

            val clickedPosition = recyclerView?.getChildAdapterPosition(view)
            if ((clickedPosition != activeCardPosition) && (clickedPosition != null)) {
                if (clickedPosition > activeCardPosition) {
                    recyclerView?.smoothScrollToPosition(clickedPosition)
                    onActiveCardChange(clickedPosition)
                }
            }
        }
    }
}
