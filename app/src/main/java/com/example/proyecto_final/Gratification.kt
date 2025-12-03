package com.example.proyecto_final

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.view.animation.OvershootInterpolator // Para el efecto de rebote
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

/**
 * Fragmento que muestra el resultado (porcentaje de aciertos) de la lección terminada.
 */
class Gratification : Fragment() {

    // Claves de argumentos definidas como constantes de la clase
    companion object {
        private const val ARG_CORRECTAS = "CORRECTAS"
        private const val ARG_TOTAL = "TOTAL_FRASES"
        // ARG_REWARDS y el parámetro 'rewards' han sido eliminados de newInstance

        /**
         * Método estático para crear una nueva instancia pasando solo el puntaje.
         */
        @JvmStatic fun newInstance(correctas: Int, total: Int) =
            Gratification().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CORRECTAS, correctas)
                    putInt(ARG_TOTAL, total)
                    // Se elimina putInt(ARG_REWARDS, rewards)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gratification, container, false)

        val scoreTextView = view.findViewById<TextView>(R.id.result_score_text)
        val continueButton = view.findViewById<Button>(R.id.result_button_continue)
        val scoreCard = view.findViewById<MaterialCardView>(R.id.score_card_view)
        val statusTextView = view.findViewById<TextView>(R.id.result_status_message)

        // REFERENCIA CLAVE PARA LA ANIMACIÓN
        val rewardsIcon = view.findViewById<ImageView>(R.id.achievement_icon)
        // Se elimina la referencia a rewardsTextView

        //  Obtener los argumentos
        val correctas = arguments?.getInt(ARG_CORRECTAS) ?: 0
        val total = arguments?.getInt(ARG_TOTAL) ?: 1
        // Se elimina la lectura de puntosGanados

        //  Cálculo de porcentaje y mensaje de resultado
        val porcentaje = if (total > 0) (correctas.toFloat() / total.toFloat()) * 100 else 0f
        val percentInt = porcentaje.toInt()
        val resultText = "$correctas/$total (${percentInt}%)"
        scoreTextView.text = resultText

        val colorResource: Int

        // 3. Lógica de Colores y Mensajes
        if (porcentaje >= 100) {
            colorResource = R.color.colorCorrect
            statusTextView.text = "¡Perfecto! Un 100% impecable. 🌟"
        } else if (porcentaje >= 80 && porcentaje < 100) {
            colorResource = R.color.yellow_warning
            statusTextView.text = "¡A nada! Intenta una vez más!."
        } else {
            colorResource = R.color.red_error
            statusTextView.text = "Buen intento. ¡Puedes mejorar!"
        }

        //  Aplicar el color dinámico
        if (scoreCard != null) {
            val color = ContextCompat.getColor(requireContext(), colorResource)
            scoreCard.setCardBackgroundColor(color)
        }


        if (rewardsIcon != null) {
            rewardsIcon.apply {
                // Iniciar el icono invisible y pequeño
                scaleX = 0f
                scaleY = 0f
                alpha = 0f

                // Animar el icono para que aparezca con un efecto de rebote
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(800) // Duración de 800ms
                    .setInterpolator(OvershootInterpolator()) // Efecto de rebote
                    .start()
            }
        }

        //  botón de regreso
        continueButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
        }

        return view
    }
}