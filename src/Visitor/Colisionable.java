package Visitor;

import Enemigos.Enemigo;
import Logica.Jugador;
import Obstaculos.Obstaculo;
import Plataformas.Plataforma;
import Powerups.PowerUp;
import Proyectiles.BolaDeNieve;
import Proyectiles.Proyectil;

public interface Colisionable {
    public abstract void afectar(Jugador j);

    public abstract void afectar(Enemigo e);

    public abstract void afectar(Proyectil p);

    public abstract void afectar(BolaDeNieve b);

    public abstract void afectar(Plataforma p);

    public abstract void afectar(Obstaculo o);

    public abstract void afectar(PowerUp p);
}
