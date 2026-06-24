package Estados;

import Logica.Jugador;

public class BajandoEscalera extends EstadoJugador {

    public BajandoEscalera(Jugador j) {
        this.jugador = j;
    }

    @Override
    public void manejarEntrada() {
        // Entrada manejada externamente
    }

    @Override
    public void aplicarFisica() {
        // En escalera no aplica gravedad
    }

    @Override
    public void actualizarPosicion() {
        // El movimiento vertical lo maneja el estado lógico
    }

    @Override
    public void mover() {
    }

    @Override
    public int getIdentificadorEstado() {
        return EstadoVisualBros.BAJANDO_ESCALERA;
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
