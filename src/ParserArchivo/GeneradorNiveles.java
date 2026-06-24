package ParserArchivo;

import java.util.ArrayList;
import java.util.List;

import Fabricas.CreadorEntidades;
import Logica.Nivel;
import ModosDeJuego.ModoTipo;

public class GeneradorNiveles {

    private CreadorEntidades fabricaEntidades;
    private LectorNiveles lectorNiveles;
    private List<String> rutasNiveles;
    private int nivelActual;
    private ModoTipo modoActual;

    public GeneradorNiveles(CreadorEntidades fabrica, ModoTipo modo) {
        this.fabricaEntidades = fabrica;
        this.lectorNiveles = new LectorNiveles(fabrica);
        this.rutasNiveles = new ArrayList<>();
        this.nivelActual = 0;
        this.modoActual = modo;
        inicializarRutasNiveles();
    }
    private void inicializarRutasNiveles() {
        if (modoActual == ModoTipo.SUPERVIVENCIA) {
            rutasNiveles.add("src/ParserArchivo/nivel1_supervivencia.txt");
            rutasNiveles.add("src/ParserArchivo/nivel2_supervivencia.txt");
        } else {
            rutasNiveles.add("src/ParserArchivo/nivel1_piso1.txt");
            rutasNiveles.add("src/ParserArchivo/nivel1_piso2.txt");
            rutasNiveles.add("src/ParserArchivo/nivel1_piso3.txt");

            rutasNiveles.add("src/ParserArchivo/nivel2_piso1.txt");
            rutasNiveles.add("src/ParserArchivo/nivel2_piso2.txt");
            rutasNiveles.add("src/ParserArchivo/nivel2_piso3.txt");
        }
    }

    public Nivel generarNivel(int numeroNivel) {
        if (numeroNivel < 0 || numeroNivel >= rutasNiveles.size()) {
            System.err.println("Número de nivel inválido: " + numeroNivel);
            return null;
        }
        Nivel nivel = new Nivel();
        nivel.setFabricaEntidades(fabricaEntidades);
        String rutaNivel = rutasNiveles.get(numeroNivel);
        boolean cargaExitosa = lectorNiveles.cargarNivel(rutaNivel, nivel);

        if (!cargaExitosa) {
            System.err.println("Error al cargar el nivel: " + rutaNivel);
            return null;
        }
        return nivel;
    }

    public Nivel recargarNivelActual() {
        return generarNivel(nivelActual);
    }

    public Nivel avanzarSiguienteNivel() {
        if (hayMasNiveles()) {
            nivelActual++;
            return generarNivel(nivelActual);
        }
        return null;
    }

    public int getNumeroNivelActual() {
        return nivelActual + 1;
    }

    public int getTotalNiveles() {
        return rutasNiveles.size();
    }

    public boolean hayMasNiveles() {
        return nivelActual < rutasNiveles.size() - 1;
    }
}