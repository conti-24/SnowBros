package Estados;

import Logica.ConfiguracionFisica;
import Logica.Jugador;

public class EstadoEnAire extends EstadoLogicoJugador {
    private boolean moviendoDerecha;
    private boolean moviendoIzquierda;
    private boolean mirandoIzquierda;
    private long ultimoDisparo;
    private static final long COOLDOWN_DISPARO_MS = 300;

    // Constantes de física
    private static final float ACELERACION = ConfiguracionFisica.ACELERACION_JUGADOR * 0.6f; // Control aéreo reducido
    private static final float FRICCION = ConfiguracionFisica.FRICCION_JUGADOR * 0.95f; // Menos fricción en aire
    private static final float VELOCIDAD_MAX = ConfiguracionFisica.VELOCIDAD_MAX_JUGADOR;
    private static final float GRAVEDAD = ConfiguracionFisica.GRAVEDAD_JUGADOR;

    public EstadoEnAire(Jugador j, boolean mirandoIzquierda) {
        super(j);
        this.moviendoDerecha = false;
        this.moviendoIzquierda = false;
        this.mirandoIzquierda = mirandoIzquierda;
        this.ultimoDisparo = 0;
    }

    public EstadoEnAire(Jugador j, boolean mirandoIzquierda, boolean moviendoDerecha, boolean moviendoIzquierda) {
        super(j);
        this.moviendoDerecha = moviendoDerecha;
        this.moviendoIzquierda = moviendoIzquierda;
        this.mirandoIzquierda = mirandoIzquierda;
        this.ultimoDisparo = 0;
    }

    @Override
    public void aplicarFisicaHorizontal() {
        float aceleracionConEfectos = ACELERACION + jugador.getEfectos().getVelExtra();
        float velocidadMaxConEfectos = VELOCIDAD_MAX + jugador.getEfectos().getVelExtra();

        float velocidadActual = jugador.getVelocidadRealX();

        if (moviendoDerecha) {
            velocidadActual += aceleracionConEfectos;
            if (velocidadActual > velocidadMaxConEfectos) {
                velocidadActual = velocidadMaxConEfectos;
            }
            if (Math.abs(velocidadActual) > 0.5f) {
                mirandoIzquierda = false;
            }
        } else if (moviendoIzquierda) {
            velocidadActual -= aceleracionConEfectos;
            if (velocidadActual < -velocidadMaxConEfectos) {
                velocidadActual = -velocidadMaxConEfectos;
            }
            if (Math.abs(velocidadActual) > 0.5f) {
                mirandoIzquierda = true;
            }
        } else {
            velocidadActual *= FRICCION;
        }

        jugador.setVelocidadRealX(velocidadActual);
    }

    @Override
    public void aplicarFisicaVertical() {
        float velocidadY = jugador.getVelocidadRealY();
        velocidadY += GRAVEDAD; 
        jugador.setVelocidadRealY(velocidadY);
    }

    @Override
    public void actualizarPosicion() {
        int nuevaPosX = jugador.getPosX() + Math.round(jugador.getVelocidadRealX());
        int nuevaPosY = jugador.getPosY() + Math.round(jugador.getVelocidadRealY());

        jugador.setPosX(nuevaPosX);
        jugador.setPosY(nuevaPosY);
    }

    @Override
    public boolean intentarSaltar() {
        return false;
    }

    @Override
    public boolean intentarDisparar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo < COOLDOWN_DISPARO_MS) {
            return false;
        }
        ultimoDisparo = ahora;

        if (mirandoIzquierda) {
            jugador.lanzarBolaDeNieveIzquierda();
        } else {
            jugador.lanzarBolaDeNieveDerecha();
        }
        return true;
    }

    @Override
    public boolean intentarSubirEscalera() {
        if (jugador.verificarEscaleraEnPosicion()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean intentarBajar() {
        return false;
    }

    @Override
    public void moverDerecha() {
        this.moviendoDerecha = true;
        this.moviendoIzquierda = false;
    }

    @Override
    public void moverIzquierda() {
        this.moviendoIzquierda = true;
        this.moviendoDerecha = false;
    }

    @Override
    public void moverArriba() {
        // No puede hacer nada en el aire
    }

    @Override
    public void moverAbajo() {
        // No puede hacer nada en el aire
    }

    @Override
    public void detenerMovimientoHorizontal() {
        this.moviendoDerecha = false;
        this.moviendoIzquierda = false;
    }

    @Override
    public void detenerMovimientoVertical() {
        // No aplicable en aire
    }

    @Override
    public boolean estaEnElAire() {
        return true;
    }

    @Override
    public boolean estaEnEscalera() {
        return false;
    }

    @Override
    public boolean puedeColisionarConPlataformas() {
        return true;
    }

    @Override
    public boolean estaAtravesandoPlataforma() {
        return false;
    }

    @Override
    public boolean estaMirandoIzquierda() {
        return mirandoIzquierda;
    }

    @Override
    public boolean estaMoviendoDerecha() {
        return moviendoDerecha;
    }

    @Override
    public boolean estaMoviendoIzquierda() {
        return moviendoIzquierda;
    }

    @Override
    public boolean estaSubiendoEscalera() {
        return false;
    }

    @Override
    public boolean estaBajandoEscalera() {
        return false;
    }

    @Override
    public boolean hayEscaleraEnPosicion() {
        return jugador.verificarEscaleraEnPosicion();
    }

    @Override
    public boolean haySoporteDebajo() {
        return jugador.haySoporteDebajo();
    }

    @Override
    public void disparar() {
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo < COOLDOWN_DISPARO_MS) {
            return;
        }
        ultimoDisparo = ahora;

        if (mirandoIzquierda) {
            jugador.lanzarBolaDeNieveIzquierda();
        } else {
            jugador.lanzarBolaDeNieveDerecha();
        }
    }

    @Override
    public EstadoLogicoJugador verificarTransiciones() {
        if (jugador.haySoporteDebajo() && jugador.getVelocidadRealY() <= 0) {
            return new EstadoEnSuelo(jugador, mirandoIzquierda, moviendoDerecha, moviendoIzquierda);
        }

        return null; 
    }

    @Override
    public void notificarAterrizaje() {
        // La transición se maneja en verificarTransiciones()
    }

    @Override
    public void notificarSueloResbaladizo() {
    }

    @Override
    public void notificarColisionTecho() {
        if (jugador.getVelocidadRealY() > 0) {
            jugador.setVelocidadRealY(0);
        }
    }

    public boolean isMoviendoDerecha() {
        return moviendoDerecha;
    }

    public boolean isMoviendoIzquierda() {
        return moviendoIzquierda;
    }

    @Override
    public void actualizarEstadoVisual() {
        if (!mirandoIzquierda) {
                jugador.cambiarEstadoSiDiferente(EstadoVisualBros.SALTANDO_IZQUIERDA);
            } else {
                jugador.cambiarEstadoSiDiferente(EstadoVisualBros.SALTANDO_DERECHA);
            }
    }
}
