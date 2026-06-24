package Logica;

import Fabricas.*;
import Grafica.Observer;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Enemigos.Enemigo;

import java.awt.Rectangle;

import Estados.*;

public class Jugador extends Entidad implements Movible {
    protected InfoJugador informacion;
    protected EstadoJugador estadoActual; // Estado VISUAL (gráficos)
    protected EstadoLogicoJugador estadoLogico; // Estado LÓGICO (comportamiento)
    protected CreadorEntidades fabricaEntidades;
    protected Efectos efectos;
    protected int posxInicial;
    protected int posyInicial;
    protected float potenciaBaseDisparo;
    protected Map<Integer, EstadoJugador> mapeoEstados;

    private float velocidadRealX;
    private float velocidadRealY;
    private static final float FUERZA_SALTO = ConfiguracionFisica.FUERZA_SALTO_JUGADOR;
    
    private boolean moviendoArriba = false;
    private boolean moviendoAbajo = false;

    private static final int LIMITE_PISO = ConfiguracionFisica.LIMITE_PISO;
    private static final int LIMITE_TECHO = ConfiguracionFisica.LIMITE_TECHO;

    public Jugador(Sprites spr, CreadorEntidades ce) {
        crearMapeoEstados();
        fabricaEntidades = ce;
        sprites = spr;
        posx = 100;
        posy = LIMITE_PISO + 1;
        this.ancho = 80;
        this.alto = 80;
        posxInicial = posx;
        posyInicial = posy;
        estadoActual = new MoviendoDerecha(this);
        efectos = new Efectos(this);
        this.cajaColision = new Rectangle(posx, posy, ancho, alto);
        cajaColision.setBounds(posx, posy, 80, 80);
        velocidadX = 0;
        velocidadY = 0;
        velocidadRealX = 0;
        velocidadRealY = 0;
        potenciaBaseDisparo = 1;
        observers = new ArrayList<>();
        sprites.set_estado_actual(estadoActual.getIdentificadorEstado());
        informacion = new InfoJugador();
        estadoLogico = new EstadoEnSuelo(this);
    }

    public float getFuerzaSalto() {
        return FUERZA_SALTO;
    }

    public int getLimitePiso() {
        return LIMITE_PISO;
    }

    public int getLimiteTecho() {
        return LIMITE_TECHO;
    }

    public void setVelocidadRealX(float vx) {
        this.velocidadRealX = vx;
    }

    public void setVelocidadRealY(float vy) {
        this.velocidadRealY = vy;
    }

    public boolean haySoporteDebajo() {
        if (nivel == null) {
            return false;
        }
        Rectangle areaDebajo = new Rectangle(posx + 5, posy - 5, ancho - 10, 10);
        ArrayList<Entidad> plataformasCopia = new ArrayList<>(nivel.getSuperficiesSolidas());
        for (Entidad plataforma : plataformasCopia) {
            Rectangle cajaEntidad = plataforma.getRectangulo();
            if (areaDebajo.intersects(cajaEntidad)) {
                return true;
            }
        }
        return false;
    }

    public boolean verificarEscaleraEnPosicion() {
        if (nivel == null) {
            return false;
        }
        Rectangle cajaJugador = this.cajaColision;
        for (Obstaculos.Escalera escalera : nivel.getEscaleras()) {
            if (escalera.estaEnEscalera(cajaJugador)) {
                return true;
            }
        }
        return false;
    }

    public void verificarColisionPiso() {
        estadoLogico.verificarColisionPiso(posx, posy);
    }

    public void actualizarCajaColision() {
        this.cajaColision.setLocation(posx, posy);
    }

    public void cambiarEstadoSiDiferente(int nuevoEstadoId) {
        EstadoJugador nuevoEstado = mapeoEstados.get(nuevoEstadoId);
        if (nuevoEstado != null && estadoActual.getClass() != nuevoEstado.getClass()) {
            cambiarEstado(nuevoEstado);
        }
    }

    public void aplicarSueloResbaladizo() {
        estadoLogico.notificarSueloResbaladizo();
    }
    public void desaplicarEfectos() {
        estadoLogico = new EstadoEnSuelo(this);
    }

