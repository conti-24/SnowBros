package ModosDeJuego;

import java.util.ArrayList;
import java.util.Comparator;

import Logica.InfoJugador;

public class Ranking {
    protected ArrayList<EntradaRanking> top5;
    protected String tipoModo; // "clasico", "contrarreloj", "supervivencia"

    public Ranking(String tipoModo) {
        this.tipoModo = tipoModo;
        cargarRanking();
    }
    private void cargarRanking() {
        switch (tipoModo) {
            case "clasico":
                top5 = GestorRankings.cargarRankingClasico();
                break;
            case "contrarreloj":
                top5 = GestorRankings.cargarRankingContrarreloj();
                break;
            case "supervivencia":
                top5 = GestorRankings.cargarRankingSupervivencia();
                break;
            default:
                top5 = new ArrayList<>();
        }
    }
    private void guardarRanking() {
        switch (tipoModo) {
            case "clasico":
                GestorRankings.guardarRankingClasico(top5);
                break;
            case "contrarreloj":
                GestorRankings.guardarRankingContrarreloj(top5);
                break;
            case "supervivencia":
                GestorRankings.guardarRankingSupervivencia(top5);
                break;
        }
    }
    public void actualizarTop5(InfoJugador infoJugador) {
        if (infoJugador == null || infoJugador.getNombre() == null) {
            return;
        }
        String nombre = infoJugador.getNombre();
        int puntaje = infoJugador.getPuntaje();
        if (nombre == null || nombre.trim().isEmpty() || "jugador".equalsIgnoreCase(nombre.trim())) {
            // Evitar que el placeholder "Jugador" sea insertado en el top
            return;
        }
        EntradaRanking entradaExistente = null;
        for (EntradaRanking entrada : top5) {
            if (entrada.getNombre().equals(nombre)) {
                entradaExistente = entrada;
                break;
            }
        }
        if (entradaExistente != null) {
            if (puntaje > entradaExistente.getPuntaje()) {
                top5.remove(entradaExistente);
                top5.add(new EntradaRanking(nombre, puntaje));
            }
        } else {
            top5.add(new EntradaRanking(nombre, puntaje));
        }
        top5.sort(Comparator.comparingInt(EntradaRanking::getPuntaje).reversed());
        if (top5.size() > 5) {
            top5.remove(5);
        }
        guardarRanking();
    }

    public ArrayList<EntradaRanking> getTop5() {
        return new ArrayList<>(top5); 
    }

    public boolean esTop5(int puntaje) {
        if (top5.size() < 5) {
            return true;
        }
        return puntaje > top5.get(4).getPuntaje();
    }

    public void guardarRankingDirecto(ArrayList<EntradaRanking> rankingModificado) {
        this.top5 = rankingModificado;
        guardarRanking();
    }

    public void recargarRanking() {
        cargarRanking();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TOP 5 RANKING:\n");
        for (int i = 0; i < top5.size(); i++) {
            sb.append((i + 1)).append(". ").append(top5.get(i)).append("\n");
        }
        return sb.toString();
    }
}
