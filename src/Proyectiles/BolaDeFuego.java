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

public class BolaDeFuego extends Proyectil {
    private boolean yaColisiono;
    protected boolean moviendoDerecha;
    protected float velocidadRealX;
    private static final float VELOCIDAD = 10f;

    public BolaDeFuego(Sprites sprites, int px, int py, boolean direccionDerecha) {
        this.sprites = sprites;
        this.posx = px;
        this.posy = py;
        this.ancho = 40;
        this.alto = 40;
        moviendoDerecha = direccionDerecha;
        velocidadRealX = direccionDerecha ? VELOCIDAD : -VELOCIDAD;
        
        // Sprite 0: derecha, Sprite 1: izquierda
        sprites.set_estado_actual(direccionDerecha ? 0 : 1);
        
        cajaColision = new Rectangle(posx, posy, ancho, alto);
        observers = new ArrayList<>();
        yaColisiono = false;
    }

    public void mover() {
        int posAnteriorX = posx;
        int posAnteriorY = posy;

        posx += (int) velocidadRealX;

        cajaColision.setLocation(posx, posy);

        // Verificar si salió del mapa
        if (posx < 0 || posx > 1200) {
            eliminarDelNivel();
        }

        if (posAnteriorX != posx || posAnteriorY != posy) {
            notificar();
        }
    }

    private void eliminarDelNivel() {
        if (nivel != null) {
            nivel.eliminarEntidad(this);
            nivel.EliminarMovible(this);
        }
    }

    @Override
    public void chocar(Colisionable c) {
        if (!yaColisiono) {
            c.afectar(this);
        }
    }

    @Override
    public void afectar(Jugador j) {
        if (!yaColisiono && !j.getInformacion().getInvulnerable()) {
            yaColisiono = true;
            j.getInformacion().ajustarVidas(-1);
            j.getInformacion().setInvulnerable(true);
            j.respawn();
            eliminarDelNivel();
        }
    }

    @Override
    public void afectar(Enemigo e) {
        // La bola de fuego no afecta a otros enemigos
    }

    @Override
    public void afectar(Proyectil p) {
        // No hace nada con otros proyectiles
    }

    @Override
    public void afectar(BolaDeNieve b) {
        // Las bolas de nieve pueden destruir las bolas de fuego
        if (!yaColisiono) {
            yaColisiono = true;
            eliminarDelNivel();
        }
    }

    @Override
    public void afectar(Plataforma p) {
        // No colisiona con plataformas (atraviesa)
    }

    @Override
    public void afectar(Obstaculo o) {
        // Colisiona con obstáculos y desaparece
        if (!yaColisiono) {
            yaColisiono = true;
            eliminarDelNivel();
        }
    }

    @Override
    public void afectar(PowerUp p) {
        // No hace nada con powerups
    }
}
