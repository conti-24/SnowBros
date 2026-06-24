package Plataformas;

import java.awt.Rectangle;
import java.util.ArrayList;

import Enemigos.Enemigo;
import Logica.Entidad;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class CajaSuperiorPlataformaMovil extends Entidad{
    protected PlataformaMovil sueloPadre;
    private static final int GROSOR_ZONA = 5; 

    public CajaSuperiorPlataformaMovil(PlataformaMovil pm, int px, int py) {
        this.sueloPadre = pm;
        this.observers = new ArrayList<>();
        Rectangle cajaSuelo = sueloPadre.getRectangulo();
        this.posx = cajaSuelo.x;
        this.posy = cajaSuelo.y + cajaSuelo.height;
        this.ancho = cajaSuelo.width;
        this.alto = GROSOR_ZONA;
        this.cajaColision = new Rectangle(posx, posy, ancho, alto);
        this.sprites = null;
    }

    @Override
    public void chocar(Colisionable c) {
        // no choca con nada
    }

    @Override
    public void afectar(Jugador j) {
        sueloPadre.afectarPorCajaSuperior(j);   
    }

    @Override
    public void afectar(Enemigo e) {
        sueloPadre.afectarPorCajaSuperior(e);
    }

    @Override
    public void afectar(Proyectil p) {
        // no hace nada
    }

    @Override
    public void afectar(BolaDeNieve b) {
        // no hace nada
    }

    @Override
    public void afectar(Plataforma p) {
        // no hace nada
    }

    @Override
    public void afectar(Obstaculo o) {
        // no hace nada
    }

    @Override
    public void afectar(PowerUp p) {
        // no hace nada
    }

    public boolean esObservable() {
        return false;
    }
}
