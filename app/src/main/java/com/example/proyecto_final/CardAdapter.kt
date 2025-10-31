package com.example.proyecto_final

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private val cards: List<CardData>,    private val onCardClick: (CardData) -> Unit) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(val card: Card) : RecyclerView.ViewHolder(card)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val card = Card(parent.context)
        val params = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        card.layoutParams = params
        return CardViewHolder(card)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val data = cards[position]
        holder.card.txtTitle?.text = data.title
        holder.card.txtSubtitle?.text = data.subtitle
        holder.card.cardImg?.setImageResource(data.image)
        holder.card.cardIconImg?.setImageResource(data.icon)
        holder.card.setOnClickListener {
            onCardClick(data)
        }
    }

    override fun getItemCount(): Int = cards.size
}