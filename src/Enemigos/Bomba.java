package Enemigos;

import java.util.ArrayList;
import java.awt.Rectangle;

import Fabricas.CreadorEntidades;
import Fabricas.Sprites;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class Bomba extends Enemigo {
    private long tiempoCreacion;
    private static final long TIEMPO_EXPLOSION = 3000; 
    private boolean estaExplotando;
    private long tiempoInicioExplosion;
    private static final long DURACION_ANIMACION_EXPLOSION = 300; 
    private int frameAnimacionExplosion;

    private static final int SPRITE_BOMBA_VOLANDO = 8;
    private static final int SPRITE_BOMBA_EXPL_1 = 4;
    private static final int SPRITE_BOMBA_EXPL_2 = 5;

    public Bomba(Sprites spr, int px, int py, CreadorEntidades ce, float velX, float velY) {
        super();
        this.sprites = spr;
        ancho = 80;
        alto = 80;
        posx = px;
        posy = py;
        cajaColision = new Rectangle(px, py, ancho, alto);
        observers = new ArrayList<>();
        fabricaEntidades = ce;
        powerUpAlMorir = null;
        disparosHastaCongelar = 1; 
        puntosAlCongelar = 100;
        puntosAlMorir = 200;

        tiempoCreacion = System.currentTimeMillis();
        estaExplotando = false;
        frameAnimacionExplosion = 0;
        velocidadRealX = velX;
        velocidadRealY = velY;
        sprites.set_estado_actual(SPRITE_BOMBA_VOLANDO);
        notificar();
    }

    @Override
    public void mover() {
        if (estaExplotando) {
            manejarAnimacionExplosion();
            return;
        }
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoCreacion;
        if (tiempoTranscurrido >= TIEMPO_EXPLOSION) {
            iniciarExplosion();
            return;
        }
        estadoEnemigo.actualizar();
        estadoEnemigo.mover(this);
    }

    @Override
    protected void moverNormal() {
        aplicarFisicaBase();
        if (posy <= 80) {
            iniciarExplosion();
        }
    }

    private void iniciarExplosion() {
        if (estaExplotando) {
            return;
        }

        estaExplotando = true;
        tiempoInicioExplosion = System.currentTimeMillis();
        frameAnimacionExplosion = 0;
    }

    private void manejarAnimacionExplosion() {
        long tiempoExplosion = System.currentTimeMillis() - tiempoInicioExplosion;
        frameAnimacionExplosion++;
        if (frameAnimacionExplosion >= 5) { 
            frameAnimacionExplosion = 0;
        }

        if (frameAnimacionExplosion < 3) {
            sprites.set_estado_actual(SPRITE_BOMBA_EXPL_1);
        } else {
            sprites.set_estado_actual(SPRITE_BOMBA_EXPL_2);
        }
        notificar();

        // Eliminar bomba después de la animación
        if (tiempoExplosion >= DURACION_ANIMACION_EXPLOSION) {
            eliminarBomba();
        }
    }
    private void eliminarBomba() {
        for (Grafica.Observer o : observers) {
            o.eliminarDePantalla();
        }
        if (nivel != null) {
            nivel.eliminarEntidad(this);
            nivel.EliminarMovible(this);
        }
    }

    @Override
    public void disparar() {
        // Las bombas no disparan
    }

    @Override
    public void chocar(Colisionable c) {
        if (!estaExplotando) {
            c.afectar(this);
        }
    }

    @Override
    public void afectar(Jugador j) {
        if (!estaExplotando) {
            if (!estadoEnemigo.afectarJugador(j)) {
                if (!j.getInformacion().getInvulnerable()) {
                    j.perderVida();
                }
            }
            iniciarExplosion();
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
    }

    @Override
    public void afectar(Plataforma p) {
        if (!estaExplotando) {
            iniciarExplosion();
        }
    }

    @Override
    public void afectar(Obstaculo o) {
        if (!estaExplotando) {
            iniciarExplosion();
        }
    }

    @Override
    public void afectar(PowerUp p) {
        // No interactúa
    }
}
