package Grafica;

import Logica.Jugador;

/**
 * Observer gráfico específico para el jugador.
 * Maneja el efecto visual de parpadeo durante la invulnerabilidad.
 */
public class ObserverGraficoJugador extends ObserverGrafico {
    private Jugador jugador;

    public ObserverGraficoJugador(Jugador jugador) {
        super(jugador);
        this.jugador = jugador;
    }

    @Override
    public void actualizar() {
        if (jugador == null || jugador.getInformacion() == null) {
            super.actualizar();
            return;
        }
        boolean debeSerVisible = jugador.getInformacion().debeSerVisible();
        setVisible(debeSerVisible);
        if (debeSerVisible) {
            actualizarImagen();
            actualizarPosTamano();
        }
    }
}
