package Enemigos;

public class EstadoCongeladoEstatico extends EstadoCongelado {
    
    public EstadoCongeladoEstatico(Enemigo enemigo) {
        super(enemigo);
    }

    @Override
    protected void moverEspecifico() {
        enemigo.aplicarFisicaBase();
    }

    @Override
    public void aplicarFisica() {
        enemigo.aplicarFisicaBase();
    }

    @Override
    public void actualizarSprite() {
        if (enemigo.getSprites() != null) {
            enemigo.getSprites().set_estado_actual(4);
        }
        
        if (enemigo.spritesCongelamiento != null) {
            enemigo.spritesCongelamiento.set_estado_actual(3);
        }
        
        enemigo.notificar();
    }

    @Override
    public boolean esVulnerable() {
        return true; 
    }

    @Override
    public boolean afectarJugador(Logica.Jugador jugador) {
        boolean empujarDerecha = jugador.getPosX() < enemigo.getPosX();
        convertirEnBolaDeNieve(Enemigo.VELOCIDAD_BOLA, empujarDerecha, jugador);
        return true;
    }

    @Override
    public boolean afectarEnemigo(Enemigo otroEnemigo) {
        if (otroEnemigo.esBolaDeNieve() && Math.abs(otroEnemigo.velocidadRealX) > 2f) {
            enemigo.morirPorImpacto();
            return true;
        }
        return false;
    }

    @Override
    public boolean esBolaDeNieve() {
        return false;
    }

    @Override
    public boolean bolaEstaQuieta() {
        return false;
    }

    @Override
    public void convertirEnBolaDeNieve(float velocidadInicial, boolean haciaLaDerecha, Logica.Jugador jugador) {
        if (enemigo.estaCompletamenteCongelado()) {
            EstadoBolaDeNieve estadoBola = new EstadoBolaDeNieve(enemigo);
            estadoBola.inicializar(velocidadInicial, haciaLaDerecha, jugador);
            enemigo.setEstadoEnemigo(estadoBola);
            enemigo.notificar();
        }
    }

    @Override
    public void aplicarImpulso(float velocidad, boolean haciaLaDerecha) {
        // No hace nada en estado estático
    }

    @Override
    public void limpiarModoBola() {
        // No tiene modo bola que limpiar
    }

    @Override
    public Logica.Jugador getEmpujadoPor() {
        return null;
    }
}
