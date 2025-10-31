package com.example.proyecto_final

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

class Card : FrameLayout {
    var txtTitle: TextView? = null
    var txtSubtitle: TextView? = null
    var cardImg: ImageView? = null
    var cardIconImg: ImageView? = null

    constructor(context: Context) : super(context) {
        initView()
    }
    constructor(context: Context,attrs: AttributeSet) : super(context,attrs){
        initView()
        val a = getContext().obtainStyledAttributes(attrs,R.styleable.Card)
        val title = a.getString(R.styleable.Card_card_title)
        val subtitle = a.getString(R.styleable.Card_card_subtitle)
        val image = a.getResourceId(R.styleable.Card_card_image,0)
        val imageIcon = a.getResourceId(R.styleable.Card_card_icon_image,0)
        txtTitle!!.text = title
        txtSubtitle!!.text = subtitle
        if (image != 0) {
            cardImg!!.setImageResource(image)
            cardIconImg!!.setImageResource(imageIcon)
        }

    }
    constructor(context: Context,attrs: AttributeSet,defStyleAttr: Int) : super(context,attrs,defStyleAttr){
        initView()
        val a = getContext().obtainStyledAttributes(attrs,R.styleable.Card)
        val title = a.getString(R.styleable.Card_card_title)
        val subtitle = a.getString(R.styleable.Card_card_subtitle)
        val image = a.getResourceId(R.styleable.Card_card_image,0)
        val imageIcon = a.getResourceId(R.styleable.Card_card_icon_image,0)
        txtTitle!!.text = title
        txtSubtitle!!.text = subtitle
        if (image != 0) {
            cardImg!!.setImageResource(image)
            cardIconImg!!.setImageResource(imageIcon)
        }
    }

    fun initView(){
        val li = LayoutInflater.from(context)
        li.inflate(R.layout.item_card,this,true)
        txtTitle = findViewById(R.id.item_card_title)
        txtSubtitle = findViewById(R.id.item_card_subtitle)
        cardImg = findViewById(R.id.item_card_image)
        cardIconImg = findViewById(R.id.item_card_icon)

    }
}