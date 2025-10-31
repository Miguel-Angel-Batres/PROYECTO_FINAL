package com.example.proyecto_final

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView


class GamePanel : FrameLayout {
    var txtTitle: TextView? = null
    var txtSubtitle: TextView? = null
    var gameImg: ImageView? = null

    constructor(context: Context) : super(context) {
        initView()
    }
    constructor(context: Context,attrs: AttributeSet) : super(context,attrs){
        initView()
        val a = getContext().obtainStyledAttributes(attrs,R.styleable.GameCard)
        val title = a.getString(R.styleable.GameCard_game_title)
        val subtitle = a.getString(R.styleable.GameCard_game_subtitle)
        val image = a.getResourceId(R.styleable.GameCard_game_image,0)

        txtTitle!!.text = title
        txtSubtitle!!.text = subtitle
        if (image != 0) {
            gameImg!!.setImageResource(image)
        }

    }
    constructor(context: Context,attrs: AttributeSet,defStyleAttr: Int) : super(context,attrs,defStyleAttr){
        initView()
        val a = getContext().obtainStyledAttributes(attrs,R.styleable.GameCard)
        val title = a.getString(R.styleable.GameCard_game_title)
        val subtitle = a.getString(R.styleable.GameCard_game_subtitle)
        val image = a.getResourceId(R.styleable.GameCard_game_image,0)

        txtTitle!!.text = title
        txtSubtitle!!.text = subtitle
        if (image != 0) {
            gameImg!!.setImageResource(image)
        }
    }


    fun initView(){
        val li = LayoutInflater.from(context)
        li.inflate(R.layout.item_game_card,this,true)
        txtTitle = findViewById(R.id.game_title)
        txtSubtitle = findViewById(R.id.game_subtitle)
        gameImg = findViewById(R.id.game_img)

    }
}