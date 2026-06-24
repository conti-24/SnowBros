package Obstaculos;

import java.util.ArrayList;

import Enemigos.Enemigo;

import java.awt.Rectangle;

import Fabricas.Sprites;
import Logica.Jugador;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class ParedDestructible extends Obstaculo {

    public ParedDestructible(Sprites sprites, int px, int py) {
        super(sprites, px, py);
        ancho = 50;
        alto = 50;
        cajaColision = new Rectangle(px, py, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
    }

    public Sprites getSprites() {
        return sprites;
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
        Rectangle jugadorRect = j.getRectangulo();
        Rectangle paredRect = this.cajaColision;
        int jugadorCentroX = jugadorRect.x + jugadorRect.width / 2;
        int paredCentroX = paredRect.x + paredRect.width / 2;
        if (jugadorCentroX < paredCentroX) {
            j.setPosX(this.posx - jugadorRect.width);
        } else {
            j.setPosX(this.posx + this.ancho);
        }
    }

    @Override
    public void afectar(Enemigo e) {
        Rectangle enemigoRect = e.getRectangulo();
        Rectangle paredRect = this.cajaColision;
        int enemigoCentroX = enemigoRect.x + enemigoRect.width / 2;
        int paredCentroX = paredRect.x + paredRect.width / 2;
        int margen = 2;
        if (enemigoCentroX < paredCentroX) {
            e.setPosX(this.getPosX() - enemigoRect.width - margen);
        } else {
            e.setPosX(this.getPosX() + this.getAncho() + margen);
        }
        if (!e.esBolaDeNieve()) {
            e.getRectangulo().setLocation(e.getPosX(), e.getPosY());
            e.cambiarDireccion();
        }
        e.afectarPorParedDestructible(this);
    }

    public synchronized void destruir() {
        cajaColision.setBounds(0, 0, 0, 0);
        nivel.eliminarEntidad(this);
        nivel.getJuego().getJugador().getInformacion().incrementarPuntaje(250 / 2);
    }

    @Override
    public void afectar(Proyectil p) {
        p.setVelocidadRealX(0);
        p.setMoviendoIzquierda(false);
        p.setMoviendoDerecha(false);
        nivel.eliminarEntidad(p);
        nivel.EliminarMovible(p);
    }

    @Override
    public void afectar(BolaDeNieve b) {
        b.setVelocidadRealX(0);
        b.setMoviendoIzquierda(false);
        b.setMoviendoDerecha(false);
        nivel.eliminarEntidad(b);
        nivel.EliminarMovible(b);

    }

    @Override
    public void afectar(Plataforma p) {

    }

    @Override
    public void afectar(Obstaculo o) {

    }

    @Override
    public void afectar(PowerUp p) {

    }

}
