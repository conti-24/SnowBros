package Estados;

import Logica.Jugador;

public abstract class EstadoJugador extends EstadoJugadorBase {
    protected Jugador jugador;

    public abstract void mover();

    public abstract void disparar();

    public abstract int getIdentificadorEstado();

    public abstract void aplicarFisica();

    public abstract void actualizarPosicion();

    public abstract void manejarEntrada();
}
