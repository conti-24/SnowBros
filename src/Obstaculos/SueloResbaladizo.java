package Obstaculos;

import java.util.ArrayList;

import Enemigos.Enemigo;
import Fabricas.Sprites;
import Logica.Jugador;
import Plataformas.Piso;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class SueloResbaladizo extends Piso {

    CajaSuperiorSueloResbaladizo cajaSuperior;

    public SueloResbaladizo(Sprites sprites, int px, int py) {
        super(sprites, px, py);
        ancho = 50;
        alto = 50;
        cajaColision = new java.awt.Rectangle(px, py, ancho, alto);
        cajaSuperior = new CajaSuperiorSueloResbaladizo(this, posx, posy);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
    }

    public Sprites getSprites() {
        return sprites;
    }

    public CajaSuperiorSueloResbaladizo getCajaSuperior() {
        return cajaSuperior;
    }

    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
        j.aplicarSueloResbaladizo();
        notificar();
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

    }

    @Override
    public void afectar(Obstaculo o) {

    }

    @Override
    public void afectar(PowerUp p) {

    }

}