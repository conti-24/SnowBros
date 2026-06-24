package Proyectiles;

import Enemigos.Enemigo;
import Logica.Entidad;
import Logica.Jugador;
import Logica.Movible;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Visitor.Colisionable;

public abstract class Proyectil extends Entidad implements Movible {

    protected boolean moviendoDerecha;
    protected boolean moviendoIzquierda;
    protected float velocidadRealX;
    private static final float ACELERACION = 2f;
    private static final float VELOCIDAD_MAX = 8f;

    public void mover() {
        int posAnteriorX = posx;
        int posAnteriorY = posy;

        aplicarFisicaHorizontal();
        posx += (int) velocidadRealX;
        cajaColision.setLocation(posx, posy);
        if (posAnteriorX != posx || posAnteriorY != posy) {
            notificar();
        }
    }

    private void aplicarFisicaHorizontal() {
        if (moviendoDerecha) {
            velocidadRealX += ACELERACION;
            if (velocidadRealX > VELOCIDAD_MAX) {
                velocidadRealX = VELOCIDAD_MAX;
            }
        } else if (moviendoIzquierda) {
            velocidadRealX -= ACELERACION;
            if (velocidadRealX < -VELOCIDAD_MAX) {
                velocidadRealX = -VELOCIDAD_MAX;
            }
        }
    }

    public void setMoviendoDerecha(boolean moviendoDerecha) {
        this.moviendoDerecha = moviendoDerecha;
    }

    public void setMoviendoIzquierda(boolean moviendoIzquierda) {
        this.moviendoIzquierda = moviendoIzquierda;
    }

    public void setVelocidadRealX(float velocidadRealX) {
        this.velocidadRealX = velocidadRealX;
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
        velocidadRealX = 0;
        moviendoDerecha = false;
        moviendoIzquierda = false;
        nivel.eliminarEntidad(this);
        nivel.EliminarMovible(this);
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
