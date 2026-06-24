package Enemigos;

import java.util.ArrayList;
import java.awt.Rectangle;

import Fabricas.CreadorEntidades;
import Fabricas.Sprites;
import Logica.Jugador;
import Logica.SoundManager;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class Calabaza extends Enemigo {
    protected boolean aturdido;
    private int framesAturdido;

    public Calabaza(Sprites spr, int px, int py, PowerUp p, CreadorEntidades ce) {
        super();
        this.sprites = spr;
        ancho = 80;
        alto = 80;
        posx = px;
        posy = py;
        cajaColision = new Rectangle(px, py, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
        powerUpAlMorir = p;
        fabricaEntidades = ce;
        aturdido = false;
        framesAturdido = 0;
    }

    public void cambiarEstadoAturdir() {
        if (!aturdido) {
            aturdido = true;
            framesAturdido = 180; 
            sprites.set_estado_actual(0);
            notificar();
        }
    }

    public boolean estaAturdido() {
        return aturdido;
    }

    @Override
    public void cambiarDireccion() {
        moviendoDerecha = !moviendoDerecha;
        if (!aturdido) {
            sprites.set_estado_actual(moviendoDerecha ? 1 : 3);
            notificar();
        }
    }

    @Override
    public void disparar() {
    }

    @Override
    protected void moverNormal() {
        if (aturdido) {
            if (framesAturdido > 0) {
                framesAturdido--;
            }
            if (framesAturdido <= 0) {
                aturdido = false;
                sprites.set_estado_actual(moviendoDerecha ? 1 : 3);
                notificar();
            }
            velocidadRealX = 0;
            velocidadRealY += GRAVEDAD; 
            if (velocidadRealY < -15f) {
                velocidadRealY = -15f;
            }
            posy += (int) velocidadRealY;
            if (posy < LIMITE_PISO) {
                posy = LIMITE_PISO;
                velocidadRealY = 0;
            }
            
        } else {
            if (moviendoDerecha) {
                velocidadRealX = VELOCIDAD_ENEMIGO * 0.7f;
            } else {
                velocidadRealX = -VELOCIDAD_ENEMIGO * 0.7f;
            }
            long tiempoActual = System.currentTimeMillis();
            if (tiempoActual % 4000 < 2000) {
                velocidadRealY = -2.0f;
            } else {
                velocidadRealY = 2.0f;
            }
            posx += velocidadRealX;
            posy += velocidadRealY;
            if (posx <= LIMITE_PARED1 || posx >= LIMITE_PARED2 - ancho) {
                cambiarDireccion();
            }
            if (posy < 50) {
                posy = 50;
            }
            if (posy > 650) {
                posy = 650;
            }
        }
        if (aturdido) {
            sprites.set_estado_actual(0);
        } else {
            sprites.set_estado_actual(moviendoDerecha ? 1 : 3);
        }
        notificar();
        cajaColision.setLocation(posx, posy);
    }

    public void morirPorImpacto() {
        // No hace nada, la calabaza no muere al ser impactada por una esfera de nieve
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
        if (!estadoEnemigo.afectarJugador(j)) {
            if (!j.getInformacion().getInvulnerable()) {
                j.perderVida();
            }
        }
    }

    @Override
    public void afectar(Enemigo e) {
        estadoEnemigo.afectarEnemigo(e);
    }

    @Override
    public void afectar(Proyectil p) {

    }

    @Override
    public void afectar(BolaDeNieve b) {
        if (b.yaColisiono()) {
            return;
        }
        
        b.setYaColisiono(true);
        SoundManager.getInstance().playSound("hit");
        cambiarEstadoAturdir();
        
        // Detener la bola
        b.setVelocidadRealX(0);
        b.setMoviendoDerecha(false);
        b.setMoviendoIzquierda(false);
        
        // Eliminar la bola
        if (nivel != null) {
            nivel.eliminarEntidad(b);
            nivel.EliminarMovible(b);
        }
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

    public boolean estaCompletamenteCongelado() {
        return false; 
    }

    public boolean puedeAtravesarParedes() {
        return true;
    }

}
