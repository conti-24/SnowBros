package ModosDeJuego;

import Logica.Jugador;
import Logica.Nivel;

public class Clasico extends ModoDeJuego {

    public Clasico(Nivel n, Jugador j) {
        super(n, j);
        this.ranking = new Ranking("clasico");
    }

    public void verificarFinal() {
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
        return nivel.getTiempoTranscurrido();
    }

}
