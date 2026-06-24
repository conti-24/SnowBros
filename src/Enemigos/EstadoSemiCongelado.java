package Enemigos;

public class EstadoSemiCongelado extends EstadoEnemigo {
    public EstadoSemiCongelado(Enemigo enemigo) {
        super(enemigo);
    }

    private int framesDesdeUltimaReduccion = 0;
    private static final int FRAMES_POR_REDUCCION = 180; 

    @Override
    public void actualizar() {
        framesDesdeUltimaReduccion++;
        if (framesDesdeUltimaReduccion >= FRAMES_POR_REDUCCION) {
            framesDesdeUltimaReduccion = 0;
            enemigo.reducirCongelamiento(1);
        }
    }

    @Override
    protected void moverEspecifico() {
        enemigo.aplicarFisicaBase();
    }

    @Override
    protected boolean puedeMoverse() {
        return true;
    }

    @Override
    public void recibirDisparo() {
        enemigo.setEstadoEnemigo(enemigo.crearEstadoCongelado());
    }

    @Override
    public void aplicarFisica() {
        enemigo.aplicarFisicaBase();
    }

    @Override
    public void actualizarSprite() {
        if (enemigo.getSprites() != null) {
            enemigo.getSprites().set_estado_actual(1);
        }
        
        if (enemigo.spritesCongelamiento != null) {
            float porcentaje = (float) enemigo.disparosRecibidos / enemigo.disparosHastaCongelar;
            
            if (porcentaje < 0.34f) {
                enemigo.spritesCongelamiento.set_estado_actual(0);
            } else if (porcentaje < 0.68f) {
                enemigo.spritesCongelamiento.set_estado_actual(1);
            } else {
                enemigo.spritesCongelamiento.set_estado_actual(2);
            }
        }
        
        enemigo.notificar();
    }

    @Override
    public boolean esVulnerable() {
        return true; 
    }

    public boolean afectarJugador(Logica.Jugador jugador) {
        return true; 
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
        return false; 
    }
}
