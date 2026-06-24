package Proyectiles;

import java.awt.Rectangle;
import java.util.ArrayList;

import Enemigos.Enemigo;
import Fabricas.Sprites;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Visitor.Colisionable;

public class ProyectilMoghera extends Proyectil {
    private boolean yaColisiono;
    protected boolean moviendoDerecha;
    protected float velocidadRealX;
    private static final float VELOCIDAD = 8f;
    private int frameContador;
    private static final int FRAMES_POR_SPRITE = 3;

    public ProyectilMoghera(Sprites sprites, int px, int py, boolean direccionDerecha) {
        this.sprites = sprites;
        this.posx = px;
        this.posy = py;
        this.ancho = 120;
        this.alto = 40;
        sprites.set_estado_actual(0);
        cajaColision = new Rectangle(posx, posy, ancho, alto);
        observers = new ArrayList<>();
        yaColisiono = false;
        frameContador = 0;

        moviendoDerecha = direccionDerecha;
        velocidadRealX = direccionDerecha ? VELOCIDAD : -VELOCIDAD;
    }

    @Override
    public void mover() {
        int posAnteriorX = posx;
        int posAnteriorY = posy;

        posx += (int) velocidadRealX;
        frameContador++;
        if (frameContador >= FRAMES_POR_SPRITE) {
            frameContador = 0;
            // Alternar entre sprites 0 y 1 para animación
            int spriteActual = (frameContador / FRAMES_POR_SPRITE) % 2;
            sprites.set_estado_actual(spriteActual);
        }

        cajaColision.setLocation(posx, posy);

        // Verificar si salió del mapa
        if (posx < 0 || posx > 1200) {
            eliminarDelNivel();
        }

        if (posAnteriorX != posx || posAnteriorY != posy) {
            notificar();
        }
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
        if (!yaColisiono) {
            yaColisiono = true;
            if (!j.getInformacion().getInvulnerable()) {
                j.perderVida();
            }
            eliminarDelNivel();
        }
    }

    @Override
    public void afectar(Enemigo e) {
        // No afecta a otros enemigos
    }

    @Override
    public void afectar(Proyectil p) {
        // No interactúa con otros proyectiles
    }

    @Override
    public void afectar(BolaDeNieve b) {
        // Colisiona con bolas de nieve del jugador y se destruye
        if (!yaColisiono) {
            yaColisiono = true;
            eliminarDelNivel();
        }
    }

    @Override
    public void afectar(Plataforma p) {
        // No colisiona con plataformas, las atraviesa
    }

    @Override
    public void afectar(Obstaculo o) {
        // Colisiona con obstáculos (paredes) y se destruye
        if (!yaColisiono) {
            yaColisiono = true;
            eliminarDelNivel();
        }
    }

    @Override
    public void afectar(PowerUp p) {
        // No interactúa con PowerUps
    }

    private void eliminarDelNivel() {
        if (nivel != null) {
            nivel.eliminarEntidad(this);
            nivel.EliminarMovible(this);
        }
    }

    @Override
    public Sprites getSprites() {
        return sprites;
    }
}
