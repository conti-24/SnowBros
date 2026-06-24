package Plataformas;

import Fabricas.Sprites;
import Logica.Entidad;

public abstract class Plataforma extends Entidad {

    public Plataforma(Sprites sprites, int px, int py) {
        this.sprites = sprites;
        this.posx = px;
        this.posy = py;
    }
}
