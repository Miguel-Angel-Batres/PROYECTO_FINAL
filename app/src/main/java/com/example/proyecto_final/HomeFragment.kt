package com.example.proyecto_final

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var recyclerbooks : RecyclerView
    lateinit var recyclergames : RecyclerView
    lateinit var recyclerlessons : RecyclerView

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
        var view =  inflater.inflate(R.layout.fragment_home, container, false)
        recyclerbooks = view.findViewById<RecyclerView>(R.id.recyclerBooks)
        recyclergames = view.findViewById<RecyclerView>(R.id.recyclerGames)
        recyclerlessons = view.findViewById<RecyclerView>(R.id.recyclerLessons)
        var see_games: TextView = view.findViewById<TextView>(R.id.see_games)
        var see_lessons: TextView = view.findViewById<TextView>(R.id.see_lessons)
        var see_books: TextView = view.findViewById<TextView>(R.id.see_books)

        see_games.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragmentFromFragment(GamesFragment(),R.id.bottom_games)
        }
        see_lessons.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragmentFromFragment(LessonsFragment(),R.id.bottom_lessons)
        }
        see_books.setOnClickListener{
            (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
        }

        val games = listOf(
            CardData("game_flashcards", "Flashcards", "Aprende rápido", R.drawable.flashcards, R.drawable.flashcard_icon),
            CardData("game_memorama", "Memorama", "Entrena tu memoria", R.drawable.memory_game, R.drawable.memory_icon),
        )
        val books = listOf(CardData(
                id = "pride_and_prejudice",
                title = "Pride and Prejudice",
                subtitle = "Jane Austen",
                image = R.drawable.pride_and_prejudice,
                icon = R.drawable.romance
            ),
            CardData(
                id = "alice_adventures_in_wonderland",
                title = "Alice's Adventures in Wonderland",
                subtitle = "Lewis Carroll",
                image = R.drawable.alice_adventures_in_wonderland,
                icon = R.drawable.ciencia_ficcion
            ),
            CardData(
                id = "the_adventures_of_sherlock_holmes",
                title = "The Adventures of Sherlock Holmes",
                subtitle = "Arthur Conan Doyle",
                image = R.drawable.sherlock_holmes,
                icon = R.drawable.misterio
            ),
            CardData(
                id = "frankestein",
                title = "Frankenstein",
                subtitle = "Mary Shelley",
                image = R.drawable.frankestein,
                icon = R.drawable.horror
            ),
            CardData(
                id = "dracula",
                title = "Dracula",
                subtitle = "Bram Stoker",
                image = R.drawable.dracula,
                icon = R.drawable.horror
            ),
            CardData(
                id = "the_picture_of_dorian_gray",
                title = "The Picture of Dorian Gray",
                subtitle = "Oscar Wilde",
                image = R.drawable.dorian_gray,
                icon = R.drawable.misterio
            ),
            CardData(
                id = "moby_dick",
                title = "Moby Dick",
                subtitle = "Herman Melville",
                image = R.drawable.moby_dick,
                icon = R.drawable.misterio
            ),
            CardData(
                id = "the_war_of_the_worlds",
                title = "The War of the Worlds",
                subtitle = "H. G. Wells",
                image = R.drawable.the_war_of_the_world,
                icon = R.drawable.ciencia_ficcion
            )
        )
        val lessons = listOf(
            CardData(
                id = "lesson_verbs_tenses",
                title = "Verbs and Tenses",
                subtitle = "Aprende tiempos verbales y cómo usarlos correctamente.",
                image = R.drawable.zorro1,
                icon = R.drawable.tiempo
            ),
            CardData(
                id = "lesson_nouns_articles",
                title = "Nouns, Articles and Adjectives",
                subtitle = "Domina las bases de la gramática en inglés.",
                image = R.drawable.zorro2,
                icon = R.drawable.articles
            ),
            CardData(
                id = "lesson_pronouns_possessives",
                title = "Pronouns and Possessives",
                subtitle = "Aprende a usar correctamente los pronombres y posesivos.",
                image = R.drawable.zorro3,
                icon = R.drawable.noun
            ),
            CardData(
                id = "lesson_prepositions",
                title = "Prepositions",
                subtitle = "Descubre cómo las preposiciones cambian el sentido de las oraciones.",
                image = R.drawable.zorro4,
                icon = R.drawable.ubicacion
            ),
            CardData(
                id = "lesson_compound_sentences",
                title = "Compound Sentences and Connectors",
                subtitle = "Conecta ideas con frases complejas y conectores.",
                image = R.drawable.zorro5,
                icon = R.drawable.cadena
            ),
            CardData(
                id = "lesson_questions_negations",
                title = "Questions and Negations",
                subtitle = "Construye preguntas y oraciones negativas fácilmente.",
                image = R.drawable.zorro6,
                icon = R.drawable.question
            ),
        )

        recyclergames.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerbooks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerlessons.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclergames.adapter = CardAdapter(games){ card ->
            when(card.id){
                "game_flashcards" ->  (requireActivity() as MainActivity).replaceFragmentFromFragment(CardsPractice_fragment(),R.id.bottom_games)
                "game_memorama" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(CardsPractice_fragment(),R.id.bottom_games)
            }
        }
        recyclerbooks.adapter = CardAdapter(books){ card ->
            when(card.id){
                "book_whispers" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_midnight_forest" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_broken_mirror" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_hourless_clock" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_shadow_under_bed" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_alice_wonderland" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_red_riding_hood" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_black_cat" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_dracula" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
                "book_frankenstein" -> (requireActivity() as MainActivity).replaceFragmentFromFragment(BooksFragment(),R.id.bottom_books)
            }
        }
        recyclerlessons.adapter = CardAdapter(lessons){ card ->
            when(card.id){
                "lesson_verbs_tenses" -> (requireActivity() as MainActivity).replaceFragmentinID(LessonsFragment(),R.id.Verbs_and_tenses, R.id.bottom_lessons)
                "lesson_nouns_articles" -> (requireActivity() as MainActivity).replaceFragmentinID(LessonsFragment(),R.id.Nouns_articles_adjectives, R.id.bottom_lessons)
                "lesson_pronouns_possessives" -> (requireActivity() as MainActivity).replaceFragmentinID(LessonsFragment(),R.id.Pronouns_and_possessives, R.id.bottom_lessons)
                "lesson_prepositions" -> (requireActivity() as MainActivity).replaceFragmentinID(LessonsFragment(),R.id.Prepositions, R.id.bottom_lessons)
                "lesson_compound_sentences" -> (requireActivity() as MainActivity).replaceFragmentinID(LessonsFragment(),R.id.Compound_sentences_connectors, R.id.bottom_lessons)
                "lesson_questions_negations" -> (requireActivity() as MainActivity).replaceFragmentinID(LessonsFragment(),R.id.Questions_and_negations, R.id.bottom_lessons)
            }
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Definimos la solicitud de la fuente una sola vez
        val request = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Ultra",
            R.array.com_google_android_gms_fonts_certs
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
                applyFontToAllTextViews(view, typeface)
                listOf(recyclerbooks, recyclergames, recyclerlessons).forEach { recycler ->
                    val childCount = recycler.childCount
                    for (i in 0 until childCount) {
                        val child = recycler.getChildAt(i)
                        applyFontToAllTextViews(child, typeface)
                    }

                    recycler.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
                        override fun onChildViewAttachedToWindow(child: View) {
                            applyFontToAllTextViews(child, typeface)
                        }
                        override fun onChildViewDetachedFromWindow(child: View) {}
                    })
                }
            }

            override fun onTypefaceRequestFailed(reason: Int) {

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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
