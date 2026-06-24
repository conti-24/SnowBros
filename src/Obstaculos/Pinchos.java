package Obstaculos;

import java.awt.Rectangle;
import java.util.ArrayList;

import Enemigos.Enemigo;
import Fabricas.Sprites;
import Logica.Jugador;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class Pinchos extends Obstaculo {

    public Pinchos(Sprites sprites, int px, int py) {
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
        if (!j.getInformacion().getInvulnerable() && j.getInformacion().getVidas() > 0) {
            j.perderVida();
        } else {
        }

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
