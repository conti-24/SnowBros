package Plataformas;

import java.awt.Rectangle;
import java.util.ArrayList;

import Fabricas.Sprites;
import Logica.Movible;
import Enemigos.Enemigo;
import Logica.Entidad;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class PlataformaMovil extends Plataforma implements Movible {
    private float velocidadX;
    private int limiteIzq;
    private int limiteDer;
    private boolean moviendoDerecha;
    private float velocidadY;
    private int limiteArriba;
    private int limiteAbajo;
    private boolean moviendoArriba;
    private CajaSuperiorPlataformaMovil cajaSuperior;
    private int contadorBonus = 0;

    public PlataformaMovil(Sprites sprites, int posicionx, int posiciony, boolean iniciaDerecha) {
        super(sprites, posicionx, posiciony);
        ancho = 280;
        alto = 50;
        cajaColision = new Rectangle(posicionx, posiciony, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
        cajaSuperior = new CajaSuperiorPlataformaMovil(this, posicionx, posiciony);

        this.limiteIzq = 0 + 50; // margen interno
        this.limiteDer = 1150 - 50;
        this.velocidadX = 1.0f; // velocidad por frame (ajustable)
        this.moviendoDerecha = iniciaDerecha;

        this.velocidadY = 0f;
        this.limiteArriba = posiciony;
        this.limiteAbajo = posiciony;
        this.moviendoArriba = false;
    }

    @Override
    public Sprites getSprites() {
        return sprites;
    }

    public boolean isMoviendoDerecha() {
        return moviendoDerecha;
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
        // La interacción con el jugador se maneja a través de la caja superior
        // No se necesita lógica adicional aquí
    }

    @Override
    public void afectar(Enemigo e) {
        // La interacción con enemigos se maneja a través de la caja superior
        // No se necesita lógica adicional aquí
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

    @Override
    public void mover() {
        int dx = 0;
        int dy = 0;

        if (velocidadX != 0f) {
            dx = (int) Math.signum(moviendoDerecha ? velocidadX : -velocidadX);
            posx += dx;
            if (posx <= limiteIzq) {
                posx = limiteIzq;
                moviendoDerecha = true;
            } else if (posx >= limiteDer) {
                posx = limiteDer;
                moviendoDerecha = false;
            }
        }

        if (velocidadY != 0f) {
            dy = (int) Math.signum(moviendoArriba ? -velocidadY : velocidadY);
            posy += dy;
            if (posy <= limiteArriba) {
                posy = limiteArriba;
                moviendoArriba = false;
            } else if (posy >= limiteAbajo) {
                posy = limiteAbajo;
                moviendoArriba = true;
            }
        }
        cajaColision.setLocation(posx, posy);
        if (cajaSuperior != null) {
            cajaSuperior.setPosX(posx);
            cajaSuperior.setPosY(posy + alto);
            cajaSuperior.getRectangulo().setLocation(posx, posy + alto);
        }
        notificar();
    }

    public void setLimites(int izq, int der) {
        this.limiteIzq = izq;
        this.limiteDer = der;
    }

    public void setVelocidad(float v) {
        this.velocidadX = v;
    }

    public void setVelocidadY(float v) {
        this.velocidadY = v;
    }

    public void afectarPorCajaSuperior(Jugador j) {
        contadorBonus++;
        if (contadorBonus >= 120) {
            if (nivel != null && nivel.getJuego() != null && nivel.getJuego().getJugador() != null) {
                nivel.getJuego().getJugador().getInformacion().incrementarPuntaje(200);
            }
            contadorBonus = 0;
        }

        if (isHorizontal()) {
            int desplazamiento = (int) Math.signum(moviendoDerecha ? velocidadX : -velocidadX);
            j.setPosX(j.getPosX() + desplazamiento);
            j.getRectangulo().setLocation(j.getPosX(), j.getPosY());
            j.notificar();
        } else if (velocidadY != 0) {
            int desplazamiento = (int) Math.signum(moviendoArriba ? -velocidadY : velocidadY);
            j.setPosY(j.getPosY() + desplazamiento);
            j.getRectangulo().setLocation(j.getPosX(), j.getPosY());
            j.notificar();
        }
    }

    public void afectarPorCajaSuperior(Enemigo e) {
        e.velocidadRealY = 0;
        e.enElAire = false;
        
        if (isHorizontal()) {
            int desplazamiento = (int) Math.signum(moviendoDerecha ? velocidadX : -velocidadX);
            e.setPosX(e.getPosX() + desplazamiento);
            e.getRectangulo().setLocation(e.getPosX(), e.getPosY());
            e.notificar();
        } else if (velocidadY != 0) {
            int desplazamiento = (int) Math.signum(moviendoArriba ? -velocidadY : velocidadY);
            e.setPosY(e.getPosY() + desplazamiento);
            e.getRectangulo().setLocation(e.getPosX(), e.getPosY());
            e.notificar();
        }
    }

    public boolean isHorizontal() {
        return velocidadX != 0;
    }

    public boolean isMoviendoArriba() {
        return moviendoArriba;
    }

    public void setLimitesVerticales(int arriba, int abajo) {
        this.limiteArriba = arriba;
        this.limiteAbajo = abajo;
    }

    public void setInicioDireccion(boolean derecha) {
        this.moviendoDerecha = derecha;
    }

    public Entidad getCajaSuperior() {
        return cajaSuperior;
    }
}
