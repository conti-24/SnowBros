package ModosDeJuego;

import Logica.Jugador;
import Logica.Nivel;

public class Contrarreloj extends ModoDeJuego {
    private static final int TIEMPO_LIMITE = 120;

    public Contrarreloj(Nivel n, Jugador j) {
        super(n, j);
        this.ranking = new Ranking("contrarreloj");
    }

    public void verificarFinal() {
        int tiempoActual = nivel.getTiempoTranscurrido();

        if (tiempoActual >= TIEMPO_LIMITE) {
            ranking.actualizarTop5(jugador.getInformacion());
            if (juego != null) {
                juego.terminarPartida(false);
            }
        }
        if (jugador.getInformacion().getVidas() <= 0) {
            ranking.actualizarTop5(jugador.getInformacion());
            if (juego != null) {
                juego.terminarPartida(false);
            }
        }
    }

    @Override
    public int getTiempoRestante() {
        if (nivel == null)
            return -1;
        int restante = TIEMPO_LIMITE - nivel.getTiempoTranscurrido();
        return restante;
    }

}
