package ModosDeJuego;

import java.io.*;
import java.util.ArrayList;

public class GestorRankings {
    private static final String DIRECTORIO_DATOS = "datos_rankings";
    private static final String ARCHIVO_CLASICO = "ranking_clasico.dat";
    private static final String ARCHIVO_CONTRARRELOJ = "ranking_contrarreloj.dat";
    private static final String ARCHIVO_SUPERVIVENCIA = "ranking_supervivencia.dat";

    public static void guardarRanking(ArrayList<EntradaRanking> ranking, String nombreArchivo) {
        try {
            File directorio = new File(DIRECTORIO_DATOS);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }
            File archivo = new File(DIRECTORIO_DATOS, nombreArchivo);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                oos.writeObject(ranking);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar ranking: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<EntradaRanking> cargarRanking(String nombreArchivo) {
        File archivo = new File(DIRECTORIO_DATOS, nombreArchivo);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (ArrayList<EntradaRanking>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar ranking: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void guardarRankingClasico(ArrayList<EntradaRanking> ranking) {
        guardarRanking(ranking, ARCHIVO_CLASICO);
    }

    public static void guardarRankingContrarreloj(ArrayList<EntradaRanking> ranking) {
        guardarRanking(ranking, ARCHIVO_CONTRARRELOJ);
    }

    public static void guardarRankingSupervivencia(ArrayList<EntradaRanking> ranking) {
        guardarRanking(ranking, ARCHIVO_SUPERVIVENCIA);
    }

    public static ArrayList<EntradaRanking> cargarRankingClasico() {
        return cargarRanking(ARCHIVO_CLASICO);
    }

    public static ArrayList<EntradaRanking> cargarRankingContrarreloj() {
        return cargarRanking(ARCHIVO_CONTRARRELOJ);
    }

    public static ArrayList<EntradaRanking> cargarRankingSupervivencia() {
        return cargarRanking(ARCHIVO_SUPERVIVENCIA);
    }
}
