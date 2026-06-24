package Enemigos;

import Fabricas.CreadorEntidades;
import Fabricas.Sprites;
import Grafica.ObserverGrafico;
import Grafica.ObserverGraficoEnemigo;
import Logica.ConfiguracionFisica;
import Logica.Entidad;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Visitor.Colisionable;
import Logica.Movible;
import Obstaculos.ParedDestructible;

public abstract class Enemigo extends Entidad implements Movible {

    // ========== DATOS BASE (configuración) ==========
    protected int puntosAlCongelar;
    protected int puntosAlMorir;
    protected int disparosHastaCongelar;
    protected int disparosRecibidos;
    protected CreadorEntidades fabricaEntidades;
    protected PowerUp powerUpAlMorir;
    protected Sprites spritesCongelamiento;
    protected Sprites spritesBolaRodando; 

    // ========== FÍSICA Y MOVIMIENTO ==========
    public float velocidadRealX;
    public float velocidadRealY;
    public boolean moviendoDerecha;
    public boolean enElAire; 

    // ========== FLAGS DE CONTROL ==========
    protected boolean detenido;
    protected boolean esJefe;
    protected boolean yaMuerto = false; 

    // ========== CONSTANTES ==========
    protected static final float VELOCIDAD_BOLA = ConfiguracionFisica.VELOCIDAD_BOLA_NIEVE;
    protected static final float FRICCION_BOLA = ConfiguracionFisica.FRICCION_BOLA;
    protected static final int FRAMES_POR_SPRITE = 4;
    protected static final float GRAVEDAD = ConfiguracionFisica.GRAVEDAD;
    protected static final float VELOCIDAD_ENEMIGO = ConfiguracionFisica.VELOCIDAD_ENEMIGO_BASE;
    protected static final float FUERZA_SALTO_ENEMIGO = ConfiguracionFisica.FUERZA_SALTO_ENEMIGO;
    protected static final int LIMITE_PISO = ConfiguracionFisica.LIMITE_PISO;
    protected static final int LIMITE_TECHO = ConfiguracionFisica.LIMITE_TECHO;
    protected static final int LIMITE_PARED1 = ConfiguracionFisica.LIMITE_PARED1;
    protected static final int LIMITE_PARED2 = ConfiguracionFisica.LIMITE_PARED2;

    // ========== SISTEMA DE ESTADOS ==========
    protected EstadoNormal estadoNormal;
    protected EstadoSemiCongelado estadoSemiCongelado;
    protected EstadoEnemigo estadoEnemigo;

    public Enemigo() {
        estadoNormal = new EstadoNormal(this);
        estadoSemiCongelado = new EstadoSemiCongelado(this);
        estadoEnemigo = estadoNormal;
        disparosRecibidos = 0;
        velocidadRealX = VELOCIDAD_ENEMIGO;
        velocidadRealY = 0;
        moviendoDerecha = true;
        enElAire = false;
        esJefe = false;
    }

    public void recibirDisparo() {
        estadoEnemigo.recibirDisparo();
    }

    public void recibirDisparoConPotencia(float potencia) {
        estadoEnemigo.recibirDisparoConPotencia(potencia);
    }

    public void recibirDisparoConBola(Proyectiles.BolaDeNieve bola) {
        estadoEnemigo.recibirDisparoConBola(bola);
    }

    public void mover() {
        if (detenido && !esBolaDeNieve()) {
            return;
        }
        enElAire = true;
        estadoEnemigo.mover(this);
    }

    public abstract void disparar();

    public void morirPorImpacto() {
        estadoEnemigo.morirPorImpacto();
    }

    public void aplicarImpulso(float velocidad, boolean haciaLaDerecha) {
        estadoEnemigo.aplicarImpulso(velocidad, haciaLaDerecha);
    }
    protected abstract void moverNormal();

    public void intentarDisparar() {
        if (!estaCongelado() && !esBolaDeNieve() && !detenido) {
            disparar();
        }
    }

    public void intentarSaltar() {
        if (!enElAire && !estaCongelado() && !detenido) {
            velocidadRealY = FUERZA_SALTO_ENEMIGO;
            enElAire = true;
        }
    }

    public void intentarCambiarDireccion() {
        if (!estaCongelado() && !detenido) {
            cambiarDireccion();
        }
    }

    public void cambiarDireccion() {
        moviendoDerecha = !moviendoDerecha;
        velocidadRealX = moviendoDerecha ? VELOCIDAD_ENEMIGO : -VELOCIDAD_ENEMIGO;
    }

    public void aplicarFisicaBase() {
        int posAnteriorX = posx;
        int posAnteriorY = posy;

        boolean seMovioHorizontalmente = false;
        if (!estaCongelado()) {
            int nuevaPosX = posx + (int) velocidadRealX;
            if (nuevaPosX != posx) {
                posx = nuevaPosX;
                seMovioHorizontalmente = true;
            }
        }

        cajaColision.setLocation(posx, posy);

        // Solo verificar soporte si se movió horizontalmente y no está en el aire
        if (seMovioHorizontalmente && !enElAire && !estaCongelado()) {
            verificarSoporteDebajo();
        }

        if (enElAire) {
            velocidadRealY += GRAVEDAD;
            if (velocidadRealY < -15f) {
                velocidadRealY = -15f;
            }
        } else {
            velocidadRealY = 0;
        }

        posy += (int) velocidadRealY;

        if (posy < LIMITE_PISO) {
            posy = LIMITE_PISO;
            velocidadRealY = 0;
            enElAire = false;
        }

        if (posy > LIMITE_TECHO) {
            posy = LIMITE_TECHO;
            velocidadRealY = 0;
        }

        cajaColision.setLocation(posx, posy);

        if (posAnteriorX != posx || posAnteriorY != posy) {
            notificar();
        }
    }
    protected void verificarSoporteDebajo() {
        if (nivel == null) {
            return;
        }
        int anchoReducido = ancho - 10; 
        int altoDeteccion = 3; 

        java.awt.Rectangle zonaDeteccion = new java.awt.Rectangle(
                posx + 5, 
                posy - altoDeteccion, 
                anchoReducido,
                altoDeteccion);

        boolean haySoporte = false;
        for (Entidad superficie : nivel.getSuperficiesSolidas()) {
            if (superficie.getRectangulo().intersects(zonaDeteccion)) {
                haySoporte = true;
                break;
            }
        }
        if (!haySoporte) {
            enElAire = true;
        }
    }
    protected EstadoCongelado crearEstadoCongelado() {
        return new EstadoCongeladoEstatico(this);
    }

