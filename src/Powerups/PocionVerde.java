package Powerups;

import Enemigos.Enemigo;
import Fabricas.Sprites;
import Logica.Jugador;
import Logica.SoundManager;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class PocionVerde extends PowerUp {

    public PocionVerde(Sprites sprites, int px, int py) {
        super(300, 5000, "activo"); // 5 segundos = 5000 ms
        this.sprites = sprites;
        this.posx = px;
        this.posy = py;
        posicionar(px, py);
    }

    public void afectar(Jugador j) {
        SoundManager.getInstance().playSound("potion_green");
        
        j.getInformacion().incrementarPuntaje(puntaje);
        j.detenerTodosEnemigos();
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
