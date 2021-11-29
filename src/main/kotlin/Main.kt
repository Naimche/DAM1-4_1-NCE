class Modulo(val maxAlumnos: Int = 10) {
    var alumnos = arrayOfNulls<Alumno?>(maxAlumnos)
    var evaluaciones = Array(4) { FloatArray(maxAlumnos) {-1F} }

    companion object {
        const val EV_PRIMER = 0
        const val EV_SEGUNDA = 1
        const val EV_TERCERA = 2
        const val EV_FINAL = 3
    }

    var contador = 0
    fun matricularAlumno(alumno: Alumno): Boolean {
        if (contador < maxAlumnos) {
            alumnos[contador] = alumno
            contador++
        }
        return true
    }
    fun traducirEvaluacion(evaluacion: String): Int {
        return when (evaluacion.uppercase()) {
            "1" -> EV_PRIMER
            "PRIMERA" -> EV_PRIMER
            "2" -> EV_SEGUNDA
            "SEGUNDA" -> EV_SEGUNDA
            "3" -> EV_TERCERA
            "TERCERA" -> EV_TERCERA
            "4" -> EV_FINAL
            "FINAL" -> EV_FINAL
            else -> -1
        }
    }
    fun bajaAlumno(idAlumno: String): Boolean {
        for (i in 0..10) {
            if (alumnos[i]?.idAlumno == idAlumno) {
                alumnos[i] = null
                return true
            }
        }
        return false
    }

    fun establecerNota(idAlumno: String, evaluacion: String, nota: Float): Boolean {
        evaluaciones[evaluacion.toInt()][alumnos.indexOfFirst { it?.idAlumno == idAlumno }] = nota
        return true

    }

    fun notaMedia(evaluacion: String): Float {
        var media = 0.0F
        var alumnosCalificados = 0
        if (evaluacion == "0" || evaluacion == "1" || evaluacion == "2" || evaluacion == "3") {
            for (i in alumnos.indices) {
                if (evaluaciones[evaluacion.toInt()][i] > 0.0F) {
                    media += evaluaciones[evaluacion.toInt()][i]
                    alumnosCalificados++
                }
            }
        }
        media /= alumnosCalificados
        return media
    }

    fun hayAlumnosAprobados(evaluacion: String):Boolean {
        return evaluaciones[evaluacion.toInt()].any() {it >= 5}
    }
    fun hayAlumnosConDiez(evaluacion: String): Boolean {
        return evaluaciones[evaluacion.toInt()].any() {it == 10F}
    }
    fun primeraNotaNoAprobada(evaluacion: String): Float{
        evaluaciones[evaluacion.toInt()].sortDescending()
        return evaluaciones[evaluacion.toInt()].first(){it < 5F && it >= 0F}
    }
    fun notaMasBaja(evaluacion: String) = evaluaciones[evaluacion.toInt()].filter { it > -1F }.minOrNull()
    fun notaMasAlta(evaluacion: String) = evaluaciones[evaluacion.toInt()].filter { it > -1F }.maxOrNull()
    fun numeroDeAprobados(evaluacion: String) :Int = evaluaciones[evaluacion.toInt()].count { it > 5F }
    fun listaNotas(evaluacion: String = "FINAL"): List<Pair<Alumno, Float>> {
        val eval = traducirEvaluacion(evaluacion)
        val listaNotas: MutableList<Pair<Alumno, Float>> = mutableListOf()
        if (eval >= EV_PRIMER) {
            for (i in 0 until this.maxAlumnos) {
                if (alumnos[i] != null) listaNotas.add(Pair(alumnos[i]!!, evaluaciones[eval][i]))
            }
        }
        return listaNotas
    }
    fun calculaEvaluacionFinal() {
        for (i in 0 until maxAlumnos) {
            evaluaciones[EV_FINAL][i] =
                (evaluaciones[EV_PRIMER][i] + evaluaciones[EV_SEGUNDA][i] + evaluaciones[EV_TERCERA][i]) / 3
        }
    }
    fun listaNotasOrdenadas(evaluacion: String): List<Pair<Alumno, Float>> {
        val eval = traducirEvaluacion(evaluacion)
        val listaNotas = mutableListOf<Pair<Alumno, Float>>()
        if (eval >= EV_PRIMER) {
            for (i in 0 until this.maxAlumnos) {
                if (alumnos[i] != null) listaNotas.add(Pair(alumnos[i]!!, evaluaciones[eval][i]))
            }
        }
        return listaNotas.sortedBy { it.second }
    }

}


data class Alumno(val idAlumno: String, val nombre: String, val apellido1: String, val apellido2: String) {}

fun main() {
    val programacion = Modulo(15)
    val alumno1 = Alumno("A123", "Ana", "Perez", "Gómez")
    val alumno2 = Alumno("B456", "Jesús", "Calvellido", "Toro")
    val alumno3 = Alumno("C789", "Pepe", "Torres", "Morales")
    val alumno4 = Alumno("D147", "Yolanda", "Vargas", "Naranjo")
    val alumno5 = Alumno("E258", "Pedro", "López", "Cruz")
    val alumno6 = Alumno("F369", "Luis", "Jesus", "Toro")

    programacion.matricularAlumno(alumno1)
    programacion.matricularAlumno(alumno2)
    programacion.matricularAlumno(alumno3)
    programacion.matricularAlumno(alumno4)
    programacion.matricularAlumno(alumno5)
    programacion.matricularAlumno(alumno6)
    programacion.establecerNota("A123","0", 7F)
    programacion.establecerNota("B456", "0", 3F)
    programacion.establecerNota("F369", "0", 9F)
    programacion.establecerNota("E258", "0", 4.9F)

    programacion.bajaAlumno("C789")
    programacion.bajaAlumno("D147")

    programacion.calculaEvaluacionFinal()

    println("Lista de notas")
    println(programacion.listaNotas())

    println("Primera nota no aprobada ${programacion.primeraNotaNoAprobada("0")}")
    println("Nota mas baja ${programacion.notaMasBaja("0")}")
    println("Nota mas alta ${programacion.notaMasAlta("0")}")
    if(programacion.hayAlumnosConDiez("0")){
        println("Si hay dieces")
    }
    println("Numero de aprobados ${programacion.numeroDeAprobados("0")}")

    println("Lista de notas ordenadas")
    println(programacion.listaNotasOrdenadas("PRIMERA"))

}