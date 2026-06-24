package Powerups;

import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;
import Enemigos.Enemigo;
import Fabricas.Sprites;

public class PocionAzul extends PowerUp {

    public PocionAzul(Sprites sprites, int px, int py) {
        super(300, 5000, "activo"); // 5 segundos = 5000 ms
        this.sprites = sprites;
        this.posx = px;
        this.posy = py;
        posicionar(px, py);
    }

    public void afectar(Jugador j) {
        if (j.getEfectos().hayVelocidad()) {
            return;
        }

        j.getInformacion().incrementarPuntaje(300);
        j.getEfectos().ajustarVelExtra(5);
        borrarme();
        notificar();
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
