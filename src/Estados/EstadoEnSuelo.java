package Estados;

import Logica.ConfiguracionFisica;
import Logica.Jugador;
import Logica.SoundManager;

/**
 * Estado cuando el jugador está en el suelo.
 * Puede caminar, saltar, disparar, subir escaleras.
 */
public class EstadoEnSuelo extends EstadoLogicoJugador {
    private boolean moviendoDerecha;
    private boolean moviendoIzquierda;
    private boolean mirandoIzquierda;
    private long ultimoDisparo;
    private static final long COOLDOWN_DISPARO_MS = 300;

    // Constantes de física
    private float aceleracion;
    private float friccion;
    private float velocidadMax;
    private static final float ACELERACION_BASE = ConfiguracionFisica.ACELERACION_JUGADOR;
    private static final float FRICCION_BASE = ConfiguracionFisica.FRICCION_JUGADOR;
    private static final float VELOCIDAD_MAX_BASE = ConfiguracionFisica.VELOCIDAD_MAX_JUGADOR;
    private static final float FUERZA_SALTO = ConfiguracionFisica.FUERZA_SALTO_JUGADOR;

    // Suelo resbaladizo
    private long aceleracionReducidaHasta = 0L;
    
    // Flag para atravesar plataforma
    private boolean quiereAtravesarPlataforma = false;

    public EstadoEnSuelo(Jugador j) {
        super(j);
        this.moviendoDerecha = false;
        this.moviendoIzquierda = false;
        this.mirandoIzquierda = false;
        this.ultimoDisparo = 0;
        this.aceleracion = ACELERACION_BASE;
        this.friccion = FRICCION_BASE;
        this.velocidadMax = VELOCIDAD_MAX_BASE;
    }

    public EstadoEnSuelo(Jugador j, boolean mirandoIzquierda, boolean moviendoDerecha, boolean moviendoIzquierda) {
        super(j);
        this.moviendoDerecha = moviendoDerecha;
        this.moviendoIzquierda = moviendoIzquierda;
        this.mirandoIzquierda = mirandoIzquierda;
        this.ultimoDisparo = 0;
        this.aceleracion = ACELERACION_BASE;
        this.friccion = FRICCION_BASE;
        this.velocidadMax = VELOCIDAD_MAX_BASE;
    }

    @Override
    public void aplicarFisicaHorizontal() {
        if (aceleracionReducidaHasta > 0 && System.currentTimeMillis() > aceleracionReducidaHasta) {
            aceleracion = ACELERACION_BASE;
            aceleracionReducidaHasta = 0L;
        }

        float aceleracionConEfectos = aceleracion + jugador.getEfectos().getVelExtra();
        float velocidadMaxConEfectos = velocidadMax + jugador.getEfectos().getVelExtra();

        float velocidadActual = jugador.getVelocidadRealX();

        if (moviendoDerecha) {
            velocidadActual += aceleracionConEfectos;
            if (velocidadActual > velocidadMaxConEfectos) {
                velocidadActual = velocidadMaxConEfectos;
            }
            mirandoIzquierda = false;
        } else if (moviendoIzquierda) {
            velocidadActual -= aceleracionConEfectos;
            if (velocidadActual < -velocidadMaxConEfectos) {
                velocidadActual = -velocidadMaxConEfectos;
            }
            mirandoIzquierda = true;
        } else {
            velocidadActual *= friccion;
        }

        jugador.setVelocidadRealX(velocidadActual);
    }

    @Override
    public void aplicarFisicaVertical() {
        if (jugador.getVelocidadRealY() <= 0) {
            jugador.setVelocidadRealY(0);
        }
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
        jugador.setVelocidadRealY(FUERZA_SALTO); 
        SoundManager.getInstance().playSound("jump");
        return true;
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
        return jugador.verificarEscaleraEnPosicion();
    }

    @Override
    public boolean intentarBajar() {
        if (jugador.haySoporteDebajo()) {
            quiereAtravesarPlataforma = true;
            return true;
        }
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
    }

    @Override
    public void moverAbajo() {
        if (jugador.haySoporteDebajo()) {
            quiereAtravesarPlataforma = true;
        }
    }

    @Override
    public void detenerMovimientoHorizontal() {
        this.moviendoDerecha = false;
        this.moviendoIzquierda = false;
    }

    @Override
    public void detenerMovimientoVertical() {
        quiereAtravesarPlataforma = false;
    }

    @Override
    public boolean estaEnElAire() {
        return false;
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
        if (quiereAtravesarPlataforma && jugador.haySoporteDebajo()) {
            quiereAtravesarPlataforma = false;
            return new EstadoAtravesandoPlataforma(jugador, mirandoIzquierda);
        }
        if (!jugador.haySoporteDebajo() || jugador.getVelocidadRealY() > 0) {
            return new EstadoEnAire(jugador, mirandoIzquierda, moviendoDerecha, moviendoIzquierda);
        }

        return null; 
    }

    @Override
    public void notificarAterrizaje() {
    }

    @Override
    public void notificarSueloResbaladizo() {
        aceleracion = 0.2f * ConfiguracionFisica.FACTOR_VELOCIDAD;
        aceleracionReducidaHasta = System.currentTimeMillis() + 2000; // 2s
    }

    @Override
    public void notificarColisionTecho() {
    }

    public boolean isMoviendoDerecha() {
        return moviendoDerecha;
    }

    public boolean isMoviendoIzquierda() {
        return moviendoIzquierda;
    }

    @Override
    public void actualizarEstadoVisual() {
        if (mirandoIzquierda) {
            jugador.cambiarEstadoSiDiferente(EstadoVisualBros.CAMINANDO_IZQUIERDA);
        } else {
            jugador.cambiarEstadoSiDiferente(EstadoVisualBros.CAMINANDO_DERECHA);
        }
    }
}
