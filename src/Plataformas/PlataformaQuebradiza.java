package Plataformas;

import java.util.ArrayList;

import Enemigos.Enemigo;
import java.awt.Rectangle;
import Fabricas.Sprites;
import Logica.Entidad;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;
import Visitor.Colisionable;
import Logica.SoundManager;

public class PlataformaQuebradiza extends Plataforma {
    boolean activo;
    CajaSuperiorPlataformaQuebradiza cajaSuperior;

    public PlataformaQuebradiza(Sprites sprites, int posicionx, int posiciony) {
        super(sprites, posicionx, posiciony);
        ancho = 300;
        alto = 50;
        cajaColision = new Rectangle(posicionx, posiciony, ancho, alto);
        observers = new ArrayList<>();
        sprites.set_estado_actual(0);
        activo = true;
        cajaSuperior = new CajaSuperiorPlataformaQuebradiza(this, posx, posy);
    }

    public void destruirPlataforma() {
        activo = false;
        if (nivel != null) {
            nivel.eliminarEntidad(this);
            nivel.eliminarEntidad(cajaSuperior);
            nivel.eliminarSuperficieSolida(this);
            nivel.getJuego().getJugador().getInformacion().incrementarPuntaje(250);
        }
    }

    @Override
    public void chocar(Colisionable c) {
        c.afectar(this);
    }

    @Override
    public void afectar(Jugador j) {
         SoundManager.getInstance().playSound("rock_break");
        if (j.getVelocidadRealY() <= 0) {
            destruirPlataforma();
        }
    }

    public void afectarPorCajaSuperior() {
        destruirPlataforma();
    }

    @Override
    public void afectar(Enemigo e) {
        Rectangle cajaPlataforma = this.cajaColision;
        Rectangle cajaEnemigo = e.getRectangulo();
        int pieEnemigo = cajaEnemigo.y;
        int superficiePlataforma = cajaPlataforma.y + cajaPlataforma.height;
        boolean solapamientoHorizontal = cajaEnemigo.x + cajaEnemigo.width > cajaPlataforma.x &&
                cajaEnemigo.x < cajaPlataforma.x + cajaPlataforma.width;

        if (!solapamientoHorizontal) {
            return;
        }

        int diferencia = pieEnemigo - superficiePlataforma;
        if (e.velocidadRealY <= 0 && diferencia >= -20 && diferencia <= 5) {
            e.setPosY(superficiePlataforma);
            e.velocidadRealY = 0;
            e.enElAire = false;
        }
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

    public Entidad getCajaSuperior() {
        return cajaSuperior;
    }
}
