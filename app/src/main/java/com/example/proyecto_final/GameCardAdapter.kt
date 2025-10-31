package com.example.proyecto_final

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameCardAdapter(private val items: List<GameCard>) :
    RecyclerView.Adapter<GameCardAdapter.GameCardViewHolder>() {

    inner class GameCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.cardImage)
        val title: TextView = view.findViewById(R.id.cardTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)
        return GameCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameCardViewHolder, position: Int) {
        val card = items[position]
        holder.image.setImageResource(card.image)
        holder.title.text = card.word
    }

    override fun getItemCount(): Int = items.size
}
