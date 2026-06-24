package ModosDeJuego;

import Grafica.Observer;
import Logica.Jugador;
import Logica.Nivel;
import Logica.Juego;

public abstract class ModoDeJuego implements Observer {
    protected Nivel nivel;
    protected Ranking ranking;
    protected Jugador jugador;
    protected Juego juego;

    public ModoDeJuego(Nivel n, Jugador j) {
        this.nivel = n;
        this.jugador = j;
        if (this.nivel != null) {
            this.nivel.agregarObserver(this);
        }
        if (j != null) {
            j.agregarObserver(this);
        }
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setJuego(Juego j) {
        this.juego = j;
    }

    public void setNivel(Nivel n) {
        if (this.nivel != null) {
            this.nivel.quitarObserver(this);
        }
        this.nivel = n;
        if (this.nivel != null) {
            this.nivel.agregarObserver(this);
        }
    }

    public void actualizar() {
        verificarFinal();
    }

    public abstract void verificarFinal();

    public int getTiempoRestante() {
        return -1;
    }

    public void tick() {
    }

    public int[] obtenerInfoNivelPiso() {
        return null;
    }

    public boolean prevenirAvanceAutomatico() {
        return false;
    }

    public void procesarCambiosNivelPendientes() {
        // Por defecto no hace nada
    }

    public void nivelAvanzado(Nivel nuevoNivel, Juego juego) {
        setNivel(nuevoNivel);
    }

}
