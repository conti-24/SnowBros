package Estados;

import Logica.Jugador;

public class EstadoEnEscalera extends EstadoLogicoJugador {
    private boolean subiendoEscalera;
    private boolean bajandoEscalera;
    private boolean mirandoIzquierda;
    private long ultimoDisparo;
    private static final long COOLDOWN_DISPARO_MS = 300;
    private static final float VELOCIDAD_ESCALERA = 4.0f; 

    public EstadoEnEscalera(Jugador j, boolean mirandoIzquierda) {
        super(j);
        this.mirandoIzquierda = mirandoIzquierda;
        this.subiendoEscalera = false; 
        this.bajandoEscalera = false;
        this.ultimoDisparo = 0;
    }

    @Override
    public void aplicarFisicaHorizontal() {
        jugador.setVelocidadRealX(0);
    }

    @Override
    public void aplicarFisicaVertical() {
        float velocidadY = 0;

        if (subiendoEscalera) {
            velocidadY = VELOCIDAD_ESCALERA;
        } else if (bajandoEscalera) {
            velocidadY = -VELOCIDAD_ESCALERA;
        }

        jugador.setVelocidadRealY(velocidadY);
    }

    @Override
    public void actualizarPosicion() {
        int nuevaPosY = jugador.getPosY() + Math.round(jugador.getVelocidadRealY());
        jugador.setPosY(nuevaPosY);
    }

    @Override
    public boolean intentarSaltar() {
        jugador.setVelocidadRealY(jugador.getFuerzaSalto());
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
        if (jugador.verificarEscaleraEnPosicion()) {
            subiendoEscalera = true;
            bajandoEscalera = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean intentarBajar() {
        if (jugador.verificarEscaleraEnPosicion()) {
            bajandoEscalera = true;
            subiendoEscalera = false;
            return true;
        }
        return false;
    }

    @Override
    public void moverDerecha() {
        mirandoIzquierda = false;
    }

    @Override
    public void moverIzquierda() {
        mirandoIzquierda = true;
    }

    @Override
    public void moverArriba() {
        subiendoEscalera = true;
        bajandoEscalera = false;
    }

    @Override
    public void moverAbajo() {
        bajandoEscalera = true;
        subiendoEscalera = false;
    }

    @Override
    public void detenerMovimientoHorizontal() {
    }

    public void detenerMovimientoVertical() {
        subiendoEscalera = false;
        bajandoEscalera = false;
    }

    @Override
    public boolean estaEnElAire() {
        return false;
    }

    @Override
    public boolean estaEnEscalera() {
        return true;
    }

    @Override
    public boolean puedeColisionarConPlataformas() {
        return !subiendoEscalera;
    }

    @Override
    public boolean estaAtravesandoPlataforma() {
        return bajandoEscalera;
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
        return subiendoEscalera;
    }

    @Override
    public boolean estaBajandoEscalera() {
        return bajandoEscalera;
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
       
        if (bajandoEscalera) {
            int limitePiso = jugador.getLimitePiso();
            if (jugador.getPosY() <= limitePiso + 15) {
                bajandoEscalera = false;
                subiendoEscalera = false;
                return new EstadoEnSuelo(jugador);
            }
            
            if (jugador.haySoporteDebajo()) {
                bajandoEscalera = false;
                subiendoEscalera = false;
                return new EstadoEnSuelo(jugador);
            }
        }
        
        if (!jugador.verificarEscaleraEnPosicion()) {
            if (jugador.haySoporteDebajo()) {
                return new EstadoEnSuelo(jugador);
            } else {
                return new EstadoEnAire(jugador, mirandoIzquierda);
            }
        }

        if (jugador.getVelocidadRealY() > 1.0f) {
            return new EstadoEnAire(jugador, mirandoIzquierda);
        }

        return null;
    }

    @Override
    public void notificarAterrizaje() {
    }

    @Override
    public void notificarSueloResbaladizo() {
    }

    @Override
    public void notificarColisionTecho() {
        if (subiendoEscalera) {
            detenerMovimientoVertical();
        }
    }

    @Override
    public void alEntrar() {
        subiendoEscalera = false;
        bajandoEscalera = false;
    }

    @Override
    public void alSalir() {
        subiendoEscalera = false;
        bajandoEscalera = false;
        jugador.setVelocidadRealY(0);
    }

    public boolean isSubiendoEscalera() {
        return subiendoEscalera;
    }

    public boolean isBajandoEscalera() {
        return bajandoEscalera;
    }

    @Override
    public void actualizarEstadoVisual() {
        if (subiendoEscalera) {
                jugador.cambiarEstadoSiDiferente(EstadoVisualBros.SUBIENDO_ESCALERA);
            } else if (bajandoEscalera) {
                jugador.cambiarEstadoSiDiferente(EstadoVisualBros.BAJANDO_ESCALERA);
            } else {
                jugador.cambiarEstadoSiDiferente(EstadoVisualBros.SUBIENDO_ESCALERA);
            }
    }
}
