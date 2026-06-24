package Enemigos;


public abstract class EstadoCongelado extends EstadoEnemigo {
    
    private int framesDesdeUltimaReduccion = 0;
    private static final int FRAMES_POR_REDUCCION = 120; 

    public EstadoCongelado(Enemigo enemigo) {
        super(enemigo);
    }

    @Override
    protected boolean puedeMoverse() {
        return true;
    }

    @Override
    public void recibirDisparo() {
    }

    @Override
    public void actualizar() {
        framesDesdeUltimaReduccion++;
        if (framesDesdeUltimaReduccion >= FRAMES_POR_REDUCCION) {
            framesDesdeUltimaReduccion = 0;
            enemigo.reducirCongelamiento(1);
        }
    }

    @Override
    public boolean puedeCambiarDireccion() {
        return false; 
    }

    @Override
    public boolean esEstadoCongelado() {
        return true; 
    }

    @Override
    public boolean esEstadoCompletamenteCongelado() {
        return true; 
    }

    @Override
    public abstract void aplicarImpulso(float velocidad, boolean haciaLaDerecha);

    public abstract Logica.Jugador getEmpujadoPor();
}
