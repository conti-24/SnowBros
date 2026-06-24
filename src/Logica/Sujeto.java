package Logica;

import Grafica.Observer;

public interface Sujeto {
    public void agregarObserver(Observer o);
    public void quitarObserver(Observer o);
    public void notificar();
}
