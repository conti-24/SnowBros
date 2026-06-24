package Obstaculos;

import java.awt.Rectangle;

import Logica.Entidad;
import Fabricas.Sprites;

public abstract class Obstaculo extends Entidad {

    public Obstaculo(Sprites sprites, int px, int py) {
        this.sprites = sprites;
        this.posx = px;
        this.posy = py;
    }

    public boolean jugadorEstaEnObstaculo(Rectangle cajaJugador) {
        return false;
    }

    @Override
    public boolean jugadorEstaEnEntidad(Rectangle cajaJugador) {
        return jugadorEstaEnObstaculo(cajaJugador);
    }

}
