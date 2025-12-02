package com.example.proyecto_final

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat // Necesario para usar colores
import com.google.android.material.card.MaterialCardView

/**
 * Fragmento que muestra el resultado (porcentaje de aciertos) de la lección terminada.
 */
class Gratification : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gratification, container, false)

        val scoreTextView = view.findViewById<TextView>(R.id.result_score_text)
        val continueButton = view.findViewById<Button>(R.id.result_button_continue)
        val scoreCard = view.findViewById<MaterialCardView>(R.id.score_card_view) // Referencia a la tarjeta
        val statusTextView = view.findViewById<TextView>(R.id.result_status_message) // Referencia al estado

        // Obtener los argumentos de aciertos y total
        val correctas = arguments?.getInt(ARG_CORRECTAS) ?: 0
        val total = arguments?.getInt(ARG_TOTAL) ?: 1

        // Calcular el porcentaje
        val porcentaje = if (total > 0) (correctas.toFloat() / total.toFloat()) * 100 else 0f
        val percentInt = porcentaje.toInt()
        val resultText = "$correctas/$total (${percentInt}%)"
        scoreTextView.text = resultText

        val colorResource: Int


        if (porcentaje >= 100) {
            // VERDE (100% de aciertos)
            colorResource = R.color.colorCorrect
            statusTextView.text = "¡Perfecto! Un 100% impecable. 🌟"
        } else if (porcentaje >= 80 && porcentaje < 99) {
            // AMARILLO (80% - 99%)
            colorResource = R.color.yellow_warning
            statusTextView.text = "¡A nada! Intenta una vez más!."
        } else {
            // ROJO (Menos del 80%)
            colorResource = R.color.red_error
            statusTextView.text = "Buen intento. ¡Puedes mejorar!"
        }
        if (scoreCard != null) {
            val color = ContextCompat.getColor(requireContext(), colorResource)
            scoreCard.setCardBackgroundColor(color)
        }

        // Manejar el botón de regreso
        continueButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
        }

        return view
    }

    companion object {
        // Claves de argumentos específicas para el resultado (reemplazan ARG_PARAM1/2)
        private const val ARG_CORRECTAS = "CORRECTAS"
        private const val ARG_TOTAL = "TOTAL_FRASES"

        @JvmStatic fun newInstance(correctas: Int, total: Int) =
            Gratification().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CORRECTAS, correctas)
                    putInt(ARG_TOTAL, total)
                }
            }
    }
}