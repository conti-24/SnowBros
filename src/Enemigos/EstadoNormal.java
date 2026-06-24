package Enemigos;

public class EstadoNormal extends EstadoEnemigo {

    
    private int framesSalto = 0;
    private int intervaloSalto = 120; 
    private int framesCambioDireccion = 0;
    private int intervaloCambioDireccion = 180; 
    private java.util.Random random = new java.util.Random();

    public EstadoNormal(Enemigo enemigo) {
        super(enemigo);
        this.intervaloSalto = 60 + random.nextInt(121); // 1-3 segundos
        this.intervaloCambioDireccion = 120 + random.nextInt(181); // 2-5 segundos
    }

    @Override
    protected void moverEspecifico() {
        enemigo.moverNormal();
    }

    @Override
    protected boolean puedeMoverse() {
        return true;
    }

    @Override
    public void recibirDisparo() {
        enemigo.setEstadoEnemigo(enemigo.getEstadoSemiCongelado());
    }

    @Override
    public void aplicarFisica() {
        enemigo.aplicarFisicaBase();
    }

    @Override
    public void actualizarSprite() {
        if (enemigo.getSprites() != null) {
            enemigo.getSprites().set_estado_actual(0);
            enemigo.notificar();
        }
    }
    
    @Override
    public void actualizarSpriteConAnimacion(int contadorAnimacion, boolean alternar) {
        
        if (enemigo.getSprites() != null) {
            int spriteBase = enemigo.moviendoDerecha ? 0 : 2;
            int nuevoEstado = spriteBase + (alternar ? 1 : 0);
            enemigo.getSprites().set_estado_actual(nuevoEstado);
            enemigo.notificar();
        }
    }

    @Override
    public boolean esVulnerable() {
        return true; 
    }

    @Override
    public boolean puedeCambiarDireccion() {
        return true; 
    }

    @Override
    public void actualizar() {
        framesSalto++;
        if (framesSalto >= intervaloSalto) {
            framesSalto = 0;
            intervaloSalto = 60 + random.nextInt(121); 
            enemigo.intentarSaltar();
        }
        framesCambioDireccion++;
        if (framesCambioDireccion >= intervaloCambioDireccion) {
            framesCambioDireccion = 0;
            intervaloCambioDireccion = 120 + random.nextInt(181); 
            enemigo.intentarCambiarDireccion();
        }
    }

    @Override
    public boolean afectarJugador(Logica.Jugador jugador) {
        if (!jugador.getInformacion().getInvulnerable()) {
            jugador.perderVida();
            return true; 
        }
        return true; 
    }

    @Override
    public boolean afectarEnemigo(Enemigo otroEnemigo) {
        return false;
    }
}
