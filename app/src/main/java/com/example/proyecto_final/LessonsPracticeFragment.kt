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
import org.w3c.dom.Text

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
//      var imagen = view.findViewById<ImageView>(R.id.imagen)
        frase = view.findViewById<TextView>(R.id.frase)
        opcion1 = view.findViewById<Button>(R.id.opcion1)
        opcion2 = view.findViewById<Button>(R.id.opcion2)
        opcion3 = view.findViewById<Button>(R.id.opcion3)
        opcion4 = view.findViewById<Button>(R.id.opcion4)
        var dbHelper = BDhelper(requireContext())
        var db = dbHelper.readableDatabase

        // obtener leccion_id de la base de datos
        var leccion_id = arguments?.getString("id_leccion")
        frases = dbHelper.getFrasesByLeccion(db,leccion_id!!.toInt())
        frases.shuffle()
        frase.text = frases[0].texto
        opcion1.text = frases[0].opciones[0]
        opcion2.text = frases[0].opciones[1]
        opcion3.text = frases[0].opciones[2]
        opcion4.text = frases[0].opciones[3]
        opcion1.setOnClickListener { handleClick(opcion1) }
        opcion2.setOnClickListener { handleClick(opcion2) }
        opcion3.setOnClickListener { handleClick(opcion3) }
        opcion4.setOnClickListener { handleClick(opcion4) }
    //   var leccion = dbHelper.getLeccion(db,leccion_id!!.toInt())
    //   imagen.setImageResource(leccion.imagen)
    //   imagen.scaleType = ImageView.ScaleType.FIT_XY
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
            ContextCompat.getColor(context,
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
            if(index==frases.size - 1){
                val bundle = Bundle()
                val fragment = LessonsFragment()
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit()
                return@postDelayed
            }else {
                index++
                println(index)
                frase.text = frases[index].texto
                opcion1.text = frases[index].opciones[0]
                opcion2.text = frases[index].opciones[1]
                opcion3.text = frases[index].opciones[2]
                opcion4.text = frases[index].opciones[3]
                btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
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