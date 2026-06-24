package Enemigos;

/**
 * Estado normal de Kamakichi: se mueve verticalmente y dispara bombas
 */
public class EstadoKamakichiNormal extends EstadoNormal {

    private final Kamakichi kamakichi;

    public EstadoKamakichiNormal(Kamakichi kamakichi) {
        super(kamakichi);
        this.kamakichi = kamakichi;
    }

    @Override
    protected void moverEspecifico() {
        kamakichi.moverKamakichiNormal();
    }

    @Override
    public void recibirDisparo() {
        if (enemigo.getDisparosRecibidos() >= enemigo.getDisparosHastaCongelar()) {
            enemigo.setEstadoEnemigo(enemigo.crearEstadoCongelado());
        }
    }

    @Override
    public void recibirDisparoConBola(Proyectiles.BolaDeNieve bola) {
        boolean estabaTotalmenteCongelado = enemigo.estaCompletamenteCongelado();
        if (enemigo.getDisparosRecibidos() >= enemigo.getDisparosHastaCongelar()) {
            if (!estabaTotalmenteCongelado && bola.getDuenio() != null) {
                bola.getDuenio().sumarPuntos(enemigo.puntosAlCongelar);
            }
            enemigo.cambiarEstado(enemigo.crearEstadoCongelado());
            kamakichi.actualizarSpriteCongelamiento();
        }
    }

    @Override
    public void aplicarFisica() {
    }
    
    @Override
    public void actualizarSprite() {
        if (enemigo.getSprites() != null) {
            enemigo.getSprites().set_estado_actual(1); // KamakichiNormal.png
            enemigo.notificar();
        }
    }

    @Override
    public boolean puedeCambiarDireccion() {
        return false; // Kamakichi no cambia dirección horizontal, solo vertical
    }

    @Override
    public boolean afectarJugador(Logica.Jugador jugador) {
        if (!jugador.getInformacion().getInvulnerable()) {
            jugador.perderVida();
        }
        return true; 
    }

    @Override
    public boolean afectarEnemigo(Enemigo otroEnemigo) {
        return false;
    }
}
