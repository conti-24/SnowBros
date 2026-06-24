package Powerups;

import Enemigos.Enemigo;
import Fabricas.Sprites;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class Fruta extends PowerUp {
    public Fruta(Sprites sprite, int px, int py) {
        super(500, 5000, "activo"); // 5 segundos = 5000 ms
        this.sprites = sprite;
        posicionar(px, py);
    }

    public void afectar(Jugador j) {
        j.getInformacion().incrementarPuntaje(500);
        borrarme();
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
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
