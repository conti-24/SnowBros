package Enemigos;

import java.util.ArrayList;
import java.awt.Rectangle;

import Fabricas.Sprites;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class DemonioRojo extends Enemigo {

    private boolean alternar;
    private int contadorAnimacion;

    public DemonioRojo(Sprites spr, int px, int py, PowerUp p) {
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
        disparosHastaCongelar = 3;
        puntosAlMorir = 300;
        puntosAlCongelar = 150;
        alternar = false;
        contadorAnimacion = 0;
    }

    protected void actualizarSpriteSegunEstado() {
        estadoEnemigo.actualizarSprite();
    }

    @Override
    public Sprites getSprites() {
        return sprites;
    }

    @Override
    public void mover() {
        if (detenido && !esBolaDeNieve()) {
            velocidadRealX = 0;
            velocidadRealY = 0;
            return;
        }
        estadoEnemigo.actualizar();
        estadoEnemigo.mover(this);

        if (!detenido) {
            contadorAnimacion++;
            if (contadorAnimacion >= 10) {
                alternar = !alternar;
                contadorAnimacion = 0;
                estadoEnemigo.actualizarSpriteConAnimacion(contadorAnimacion, alternar);
            }
        }
    }

    @Override
    protected void moverNormal() {
        aplicarFisicaBase();
    }

    @Override
    public void cambiarDireccion() {
        super.cambiarDireccion();
        actualizarSpriteSegunDireccion();
    }
    private void actualizarSpriteSegunDireccion() {
        if (!estaCongelado()) {
            int spriteBase = moviendoDerecha ? 0 : 2;
            int nuevoEstado = spriteBase + (int) (Math.random() * 2);
            sprites.set_estado_actual(nuevoEstado);
            notificar();
        }
    }

    @Override
    public void disparar() {
        // no dispara
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
    public void afectar(Plataforma p) {
        Rectangle cajaPlataforma = p.getRectangulo();
        Rectangle cajaEnemigo = this.cajaColision;

        int pieEnemigo = cajaEnemigo.y; 
        int superficiePlataforma = cajaPlataforma.y + cajaPlataforma.height; 
        int cabezaPlataforma = cajaPlataforma.y; 
        boolean solapamientoHorizontal = cajaEnemigo.x + cajaEnemigo.width > cajaPlataforma.x &&
                cajaEnemigo.x < cajaPlataforma.x + cajaPlataforma.width;

        if (!solapamientoHorizontal) {
            return;
        }
        int diferencia = pieEnemigo - superficiePlataforma;
        if (velocidadRealY <= 0 && diferencia >= -20 && pieEnemigo >= cabezaPlataforma) {
            if (diferencia <= 5) {
                posy = superficiePlataforma + 1;
                velocidadRealY = 0;
                enElAire = false;
                this.cajaColision.setLocation(posx, posy);
            }
        }
    }

    @Override
    public void afectar(Obstaculo o) {
        if (esBolaDeNieve() && Math.abs(velocidadRealX) > 2f) {
            morirPorImpacto();
        }
    }

    @Override
    public void afectar(PowerUp p) {

    }

}
