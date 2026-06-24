package Enemigos;

import java.awt.Rectangle;
import java.util.ArrayList;

import Fabricas.CreadorEntidades;
import Fabricas.Sprites;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.Proyectil;
import Visitor.Colisionable;

public class Moghera extends Enemigo {

    public Moghera(Sprites spr, int px, int py, CreadorEntidades ce, PowerUp p) {
        super();
        this.sprites = spr;
        ancho = 77 * 3;
        alto = 80 * 3;
        posx = px;
        posy = py;
        cajaColision = new Rectangle(px, py, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
        fabricaEntidades = ce;
        powerUpAlMorir = p;
        disparosHastaCongelar = 20;
        puntosAlCongelar = 1000;
        puntosAlMorir = 5000;
        esJefe = true;
        estadoNormal = new EstadoMogheraNormal(this);
        estadoEnemigo = estadoNormal;
    }

    @Override
    protected EstadoCongelado crearEstadoCongelado() {
        return new EstadoMogheraCongelado(this);
    }

    @Override
    public void mover() {
        if (detenido && !esBolaDeNieve()) {   
            return;
        }
        estadoEnemigo.actualizar();
        estadoEnemigo.mover(this);
    }

    @Override
    protected void moverNormal() {
        aplicarFisicaBase();
    }

    protected void moverMogheraNormal() {
        aplicarFisicaBase();
        actualizarSpriteSegunMovimiento();
    }

    protected void actualizarSpriteSegunEstado() {
        estadoEnemigo.actualizarSprite();
    }

    public void actualizarSpriteSegunMovimiento() {
        if (estaCongelado()) {
            estadoEnemigo.actualizarSprite();
            return;
        }

        if (enElAire) {
            // Saltando - sprites 3 (derecha) o 4 (izquierda)
            sprites.set_estado_actual(moviendoDerecha ? 3 : 4);
        } else {
            // En el suelo - sprites 0 (derecha) o 1 (izquierda)
            sprites.set_estado_actual(moviendoDerecha ? 0 : 1);
        }
        notificar();
    }

    @Override
    public void cambiarDireccion() {
        super.cambiarDireccion();
        actualizarSpriteSegunMovimiento();
        notificar();
    }

    @Override
    public synchronized void intentarSaltar() {
        super.intentarSaltar();
        if (enElAire) {
            actualizarSpriteSegunMovimiento();
            notificar();
        }
    }

    public void actualizarSpriteCongelamiento() {
        if (spritesCongelamiento != null) {
            if (estaCompletamenteCongelado()) {
                if (esBolaDeNieve()) {
                    int frame = (int) (Math.abs(posx) / 20) % 2;
                    spritesCongelamiento.set_estado_actual(frame);
                } else {
                    double porcentajeCongelamiento = (double) disparosRecibidos / disparosHastaCongelar;
                    if (porcentajeCongelamiento > 0.7f) {
                        spritesCongelamiento.set_estado_actual(2); // MobSnow3
                    } else {
                        if (porcentajeCongelamiento > 0.4f) {
                            spritesCongelamiento.set_estado_actual(1); // MobSnow4
                        } else {
                            spritesCongelamiento.set_estado_actual(0); // MobSnow2
                        }
                    }
                }
            }
            notificar();
        }
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
            super.convertirEnBolaDeNieve(velocidadInicial, haciaLaDerecha, jugador);
        }
    }

    @Override
    public void disparar() {
        if (nivel != null && fabricaEntidades != null) {
            Jugador jugador = fabricaEntidades.getJugador();
            if (jugador != null) {
                int jugadorX = jugador.getPosX();

                boolean haciaDerecha = jugadorX > posx;

                int proyectilXBase = posx + ancho / 2;

                int proyectilYPies = posy + (alto * 3 / 4); // 75% de la altura (cerca de los pies)
                int numeroAleatorio = (int) (Math.random() * 2); // disparos impredecibles
                crearYAgregarProyectil(proyectilXBase, proyectilYPies,
                        numeroAleatorio == 0 ? haciaDerecha : !haciaDerecha);

                int proyectilYPecho = posy + alto / 2; // 50% de la altura (centro/pecho)
                crearYAgregarProyectil(proyectilXBase, proyectilYPecho,
                        numeroAleatorio == 0 ? haciaDerecha : !haciaDerecha);

                int proyectilYCabeza = posy + alto / 4; // 25% de la altura (parte superior)
                crearYAgregarProyectil(proyectilXBase, proyectilYCabeza,
                        numeroAleatorio == 0 ? haciaDerecha : !haciaDerecha);
            }
        }
    }
    private void crearYAgregarProyectil(int x, int y, boolean direccionDerecha) {
        Proyectiles.ProyectilMoghera proyectilMoghera = fabricaEntidades.crearProyectilMoghera(
                x,
                y,
                direccionDerecha);

        proyectilMoghera.setNivelCorrespondiente(nivel);
        nivel.agregarEntidad(proyectilMoghera);
        nivel.agregarMovible(proyectilMoghera);
        nivel.registrarNuevaEntidad(proyectilMoghera);
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
        if (!estaCongelado() && !esBolaDeNieve()) {
            return; 
        }
        Rectangle cajaPlataforma = p.getRectangulo();
        Rectangle cajaMoghera = this.cajaColision;

        int pieMoghera = cajaMoghera.y;
        int superficiePlataforma = cajaPlataforma.y + cajaPlataforma.height;
        int cabezaPlataforma = cajaPlataforma.y;

        boolean solapamientoHorizontal = cajaMoghera.x + cajaMoghera.width > cajaPlataforma.x &&
                cajaMoghera.x < cajaPlataforma.x + cajaPlataforma.width;

        if (!solapamientoHorizontal) {
            return;
        }

        int diferencia = pieMoghera - superficiePlataforma;

        if (velocidadRealY <= 0 && diferencia >= -20 && pieMoghera >= cabezaPlataforma) {
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
        // No interactúa
    }

    @Override
    public void morirPorImpacto() {
        sprites.set_estado_actual(2); // mogheraMuere.png
        notificar();
        super.morirPorImpacto();
    }

    @Override
    public synchronized void reducirCongelamiento(int cantidad) {
        disparosRecibidos -= cantidad;
        if (disparosRecibidos < 0) {
            disparosRecibidos = 0;
        }
        if (disparosRecibidos == 0) {
            if (estadoEnemigo != estadoNormal) {
                cambiarEstado(estadoNormal);
                limpiarEfectosCongelamiento(); 
            }
        }
    }
}
