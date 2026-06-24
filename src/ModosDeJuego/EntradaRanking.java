package ModosDeJuego;

import java.io.Serializable;

public class EntradaRanking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private int puntaje;

    public EntradaRanking(String nombre, int puntaje) {
        this.nombre = nombre;
        this.puntaje = puntaje;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    @Override
    public String toString() {
        return nombre + " - " + puntaje + " pts";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}
