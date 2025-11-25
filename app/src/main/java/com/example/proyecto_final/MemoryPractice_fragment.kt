package com.example.proyecto_final

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MemoryPractice_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemoryPractice_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var tableLayout: TableLayout
    private lateinit var inflater: LayoutInflater
    private var param1: String? = null
    private var param2: String? = null

    private var seleccionadas: Int = 0
    private var paresEncontrados = 0
    lateinit var primeraSelecCard: GameCard
    lateinit var segundaSelecCard: GameCard
    lateinit var primeraSelecView: View
    lateinit var segundaSelecView: View
    private var cartasparajugar = mutableListOf<GameCard>()
    lateinit var countDownTimer: CountDownTimer
    lateinit var progressbar: ProgressBar
    lateinit var progressText: TextView
    val totalTimeMillis: Long = 60000
    val intervalMillis: Long = 200
    private var tiempoRestante = totalTimeMillis


    // Constantes del tablero
    private val FILAS = 4
    private val COLUMNAS = 3
    private val NUMERO_DE_PARES = (FILAS * COLUMNAS) /2


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
        var view = inflater.inflate(R.layout.fragment_memorypractice, container, false)
        var backgames = view.findViewById<ImageView>(R.id.back_games)
        progressbar = view.findViewById<ProgressBar>(R.id.countdownProgressBar)
        progressText = view.findViewById<TextView>(R.id.progresstext)

        this.inflater = inflater
        this.tableLayout = view.findViewById(R.id.tableLayout)

        cargarBarajaInicial()
        tableLayout.removeAllViews()
        paresEncontrados = 0
        seleccionadas = 0
        progressbar.max = 100
        progressbar.progress = 100

        countDownTimer = object : CountDownTimer(totalTimeMillis, intervalMillis) {

            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished * 100 / totalTimeMillis).toInt()
                val segundos = 60 * progress / 100
                progressText.text = segundos.toString()
                progressbar.progress = progress
                println(progress)
            }

            override fun onFinish() {
                progressbar.progress = 0
                (requireActivity() as MainActivity).replaceFragmentFromFragment(GamesFragment(),R.id.bottom_games)
            }
        }

        countDownTimer.start()

        // obtenemos nuestros 6 pares de la baraja
        val cartasParaLaPartida = obtenerCartasDeBaraja(NUMERO_DE_PARES)

        // si no hay suficientes cartas de la baraja se completa el juego
        if (cartasParaLaPartida.isEmpty()) {
            Toast.makeText(requireContext(), "¡Has completado todas las palabras!", Toast.LENGTH_LONG).show()
             return view
        }

        // barajeamos las cartas
        cartasParaLaPartida.shuffle()

        //construimos el tablero
        construirTablero(cartasParaLaPartida)

        backgames.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentFromFragment(GamesFragment(),R.id.bottom_games)
        }

        return view
    }


    private fun iniciarContadorNuevo(){
        progressbar.max = 100
        progressbar.progress = 100

        countDownTimer = object : CountDownTimer(totalTimeMillis, intervalMillis) {

            override fun onTick(millisUntilFinished: Long) {
                val progress = (millisUntilFinished * 100 / totalTimeMillis).toInt()
                progressbar.progress = progress
                val segundos = 60 * progress / 100
                progressText.text = segundos.toString()
                println(progress)
            }

            override fun onFinish() {
                progressbar.progress = 0
                (requireActivity() as MainActivity).replaceFragmentFromFragment(GamesFragment(),R.id.bottom_games)
            }
        }
        countDownTimer.start()
    }


    private fun iniciarNuevoJuego() {
        tableLayout.removeAllViews()
        paresEncontrados = 0
        seleccionadas = 0
        tiempoRestante = totalTimeMillis

        countDownTimer.cancel()
        iniciarContadorNuevo()

        // obtenemos nuestros 6 pares de la baraja
        val cartasParaLaPartida = obtenerCartasDeBaraja(NUMERO_DE_PARES)

        // si no hay suficientes cartas de la baraja se completa el juego
        if (cartasParaLaPartida.isEmpty()) {
            Toast.makeText(requireContext(), "¡Has completado todas las palabras!", Toast.LENGTH_LONG).show()
            return
        }

        // barajeamos las cartas
        cartasParaLaPartida.shuffle()

        //construimos el tablero
        construirTablero(cartasParaLaPartida)
    }
    private fun cargarBarajaInicial() {
        // limpiamos nuestras 12 cartas
        cartasparajugar.clear()
        // esto se carga desde la base de datos
        val dbHelper = BDhelper(requireContext())
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT palabra, traduccion FROM Palabras", null)
        if (cursor.moveToFirst()) {
            do {
                val palabra = cursor.getString(0)
                val traduccion = cursor.getString(1)
                cartasparajugar.add(GameCard(palabra, traduccion, false, false))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        cartasparajugar.shuffle()
    }
    private fun obtenerCartasDeBaraja(cantidadDePares: Int): MutableList<GameCard> {
        if (cartasparajugar.size < cantidadDePares) {
            return mutableListOf() // retorna lista vacía si no hay suficientes.
        }

        val cartasJuegoActual = mutableListOf<GameCard>()
        for (i in 0 until cantidadDePares) {
            val parOriginal = cartasparajugar.removeAt(cartasparajugar.lastIndex)

            cartasJuegoActual.add(parOriginal)
            cartasJuegoActual.add(GameCard(parOriginal.trad, parOriginal.word, false, false))
        }
        return cartasJuegoActual
    }

    private fun construirTablero(listaDeCartas: List<GameCard>) {

        // al crear un nuevo tablero, creamos 5 filas (tablerows) y 3 columnas (carViews)
        var cardIndex = 0
        for (i in 0 until FILAS) {
            val row = TableRow(requireContext())
            row.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT, 1f)
            for (j in 0 until COLUMNAS) {
                val cardView = inflater.inflate(R.layout.table_card, row, false)
                val params = TableRow.LayoutParams(0, 350, 1f)
                cardView.layoutParams = params
                configurarCarta(cardView, listaDeCartas[cardIndex])
                row.addView(cardView)
                cardIndex++
            }
            tableLayout.addView(row)
        }
    }
    private fun restarTiempo(millis: Long) {
        tiempoRestante -= millis
        if (tiempoRestante < 0) tiempoRestante = 0
        if (tiempoRestante >= 59000) tiempoRestante = 59000
        countDownTimer.cancel()
        countDownTimer = object : CountDownTimer(tiempoRestante, intervalMillis) {

            override fun onTick(millisUntilFinished: Long) {
                tiempoRestante = millisUntilFinished

                val progress = (millisUntilFinished * 100 / totalTimeMillis).toInt()
                val segundos = 60 * progress / 100
                progressText.text = segundos.toString()
                progressbar.progress = progress
            }

            override fun onFinish() {
                progressbar.progress = 0
                (requireActivity() as MainActivity).replaceFragmentFromFragment(GamesFragment(), R.id.bottom_games)
            }
        }
        countDownTimer.start()
    }
    private fun configurarCarta(cardView: View,card: GameCard){
            val imageView = cardView.findViewById<ImageView>(R.id.imageView)
            val textView = cardView.findViewById<TextView>(R.id.textView)
            val borde = cardView.findViewById<ConstraintLayout>(R.id.borde)

             imageView.scaleType = ImageView.ScaleType.FIT_XY

            imageView.visibility = View.VISIBLE
            textView.visibility = View.GONE
            textView.text = card.word
            cardView.isClickable = true

            cardView.setOnClickListener { clickedView ->
                if (seleccionadas >= 2 || !clickedView.isClickable) return@setOnClickListener

                imageView.visibility = View.INVISIBLE
                textView.visibility = View.VISIBLE
                borde.setBackgroundResource(R.drawable.border_selected)
                clickedView.isClickable = false // Evitar doble clic
                seleccionadas++

                if (seleccionadas == 1) {
                    primeraSelecCard = card
                    primeraSelecView = clickedView
                } else { // seleccionadas == 2
                    segundaSelecCard = card
                    segundaSelecView = clickedView

                    if (primeraSelecCard.word == segundaSelecCard.trad) {
                        // se consiguio par
                        paresEncontrados++
                        primeraSelecView.isClickable = false
                        segundaSelecView.isClickable = false
                        progressText.setTextColor(resources.getColor(R.color.colorCorrect))

                        progressText.animate()
                            .scaleX(1.5f)
                            .scaleY(1.5f)
                            .setDuration(200)
                            .withEndAction {
                                progressText.setTextColor(resources.getColor(R.color.colorTextPrimary))
                                progressText.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(300)
                                    .start()
                            }

                        progressbar.progressTintList =
                            ColorStateList.valueOf(resources.getColor(R.color.colorCorrect))

                        progressbar.animate()
                            .scaleX(1.05f)
                            .scaleY(2.5f)
                            .setDuration(200)
                            .withEndAction {
                                restarTiempo(-3000)
                                progressbar.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(300)
                                    .withEndAction {
                                        progressbar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.colorBorder))
                                    }
                                    .start()
                            }
                            .start()
                        if (paresEncontrados == NUMERO_DE_PARES) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                iniciarNuevoJuego()
                            }, 1200)
                        }
                        seleccionadas = 0
                    } else {
                        // par incorrecto

                        Handler(Looper.getMainLooper()).postDelayed({
                            (primeraSelecView.findViewById<ImageView>(R.id.imageView)).visibility = View.VISIBLE
                            (segundaSelecView.findViewById<ImageView>(R.id.imageView)).visibility = View.VISIBLE
                            (primeraSelecView.findViewById<TextView>(R.id.textView)).visibility = View.GONE
                            (segundaSelecView.findViewById<TextView>(R.id.textView)).visibility = View.GONE
                            (segundaSelecView.findViewById<ConstraintLayout>(R.id.borde)).setBackgroundResource(R.drawable.border_card)
                            (primeraSelecView.findViewById<ConstraintLayout>(R.id.borde)).setBackgroundResource(R.drawable.border_card)
                            primeraSelecView.isClickable = true
                            segundaSelecView.isClickable = true
                            seleccionadas = 0
                            progressbar.progressTintList =
                                ColorStateList.valueOf(resources.getColor(R.color.colorError))

                            progressText.setTextColor(resources.getColor(R.color.colorError))

                            progressText.animate()
                                .scaleX(1.5f)
                                .scaleY(1.5f)
                                .setDuration(200)
                                .withEndAction {
                                    progressText.setTextColor(resources.getColor(R.color.colorTextPrimary))
                                    progressText.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(300)
                                        .start()
                                }


                            progressbar.animate()
                                .scaleX(1.05f)
                                .scaleY(2.5f)
                                .setDuration(200)
                                .withEndAction {
                                    progressbar.progressTintList = ColorStateList.valueOf(resources.getColor(R.color.colorBorder))
                                    restarTiempo(3000)
                                    progressbar.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(300)
                                        .withEndAction {
                                        }
                                        .start()
                                }
                                .start()
                        }, 1000)
                    }
                }
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MemoryPractice_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MemoryPractice_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}