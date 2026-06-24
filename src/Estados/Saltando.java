package Estados;

import Logica.Jugador;

public class Saltando extends EstadoJugador {

    public Saltando(Jugador j) {
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
        if (jugador.getVelocidadRealX() >= 0) {
            return EstadoVisualBros.SALTANDO_DERECHA;
        } else {
            return EstadoVisualBros.SALTANDO_IZQUIERDA;
        }
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
