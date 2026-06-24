package Estados;

import Logica.Jugador;

public class SubiendoEscalera extends EstadoJugador {

    public SubiendoEscalera(Jugador j) {
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
        jugador.actualizarEstadoVisual();
    }

    @Override
    public void mover() {
    }

    @Override
    public int getIdentificadorEstado() {
        return EstadoVisualBros.SUBIENDO_ESCALERA;
    }

    @Override
    public void disparar() {
        if (jugador.getSprites() != null) {
            if (jugador.getEstadoLogico().estaMirandoIzquierda()) {
                jugador.getSprites().set_estado_actual(EstadoVisualBros.DISPARANDO_IZQUIERDA);
                jugador.notificar();
                jugador.lanzarBolaDeNieveIzquierda();
            } else {
                jugador.getSprites().set_estado_actual(EstadoVisualBros.DISPARANDO_DERECHA);
                jugador.notificar();
                jugador.lanzarBolaDeNieveDerecha();
            }
        }
    }
}
