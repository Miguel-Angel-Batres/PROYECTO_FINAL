package com.example.proyecto_final

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Fragmento que muestra el perfil del usuario y la sección de logros.
 */
class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    // El mapa de secciones debe estar definido DENTRO de la clase ProfileFragment
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
        // Inflar el layout del perfil
        var view = inflater.inflate(R.layout.fragment_profile, container, false)

        var dbHelper = BDhelper(requireContext())
        var db = dbHelper.readableDatabase



        //  Obtener el total de lecciones para el cálculo de logros
        val (completedCount, totalCount) = dbHelper.getGlobalProgressStats(db)

        //  Referencias de Vistas
        val rewardsRecyclerView = view.findViewById<RecyclerView>(R.id.rewards_recycler_view)
        val rewardsHeader = view.findViewById<MaterialCardView>(R.id.rewards_header_card)
        val toggleIcon = view.findViewById<ImageView>(R.id.rewards_toggle_icon)
        val rewardsCountText = view.findViewById<TextView>(R.id.rewards_count_text)
        val rewardsHeaderLayout = view.findViewById<RelativeLayout>(R.id.rewards_header_layout)

        //  Cargar y verificar logros
        val staticRewardsList = dbHelper.getRewardsList().toMutableList()

        val checkedRewards = checkRewardStates(dbHelper, db, staticRewardsList, totalCount)

        db.close()

        // logros

        val unlockedCount = checkedRewards.count { it.isUnlocked }
        val totalRewards = checkedRewards.size

        // Actualiza el texto del contador
        rewardsCountText.text = "$unlockedCount/$totalRewards"

        // Lógica visual del encabezado
        if (unlockedCount > 0) {
            rewardsHeaderLayout.setBackgroundResource(R.color.colorSurface) // Cambiar color si hay logros
        } else {
            rewardsHeaderLayout.setBackgroundResource(R.color.colorPrimary) // Color base si está vacío
        }

        // Configurar RecyclerView
        rewardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        rewardsRecyclerView.adapter = RewardAdapter(checkedRewards)

        // Menu Desplegable
        rewardsHeader.setOnClickListener {
            if (rewardsRecyclerView.visibility == View.GONE) {
                // EXPANDIR
                rewardsRecyclerView.visibility = View.VISIBLE
                toggleIcon.animate().rotation(180f).setDuration(300).start()
            } else {
                // COLAPSAR
                rewardsRecyclerView.visibility = View.GONE
                toggleIcon.animate().rotation(0f).setDuration(300).start()
            }
        }

        return view
    }

    // --- FUNCIÓN DE VERIFICACIÓN MOVIDA DENTRO DE LA CLASE ---
    /**
     * Función que verifica el estado de desbloqueo de cada logro.
     * Esta función debe estar dentro de la clase ProfileFragment.
     */
    private fun checkRewardStates(dbHelper: BDhelper, db: SQLiteDatabase, rewards: MutableList<Reward>, totalLessons: Int): List<Reward> {
        val completedLessonsCount = dbHelper.getGlobalProgressStats(db).first
        val allSections = sectionMap.keys // Acceso directo al mapa

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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}