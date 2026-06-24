package Estados;

import Logica.Jugador;
import Logica.SoundManager;
import Proyectiles.BolaDeNieve;

public abstract class EstadoLogicoJugador {
    protected Jugador jugador;

    public EstadoLogicoJugador(Jugador j) {
        this.jugador = j;
    }

    public abstract void aplicarFisicaHorizontal();

    public abstract void aplicarFisicaVertical();

    public abstract void actualizarPosicion();

    public abstract boolean intentarSaltar();

    public abstract boolean intentarDisparar();

    public abstract boolean intentarSubirEscalera();

    public abstract boolean intentarBajar();

    public abstract void moverDerecha();

    public abstract void moverIzquierda();

    public abstract void moverArriba();

    public abstract void moverAbajo();

    public abstract void detenerMovimientoHorizontal();

    public abstract void detenerMovimientoVertical();

    public abstract boolean estaEnElAire();

    public abstract boolean estaEnEscalera();

    public abstract boolean puedeColisionarConPlataformas();

    public abstract boolean estaAtravesandoPlataforma();

    public abstract boolean estaMirandoIzquierda();

    public abstract boolean estaMoviendoDerecha();

    public abstract boolean estaMoviendoIzquierda();

    public abstract boolean estaSubiendoEscalera();

    public abstract boolean estaBajandoEscalera();

    public abstract boolean hayEscaleraEnPosicion();

    public abstract boolean haySoporteDebajo();

    public abstract void disparar();

    public abstract EstadoLogicoJugador verificarTransiciones();

    public void alEntrar() {
    }

    public void alSalir() {
    }

    public abstract void notificarAterrizaje();

    public abstract void notificarSueloResbaladizo();

    public abstract void notificarColisionTecho();

    public void lanzarBolaDeNieveDerecha(){
        int bolaPosX = jugador.getPosX()+ jugador.getAncho();
        int bolaPosY = jugador.getPosY() + (int) (jugador.getAlto() * 0.5f);

        BolaDeNieve bola = jugador.getFabricaEntidades().getBolaDeNieve(bolaPosX, bolaPosY);
        bola.setNivelCorrespondiente(jugador.getNivel());
        bola.setMoviendoDerecha(true);
        bola.setMoviendoIzquierda(false);
        bola.setPotencia(jugador.getPotenciaDisparo());
        bola.setDuenio(jugador);

        jugador.getNivel().agregarEntidad(bola);
        jugador.getNivel().agregarMovible(bola);

        SoundManager.getInstance().playSound("shoot");
        jugador.getNivel().registrarNuevaEntidad(bola);
    }
    public void lanzarBolaDeNieveIzquierda() {
        int bolaPosX = jugador.getPosX();
        int bolaPosY = jugador.getPosY() + (int) (jugador.getAlto() * 0.5f);

        BolaDeNieve bola = jugador.getFabricaEntidades().getBolaDeNieve(bolaPosX, bolaPosY);
        bola.setNivelCorrespondiente(jugador.getNivel());
        bola.setMoviendoDerecha(false);
        bola.setMoviendoIzquierda(true);
        bola.setPotencia(jugador.getPotenciaDisparo());
        bola.setDuenio(jugador);

        jugador.getNivel().agregarEntidad(bola);
        jugador.getNivel().agregarMovible(bola);

        SoundManager.getInstance().playSound("shoot");
        jugador.getNivel().registrarNuevaEntidad(bola);
    }
    public void verificarColisionPiso(int px, int py){
        if (jugador.getPosY() <= jugador.getLimitePiso() + 2) {
            jugador.setPosY(jugador.getLimitePiso() + 1);
            jugador.setVelocidadRealY(0);
            jugador.getEstadoLogico().notificarAterrizaje();
        }

        if (jugador.getPosY() >= jugador.getLimiteTecho()) {
            jugador.setPosY(jugador.getLimiteTecho() - 1);
            jugador.setVelocidadRealY(0);
            jugador.getEstadoLogico().notificarColisionTecho();
        }
    }
    public abstract void actualizarEstadoVisual();  
}
