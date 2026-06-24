package Estados;

import Logica.Jugador;

public class Cayendo extends EstadoJugador {

    public Cayendo(Jugador j) {
        jugador = j;
    }

    @Override
    public void manejarEntrada() {
        // Mantener dirección
    }

    @Override
    public void aplicarFisica() {
        // La física ahora la maneja el estado lógico
        // Este método no debería ser necesario en estados visuales
    }

    @Override
    public void actualizarPosicion() {
    }

    @Override
    public void mover() {
        // Sin animación especial
    }

    @Override
    public void disparar() {
        // Solo mostrar sprite de disparo (el estado lógico maneja el cooldown y lanza el proyectil)
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

    @Override
    public int getIdentificadorEstado() {
        if (jugador.getEstadoLogico().estaMirandoIzquierda()) {
            return EstadoVisualBros.SALTANDO_IZQUIERDA;
        } else {
            return EstadoVisualBros.SALTANDO_DERECHA;
        }
    }
}
