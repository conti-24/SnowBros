package Powerups;

import java.awt.Rectangle;
import java.util.ArrayList;

import Logica.Entidad;

public abstract class PowerUp extends Entidad {

    protected int puntaje;
    protected int tiempoDeVida;
    protected String estado;

    public PowerUp(int puntaje, int tiempoDeVida, String estado) {
        this.puntaje = puntaje;
        this.tiempoDeVida = tiempoDeVida;
        this.estado = estado;
        observers = new ArrayList<>();
        alto = 40;
        ancho = 40;
        cajaColision = new Rectangle(0, 0, ancho, alto);
    }

    public int getPuntaje() {
        return puntaje;
    }

    public int getTiempoDeVida() {
        return tiempoDeVida;
    }

    public void setTiempoDeVida(int t) {
        tiempoDeVida = t;
    }

    public void setPuntaje(int p) {
        puntaje = p;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String s) {
        this.estado = s;
    }

    public void borrarme() {
        estado = "inactivo";
        tiempoDeVida = 0;
        if (nivel != null) {
            nivel.eliminarPowerUp(this);
        }
    }

    public void posicionar(int px, int py) {
        this.posx = px;
        this.posy = py;
        if (this.cajaColision == null) {
            this.cajaColision = new Rectangle(px, py, ancho, alto);
        } else {
            this.cajaColision.setBounds(px, py, ancho, alto);
        }
    }

    @Override
    public void tick(int dtMs) {
        // Si tiempoDeVida es positivo, decrementar y eliminar cuando expire
        if (this.tiempoDeVida > 0) {
            this.tiempoDeVida -= dtMs;
            if (this.tiempoDeVida <= 0) {
                borrarme();
            }
        }
    }

    @Override
    public void chocar(Visitor.Colisionable c) {
        c.afectar(this);
    }

}
