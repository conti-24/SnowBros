package Estados;

import Logica.Jugador;

public class Quieto extends EstadoJugador {

    public Quieto(Jugador j) {
        this.jugador = j;
    }

    @Override
    public void manejarEntrada() {
    }

    @Override
    public void aplicarFisica() {
    }

    @Override
    public void actualizarPosicion() {
    }

    @Override
    public void mover() {
    }

    @Override
    public int getIdentificadorEstado() {
        return jugador.getEstadoLogico().estaMirandoIzquierda() ? EstadoVisualBros.CAMINANDO_IZQUIERDA_1
                : EstadoVisualBros.CAMINANDO_DERECHA_1;
    }

    @Override
    public void disparar() {
        if (jugador.getSprites() != null) {
            if (jugador.getEstadoLogico().estaMirandoIzquierda()) {
                jugador.getSprites().set_estado_actual(EstadoVisualBros.DISPARANDO_IZQUIERDA);
                jugador.notificar();
            } else {
                jugador.getSprites().set_estado_actual(EstadoVisualBros.DISPARANDO_DERECHA);
                jugador.notificar();
            }
        }
    }
}