    public void cambiarEstado(EstadoEnemigo estadoNuevo) {
        estadoEnemigo = estadoNuevo;
        estadoEnemigo.actualizarSprite();
        notificar();
    }

    public synchronized void reducirCongelamiento(int cantidad) {
        disparosRecibidos -= cantidad;
        if (disparosRecibidos < 0) {
            disparosRecibidos = 0;
        }

        if (disparosRecibidos == 0) {
            cambiarEstado(estadoNormal);
            limpiarEfectosCongelamiento();
        } else if (disparosRecibidos < disparosHastaCongelar / 2) {
            cambiarEstado(estadoNormal);
            limpiarEfectosCongelamiento();
        } else if (disparosRecibidos < disparosHastaCongelar) {
            cambiarEstado(estadoSemiCongelado);
            estadoEnemigo.limpiarModoBola(); // Limpiar cualquier modo bola que pudiera existir
            estadoEnemigo.actualizarSprite();
        } else {
            estadoEnemigo.actualizarSprite();
        }
    }

    protected void limpiarEfectosCongelamiento() {
        estadoEnemigo.limpiarModoBola();
        if (spritesCongelamiento != null) {
            spritesCongelamiento.set_estado_actual(0);
        }
    }

    public void convertirEnBolaDeNieve(float velocidadInicial, boolean haciaLaDerecha) {
        estadoEnemigo.convertirEnBolaDeNieve(velocidadInicial, haciaLaDerecha, null);
    }

    public void convertirEnBolaDeNieve(float velocidadInicial, boolean haciaLaDerecha, Logica.Jugador jugador) {
        estadoEnemigo.convertirEnBolaDeNieve(velocidadInicial, haciaLaDerecha, jugador);
    }
    @Override
    public ObserverGrafico crearObserverGrafico() {
        return new ObserverGraficoEnemigo(this);
    }

    public boolean estaCongelado() {
        return estadoEnemigo.esEstadoCongelado();
    }

    public boolean estaCompletamenteCongelado() {
        return estadoEnemigo.esEstadoCompletamenteCongelado();
    }

    public boolean esBolaDeNieve() {
        return estadoEnemigo.esBolaDeNieve();
    }

    public boolean bolaEstaQuieta() {
        return estadoEnemigo.bolaEstaQuieta();
    }

    public boolean esJefe() {
        return esJefe;
    }

    public void setEsJefe(boolean esJefe) {
        this.esJefe = esJefe;
    }

    public void setEnElAire(boolean enElAire) {
        this.enElAire = enElAire;
    }

    public boolean isEnElAire() {
        return enElAire;
    }

    public Sprites getSpritesCongelamiento() {
        return spritesCongelamiento;
    }

    public void setSpritesCongelamiento(Sprites spritesCongelamiento) {
        this.spritesCongelamiento = spritesCongelamiento;
    }

    public Sprites getSpritesBolaRodando() {
        return spritesBolaRodando;
    }

    public void setSpritesBolaRodando(Sprites spritesBolaRodando) {
        this.spritesBolaRodando = spritesBolaRodando;
    }

    public int getDisparosRecibidos() {
        return disparosRecibidos;
    }

    public int getDisparosHastaCongelar() {
        return disparosHastaCongelar;
    }

    public EstadoEnemigo getEstadoActual() {
        return estadoEnemigo;
    }

    public EstadoNormal getEstadoNormal() {
        return estadoNormal;
    }

    public EstadoSemiCongelado getEstadoSemiCongelado() {
        return estadoSemiCongelado;
    }

    public void setEstadoEnemigo(EstadoEnemigo estado) {
        estadoEnemigo = estado;
    }

    public void setDetenido(boolean detenido) {
        this.detenido = detenido;
    }

    public boolean puedeSerAfectadoPorBolaDeNieve() {
        return true;
    }

    public boolean puedeAtravesarParedes() {
        return false;
    }

    public void afectarPorParedDestructible(ParedDestructible p) {
        estadoEnemigo.afectarPorParedDestructible(p);
    }

    @Override
    public void afectar(BolaDeNieve b) {
        estadoEnemigo.afectarPorBolaDeNieve(b);
    }

    public void eliminarBolaDeNieve(BolaDeNieve b) {
        if (nivel != null) {
            nivel.eliminarEntidad(b);
            nivel.EliminarMovible(b);
        }
    }

    public void chocar(Colisionable c) {
        // Si el enemigo ya murió, no procesar más colisiones
        if (yaMuerto) {
            return;
        }
        c.afectar(this);
    }
}