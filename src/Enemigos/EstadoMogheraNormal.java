package Enemigos;

public class EstadoMogheraNormal extends EstadoNormal {

    private final Moghera moghera;
    private int framesDesdeUltimoDisparo = 0;
    private int intervaloProximoDisparo = 0;
    
    
    private int framesDesdeUltimoSalto = 0;
    private int intervaloProximoSalto = 0;
    
    private java.util.Random random = new java.util.Random();

    public EstadoMogheraNormal(Moghera moghera) {
        super(moghera);
        this.moghera = moghera;
        this.intervaloProximoDisparo = 90 + random.nextInt(91);
        this.intervaloProximoSalto = 120 + random.nextInt(121);
    }

    @Override
    public void actualizar() {
        framesDesdeUltimoDisparo++;
        if (framesDesdeUltimoDisparo >= intervaloProximoDisparo) {
            framesDesdeUltimoDisparo = 0;
            intervaloProximoDisparo = 90 + random.nextInt(91);
            moghera.disparar();
        }
        
        // Control de salto
        framesDesdeUltimoSalto++;
        if (framesDesdeUltimoSalto >= intervaloProximoSalto) {
            framesDesdeUltimoSalto = 0;
            intervaloProximoSalto = 120 + random.nextInt(121);
            moghera.intentarSaltar();
        }
    }

    @Override
    protected void moverEspecifico() {
        moghera.moverMogheraNormal();
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
        }
    }

    @Override
    public void aplicarFisica() {
        super.aplicarFisica();
    }
    
    @Override
    public void actualizarSprite() {
        moghera.actualizarSpriteSegunMovimiento();
    }

    @Override
    public boolean puedeCambiarDireccion() {
        return true; 
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