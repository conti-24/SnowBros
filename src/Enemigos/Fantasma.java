package Enemigos;

import java.awt.Rectangle;
import java.util.ArrayList;

import Fabricas.CreadorEntidades;
import Fabricas.Sprites;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class Fantasma extends Enemigo {
    

    public Fantasma(Sprites spr, int px, int py, PowerUp p, CreadorEntidades ce) {
        super();
        this.sprites = spr;
        ancho = 80;
        alto = 80;
        posx = px;
        posy = py;
        cajaColision = new Rectangle(px, py, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
        powerUpAlMorir = p;
        fabricaEntidades = ce;
    }

   @Override
    public void cambiarDireccion() {
        moviendoDerecha = !moviendoDerecha;
            sprites.set_estado_actual(moviendoDerecha ? 1 : 3);
            notificar();
    }

    @Override
    public void disparar() {
        
    }

    @Override
    protected void moverNormal() {
        if (moviendoDerecha) {
            velocidadRealX = VELOCIDAD_ENEMIGO;
        } else {
            velocidadRealX = -VELOCIDAD_ENEMIGO;
        }
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual % 3000 < 1500) { 
            velocidadRealY = -2.5f; 
        } else { 
            velocidadRealY = 2.5f; 
        }
        sprites.set_estado_actual(moviendoDerecha ? 1 : 3);
        
        notificar();

        posx += velocidadRealX;
        posy += velocidadRealY;

        if (posx <= LIMITE_PARED1 || posx >= LIMITE_PARED2 - ancho) {
            cambiarDireccion();
        }

        if (posy < 50) { 
            posy = 50;
        }
        if (posy > 650) { 
            posy = 650;
        }

        cajaColision.setLocation(posx, posy);
    }

    public void morirPorImpacto() {
        // No hace nada
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
        if (!estadoEnemigo.afectarJugador(j)) {
            if (!j.getInformacion().getInvulnerable()) {
                j.perderVida();
            }
        }
    }

    @Override
    public void afectar(Enemigo e) {
        estadoEnemigo.afectarEnemigo(e);
    }

    @Override
    public void afectar(Proyectil p) {

    }

    @Override
    public void afectar(BolaDeNieve b) {
        
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

    public boolean estaCompletamenteCongelado() {
        return false; 
    }

    public boolean puedeSerAfectadoPorBolaDeNieve() {
        return false; 
    }

    public boolean puedeAtravesarParedes() {
        return true; 
    }

}
