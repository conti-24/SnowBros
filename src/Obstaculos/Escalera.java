package Obstaculos;

import java.util.ArrayList;
import java.awt.Rectangle;

import Enemigos.Enemigo;
import Fabricas.Sprites;
import Grafica.Observer;
import Logica.Jugador;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class Escalera extends Obstaculo {

    public Escalera(Sprites sprites, int posicionx, int posiciony) {
        super(sprites, posicionx, posiciony);
        ancho = 60;
        alto = 90;
        cajaColision = new Rectangle(posicionx, posiciony, ancho-30, alto);
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
        Rectangle cajaEscalera = this.cajaColision;
        Rectangle cajaJugador = j.getRectangulo();
        if (cajaEscalera.intersects(cajaJugador)) {
            int limitePiso = 50; 
            boolean estaCercaDelSuelo = j.getPosY() <= limitePiso + 20;
            
            if (j.isMoviendoArriba() && !j.getEstadoLogico().estaEnEscalera() && !estaCercaDelSuelo) {
                j.cambiarEstadoLogicoAEscalera();
            }
            
            if (j.getVelocidadRealY() <= 0 && !j.getEstadoLogico().estaEnEscalera()) {
                int pieJugador = cajaJugador.y;
                int superficieEscalera = cajaEscalera.y + cajaEscalera.height;
                int diferencia = pieJugador - superficieEscalera;
                
                if (diferencia >= -20 && diferencia <= 5) {
                    j.setPosY(superficieEscalera);
                    j.setVelocidadRealY(0);
                    j.getEstadoLogico().notificarAterrizaje();
                    j.getRectangulo().setLocation(j.getPosX(), superficieEscalera);
                }
            }
        }
    }

    @Override
    public void afectar(Enemigo e) {
        // Los enemigos no interactúan con la escalera
    }

    @Override
    public void afectar(Proyectil p) {
        // Los proyectiles no interactúan con la escalera
    }

    @Override
    public void afectar(BolaDeNieve b) {
        // Las bolas de nieve no interactúan con la escalera
    }

    @Override
    public void afectar(Plataforma p) {
        // No interactúa con otras plataformas
    }

    @Override
    public void afectar(Obstaculo o) {
        // No interactúa con otros obstáculos
    }

    @Override
    public void afectar(PowerUp pw) {
        // No interactúa con power-ups
    }

    @Override
    public Rectangle getRectangulo() {
        return cajaColision;
    }

    public void agregarObserver(Observer observer) {
        observers.add(observer);
    }

    public boolean estaEnEscalera(Rectangle cajaJugador) {
        return cajaColision.intersects(cajaJugador);
    }

    @Override
    public boolean jugadorEstaEnObstaculo(Rectangle cajaJugador) {
        return estaEnEscalera(cajaJugador);
    }
}
