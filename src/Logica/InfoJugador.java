package Logica;

import java.util.ArrayList;

import Grafica.Observer;

public class InfoJugador implements Sujeto {
    protected int puntaje;
    protected int vidas;
    protected String nombre;
    protected boolean invulnerable;
    private int contadorInvulnerabilidad = 0;
    private static final int DURACION_INVULNERABILIDAD_FRAMES = 120; // 2s a 60fps
    protected ArrayList<Observer> observers;

    public InfoJugador() {
        puntaje = 0;
        vidas = 3;
        nombre = "Jugador";
        observers = new ArrayList<>();
        invulnerable = false;
    }

    public InfoJugador(String nombre) {
        this();
        this.nombre = nombre;
    }

    public void ajustarVidas(int n) {
        vidas = vidas + n;
        notificar();
    }

    public int getVidas() {
        return vidas;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public boolean getInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean i) {
        invulnerable = i;
        if (i)
            contadorInvulnerabilidad = DURACION_INVULNERABILIDAD_FRAMES;
    }

    public void actualizarInvulnerabilidad() {
        if (invulnerable) {
            if (--contadorInvulnerabilidad <= 0) {
                invulnerable = false;
                contadorInvulnerabilidad = 0;
                notificar();
            }
        }
    }

    public boolean debeSerVisible() {
        if (!invulnerable) {
            return true; 
        }
        if (contadorInvulnerabilidad > 60) {
            return ((contadorInvulnerabilidad / 6) % 2) == 0;
        }
        return true;
    }

    public void incrementarPuntaje(int n) {
        puntaje = puntaje + n;
        notificar();
    }

    @Override
    public void agregarObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void quitarObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notificar() {
        for (Observer observer : observers) {
            observer.actualizar();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