    public void crearMapeoEstados() {
        mapeoEstados = new HashMap<>();
        mapeoEstados.put(EstadoVisualBros.CAMINANDO_DERECHA, new MoviendoDerecha(this));
        mapeoEstados.put(EstadoVisualBros.CAMINANDO_IZQUIERDA, new MoviendoIzquierda(this));
        mapeoEstados.put(EstadoVisualBros.SALTANDO_DERECHA, new Saltando(this));
        mapeoEstados.put(EstadoVisualBros.SUBIENDO_ESCALERA, new SubiendoEscalera(this));
        mapeoEstados.put(EstadoVisualBros.BAJANDO_ESCALERA, new BajandoEscalera(this));
        mapeoEstados.put(EstadoVisualBros.SALTANDO_IZQUIERDA, new Saltando(this));
    }

    public Map<Integer, EstadoJugador> getMapeoEstados() {
        return mapeoEstados;
    }

    public void mover() {
        int posicionAnteriorX = posx;
        int posicionAnteriorY = posy;
        aplicarInputContinuo();
        aplicarFisicaEstadoLogico();
        verificarColisionPiso();
        verificarTransicionesEstadoLogico();
        actualizarEstadoVisual();
        animarEstadoVisual();
        if (posicionAnteriorX != posx || posicionAnteriorY != posy) {
            this.cajaColision.setLocation(posx, posy);
            notificar();
        }
    }

    private void animarEstadoVisual() {
        if (estadoActual != null) {
            estadoActual.mover();
        }
    }

    private void verificarTransicionesEstadoLogico() {
        EstadoLogicoJugador nuevoEstado = estadoLogico.verificarTransiciones();
        if (nuevoEstado != null) {
            cambiarEstadoLogico(nuevoEstado);
        }
    }

    private void aplicarFisicaEstadoLogico() {
        estadoLogico.aplicarFisicaHorizontal();
        estadoLogico.aplicarFisicaVertical();
        estadoLogico.actualizarPosicion();
    }

    private void aplicarInputContinuo() {
        if (moviendoArriba) {
            estadoLogico.moverArriba();
        }
        if (moviendoAbajo) {
            estadoLogico.moverAbajo();
        }
    }

    private void cambiarEstadoLogico(EstadoLogicoJugador nuevoEstado) {
        if (estadoLogico != null) {
            estadoLogico.alSalir();
        }
        estadoLogico = nuevoEstado;
        estadoLogico.alEntrar();
    }

    public void cambiarEstadoLogicoAEscalera() {
        if (!estadoLogico.estaEnEscalera() && moviendoArriba) {
            boolean mirandoIzq = estadoLogico.estaMirandoIzquierda();
            cambiarEstadoLogico(new EstadoEnEscalera(this, mirandoIzq));
        }
    }

    public void bajar() {
        estadoLogico.intentarBajar();
    }

    public void actualizarEstadoVisual() {
        estadoLogico.actualizarEstadoVisual();
    }

    public void disparar() {
        estadoLogico.disparar();
        if (estadoActual != null) {
            estadoActual.disparar();
        }
    }

    public void cambiarEstado(EstadoJugador nuevoEstado) {
        estadoActual = nuevoEstado;
        int idEstadoVisual = nuevoEstado.getIdentificadorEstado();
        sprites.set_estado_actual(idEstadoVisual);
    }

    public EstadoJugador getEstadoActual() {
        return estadoActual;
    }

    public EstadoLogicoJugador getEstadoLogico() {
        return estadoLogico;
    }

    public float getPotenciaBaseDisparo() {
        return potenciaBaseDisparo;
    }

    public float getPotenciaDisparo() {
        return potenciaBaseDisparo + efectos.getPotExtra();
    }

    public void setMoviendoDerecha(boolean moviendo) {
        if (moviendo) {
            estadoLogico.moverDerecha();
        } else {
            estadoLogico.detenerMovimientoHorizontal();
        }
    }

    public void setMoviendoIzquierda(boolean moviendo) {
        if (moviendo) {
            estadoLogico.moverIzquierda();
        } else {
            estadoLogico.detenerMovimientoHorizontal();
        }
    }

    public boolean isMoviendoDerecha() {
        return estadoLogico.estaMoviendoDerecha();
    }

    public boolean isMoviendoIzquierda() {
        return estadoLogico.estaMoviendoIzquierda();
    }

    public void setMoviendoArriba(boolean moviendo) {
        boolean cambio = (moviendo != this.moviendoArriba);    
        if (moviendo && cambio) {
            boolean subiendoEscalera = estadoLogico.intentarSubirEscalera();
            if (!subiendoEscalera) {
                estadoLogico.intentarSaltar();
            }
        }     
        this.moviendoArriba = moviendo;       
        if (!moviendo) {
            estadoLogico.detenerMovimientoVertical();
        }
    }
    
