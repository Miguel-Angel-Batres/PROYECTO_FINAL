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
        val image: ImageView = view.findViewById(R.id.cardBackImage)
        val title: TextView = view.findViewById(R.id.cardTitle)
        val front: View = view.findViewById(R.id.frontCard)
        val back: View = view.findViewById(R.id.backCard)
        val backText: TextView = view.findViewById(R.id.cardBackText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)
        return GameCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameCardViewHolder, position: Int) {
        val card = items[position]
        holder.title.text = card.word
        holder.backText.text = card.trad

        holder.itemView.setOnClickListener {
            val isBackVisible = holder.back.visibility == View.VISIBLE
            var showView: View
            var hideView: View
            if (isBackVisible) {
                hideView = holder.back
                showView = holder.front
            } else {
                hideView = holder.front
                showView = holder.back
            }

            val scale = holder.itemView.context.resources.displayMetrics.density
            holder.itemView.cameraDistance = 8000 * scale

            hideView.animate()
                .rotationY(90f)
                .setDuration(150)
                .withEndAction {
                    hideView.visibility = View.GONE
                    showView.visibility = View.VISIBLE
                    showView.rotationY = -90f
                    showView.animate()
                        .rotationY(0f)
                        .setDuration(150)
                        .start()
                }
                .start()
            card.volteada = !isBackVisible
        }
    }

    override fun getItemCount(): Int = items.size
}
