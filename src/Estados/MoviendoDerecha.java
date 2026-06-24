package Estados;

import Logica.Jugador;

public class MoviendoDerecha extends EstadoJugador {

    private static final int VELOCIDAD_ANIMACION = 10;
    private static final int DURACION_SPRITE_DISPARO = 8; 
    private int contadorFrames;
    private int frameActual;
    private int[] secuenciaAnimacion;
    private int contadorDisparo; 

    public MoviendoDerecha(Jugador j) {
        jugador = j;
        this.contadorFrames = 0;
        this.frameActual = 0;
        this.contadorDisparo = 0;

        this.secuenciaAnimacion = new int[] {
                EstadoVisualBros.CAMINANDO_DERECHA_1,
                EstadoVisualBros.CAMINANDO_DERECHA_2,
        };
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
        if (contadorDisparo > 0) {
            contadorDisparo--;
            return; 
        }
        
        if (Math.abs(jugador.getVelocidadRealX()) < 0.1f) {
            if (jugador.getSprites() != null) {
                jugador.getSprites().set_estado_actual(EstadoVisualBros.CAMINANDO_DERECHA);
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
        if (jugador.getSprites() != null) {
            jugador.getSprites().set_estado_actual(EstadoVisualBros.DISPARANDO_DERECHA);
            jugador.notificar();
        }
        contadorDisparo = DURACION_SPRITE_DISPARO; 
    }

    @Override
    public int getIdentificadorEstado() {
        return secuenciaAnimacion[frameActual];
    }
}
