package com.example.proyecto_final

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.util.Log // Asegúrate de importar Log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LessonsPracticeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LessonsPracticeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // CLAVE: Variable para almacenar el ID de la lección actual
    private var idLeccionActual: Int? = null

    lateinit var frase_actual: Frase
    lateinit var frases: MutableList<Frase>
    private var index: Int = 0
    private var correctas: Int = 0
    private var incorrectas: Int = 0
    lateinit var frase: TextView
    lateinit var opcion1: Button
    lateinit var opcion2: Button
    lateinit var opcion3: Button
    lateinit var opcion4: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_lessons_practice, container, false)
        frase = view.findViewById<TextView>(R.id.frase)
        opcion1 = view.findViewById<Button>(R.id.opcion1)
        opcion2 = view.findViewById<Button>(R.id.opcion2)
        opcion3 = view.findViewById<Button>(R.id.opcion3)
        opcion4 = view.findViewById<Button>(R.id.opcion4)

        var dbHelper = BDhelper(requireContext())
        var db = dbHelper.readableDatabase

        // obtener leccion_id de la base de datos y ALMACENARLO
        var leccion_id_str = arguments?.getString("id_leccion")
        idLeccionActual = leccion_id_str?.toIntOrNull()

        // Cargar frases (usando el id de la lección)
        if (idLeccionActual != null) {
            frases = dbHelper.getFrasesByLeccion(db, idLeccionActual!!)
            frases.shuffle()

            // Inicialización de la UI con la primera frase
            frase.text = frases[0].texto
            opcion1.text = frases[0].opciones[0]
            opcion2.text = frases[0].opciones[1]
            opcion3.text = frases[0].opciones[2]
            opcion4.text = frases[0].opciones[3]

            opcion1.setOnClickListener { handleClick(opcion1) }
            opcion2.setOnClickListener { handleClick(opcion2) }
            opcion3.setOnClickListener { handleClick(opcion3) }
            opcion4.setOnClickListener { handleClick(opcion4) }
        }

        // Cierra la DB de lectura
        db.close()

        return view
    }

    fun isCorrect(text: String) : Boolean{
        if(text==frases[index].correcta){
            correctas++
            return true
        }else{
            incorrectas++
            return false
        }
    }

    private fun handleClick(btn: Button) {
        val esCorrecta = isCorrect(btn.text.toString())
        btn.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                if (esCorrecta) R.color.colorCorrect else R.color.colorError
            )
        )

        btn.animate()
            .scaleX(1.15f)
            .scaleY(1.15f)
            .alpha(1f)
            .setDuration(150)
            .withEndAction {
                btn.animate()
                    .alpha(0.2f)
                    .setDuration(500)
                    .start()
            }
            .start()

        btn.postDelayed({
            if(index == frases.size - 1){

                // Condición de éxito
                val fueExitosa = incorrectas == 0

                if (fueExitosa && idLeccionActual != null) {
                    // Si se completó con éxito, marca el estado binario en la DB
                    val dbHelper = BDhelper(requireContext())
                    val db = dbHelper.writableDatabase
                    dbHelper.updateLeccionCompletada(db, idLeccionActual!!)
                    db.close()
                }


                // Crear la instancia del fragmento Gratification, pasando los resultados
                val resultFragment = Gratification.newInstance(correctas, frases.size)

                // Navegar
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container, resultFragment)
                    .addToBackStack(null)
                    .commit()

                return@postDelayed
            }else {
                // Lógica para avanzar a la siguiente pregunta
                index++
                println(index)
                frase.text = frases[index].texto
                opcion1.text = frases[index].opciones[0]
                opcion2.text = frases[index].opciones[1]
                opcion3.text = frases[index].opciones[2]
                opcion4.text = frases[index].opciones[3]
                btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                btn.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(150)
                    .start()
            }
        }, 500)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LessonsPracticeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LessonsPracticeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}