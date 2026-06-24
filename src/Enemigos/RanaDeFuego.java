package Enemigos;

import java.util.ArrayList;
import java.awt.Rectangle;

import Fabricas.CreadorEntidades;
import Fabricas.Sprites;
import Logica.ConfiguracionFisica;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class RanaDeFuego extends Enemigo {

    private int framesDesdeUltimoDisparo = 0;
    private int intervaloDisparoProximo = 0;
    private int framesDesdeDisparo = 0;
    private boolean estaDisparando = false;
    private java.util.Random random = new java.util.Random();
    private boolean alternar;
    private int contadorAnimacion;    public RanaDeFuego(Sprites spr, int px, int py, CreadorEntidades ce, PowerUp p) {
        super();
        this.sprites = spr;
        ancho = 80;
        alto = 80;
        posx = px;
        posy = py;
        cajaColision = new Rectangle(px, py, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
        fabricaEntidades = ce;
        powerUpAlMorir = p;
        disparosHastaCongelar = 3;
        puntosAlCongelar = 150;
        puntosAlMorir = 300;
        this.intervaloDisparoProximo = 120 + random.nextInt(121);
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

        if (!estaCongelado() && !esBolaDeNieve()) {
            framesDesdeUltimoDisparo++;
            if (framesDesdeUltimoDisparo >= intervaloDisparoProximo) {
                framesDesdeUltimoDisparo = 0;
                intervaloDisparoProximo = 120 + random.nextInt(121);
                disparar();
            }
        }

        if (estaDisparando) {
            framesDesdeDisparo++;
            if (framesDesdeDisparo >= 12) {
                framesDesdeDisparo = 0;
                estaDisparando = false;
                if (!estaCongelado()) {
                    actualizarSpriteSegunDireccion();
                }
            }
        }

        estadoEnemigo.actualizar();
        estadoEnemigo.mover(this);

        if (!detenido && !estaDisparando && !estaCongelado()) {
            contadorAnimacion++;
            if (contadorAnimacion >= 10) {
                alternar = !alternar;
                contadorAnimacion = 0;
                actualizarSpriteSegunDireccion();
            }
        }
    }    @Override
    protected void moverNormal() {
        aplicarFisicaBase();
    }

    @Override
    public void cambiarDireccion() {
        moviendoDerecha = !moviendoDerecha;
        velocidadRealX = moviendoDerecha ? ConfiguracionFisica.VELOCIDAD_RANA_DE_FUEGO
                : -ConfiguracionFisica.VELOCIDAD_RANA_DE_FUEGO;
        actualizarSpriteSegunDireccion();
    }
    private void actualizarSpriteSegunDireccion() {
        if (!estaCongelado() && !estaDisparando) {
            int sprite;
            if (moviendoDerecha) {
                sprite = alternar ? 2 : 0;
            } else {
                sprite = alternar ? 3 : 1;
            }
            sprites.set_estado_actual(sprite);
            notificar();
        }
    }

    @Override
    public void disparar() {
        sprites.set_estado_actual(moviendoDerecha ? 4 : 5);
        estaDisparando = true;
        framesDesdeDisparo = 0;
        notificar();
        if (nivel != null && fabricaEntidades != null) {
            int proyectilX = moviendoDerecha ? (posx + ancho) : (posx - 40);
            int proyectilY = posy + (alto / 2) - 20; 

            Proyectiles.BolaDeFuego bolaDeFuego = fabricaEntidades.crearBolaDeFuego(
                    proyectilX,
                    proyectilY,
                    moviendoDerecha);

            bolaDeFuego.setNivelCorrespondiente(nivel);
            nivel.agregarEntidad(bolaDeFuego);
            nivel.agregarMovible(bolaDeFuego);
            nivel.registrarNuevaEntidad(bolaDeFuego);
        }
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

    @Override
    public void morirPorImpacto() {
        super.morirPorImpacto();
    }
}