    public boolean isMoviendoArriba() {
        return moviendoArriba;
    }

    public void setMoviendoAbajo(boolean moviendo) {
        boolean cambio = (moviendo != this.moviendoAbajo);     
        if (moviendo && cambio) {
            estadoLogico.intentarBajar();
        }     
        this.moviendoAbajo = moviendo;     
        if (!moviendo) {
            estadoLogico.detenerMovimientoVertical();
        }
    }
    
    public boolean isMoviendoAbajo() {
        return moviendoAbajo;
    }

    public void subirEscalera() {
        estadoLogico.intentarSubirEscalera();
    }

    public boolean intentarSubirEscalera() {
        return estadoLogico.intentarSubirEscalera();
    }

    public void bajarEscalera() {
        estadoLogico.intentarBajar();
    }

    public boolean isEnEscalera() {
        return estadoLogico.estaEnEscalera();
    }

    public void saltar() {
        estadoLogico.intentarSaltar();
    }

    public boolean puedeSaltar() {
        return !estadoLogico.estaEnElAire();
    }

    public boolean isEnElAire() {
        return estadoLogico.estaEnElAire();
    }

    public float getVelocidadRealX() {
        return velocidadRealX;
    }

    public float getVelocidadRealY() {
        return velocidadRealY;
    }

    @Override
    public void agregarObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void quitarObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public Sprites getSprites() {
        return sprites;
    }

    public Efectos getEfectos() {
        return efectos;
    }

    public void setEfectos(Efectos e) {
        efectos = e;
    }

    public InfoJugador getInformacion() {
        return informacion;
    }

    public void setInformacion(InfoJugador i) {
        informacion = i;
    }

    public void sumarPuntos(int puntos) {
        if (informacion != null) {
            informacion.incrementarPuntaje(puntos);
        }
    }

    public void detenerTodosEnemigos() {
        nivel.detenerEnemigos();
    }

    @Override
    public void setNivelCorrespondiente(Nivel n) {
        super.setNivelCorrespondiente(n);
    }

    public void lanzarBolaDeNieveDerecha() {
        estadoLogico.lanzarBolaDeNieveDerecha();
    }

    public void lanzarBolaDeNieveIzquierda() {
        estadoLogico.lanzarBolaDeNieveIzquierda();
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
    }

    @Override
    public void afectar(Enemigo e) {
    }

    @Override
    public void afectar(Proyectil p) {
    }

    @Override
    public void afectar(BolaDeNieve b) {
    }

    @Override
    public void afectar(Plataforma p) {
        if (estadoLogico.puedeColisionarConPlataformas()) {
            Rectangle cajaPlataforma = p.getRectangulo();
            Rectangle cajaJugador = this.cajaColision;

            int pieJugador = cajaJugador.y;
            int superficiePlataforma = cajaPlataforma.y + cajaPlataforma.height;

            boolean solapamientoHorizontal = haySolapamientoHorizontal(cajaJugador, cajaPlataforma);
            if (!solapamientoHorizontal) {
                return;
            }

            int diferencia = pieJugador - superficiePlataforma;

            if (velocidadRealY <= 0 && diferencia >= -20 && diferencia <= 5) {
                posy = superficiePlataforma;
                velocidadRealY = 0;
                estadoLogico.notificarAterrizaje();
                this.cajaColision.setLocation(posx, posy);
            }
        }
    }
    private boolean haySolapamientoHorizontal(Rectangle cajaJugador, Rectangle cajaPlataforma) {
    return cajaJugador.x + cajaJugador.width > cajaPlataforma.x &&
           cajaJugador.x < cajaPlataforma.x + cajaPlataforma.width;
    }
    @Override
    public void afectar(Obstaculo o) {
    }

    public void perderVida() {
        SoundManager.getInstance().playSound("player_hit");
        informacion.ajustarVidas(-1);
        getInformacion().setInvulnerable(true);
        respawn();
    }

    public void respawn() {
        posx = posxInicial;
        posy = posyInicial;
        velocidadRealX = 0;
        velocidadRealY = 0;
        estadoLogico = new EstadoEnSuelo(this);
        cajaColision.setLocation(posx, posy);
        notificar();
    }

    @Override
    public Grafica.ObserverGrafico crearObserverGrafico() {
        return new Grafica.ObserverGraficoJugador(this);
    }

    @Override
    public void afectar(PowerUp p) {

    }

    public CreadorEntidades getFabricaEntidades() {
        return fabricaEntidades;
    }
}