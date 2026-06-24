package Enemigos;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Rectangle;

import Fabricas.CreadorEntidades;
import Fabricas.Sprites;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class Kamakichi extends Enemigo {
    private float velocidadY;
    private boolean moviendoArriba;
    private static final float VELOCIDAD_VERTICAL = 2.0f;
    private static final int LIMITE_ARRIBA = 650;
    private static final int LIMITE_ABAJO = 250;
    private static final int POSICION_X_FIJA = 600 - 168; // Centro de pantalla (1200/2 - ancho/2)

    private long ultimoDisparo;
    private long tiempoInicio;
    private long intervaloDisparoActual;
    private static final long INTERVALO_DISPARO_INICIAL = 2000; // Inicia disparando cada 2 segundos
    private static final long INTERVALO_DISPARO_MINIMO = 500; // Mínimo 0.5 segundos
    private static final long TIEMPO_ACELERACION = 10000; // Cada 10 segundos se acelera
    private Random random;

    public Kamakichi(Sprites spr, int px, int py, CreadorEntidades ce, PowerUp p) {
        super();
        this.sprites = spr;
        ancho = 336;
        alto = 228;
        posx = POSICION_X_FIJA; 
        posy = py;
        cajaColision = new Rectangle(posx, posy, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(1); // Sprite normal (KamakichiNormal.png)
        fabricaEntidades = ce;
        powerUpAlMorir = p;
        disparosHastaCongelar = 50;
        puntosAlCongelar = 1000;
        puntosAlMorir = 5000;

        velocidadY = VELOCIDAD_VERTICAL;
        moviendoArriba = true;
        ultimoDisparo = System.currentTimeMillis();
        tiempoInicio = System.currentTimeMillis();
        intervaloDisparoActual = INTERVALO_DISPARO_INICIAL;
        random = new Random();

        // Inicializar estados específicos de Kamakichi
        estadoNormal = new EstadoKamakichiNormal(this);
        estadoEnemigo = estadoNormal;
    }

    @Override
    protected EstadoCongelado crearEstadoCongelado() {
        return new EstadoKamakichiCongelado(this);
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
    }
    public void moverKamakichiNormal() {
        moverNormal();
        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
        long aceleraciones = tiempoTranscurrido / TIEMPO_ACELERACION;
        intervaloDisparoActual = Math.max(
                INTERVALO_DISPARO_MINIMO,
                INTERVALO_DISPARO_INICIAL - (aceleraciones * 200));
        long ahora = System.currentTimeMillis();
        if (ahora - ultimoDisparo >= intervaloDisparoActual) {
            disparar();
            ultimoDisparo = ahora;
        }
    }

    protected void actualizarSpriteSegunEstado() {
        estadoEnemigo.actualizarSprite();
    }

    public void actualizarSpriteCongelamiento() {
        estadoEnemigo.actualizarSprite();
    }

    @Override
    public Sprites getSprites() {
        return sprites;
    }

    @Override
    public void recibirDisparoConBola(Proyectiles.BolaDeNieve bola) {
        disparosRecibidos += (int) bola.getPotencia();
        estadoEnemigo.recibirDisparoConBola(bola);
    }

    @Override
    public void convertirEnBolaDeNieve(float velocidadInicial, boolean haciaLaDerecha, Logica.Jugador jugador) {
        if (estaCompletamenteCongelado() && !esBolaDeNieve()) {
            ancho = 336;
            alto = 228;
            cajaColision.setSize(ancho, alto);
            super.convertirEnBolaDeNieve(velocidadInicial, haciaLaDerecha, jugador);
        }
    }

    @Override
    protected void moverNormal() {
        if (moviendoArriba) {
            posy += (int) velocidadY;
            if (posy >= LIMITE_ARRIBA) {
                posy = LIMITE_ARRIBA;
                moviendoArriba = false;
            }
        } else {
            posy -= (int) velocidadY;
            if (posy <= LIMITE_ABAJO) {
                posy = LIMITE_ABAJO;
                moviendoArriba = true;
            }
        }

        posx = POSICION_X_FIJA;

        cajaColision.setLocation(posx, posy);
        notificar();
    }

    @Override
    public void disparar() {
        if (nivel == null || fabricaEntidades == null) {
            return;
        }
        int bombaXBase = posx + (ancho / 2) - 20;
        int bombaYBase = posy + (alto / 2);
        lanzarBomba(bombaXBase, bombaYBase, 4f, 8f);
        lanzarBomba(bombaXBase, bombaYBase, 8f, 12f);
        lanzarBomba(bombaXBase, bombaYBase, 10f, 14f);
    }

    private void lanzarBomba(int x, int y, float velocidadMin, float velocidadMax) {
        double angulo = random.nextDouble() * 2 * Math.PI;

        float velocidadBase = random.nextFloat() * (velocidadMax - velocidadMin) + velocidadMin;

        float velocidadX = (float) (Math.cos(angulo) * velocidadBase);
        float velocidadY = (float) (Math.sin(angulo) * velocidadBase);

        Bomba bomba = fabricaEntidades.crearBomba(x, y, velocidadX, velocidadY);
        bomba.setNivelCorrespondiente(nivel);

        nivel.agregarEntidad(bomba);
        nivel.agregarMovible(bomba);
        nivel.registrarNuevaEntidad(bomba);
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
        estadoEnemigo.afectarJugador(j);
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
        if (estaCongelado() || esBolaDeNieve()) {
            Rectangle cajaPlataforma = p.getRectangulo();
            Rectangle cajaKamakichi = this.cajaColision;

            
            int pieKamakichi = cajaKamakichi.y;
            int superficiePlataforma = cajaPlataforma.y + cajaPlataforma.height;
            int cabezaPlataforma = cajaPlataforma.y;

            
            boolean solapamientoHorizontal = cajaKamakichi.x + cajaKamakichi.width > cajaPlataforma.x &&
                    cajaKamakichi.x < cajaPlataforma.x + cajaPlataforma.width;

            if (!solapamientoHorizontal) {
                return;
            }

            
            int diferencia = pieKamakichi - superficiePlataforma;

            
            if (velocidadRealY <= 0 && diferencia >= -20 && pieKamakichi >= cabezaPlataforma) {
                if (diferencia <= 5) {
                    
                    posy = superficiePlataforma + 1;
                    velocidadRealY = 0;
                    enElAire = false;

                    
                    this.cajaColision.setLocation(posx, posy);
                }
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
    public boolean puedeSerAfectadoPorBolaDeNieve() {
        
        return estaCompletamenteCongelado();
    }
}
