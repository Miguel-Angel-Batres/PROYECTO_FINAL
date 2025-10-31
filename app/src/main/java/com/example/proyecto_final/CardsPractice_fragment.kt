package com.example.proyecto_final

import StackLayoutManager
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackView
import java.util.Stack
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CardsPractice_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardsPractice_fragment : Fragment() {
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
        var view =  inflater.inflate(R.layout.fragment_cards_practice, container, false)
        var back = view.findViewById<ImageView>(R.id.back_games)
        val recyclerview = view.findViewById<RecyclerView>(R.id.recyclergamecards)
        val regresarcarta = view.findViewById<ImageView>(R.id.regresarcarta)
        val progreso = view.findViewById<TextView>(R.id.progreso_txt)
        val textocorrectos = view.findViewById<TextView>(R.id.textoCorrectos)
        val textoincorrectos = view.findViewById<TextView>(R.id.textoIncorrectos)

        progreso.text = "1/100"

        val listaDeDatos = MutableList(50) { i ->
            GameCard("Palabra ${i + 1}", R.drawable.ic_launcher_background)
        }
        val listaCorrectos : MutableList<GameCard> = mutableListOf()
        val listaIncorrectos : MutableList<GameCard> = mutableListOf()


        var adapter = GameCardAdapter(listaDeDatos)


        val layoutManager = StackLayoutManager(requireContext())
        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter
        regresarcarta.setOnClickListener {
            (recyclerview.layoutManager as? StackLayoutManager)?.apply {
                if (topPosition > 0) {
                    topPosition--
                    var progresoreal = topPosition + 1
                    progreso.text = progresoreal.toString() + "/100"
                    if (listaCorrectos.isNotEmpty() && listaCorrectos.contains(listaDeDatos[topPosition])) {
                        listaCorrectos.removeAt(listaCorrectos.size - 1)
                    } else if (listaIncorrectos.isNotEmpty() && listaIncorrectos.contains(listaDeDatos[topPosition])) {
                        listaIncorrectos.removeAt(listaIncorrectos.size - 1)
                    }
                    textocorrectos.text = listaCorrectos.size.toString()
                    textoincorrectos.text = listaIncorrectos.size.toString()
                    requestLayout()

                }
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // no movemos las cartas verticalmente
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                listaDeDatos.removeAt(position)
//                adapter.notifyItemRemoved(position)
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        listaIncorrectos.add(listaDeDatos[viewHolder.adapterPosition])
                        textoincorrectos.text = listaIncorrectos.size.toString()
                    }
                    ItemTouchHelper.RIGHT -> {
                        listaCorrectos.add(listaDeDatos[viewHolder.adapterPosition])
                        textocorrectos.text = listaCorrectos.size.toString()
                    }
                }
                (recyclerview.layoutManager as? StackLayoutManager)?.apply {
                    topPosition++
                    var progresoreal = topPosition + 1
                    progreso.text = progresoreal.toString() + "/100"
                    requestLayout()
                }
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val rotation = 15f * dX / recyclerView.width
                val childCount = recyclerView.childCount
                for (i in 0 until childCount) {
                    val child = recyclerView.getChildAt(i)
                    child.rotation = if (child == viewHolder.itemView) rotation else 0f
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }


        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerview)


        back.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentFromFragmentNoBarChange(GamesFragment())
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CardsPractice_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CardsPractice_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}