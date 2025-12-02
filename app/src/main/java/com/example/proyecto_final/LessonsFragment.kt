package com.example.proyecto_final

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar // Importar ProgressBar para el progreso global
import android.widget.TextView
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import android.util.Log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragmento que muestra la lista de lecciones y el progreso del usuario.
 */
class LessonsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        var view =  inflater.inflate(R.layout.fragment_lessons, container, false)

        var dbHelper = BDhelper(requireContext())
        var db = dbHelper.readableDatabase



        // Referencia a las vistas de PROGRESO GLOBAL
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_global)
        val progressText = view.findViewById<TextView>(R.id.progress_text_global)

        // Obtener estadísticas de la DB: (completadas, total)
        val (completedCount, totalCount) = dbHelper.getGlobalProgressStats(db)

        if (totalCount > 0) {
            val progressPercent = (completedCount.toFloat() / totalCount.toFloat()) * 100
            val percentInt = progressPercent.toInt()

            // Asignar progreso
            progressBar.progress = percentInt
            progressText.text = "$completedCount/$totalCount Lecciones ($percentInt%)"
        } else {
            // Caso de no haber lecciones
            progressBar.progress = 0
            progressText.text = "¡Añade tu primera lección!"
        }



        val lista_views = listOf<View>(
            view.findViewById(R.id.leccion1_1),
            view.findViewById(R.id.leccion1_2),
            view.findViewById(R.id.leccion2_1),
            view.findViewById(R.id.leccion2_2),
            view.findViewById(R.id.leccion3_1),
            view.findViewById(R.id.leccion3_2),
            view.findViewById(R.id.leccion4_1),
            view.findViewById(R.id.leccion4_2),
            view.findViewById(R.id.leccion5_1),
            view.findViewById(R.id.leccion5_2),
            view.findViewById(R.id.leccion6_1),
            view.findViewById(R.id.leccion6_2)
        )

        // Obtener las lecciones con el estado de progreso
        var lecciones = dbHelper.getLecciones(db)
        db.close() // Cerramos la conexión después de todas las lecturas

        // ID del ImageView que actuará como marca de completado
        val CHECKMARK_ICON_ID = R.id.checkmark_icon

        lecciones.forEachIndexed { index, leccion ->
            val lessonView = lista_views[index]

            // Llenado de datos (Existente)
            lessonView.findViewById<TextView>(R.id.lesson_title).text = leccion.nombre
            lessonView.findViewById<TextView>(R.id.lesson_subtitle).text = leccion.descripcion
            lessonView.findViewById<ImageView>(R.id.lesson_image).setImageResource(leccion.imagen)

         //proceso individual
            val checkmarkIcon = lessonView.findViewById<ImageView>(CHECKMARK_ICON_ID)

            // Si leccion.completada es 1 (TRUE), mostramos el icono
            if (leccion.completada == 1) {
                checkmarkIcon.visibility = View.VISIBLE
            } else {
                checkmarkIcon.visibility = View.GONE
            }
            // **********************************************

            lessonView.setOnClickListener {
                val bundle = Bundle()
                // pasar titulo de la leccion
                bundle.putString("id_leccion",leccion.id.toString())
                // comenzar fragmento de lecciones
                val fragment = LessonsPracticeFragment()
                fragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Definimos la solicitud de la fuente una sola vez
        val request = FontRequest(
            "com.google.android.gms.fonts", // Autoridad del proveedor de fuentes
            "com.google.android.gms",       // Paquete del proveedor para verificación
            "Ultra",                        // La consulta de la fuente que queremos
            R.array.com_google_android_gms_fonts_certs // Certificados para verificar la firma
        )

        // --- FUNCIÓN RECURSIVA PARA APLICAR LA FUENTE ---
        fun applyFontToAllTextViews(v: View, typeface: Typeface) {
            // Si la vista actual es un TextView, le aplicamos la fuente.
            if (v is TextView) {
                v.typeface = typeface
            }
            // Si la vista actual es un contenedor de vistas (como LinearLayout, FrameLayout, etc.),
            // recorremos sus hijos y llamamos a esta misma función para cada uno.
            else if (v is ViewGroup) {
                for (i in 0 until v.childCount) {
                    applyFontToAllTextViews(v.getChildAt(i), typeface)
                }
            }
        }

        // Creamos un callback para manejar la respuesta
        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface) {
                // Cuando la fuente se obtiene correctamente, iniciamos el recorrido
                // desde la vista raíz del fragmento.
                applyFontToAllTextViews(view, typeface)
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                // Opcional: Manejar el error si la fuente no se puede descargar.
            }
        }

        // Solicitamos la fuente
        FontsContractCompat.requestFont(
            requireContext(),
            request,
            callback,
            Handler(Looper.getMainLooper())
        )
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LessonsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LessonsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}