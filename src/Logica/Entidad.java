package Logica;

import java.awt.Rectangle;
import java.util.ArrayList;

import Fabricas.Sprites;
import Grafica.Observer;
import Visitor.Colisionable;
import Visitor.Colisionador;

public abstract class Entidad implements Sujeto, EntidadLogica, Colisionador, Colisionable {
    protected int posx;
    protected int posy;
    protected Rectangle cajaColision;
    protected ArrayList<Observer> observers;
    protected int velocidadX;
    protected int velocidadY;
    protected Nivel nivel;
    protected Sprites sprites;
    protected int alto;
    protected int ancho;

    public ArrayList<Observer> getObserversGraficos() {
        return observers;
    }

    public int getAlto() {
        return alto;
    }

    public int getAncho() {
        return ancho;
    }

    public void setNivelCorrespondiente(Nivel n) {
        nivel = n;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setVelocidadX(int vx) {
        velocidadX = vx;
    }

    public void setVelocidadY(int vy) {
        velocidadY = vy;
    }

    public int getVelocidadX() {
        return velocidadX;
    }

    public int getVelocidadY() {
        return velocidadY;
    }

    public void setPosX(int x) {
        posx = x;
    }

    public void setPosY(int y) {
        posy = y;
    }

    public int getPosX() {
        return posx;
    }

    public int getPosY() {
        return posy;
    }

    public Rectangle getRectangulo() {
        return cajaColision;
    }

    public boolean intersecta(Rectangle r) {
        return cajaColision.intersects(r);
    }

    public void agregarObserver(Observer o) {
        observers.add(o);
    }

    public void quitarObserver(Observer o) {
        observers.remove(o);
    }

    public void notificar() {
        ArrayList<Observer> observersCopia = new ArrayList<>(observers);
        for (Observer observer : observersCopia) {
            observer.actualizar();
        }
    }
    public void tick(int dtMs) {
        // Por defecto no hace nada
    }

    public Sprites getSprites() {
        return sprites;
    }

    public boolean jugadorEstaEnEntidad(Rectangle cajaJugador) {
        return false;
    }

    public boolean esObservable() {
        return true;
    }

    public void chocar(Colisionable c) {
        // Por defecto no hace nada - las subclases deben implementarlo si necesitan colisionar
    }

}
