package com.example.proyecto_final

import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout // Importación necesaria para RelativeLayout
import android.widget.TextView
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import com.google.android.material.card.MaterialCardView
import androidx.core.content.ContextCompat // Importación necesaria para obtener colores

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragmento que muestra la lista de lecciones y el progreso del usuario.
 */
class LessonsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    // Mapeo de tema_id de la DB a la clave de la condición del Logro
    private val sectionMap = mapOf(
        "verbs_tenses" to "Verbos y Tiempos",
        "nouns_articles" to "Nouns, Articles and Adjectives",
        "pronouns_possessives" to "Pronouns and Possessives",
        "prepositions" to "Prepositions",
        "compound_sentences" to "Compound Sentences and Connectors",
        "questions_negations" to "Questions and Negations"
    )

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

        //progreso global
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_global)
        val progressText = view.findViewById<TextView>(R.id.progress_text_global)

        val (completedCount, totalCount) = dbHelper.getGlobalProgressStats(db)

        if (totalCount > 0) {
            val progressPercent = (completedCount.toFloat() / totalCount.toFloat()) * 100
            val percentInt = progressPercent.toInt()

            progressBar.progress = percentInt
            progressText.text = "$completedCount/$totalCount Lecciones ($percentInt%)"
        } else {
            progressBar.progress = 0
            progressText.text = "¡Añade tu primera lección!"
        }

        //recompensas
        val rewardsRecyclerView = view.findViewById<RecyclerView>(R.id.rewards_recycler_view)

        // Inicialización de las vistas del menú desplegable
        val rewardsHeader = view.findViewById<MaterialCardView>(R.id.rewards_header_card)
        val toggleIcon = view.findViewById<ImageView>(R.id.rewards_toggle_icon)
        val rewardsCountText = view.findViewById<TextView>(R.id.rewards_count_text) // Referencia al nuevo TextView
        val rewardsHeaderLayout = view.findViewById<RelativeLayout>(R.id.rewards_header_layout) // Referencia al layout para cambiar el color

        // Cargar la lista estática de logros desde BDHelper
        val staticRewardsList = dbHelper.getRewardsList().toMutableList()

        //Verifica el estado real de cada logro
        val checkedRewards = checkRewardStates(dbHelper, db, staticRewardsList, totalCount)

        // contador y desbloqueo
        val unlockedCount = checkedRewards.count { it.isUnlocked }
        val totalRewards = checkedRewards.size

        // Actualiza el texto del contador
        rewardsCountText.text = "$unlockedCount/$totalRewards"

        // Si hay logros desbloqueados, cambia el fondo del encabezado
        if (unlockedCount > 0) {
            rewardsHeaderLayout.setBackgroundResource(R.color.colorSurface) // Usando colorSurface para destacar
        } else {
            // Mantener el color base si no hay logros
            rewardsHeaderLayout.setBackgroundResource(R.color.colorPrimary)
        }
        rewardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        rewardsRecyclerView.adapter = RewardAdapter(checkedRewards)
//menu
        rewardsHeader.setOnClickListener {
            if (rewardsRecyclerView.visibility == View.GONE) {
                // EXPANDIR
                rewardsRecyclerView.visibility = View.VISIBLE
                // Rota la flecha 180 grados (apuntando hacia arriba)
                toggleIcon.animate().rotation(180f).setDuration(300).start()
            } else {
                // COLAPSAR
                rewardsRecyclerView.visibility = View.GONE
                toggleIcon.animate().rotation(0f).setDuration(300).start()
            }
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

        var lecciones = dbHelper.getLecciones(db)
        db.close() // Cerramos la conexión después de todas las lecturas

        val CHECKMARK_ICON_ID = R.id.checkmark_icon

        lecciones.forEachIndexed { index, leccion ->
            val lessonView = lista_views[index]

            // Llenado de datos (Existente)
            lessonView.findViewById<TextView>(R.id.lesson_title).text = leccion.nombre
            lessonView.findViewById<TextView>(R.id.lesson_subtitle).text = leccion.descripcion
            lessonView.findViewById<ImageView>(R.id.lesson_image).setImageResource(leccion.imagen)

            // *** VISUALIZACIÓN DEL PROGRESO INDIVIDUAL ***
            val checkmarkIcon = lessonView.findViewById<ImageView>(CHECKMARK_ICON_ID)

            if (leccion.completada == 1) {
                checkmarkIcon.visibility = View.VISIBLE
            } else {
                checkmarkIcon.visibility = View.GONE
            }
            // **********************************************

            lessonView.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id_leccion",leccion.id.toString())
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

    /**
     * Función que verifica el estado de desbloqueo de cada logro.
     * Nota: Asume que las funciones de BDHelper (isSectionComplete) existen.
     */
    private fun checkRewardStates(dbHelper: BDhelper, db: SQLiteDatabase, rewards: MutableList<Reward>, totalLessons: Int): List<Reward> {
        val completedLessonsCount = dbHelper.getGlobalProgressStats(db).first
        val allSections = sectionMap.keys

        rewards.forEach { reward ->
            val parts = reward.condition.split(":")
            val conditionKey = parts.getOrNull(0)
            val conditionValue = parts.getOrNull(1)

            val isAchieved = when (conditionKey) {

                "CompletedLessonCount" -> completedLessonsCount >= conditionValue?.toIntOrNull() ?: Int.MAX_VALUE

                "CompletedSection" -> {
                    // Usa conditionValue (el tema_id de la DB) directamente para verificar el estado de la sección
                    conditionValue != null && dbHelper.isSectionComplete(db, conditionValue)
                }

                "CompletedAllSections" -> {
                    // Verifica si TODAS las secciones están completas
                    val allUniqueSections = dbHelper.getAllUniqueTemaIds(db) // Requiere getAllUniqueTemaIds
                    allUniqueSections.all { temaId -> dbHelper.isSectionComplete(db, temaId) }
                }

                // Aquí irían otras condiciones
                else -> false
            }

            if (isAchieved) {
                reward.isUnlocked = true
            }
        }
        return rewards
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
            if (v is TextView) {
                v.typeface = typeface
            }
            else if (v is ViewGroup) {
                for (i in 0 until v.childCount) {
                    applyFontToAllTextViews(v.getChildAt(i), typeface)
                }
            }
        }

        // Creamos un callback para manejar la respuesta
        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface) {
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
         */
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