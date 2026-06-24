package Logica;

import Fabricas.Sprites;
import Grafica.Observer;
import Grafica.ObserverGrafico;

public interface EntidadLogica {
    public int getPosX();

    public int getPosY();

    public Sprites getSprites();

    public void agregarObserver(Observer o);

    public int getAlto();

    public int getAncho();

    public default ObserverGrafico crearObserverGrafico() {
        return new ObserverGrafico(this);
    }
}
