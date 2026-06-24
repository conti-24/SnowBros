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

public class VidaExtra extends PowerUp {

    public VidaExtra(Sprites sprites, int px, int py) {
        super(300, 5000, "activo"); // 5 segundos = 5000 ms
        this.sprites = sprites;
        posicionar(px, py);

    }

    public void afectar(Jugador j) {
        SoundManager.getInstance().playSound("extra_life");
        
        j.getInformacion().ajustarVidas(1);
        this.borrarme();
        notificar();
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Enemigo e) {
        // No hace nada con enemigos
    }

    @Override
    public void afectar(Proyectil p) {
        // No hace nada con proyectiles
    }

    @Override
    public void afectar(BolaDeNieve b) {
        // No hace nada con bolas de nieve
    }

    @Override
    public void afectar(Plataforma p) {
        // No hace nada con plataformas
    }

    @Override
    public void afectar(Obstaculo o) {
        // No hace nada con obstaculos
    }

    @Override
    public void afectar(PowerUp p) {
        // No hace nada con powerup
    }
}
