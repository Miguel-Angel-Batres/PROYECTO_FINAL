
package com.example.proyecto_final

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

class RewardAdapter(private val rewards: List<Reward>) :
    RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {

    class RewardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.reward_title)
        val description: TextView = view.findViewById(R.id.reward_description)
        val points: TextView = view.findViewById(R.id.reward_points)
        val icon: ImageView = view.findViewById(R.id.reward_icon)
        val container: LinearLayout = view as LinearLayout // Para cambiar el fondo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reward, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewards[position]
        val context = holder.itemView.context

        holder.title.text = reward.title
        holder.description.text = reward.description
        holder.points.text = "+${reward.points} XP"

        // Lógica de visualización para Desbloqueado y Bloqueado
        if (reward.isUnlocked) {
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.colorTextPrimary))
            holder.points.setTextColor(ContextCompat.getColor(context, R.color.colorCorrect))
            holder.icon.setImageResource(R.drawable.trophy) // Icono a color
            holder.container.alpha = 1.0f // Visible
        } else {
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary))
            holder.points.setTextColor(ContextCompat.getColor(context, R.color.colorSecondary))
            holder.icon.setImageResource(R.drawable.ic_lock)
            holder.container.alpha = 0.5f
        }
    }

    override fun getItemCount() = rewards.size
}