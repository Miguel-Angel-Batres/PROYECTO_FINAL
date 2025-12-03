package com.example.proyecto_final

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class BDhelper(
    context: Context
): SQLiteOpenHelper(context, "BaseCentral", null, 2) {

    private val leccionIds = mutableListOf<Long>()
    private val opcionIdsPorLeccion = mutableMapOf<String, MutableList<Long>>()
    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL("DROP TABLE IF EXISTS Palabras")
        p0!!.execSQL("DROP TABLE IF EXISTS Lecciones")
        p0!!.execSQL("DROP TABLE IF EXISTS Opciones")
        p0!!.execSQL("DROP TABLE IF EXISTS Frases")
        p0!!.execSQL(
            """
             CREATE TABLE Palabras (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                palabra TEXT NOT NULL,
                traduccion TEXT,
                imagen_path TEXT
            )
            """
        )
        // Tabla Lecciones
        p0.execSQL("""
            CREATE TABLE Lecciones (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tema_id TEXT NOT NULL,
                titulo TEXT NOT NULL,
                subtitulo TEXT,
                imagen INTEGER,
                completada INTEGER DEFAULT 0
            );
        """.trimIndent())

        // Tabla Opciones
        p0.execSQL("""
            CREATE TABLE Opciones (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                texto TEXT NOT NULL
            );
        """.trimIndent())

        // Tabla Frases
        p0.execSQL("""
            CREATE TABLE Frases (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                leccion_id INTEGER NOT NULL,
                frase_incompleta TEXT NOT NULL,
                correcta_id INTEGER NOT NULL,
                opcion_a_id INTEGER NOT NULL,
                opcion_b_id INTEGER NOT NULL,
                opcion_c_id INTEGER NOT NULL,
                opcion_d_id INTEGER NOT NULL,
                FOREIGN KEY (leccion_id) REFERENCES Lecciones(id),
                FOREIGN KEY (correcta_id) REFERENCES Opciones(id),
                FOREIGN KEY (opcion_a_id) REFERENCES Opciones(id),
                FOREIGN KEY (opcion_b_id) REFERENCES Opciones(id),
                FOREIGN KEY (opcion_c_id) REFERENCES Opciones(id),
                FOREIGN KEY (opcion_d_id) REFERENCES Opciones(id)
            );
        """.trimIndent())
        p0.execSQL("""
            CREATE TABLE Logros (
                reward_id INTEGER PRIMARY KEY,
                is_unlocked INTEGER DEFAULT 0,
                xp_awarded INTEGER DEFAULT 0
            );
        """.trimIndent())
        generatePalabras(p0)
        generateLessons(p0)
        generateOptions(p0)
        generatePhrasesLeccion1_1(p0)
        generatePhrasesLeccion1_2(p0)
        generatePhrasesLeccion2_1(p0)
        generatePhrasesLeccion2_2(p0)
        generatePhrasesLeccion3_1(p0)
        generatePhrasesLeccion3_2(p0)
        generatePhrasesLeccion4_1(p0)
        generatePhrasesLeccion4_2(p0)
        generatePhrasesLeccion5_1(p0)
        generatePhrasesLeccion5_2(p0)
        generatePhrasesLeccion6_1(p0)
        generatePhrasesLeccion6_2(p0)
    }
    private fun generatePalabras(db: SQLiteDatabase){
        val palabras = listOf(
            "achieve" to "lograr",
            "acknowledge" to "reconocer",
            "acquire" to "adquirir",
            "adapt" to "adaptar",
            "adjust" to "ajustar",
            "advise" to "aconsejar",
            "allowance" to "asignación",
            "ambiguous" to "ambiguo",
            "analyze" to "analizar",
            "anticipate" to "anticipar",
            "approach" to "enfoque",
            "approve" to "aprobar",
            "argue" to "discutir",
            "arrange" to "organizar",
            "assert" to "afirmar",
            "assess" to "evaluar",
            "assign" to "asignar",
            "assume" to "suponer",
            "assure" to "asegurar",
            "astonishing" to "asombroso",
            "attempt" to "intentar",
            "attitude" to "actitud",
            "aware" to "consciente",
            "balance" to "equilibrio",
            "barely" to "apenas",
            "behavior" to "comportamiento",
            "belong" to "pertenecer",
            "beneath" to "debajo de",
            "benefit" to "beneficio",
            "bias" to "sesgo",
            "blame" to "culpar",
            "bother" to "molestar",
            "brief" to "breve",
            "broadcast" to "transmitir",
            "burden" to "carga",
            "capable" to "capaz",
            "careless" to "descuidado",
            "cease" to "cesar",
            "challenge" to "desafío",
            "circumstance" to "circunstancia",
            "collapse" to "colapsar",
            "commitment" to "compromiso",
            "compare" to "comparar",
            "complain" to "quejarse",
            "concern" to "preocupación",
            "conclude" to "concluir",
            "conduct" to "conducir",
            "confirm" to "confirmar",
            "confuse" to "confundir",
            "consequence" to "consecuencia",
            "consider" to "considerar",
            "consume" to "consumir",
            "convince" to "convencer",
            "cope" to "sobrellevar",
            "criticize" to "criticar",
            "crucial" to "crucial",
            "curious" to "curioso",
            "declare" to "declarar",
            "decrease" to "disminuir",
            "defeat" to "derrotar",
            "define" to "definir",
            "deliberate" to "deliberado",
            "deny" to "negar",
            "depend" to "depender",
            "derive" to "derivar",
            "deserve" to "merecer",
            "determine" to "determinar",
            "develop" to "desarrollar",
            "devote" to "dedicar",
            "disappear" to "desaparecer",
            "disappoint" to "decepcionar",
            "discuss" to "discutir",
            "distinguish" to "distinguir",
            "doubt" to "duda",
            "effort" to "esfuerzo",
            "efficient" to "eficiente",
            "emerge" to "surgir",
            "emphasize" to "enfatizar",
            "enable" to "habilitar",
            "encourage" to "animar",
            "enhance" to "mejorar",
            "ensure" to "asegurar",
            "environment" to "entorno",
            "establish" to "establecer",
            "estimate" to "estimar",
            "evaluate" to "evaluar",
            "evidence" to "evidencia",
            "exceed" to "exceder",
            "exclude" to "excluir",
            "expand" to "expandir",
            "expectation" to "expectativa",
            "explain" to "explicar",
            "expose" to "exponer",
            "extend" to "extender",
            "failure" to "fracaso",
            "fascinating" to "fascinante",
            "feature" to "característica",
            "fierce" to "feroz",
            "forbid" to "prohibir",
            "force" to "forzar",
            "foreign" to "extranjero",
            "former" to "anterior",
            "fulfill" to "cumplir",
            "furthermore" to "además",
            "gain" to "obtener",
            "gather" to "reunir",
            "generate" to "generar",
            "genuine" to "auténtico",
            "hesitate" to "dudar",
            "highlight" to "resaltar",
            "imply" to "implicar",
            "improve" to "mejorar",
            "include" to "incluir",
            "increase" to "aumentar",
            "indicate" to "indicar",
            "influence" to "influencia",
            "initial" to "inicial",
            "insist" to "insistir",
            "inspire" to "inspirar",
            "intend" to "intentar",
            "interpret" to "interpretar",
            "introduce" to "presentar",
            "involve" to "involucrar",
            "issue" to "tema",
            "justify" to "justificar",
            "knowledge" to "conocimiento",
            "lack" to "carecer",
            "maintain" to "mantener",
            "manage" to "gestionar",
            "measure" to "medir",
            "mention" to "mencionar",
            "nevertheless" to "sin embargo",
            "notice" to "notar",
            "obtain" to "obtener",
            "occur" to "ocurrir",
            "offer" to "ofrecer",
            "operate" to "operar",
            "oppose" to "oponerse",
            "opportunity" to "oportunidad")
        for ((english, spanish) in palabras) {
            val values = ContentValues()
            values.put("palabra", english)
            values.put("traduccion", spanish)
            db.insert("palabras", null, values)
        }
    }
    private fun generateLessons(db: SQLiteDatabase){
        val lecciones = listOf(
            // Verbs and Tenses
            arrayOf("verbs_tenses", "Lesson 1: Present Simple", "Aprende el presente simple en inglés.", R.drawable.zorro1),
            arrayOf("verbs_tenses", "Lesson 2: Past Simple", "Aprende el pasado simple en inglés.", R.drawable.zorro2),

            // Nouns, Articles and Adjectives
            arrayOf("nouns_articles", "Lesson 1: Articles", "Aprende los artículos en inglés.", R.drawable.zorro3),
            arrayOf("nouns_articles", "Lesson 2: Nouns & Adjectives", "Aprende sustantivos y adjetivos en inglés.", R.drawable.zorro4),

            // Pronouns and Possessives
            arrayOf("pronouns_possessives", "Lesson 1: Personal Pronouns", "Aprende los pronombres personales.", R.drawable.zorro5),
            arrayOf("pronouns_possessives", "Lesson 2: Possessives", "Aprende los posesivos en inglés.", R.drawable.zorro6),

            // Prepositions
            arrayOf("prepositions", "Lesson 1: Prepositions of Place", "Descubre cómo indicar ubicación.", R.drawable.zorro3),
            arrayOf("prepositions", "Lesson 2: Prepositions of Time", "Aprende a usar preposiciones de tiempo.", R.drawable.zorro2),

            // Compound Sentences and Connectors
            arrayOf("compound_sentences", "Lesson 1: Coordinating Conjunctions", "Conecta ideas con 'and', 'but', 'or'.", R.drawable.zorro1),
            arrayOf("compound_sentences", "Lesson 2: Subordinating Conjunctions", "Usa conectores subordinantes como 'because', 'although'.", R.drawable.zorro4),

            // Questions and Negations
            arrayOf("questions_negations", "Lesson 1: Yes/No Questions", "Aprende a construir preguntas simples.", R.drawable.zorro2),
            arrayOf("questions_negations", "Lesson 2: Negations", "Construye oraciones negativas correctamente.", R.drawable.zorro5)
        )

        lecciones.forEach { l ->
            val cv = ContentValues().apply {
                put("tema_id", l[0] as String)
                put("titulo", l[1] as String)
                put("subtitulo", l[2] as String)
                put("imagen", l[3] as Int)
                put("completada", 0)
            }
            leccionIds.add(db.insert("Lecciones", null, cv))
        }

    }

    private fun generateOptions(db: SQLiteDatabase){
        val opcionesPorLeccion = mapOf(
            // Verbs and Tenses
            "verbs_tenses" to listOf(
                "is", "are", "its", "or",
                "goes", "go", "going", "gone",
                "have", "has", "had", "will",
                "do", "does", "did", "done"
            ),
            // Nouns, Articles and Adjectives
            "nouns_articles" to listOf(
                "a", "an", "the", "some",
                "apple", "book", "car", "house",
                "red", "big", "small", "beautiful",
                "my", "your", "our", "their"
            ),
            // Pronouns and Possessives
            "pronouns_possessives" to listOf(
                "I", "you", "he", "she",
                "we", "they", "my", "your",
                "his", "her", "our", "their",
                "me", "him", "her", "us"
            ),
            // Prepositions
            "prepositions" to listOf(
                "in", "on", "at", "from",
                "to", "with", "about", "under",
                "over", "between", "among", "behind",
                "near", "beside", "above", "below"
            ),
            // Compound Sentences and Connectors
            "compound_sentences" to listOf(
                "and", "but", "or", "because",
                "although", "so", "for", "nor",
                "yet", "since", "unless", "while",
                "after", "before", "if", "when"
            ),
            // Questions and Negations
            "questions_negations" to listOf(
                "do", "does", "did", "is",
                "are", "was", "were", "not",
                "never", "always", "sometimes", "can",
                "will", "shall", "would", "could"
            )
        )

        opcionesPorLeccion.forEach { (leccion, opciones) ->
            val ids = mutableListOf<Long>()
            opciones.forEach { texto ->
                val cv = ContentValues().apply { put("texto", texto) }
                val id = db.insert("Opciones", null, cv)
                ids.add(id)
            }
            opcionIdsPorLeccion[leccion] = ids
        }
    }
    //
    fun updateLeccionCompletada(db: SQLiteDatabase, idLeccion: Int) {
        val values = ContentValues().apply {
            put("completada", 1) // Marcar como completada
        }

        val selection = "id = ?"
        val selectionArgs = arrayOf(idLeccion.toString())

        val count = db.update(
            "Lecciones",
            values,
            selection,
            selectionArgs
        )
        Log.d("BDHelper", "Lección $idLeccion actualizada. Filas afectadas: $count")
    }
    //progreso global
    fun getGlobalProgressStats(db: SQLiteDatabase): Pair<Int, Int> {
        var completedCount = 0
        var totalCount = 0

        // Consulta para contar el total de lecciones
        val totalCursor = db.rawQuery("SELECT COUNT(*) FROM Lecciones", null)
        if (totalCursor.moveToFirst()) {
            totalCount = totalCursor.getInt(0)
        }
        totalCursor.close()

        // Consulta para contar las lecciones marcadas como completadas
        val completedCursor = db.rawQuery("SELECT COUNT(*) FROM Lecciones WHERE completada = 1", null)
        if (completedCursor.moveToFirst()) {
            completedCount = completedCursor.getInt(0)
        }
        completedCursor.close()

        return Pair(completedCount, totalCount)
    }
    fun isSectionComplete(db: SQLiteDatabase, temaId: String): Boolean {
        // Consulta para contar las lecciones del tema que NO están completadas (completada = 0)
        val query = "SELECT COUNT(id) FROM Lecciones WHERE tema_id = ? AND completada = 0"

        val cursor = db.rawQuery(query, arrayOf(temaId))
        var incompleteCount = 0

        if (cursor.moveToFirst()) {
            incompleteCount = cursor.getInt(0)
        }
        cursor.close()

        // Si la cuenta de lecciones incompletas es 0, toda la sección está completa.
        return incompleteCount == 0
    }


    // El mapeo de secciones a sus claves cortas de la DB (tema_id)
    private val sectionTopicMap = mapOf(
        "Verbos y Tiempos" to "verbs_tenses",
        "Nouns, Articles and Adjectives" to "nouns_articles",
        "Pronouns and Possessives" to "pronouns_possessives",
        "Prepositions" to "prepositions",
        "Compound Sentences and Connectors" to "compound_sentences",
        "Questions and Negations" to "questions_negations"
    )


    fun getRewardsList(): List<Reward> {
        return listOf(
            Reward(1, "Iniciador", "Completa tu primera lección.", 50, "CompletedLessonCount:1"),

            Reward(2, "Maestro Verbal", "Completa las lecciones de la sección 'Verbos y Tiempos'.", 150, "CompletedSection:verbs_tenses"),
            Reward(3, "Arquitecto de Palabras", "Completa la sección 'Nouns, Articles and Adjectives'.", 150, "CompletedSection:nouns_articles"),
            Reward(4, "Identidad Total", "Completa la sección 'Pronombres y Posesivos'.", 100, "CompletedSection:pronouns_possessives"),
            Reward(5, "Conector Lógico", "Completa la sección 'Compound Sentences and Connectors'.", 100, "CompletedSection:compound_sentences"),
            Reward(6, "Navegante Espacial", "Completa la sección 'Preposiciones'.", 80, "CompletedSection:prepositions"),
            Reward(7, "Cazador de Preguntas", "Completa la sección 'Questions and Negations'.", 120, "CompletedSection:questions_negations"),

            Reward(8, "Mente Maestra (100%)", "Obtén un puntaje perfecto (100%) en cualquier lección.", 200, "PerfectScore:1"),
            Reward(9, "El Gran Gramático", "Completa TODAS las secciones principales.", 300, "CompletedAllSections:True")
        ).map { reward ->
            // Inicializa todos los logros como BLOQUEADOS para que la lógica de verificación los actualice.
            reward.apply { isUnlocked = false }
        }
    }
    fun getAllUniqueTemaIds(db: SQLiteDatabase): List<String> {
        val temaIds = mutableListOf<String>()
        // Consulta SQL para obtener solo los valores distintos de la columna tema_id
        val cursor = db.rawQuery("SELECT DISTINCT tema_id FROM Lecciones", null)

        if (cursor.moveToFirst()) {
            do {
                temaIds.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return temaIds
    }

    private fun generatePhrasesLeccion1_1(db: SQLiteDatabase){
        val leccionId = leccionIds[0] // Primera lección
        val opcionesLeccion = opcionIdsPorLeccion["verbs_tenses"] ?: return

        val frases = listOf(
            Triple(
                "I am introducing myself in class. My name _ Miguel",
                opcionesLeccion[0],
                listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])
            ),
            Triple(
                "Every morning, she leaves home early. She _ to school every day",
                opcionesLeccion[4],
                listOf(opcionesLeccion[4], opcionesLeccion[5], opcionesLeccion[6], opcionesLeccion[7])
            ),
            Triple(
                "Before going to sleep, I like to relax. I _ a book every night",
                opcionesLeccion[8],
                listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])
            ),
            Triple(
                "He trains a lot for the marathon. He _ very fast",
                opcionesLeccion[0],
                listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])
            ),
            Triple(
                "The kids are outside right now. They _ playing football",
                opcionesLeccion[1],
                listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])
            ),
            Triple(
                "Every day before work, we eat something together. We _ breakfast at 7am",
                opcionesLeccion[9],
                listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])
            ),
            Triple(
                "Listen! I think the pet escaped again. The cat _ on the roof",
                opcionesLeccion[0],
                listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])
            ),
            Triple(
                "Yesterday I needed some groceries. I _ to the store yesterday",
                opcionesLeccion[10],
                listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])
            ),
            Triple(
                "She prefers tea instead of coffee. She _ not like coffee",
                opcionesLeccion[13],
                listOf(opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[14], opcionesLeccion[15])
            ),
            Triple(
                "He saved money for months. He _ a new car",
                opcionesLeccion[9],
                listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])
            )
        )


        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion1_2(db: SQLiteDatabase){
        val leccionId = leccionIds[1] // Segunda lección: Past Simple
        val opcionesLeccion = opcionIdsPorLeccion["verbs_tenses"] ?: return

        val frases = listOf(
            Triple("I _ to the store yesterday", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("She _ a beautiful dress last week", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("They _ at the park on Sunday", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("He _ very tired after the game", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("We _ a great time on our trip", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("The cat _ on the roof yesterday", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("I _ breakfast at 8am this morning", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("She _ not feel well yesterday", opcionesLeccion[11], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("He _ a new car last month", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("They _ very happy with the results", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11]))
        )
        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion2_1(db: SQLiteDatabase){
        val leccionId = leccionIds[2] // Primera lección del tema nouns_articles
        val opcionesLeccion = opcionIdsPorLeccion["nouns_articles"] ?: return

        val frases = listOf(
            Triple("I have _ apple", opcionesLeccion[1], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("She bought _ car", opcionesLeccion[2], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("He needs _ umbrella", opcionesLeccion[1], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("We saw _ dog in the park", opcionesLeccion[2], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("She has _ idea", opcionesLeccion[1], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("He wants _ orange", opcionesLeccion[1], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("I need _ pen", opcionesLeccion[0], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("They are reading _ book", opcionesLeccion[2], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("We bought _ house last year", opcionesLeccion[2], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])),
            Triple("I saw _ cat on the roof", opcionesLeccion[2], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3]))
        )

        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion2_2(db: SQLiteDatabase){
        val leccionId = leccionIds[3] // Segunda lección del tema nouns_articles
        val opcionesLeccion = opcionIdsPorLeccion["nouns_articles"] ?: return

        val frases = listOf(
            Triple("I have a _ apple", opcionesLeccion[8], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("She bought a _ car", opcionesLeccion[9], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("He has a _ house", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("They saw a _ dog", opcionesLeccion[11], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("I need a _ pen", opcionesLeccion[8], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("She has a _ idea", opcionesLeccion[11], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("We are reading a _ book", opcionesLeccion[9], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("He wants a _ orange", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("They bought a _ house", opcionesLeccion[10], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])),
            Triple("I saw a _ cat", opcionesLeccion[9], listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11]))
        )

        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion3_1(db: SQLiteDatabase){
        val leccionId = leccionIds[4]
        val opcionesLeccion = opcionIdsPorLeccion["pronouns_possessives"] ?: return

        val frases = listOf(
            Triple(
                "We met yesterday and we got along really well. _ am your friend",
                opcionesLeccion[0],
                listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[3])
            ),
            Triple(
                "I bought this yesterday at the bookstore. This is _ book",
                opcionesLeccion[6],
                listOf(opcionesLeccion[6], opcionesLeccion[7], opcionesLeccion[8], opcionesLeccion[9])
            ),
            Triple(
                "Look at John! _ is playing soccer",
                opcionesLeccion[2],
                listOf(opcionesLeccion[0], opcionesLeccion[2], opcionesLeccion[3], opcionesLeccion[5])
            ),
            Triple(
                "We respect our instructor a lot. We love _ teacher",
                opcionesLeccion[10],
                listOf(opcionesLeccion[10], opcionesLeccion[11], opcionesLeccion[6], opcionesLeccion[7])
            ),
            Triple(
                "Anna wanted to surprise me, so she wrapped a present. She gave the gift to _",
                opcionesLeccion[14],
                listOf(opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[14], opcionesLeccion[15])
            ),
            Triple(
                "Look, the whole team is ready. _ are going to the park",
                opcionesLeccion[5],
                listOf(opcionesLeccion[1], opcionesLeccion[4], opcionesLeccion[5], opcionesLeccion[0])
            ),
            Triple(
                "We have lived here for many years. This is _ house",
                opcionesLeccion[10],
                listOf(opcionesLeccion[10], opcionesLeccion[11], opcionesLeccion[6], opcionesLeccion[7])
            ),
            Triple(
                "Carlos forgot his notebook. The book belongs to _",
                opcionesLeccion[13],
                listOf(opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[14], opcionesLeccion[15])
            ),
            Triple(
                "Maria passed her final exam today. _ is very happy today",
                opcionesLeccion[3],
                listOf(opcionesLeccion[2], opcionesLeccion[3], opcionesLeccion[0], opcionesLeccion[1])
            ),
            Triple(
                "The family can't find them anywhere. They lost _ keys",
                opcionesLeccion[11],
                listOf(opcionesLeccion[10], opcionesLeccion[11], opcionesLeccion[6], opcionesLeccion[7])
            )
        )


        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion3_2(db: SQLiteDatabase){
        val leccionId = leccionIds[5] // Segunda lección de pronouns_possessives
        val opcionesLeccion = opcionIdsPorLeccion["pronouns_possessives"] ?: return

        val frases = listOf(
            Triple(
                "I bought this yesterday with my own money. This is _ car, not yours",
                opcionesLeccion[6],
                listOf(opcionesLeccion[6], opcionesLeccion[7], opcionesLeccion[10], opcionesLeccion[11])
            ),

            Triple(
                "Maria is a nurse and works at the hospital. _ mother is a doctor",
                opcionesLeccion[9],
                listOf(opcionesLeccion[9], opcionesLeccion[8], opcionesLeccion[7], opcionesLeccion[6])
            ),

            Triple(
                "We are organizing a party this weekend. We invited _ to the party",
                opcionesLeccion[13],
                listOf(opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[14], opcionesLeccion[15])
            ),

            Triple(
                "The children are very excited. They are visiting _ grandparents today",
                opcionesLeccion[11],
                listOf(opcionesLeccion[10], opcionesLeccion[11], opcionesLeccion[6], opcionesLeccion[7])
            ),

            Triple(
                "John left home quickly this morning. He forgot _ wallet at home",
                opcionesLeccion[8],
                listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[6], opcionesLeccion[7])
            ),

            Triple(
                "The class is almost over. The teacher is talking to _",
                opcionesLeccion[12],
                listOf(opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[14], opcionesLeccion[15])
            ),

            Triple(
                "We worked on the project together. We finished _ homework early",
                opcionesLeccion[10],
                listOf(opcionesLeccion[10], opcionesLeccion[6], opcionesLeccion[11], opcionesLeccion[7])
            ),

            Triple(
                "She found the book interesting and wants to share it. She wants to give the book to _",
                opcionesLeccion[14],
                listOf(opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[14], opcionesLeccion[15])
            ),

            Triple(
                "John listens to this song every morning on his way to work. He says it motivates him. This is _ favorite song",
                opcionesLeccion[8],
                listOf(opcionesLeccion[8], opcionesLeccion[9], opcionesLeccion[10], opcionesLeccion[11])
            ),

            Triple(
                "My grandparents built this place many years ago. The house is _",
                opcionesLeccion[11],
                listOf(opcionesLeccion[11], opcionesLeccion[10], opcionesLeccion[8], opcionesLeccion[6])
            )
        )
        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion4_1(db: SQLiteDatabase){
        val leccionId = leccionIds[6]
        val opcionesLeccion = opcionIdsPorLeccion["prepositions"] ?: return

        val frases = listOf(
            Triple("The cat is _ the table", opcionesLeccion[7], listOf(opcionesLeccion[7], opcionesLeccion[14], opcionesLeccion[8], opcionesLeccion[1])),
            Triple("The keys are _ the drawer", opcionesLeccion[0], listOf(opcionesLeccion[0], opcionesLeccion[15], opcionesLeccion[7], opcionesLeccion[13])),
            Triple("The painting is _ the wall", opcionesLeccion[1], listOf(opcionesLeccion[1], opcionesLeccion[14], opcionesLeccion[0], opcionesLeccion[12])),
            Triple("The lamp is _ the bed", opcionesLeccion[14], listOf(opcionesLeccion[14], opcionesLeccion[8], opcionesLeccion[1], opcionesLeccion[0])),
            Triple("The dog is _ the house", opcionesLeccion[12], listOf(opcionesLeccion[12], opcionesLeccion[11], opcionesLeccion[13], opcionesLeccion[10])),
            Triple("The store is _ the bank", opcionesLeccion[9], listOf(opcionesLeccion[9], opcionesLeccion[13], opcionesLeccion[12], opcionesLeccion[0])),
            Triple("The bird flew _ the trees", opcionesLeccion[8], listOf(opcionesLeccion[8], opcionesLeccion[14], opcionesLeccion[0], opcionesLeccion[11])),
            Triple("The car is parked _ the building", opcionesLeccion[13], listOf(opcionesLeccion[13], opcionesLeccion[12], opcionesLeccion[7], opcionesLeccion[15])),
            Triple("There is a garden _ the house", opcionesLeccion[11], listOf(opcionesLeccion[11], opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[10])),
            Triple("The airplane is _ the clouds", opcionesLeccion[14], listOf(opcionesLeccion[14], opcionesLeccion[8], opcionesLeccion[1], opcionesLeccion[0]))
        )

        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion4_2(db: SQLiteDatabase){
        val leccionId = leccionIds[7]
        val opcionesLeccion = opcionIdsPorLeccion["prepositions"] ?: return

        val frases = listOf(
            Triple("We will meet _ 6 PM", opcionesLeccion[2], listOf(opcionesLeccion[2], opcionesLeccion[1], opcionesLeccion[0], opcionesLeccion[6])),
            Triple("I am going _ school", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[0], opcionesLeccion[5], opcionesLeccion[1])),
            Triple("He spoke _ his day", opcionesLeccion[6], listOf(opcionesLeccion[6], opcionesLeccion[5], opcionesLeccion[4], opcionesLeccion[7])),
            Triple("She traveled _ Japan", opcionesLeccion[3], listOf(opcionesLeccion[3], opcionesLeccion[4], opcionesLeccion[2], opcionesLeccion[6])),
            Triple("I am staying _ my parents", opcionesLeccion[5], listOf(opcionesLeccion[5], opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[6])),
            Triple("They arrived _ night", opcionesLeccion[2], listOf(opcionesLeccion[2], opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[14])),
            Triple("The movie starts _ noon", opcionesLeccion[2], listOf(opcionesLeccion[2], opcionesLeccion[1], opcionesLeccion[0], opcionesLeccion[4])),
            Triple("I walked _ the river", opcionesLeccion[12], listOf(opcionesLeccion[12], opcionesLeccion[5], opcionesLeccion[13], opcionesLeccion[0])),
            Triple("She sat _ her best friend", opcionesLeccion[13], listOf(opcionesLeccion[13], opcionesLeccion[12], opcionesLeccion[5], opcionesLeccion[11])),
            Triple("We stayed _ the afternoon", opcionesLeccion[2], listOf(opcionesLeccion[2], opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[3]))
        )

        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion5_1(db: SQLiteDatabase){
        val leccionId = leccionIds[8] // 1ra de compound_sentences
        val opcionesLeccion = opcionIdsPorLeccion["compound_sentences"] ?: return

        val frases = listOf(
            Triple("I like apples _ bananas", opcionesLeccion[0], listOf(opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[2], opcionesLeccion[4])),
            Triple("She is tired _ she keeps working", opcionesLeccion[8], listOf(opcionesLeccion[8], opcionesLeccion[0], opcionesLeccion[5], opcionesLeccion[1])),
            Triple("We can go to the park _ stay home", opcionesLeccion[2], listOf(opcionesLeccion[2], opcionesLeccion[1], opcionesLeccion[0], opcionesLeccion[4])),
            Triple("He studied a lot _ he failed the test", opcionesLeccion[1], listOf(opcionesLeccion[1], opcionesLeccion[8], opcionesLeccion[0], opcionesLeccion[6])),
            Triple("I wanted to go, _ it was raining", opcionesLeccion[8], listOf(opcionesLeccion[8], opcionesLeccion[1], opcionesLeccion[3], opcionesLeccion[4])),
            Triple("You can take tea _ coffee", opcionesLeccion[2], listOf(opcionesLeccion[2], opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[5])),
            Triple("He is tall _ strong", opcionesLeccion[0], listOf(opcionesLeccion[0], opcionesLeccion[8], opcionesLeccion[1], opcionesLeccion[2])),
            Triple("I called you many times, _ you didn't answer", opcionesLeccion[1], listOf(opcionesLeccion[1], opcionesLeccion[0], opcionesLeccion[8], opcionesLeccion[6])),
            Triple("She didn’t eat meat, _ did she drink milk", opcionesLeccion[7], listOf(opcionesLeccion[7], opcionesLeccion[1], opcionesLeccion[8], opcionesLeccion[0])),
            Triple("He was hungry _ he made a sandwich", opcionesLeccion[5], listOf(opcionesLeccion[5], opcionesLeccion[0], opcionesLeccion[1], opcionesLeccion[8]))
        )

        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion5_2(db: SQLiteDatabase){
        val leccionId = leccionIds[9] // 2da de compound_sentences
        val opcionesLeccion = opcionIdsPorLeccion["compound_sentences"] ?: return

        val frases = listOf(
            Triple("I stayed home _ it was raining", opcionesLeccion[3], listOf(opcionesLeccion[3], opcionesLeccion[1], opcionesLeccion[5], opcionesLeccion[9])),
            Triple("She went to bed _ she was tired", opcionesLeccion[9], listOf(opcionesLeccion[9], opcionesLeccion[3], opcionesLeccion[4], opcionesLeccion[8])),
            Triple("We will go _ it stops raining", opcionesLeccion[14], listOf(opcionesLeccion[14], opcionesLeccion[15], opcionesLeccion[2], opcionesLeccion[0])),
            Triple("He kept smiling _ he was sad", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[3], opcionesLeccion[1], opcionesLeccion[8])),
            Triple("I can't go _ I finish my homework", opcionesLeccion[10], listOf(opcionesLeccion[10], opcionesLeccion[3], opcionesLeccion[6], opcionesLeccion[14])),
            Triple("Call me _ you arrive", opcionesLeccion[15], listOf(opcionesLeccion[15], opcionesLeccion[14], opcionesLeccion[13], opcionesLeccion[6])),
            Triple("We ate dinner _ watching TV", opcionesLeccion[11], listOf(opcionesLeccion[11], opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[6])),
            Triple("I will start studying _ lunch", opcionesLeccion[13], listOf(opcionesLeccion[13], opcionesLeccion[12], opcionesLeccion[15], opcionesLeccion[9])),
            Triple("He left _ the meeting ended", opcionesLeccion[12], listOf(opcionesLeccion[12], opcionesLeccion[13], opcionesLeccion[11], opcionesLeccion[15])),
            Triple("We won't go _ you call us", opcionesLeccion[14], listOf(opcionesLeccion[14], opcionesLeccion[10], opcionesLeccion[9], opcionesLeccion[3]))
        )

        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion6_1(db: SQLiteDatabase){
        val leccionId = leccionIds[10] // 1ra de questions_negations
        val opcionesLeccion = opcionIdsPorLeccion["questions_negations"] ?: return

        val frases = listOf(
            Triple("Do you _ speak Spanish?", opcionesLeccion[0], listOf(opcionesLeccion[0], opcionesLeccion[3], opcionesLeccion[5], opcionesLeccion[7])),
            Triple("Does she _ to the gym on weekends?", opcionesLeccion[1], listOf(opcionesLeccion[1], opcionesLeccion[4], opcionesLeccion[6], opcionesLeccion[9])),
            Triple("Did they _ the movie last night?", opcionesLeccion[2], listOf(opcionesLeccion[2], opcionesLeccion[0], opcionesLeccion[8], opcionesLeccion[11])),
            Triple("Would you _ to travel someday?", opcionesLeccion[3], listOf(opcionesLeccion[3], opcionesLeccion[1], opcionesLeccion[10], opcionesLeccion[14])),
            Triple("Do we _ enough time to finish?", opcionesLeccion[0], listOf(opcionesLeccion[0], opcionesLeccion[4], opcionesLeccion[9], opcionesLeccion[13])),
            Triple("Does he _ coffee every morning?", opcionesLeccion[1], listOf(opcionesLeccion[1], opcionesLeccion[7], opcionesLeccion[3], opcionesLeccion[12])),
            Triple("Did you _ him yesterday?", opcionesLeccion[2], listOf(opcionesLeccion[2], opcionesLeccion[6], opcionesLeccion[5], opcionesLeccion[10])),
            Triple("Would they _ to join us?", opcionesLeccion[3], listOf(opcionesLeccion[3], opcionesLeccion[2], opcionesLeccion[11], opcionesLeccion[8])),
            Triple("Do I _ need permission?", opcionesLeccion[0], listOf(opcionesLeccion[0], opcionesLeccion[9], opcionesLeccion[6], opcionesLeccion[4])),
            Triple("Does it _ sense now?", opcionesLeccion[1], listOf(opcionesLeccion[1], opcionesLeccion[5], opcionesLeccion[14], opcionesLeccion[13]))
        )

        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }
    private fun generatePhrasesLeccion6_2(db: SQLiteDatabase){
        val leccionId = leccionIds[11] // 2da de questions_negations
        val opcionesLeccion = opcionIdsPorLeccion["questions_negations"] ?: return

        val frases = listOf(
            Triple("I was _ happy with the result", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[6], opcionesLeccion[8], opcionesLeccion[10])),
            Triple("She is _ coming today", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[7], opcionesLeccion[11], opcionesLeccion[1])),
            Triple("They do _ want to leave yet", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[2], opcionesLeccion[9], opcionesLeccion[6])),
            Triple("He does _ like chocolate", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[5], opcionesLeccion[12], opcionesLeccion[3])),
            Triple("We are _ sure about this", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[8], opcionesLeccion[13], opcionesLeccion[6])),
            Triple("I do _ know the answer", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[10], opcionesLeccion[0], opcionesLeccion[7])),
            Triple("She was _ ready for the test", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[14], opcionesLeccion[3], opcionesLeccion[11])),
            Triple("They are _ interested in that", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[6], opcionesLeccion[2], opcionesLeccion[5])),
            Triple("He is _ my friend", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[1], opcionesLeccion[9], opcionesLeccion[13])),
            Triple("We were _ invited to the party", opcionesLeccion[4], listOf(opcionesLeccion[4], opcionesLeccion[8], opcionesLeccion[10], opcionesLeccion[12]))
        )

        frases.forEach { (frase, correctaId, opcionesList) ->
            val cv = ContentValues().apply {
                put("leccion_id", leccionId)
                put("frase_incompleta", frase)
                put("correcta_id", correctaId)
                put("opcion_a_id", opcionesList[0])
                put("opcion_b_id", opcionesList[1])
                put("opcion_c_id", opcionesList[2])
                put("opcion_d_id", opcionesList[3])
            }
            db.insert("Frases", null, cv)
        }
    }




    fun getFrasesByLeccion(db: SQLiteDatabase, leccionId: Int): MutableList<Frase> {
        val frasesList = mutableListOf<Frase>()
        val query = """
        SELECT 
            f.id, f.frase_incompleta,
            c.texto AS correcta,
            oa.texto AS opcion_a,
            ob.texto AS opcion_b,
            oc.texto AS opcion_c,
            od.texto AS opcion_d
        FROM Frases f
        JOIN Opciones c ON f.correcta_id = c.id
        JOIN Opciones oa ON f.opcion_a_id = oa.id
        JOIN Opciones ob ON f.opcion_b_id = ob.id
        JOIN Opciones oc ON f.opcion_c_id = oc.id
        JOIN Opciones od ON f.opcion_d_id = od.id
        WHERE f.leccion_id = ?
    """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(leccionId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val texto = cursor.getString(1)
                val correcta = cursor.getString(2)
                val opciones = listOf(
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6)
                )
                frasesList.add(Frase(id, texto, correcta, opciones))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return frasesList
    }

    fun getLecciones(db: SQLiteDatabase): List<Leccion> {
        val lecciones = mutableListOf<Leccion>()

        val cursor = db.rawQuery(
            "SELECT id, tema_id, titulo, subtitulo, imagen, completada FROM Lecciones",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("subtitulo"))
                val imagen = cursor.getInt(cursor.getColumnIndexOrThrow("imagen"))
                // LECTURA DEL ESTADO DE PROGRESO
                val completada = cursor.getInt(cursor.getColumnIndexOrThrow("completada"))

                lecciones.add(Leccion(id, nombre, descripcion, imagen, completada))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lecciones
    }

    fun getLeccion(db: SQLiteDatabase, id: Int): Leccion {
        val cursor = db.rawQuery("SELECT * FROM Lecciones WHERE id = ?", arrayOf(id.toString()))

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("subtitulo"))
            val imagen = cursor.getInt(cursor.getColumnIndexOrThrow("imagen"))
            // LECTURA DEL ESTADO DE PROGRESO
            val completada = cursor.getInt(cursor.getColumnIndexOrThrow("completada"))

            cursor.close()
            return Leccion(id, nombre, descripcion, imagen, completada)
        } else {
            throw Exception("No se encontró la lección con ID $id")
        }
    }


    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS Palabras")
        db.execSQL("DROP TABLE IF EXISTS Lecciones")
        db.execSQL("DROP TABLE IF EXISTS Opciones")
        db.execSQL("DROP TABLE IF EXISTS Frases")
        onCreate(db)
    }

}