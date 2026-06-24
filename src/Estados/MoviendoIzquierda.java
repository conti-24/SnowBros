package Estados;

import Logica.Jugador;

public class MoviendoIzquierda extends EstadoJugador {

    private static final int VELOCIDAD_ANIMACION = 10;
    private static final int DURACION_SPRITE_DISPARO = 8; // frames que dura el sprite de disparo
    private int contadorFrames;
    private int frameActual;
    private int[] secuenciaAnimacion;
    private int contadorDisparo; // contador para mantener el sprite de disparo visible

    public MoviendoIzquierda(Jugador j) {
        jugador = j;
        this.contadorFrames = 0;
        this.frameActual = 0;
        this.contadorDisparo = 0;

        this.secuenciaAnimacion = new int[] {
                EstadoVisualBros.CAMINANDO_IZQUIERDA_1,
                EstadoVisualBros.CAMINANDO_IZQUIERDA_2,
        };
    }

    @Override
    public void manejarEntrada() {
        // La dirección la maneja el estado lógico
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
        if (contadorDisparo > 0) {
            contadorDisparo--;
            return; 
        }
        if (Math.abs(jugador.getVelocidadRealX()) < 0.1f) {
            if (jugador.getSprites() != null) {
                jugador.getSprites().set_estado_actual(EstadoVisualBros.CAMINANDO_IZQUIERDA);
                jugador.notificar();
            }
            return;
        }
        contadorFrames++;
        if (contadorFrames >= VELOCIDAD_ANIMACION) {
            frameActual = (frameActual + 1) % secuenciaAnimacion.length;
            contadorFrames = 0;
            if (jugador.getSprites() != null) {
                jugador.getSprites().set_estado_actual(secuenciaAnimacion[frameActual]);
                jugador.notificar();
            }
        }
    }

    @Override
    public void disparar() {
        // Solo mostrar sprite de disparo (el estado lógico maneja el cooldown y lanza el proyectil)
        if (jugador.getSprites() != null) {
            jugador.getSprites().set_estado_actual(EstadoVisualBros.DISPARANDO_IZQUIERDA);
            jugador.notificar();
        }
        contadorDisparo = DURACION_SPRITE_DISPARO; 
    }

    @Override
    public int getIdentificadorEstado() {
        return secuenciaAnimacion[frameActual];
    }
}
