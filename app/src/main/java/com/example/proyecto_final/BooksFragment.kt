package com.example.proyecto_final

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import com.bumptech.glide.Glide

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BooksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BooksFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        return inflater.inflate(R.layout.fragment_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner = view.findViewById<Spinner>(R.id.book_selection_spinner)

        val bookcard = view.findViewById<View>(R.id.selected_book_card)

        val bookImage = bookcard.findViewById<ImageView>(R.id.game_img)
        val bookTitle = view.findViewById<TextView>(R.id.game_title)
        val bookSubtitle = view.findViewById<TextView>(R.id.game_subtitle)

        val openButton = view.findViewById<Button>(R.id.open_book_button)

        val books = listOf(
            Book(
                title = "Pride and Prejudice",
                author = "Jane Austen",
                year = "1813",
                summary = "A classic novel about love and social standing in 19th century England.",
                imageUrl = "https://www.gutenberg.org/cache/epub/1342/pg1342.cover.medium.jpg",
                url = "pride_and_prejudice"
            ),
            Book(
                title = "Alice's Adventures in Wonderland",
                author = "Lewis Carroll",
                year = "1865",
                summary = "Alice falls through a rabbit hole into a fantastical world.",
                imageUrl = "https://www.gutenberg.org/cache/epub/11/pg11.cover.medium.jpg",
                url = "alice_adventures_in_wonderland"
            ),
            Book(
                title = "The Adventures of Sherlock Holmes",
                author = "Arthur Conan Doyle",
                year = "1892",
                summary = "Detective Sherlock Holmes solves mysteries in Victorian London.",
                imageUrl = "https://www.gutenberg.org/cache/epub/1661/pg1661.cover.medium.jpg",
                url = "the_adventures_of_sherlock_holmes"
            ),
            Book(
                title = "Frankenstein",
                author = "Mary Shelley",
                year = "1818",
                summary = "A scientist creates a living being from dead matter, leading to tragic consequences.",
                imageUrl = "https://www.gutenberg.org/cache/epub/84/pg84.cover.medium.jpg",
                url = "frankestein"
            ),
            Book(
                title = "Dracula",
                author = "Bram Stoker",
                year = "1897",
                summary = "The classic tale of Count Dracula's attempt to move from Transylvania to England.",
                imageUrl = "https://www.gutenberg.org/cache/epub/345/pg345.cover.medium.jpg",
                url = "dracula"
            ),
            Book(
                title = "The Picture of Dorian Gray",
                author = "Oscar Wilde",
                year = "1890",
                summary = "A young man remains eternally young while his portrait ages and reflects his sins.",
                imageUrl = "https://www.gutenberg.org/cache/epub/174/pg174.cover.medium.jpg",
                url = "the_picture_of_dorian_gray"
            ),
            Book(
                title = "Moby Dick",
                author = "Herman Melville",
                year = "1851",
                summary = "Captain Ahab obsessively hunts the giant white whale, Moby Dick.",
                imageUrl = "https://www.gutenberg.org/cache/epub/2701/pg2701.cover.medium.jpg",
                url = "moby_dick"
            ),
            Book(
                title = "The War of the Worlds",
                author = "H. G. Wells",
                year = "1898",
                summary = "Martians invade Earth in this classic science fiction novel.",
                imageUrl = "https://www.gutenberg.org/cache/epub/36/pg36.cover.medium.jpg",
                url = "the_war_of_the_worlds"
            )
        )

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            books.map { it.title }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val book = books[position]
                bookTitle.text = book.title
                bookSubtitle.text = book.summary

                Glide.with(this@BooksFragment)
                    .load(book.imageUrl)
                    .into(bookImage)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        openButton.setOnClickListener {
            val selectedBook = books[spinner.selectedItemPosition]

            val readFragment = ReadFragment()
            val bundle = Bundle()
            bundle.putString("bookepub",selectedBook.url)
            readFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_container, readFragment)
                .addToBackStack(null)
                .commit()
        }


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
                // No es necesario hacer nada aquí si quieres que se use la fuente por defecto.
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
         * @return A new instance of fragment BooksFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BooksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}