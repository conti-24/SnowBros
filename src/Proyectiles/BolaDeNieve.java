package Proyectiles;

import java.awt.Rectangle;
import java.util.ArrayList;

import Enemigos.Enemigo;
import Fabricas.Sprites;
import Logica.Entidad;
import Logica.Jugador;
import Logica.Movible;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Visitor.Colisionable;

public class BolaDeNieve extends Entidad implements Movible {

    protected boolean moviendoDerecha;
    protected boolean moviendoIzquierda;
    protected float velocidadRealX;
    protected float velocidadRealY;
    private static final float GRAVEDAD = -2.0f;
    private static final float ACELERACION = 5f;
    private static final float VELOCIDAD_MAX = 13f;
    private long tiempoInicioBolaDeNieve;
    private float potencia;
    private boolean yaColisiono;
    private Jugador duenio;

    public BolaDeNieve(Sprites sprites, int px, int py) {
        this.sprites = sprites;
        this.posx = px;
        this.posy = py;
        this.ancho = 40;
        this.alto = 40;
        sprites.set_estado_actual(0);
        cajaColision = new Rectangle(posx, posy, ancho, alto);
        observers = new ArrayList<>();
        velocidadRealX = 0;
        velocidadRealY = 0;
        moviendoDerecha = false;
        moviendoIzquierda = false;
        potencia = 1.0f;
        yaColisiono = false;
        tiempoInicioBolaDeNieve = System.currentTimeMillis();

    }

    public void mover() {
        long tiempoActual = System.currentTimeMillis();
        int posAnteriorX = posx;
        int posAnteriorY = posy;

        aplicarFisicaHorizontal();
        if (tiempoActual - tiempoInicioBolaDeNieve >= 200) {
            aplicarFisicaVertical();

        }
        posx += (int) velocidadRealX;
        posy += (int) velocidadRealY;
        cajaColision.setLocation(posx, posy);

        if (posAnteriorX != posx || posAnteriorY != posy) {
            notificar();
        }
    }

    private void aplicarFisicaHorizontal() {
        if (moviendoDerecha) {
            velocidadRealX += ACELERACION;
            if (velocidadRealX > VELOCIDAD_MAX) {
                velocidadRealX = VELOCIDAD_MAX;
            }
        } else if (moviendoIzquierda) {
            velocidadRealX -= ACELERACION;
            if (velocidadRealX < -VELOCIDAD_MAX) {
                velocidadRealX = -VELOCIDAD_MAX;
            }
        }
    }

    private void aplicarFisicaVertical() {
        velocidadRealY += GRAVEDAD;

    }

    public void setMoviendoDerecha(boolean moviendoDerecha) {
        this.moviendoDerecha = moviendoDerecha;
    }

    public void setMoviendoIzquierda(boolean moviendoIzquierda) {
        this.moviendoIzquierda = moviendoIzquierda;
    }

    public void setDuenio(Jugador jugador) {
        this.duenio = jugador;
    }

    public void setYaColisiono(boolean yaColisiono) {
        this.yaColisiono = yaColisiono;
    }

    public Jugador getDuenio() {
        return this.duenio;
    }

    public void setVelocidadRealX(float velocidadRealX) {
        this.velocidadRealX = velocidadRealX;
    }

    public void setPotencia(float potencia) {
        this.potencia = potencia;
    }

    public float getPotencia() {
        return potencia;
    }

    public boolean yaColisiono() {
        return yaColisiono;
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
        e.afectar(this);
    }

    @Override
    public void afectar(Proyectil p) {

    }

    @Override
    public void afectar(BolaDeNieve b) {

    }

    @Override
    public void afectar(Plataforma p) {
        moviendoDerecha = false;
        moviendoIzquierda = false;
        nivel.eliminarEntidad(this);
        nivel.EliminarMovible(this);
    }

    @Override
    public void afectar(Obstaculo o) {

    }

    @Override
    public void afectar(PowerUp p) {

    }

}