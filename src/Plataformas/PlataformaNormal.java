package Plataformas;

import java.util.ArrayList;
import Enemigos.Enemigo;
import Fabricas.Sprites;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;
import java.awt.Rectangle;

public class PlataformaNormal extends Plataforma {
    public PlataformaNormal(Sprites sprites, int posicionx, int posiciony) {
        super(sprites, posicionx, posiciony);
        ancho = 350;
        alto = 50;
        cajaColision = new Rectangle(posicionx, posiciony, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
    }

    @Override
    public Sprites getSprites() {
        return sprites;
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
        Rectangle cajaPlataforma = this.cajaColision;
        Rectangle cajaEnemigo = e.getRectangulo();
        int pieEnemigo = cajaEnemigo.y;
        int superficiePlataforma = cajaPlataforma.y + cajaPlataforma.height;
        boolean solapamientoHorizontal = cajaEnemigo.x + cajaEnemigo.width > cajaPlataforma.x &&
                cajaEnemigo.x < cajaPlataforma.x + cajaPlataforma.width;

        if (!solapamientoHorizontal) {
            return;
        }

        int diferencia = pieEnemigo - superficiePlataforma;
        if (e.velocidadRealY <= 0 && diferencia >= -20 && diferencia <= 5) {
            e.setPosY(superficiePlataforma);
            e.velocidadRealY = 0;
            e.enElAire = false;
        }
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
