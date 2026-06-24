package Estados;

import Logica.ConfiguracionFisica;
import Logica.Jugador;

public class EstadoAtravesandoPlataforma extends EstadoLogicoJugador {
    private boolean mirandoIzquierda;
    private long tiempoInicio;
    private static final long DURACION_MS = 200;
    private static final float GRAVEDAD = ConfiguracionFisica.GRAVEDAD_JUGADOR;

    public EstadoAtravesandoPlataforma(Jugador j, boolean mirandoIzquierda) {
        super(j);
        this.mirandoIzquierda = mirandoIzquierda;
        this.tiempoInicio = System.currentTimeMillis();
    }

    @Override
    public void aplicarFisicaHorizontal() {
        float velocidadActual = jugador.getVelocidadRealX();
        velocidadActual *= 0.98f; 
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
        if (mirandoIzquierda) {
            jugador.lanzarBolaDeNieveIzquierda();
        } else {
            jugador.lanzarBolaDeNieveDerecha();
        }
        return true;
    }

    @Override
    public boolean intentarSubirEscalera() {
        return false;
    }

    @Override
    public boolean intentarBajar() {
        return false;
    }

    @Override
    public void moverDerecha() {
        // No cambia el movimiento mientras atraviesa
    }

    @Override
    public void moverIzquierda() {
        // No cambia el movimiento mientras atraviesa
    }

    @Override
    public void moverArriba() {
        // No puede hacer nada mientras atraviesa
    }

    @Override
    public void moverAbajo() {
        // Ya está atravesando
    }

    @Override
    public void detenerMovimientoHorizontal() {
        // No cambia mientras atraviesa
    }

    @Override
    public void detenerMovimientoVertical() {
        // No aplicable
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
        return false;
    }

    @Override
    public boolean estaAtravesandoPlataforma() {
        return true;
    }

    @Override
    public boolean estaMirandoIzquierda() {
        return mirandoIzquierda;
    }

    @Override
    public boolean estaMoviendoDerecha() {
        return false; 
    }

    @Override
    public boolean estaMoviendoIzquierda() {
        return false; 
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
        if (mirandoIzquierda) {
            jugador.lanzarBolaDeNieveIzquierda();
        } else {
            jugador.lanzarBolaDeNieveDerecha();
        }
    }

    @Override
    public EstadoLogicoJugador verificarTransiciones() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - tiempoInicio >= DURACION_MS) {
            return new EstadoEnAire(jugador, mirandoIzquierda);
        }

        return null; 
    }

    @Override
    public void notificarAterrizaje() {
        // Ignorar aterrizaje mientras atraviesa
    }

    @Override
    public void notificarSueloResbaladizo() {
        // No aplicable
    }

    @Override
    public void notificarColisionTecho() {
        // No aplicable
    }

    @Override
    public void actualizarEstadoVisual() {
        if(mirandoIzquierda){
            jugador.cambiarEstadoSiDiferente(EstadoVisualBros.SALTANDO_IZQUIERDA);
        } else {
            jugador.cambiarEstadoSiDiferente(EstadoVisualBros.SALTANDO_DERECHA);
        }
    }
}
