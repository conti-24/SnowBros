package Obstaculos;

import java.awt.Rectangle;
import java.util.ArrayList;

import Enemigos.Enemigo;
import Fabricas.Sprites;
import Logica.Entidad;
import Logica.Jugador;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class CajaSuperiorSueloResbaladizo extends Entidad {
    private SueloResbaladizo sueloPadre;
    private static final int GROSOR_ZONA = 5;

    public CajaSuperiorSueloResbaladizo(SueloResbaladizo sr, int px, int py) {
        this.sueloPadre = sr;
        this.observers = new ArrayList<>();
        Rectangle cajaSuelo = sr.getRectangulo();
        this.posx = cajaSuelo.x;
        this.posy = cajaSuelo.y + cajaSuelo.height;
        this.ancho = cajaSuelo.width;
        this.alto = GROSOR_ZONA;

        this.cajaColision = new Rectangle(posx, posy, ancho, alto);
        this.sprites = null;
    }

    public SueloResbaladizo getSueloPadre() {
        return sueloPadre;
    }

    @Override
    public Sprites getSprites() {
        return null; // No tiene representación visual
    }

    @Override
    public void chocar(Colisionable c) {
        // no choca con nada
    }

    @Override
    public void afectar(Jugador j) {
        sueloPadre.afectar(j);
    }

    @Override
    public void afectar(Enemigo e) {
    }

    @Override
    public void afectar(Proyectil p) {
        // No hace nada
    }

    @Override
    public void afectar(BolaDeNieve b) {
        // No hace nada
    }

    @Override
    public void afectar(Plataforma p) {
        // No hace nada
    }

    @Override
    public void afectar(Obstaculo o) {
        // No hace nada
    }

    @Override
    public void afectar(PowerUp p) {
        // No hace nada
    }

    @Override
    public boolean esObservable() {
        return false;
    }
}
