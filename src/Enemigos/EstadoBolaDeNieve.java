package Enemigos;
import Obstaculos.ParedDestructible;
import Logica.SoundManager;


public class EstadoBolaDeNieve extends EstadoCongelado {
    
    private int frameContadorBola = 0;
    private int spriteActualBola = 0;
    
    private static final int FRAMES_POR_SPRITE_BOLA = 4;

    public EstadoBolaDeNieve(Enemigo enemigo) {
        super(enemigo);
    }
    public void inicializar(float velocidadInicial, boolean haciaLaDerecha, Logica.Jugador jugador) {
        enemigo.velocidadRealX = haciaLaDerecha ? velocidadInicial : -velocidadInicial;
        enemigo.velocidadRealY = 0;
        enemigo.moviendoDerecha = haciaLaDerecha;
        if (enemigo.spritesCongelamiento != null) {
            enemigo.spritesCongelamiento.set_estado_actual(0);
        }
    }

    @Override
    protected void moverEspecifico() {
        aplicarFisicaBola();
    }

    @Override
    public void aplicarFisica() {
        aplicarFisicaBola();
    }
    
    @Override
    public void actualizar() {
        super.actualizar();
        actualizarSprite();
    }

    @Override
    public void actualizarSprite() {
        if (enemigo.getSprites() != null) {
            enemigo.getSprites().set_estado_actual(4);
        }
        if (Math.abs(enemigo.velocidadRealX) > 0.5f) {
            frameContadorBola++;
            if (frameContadorBola >= FRAMES_POR_SPRITE_BOLA) {
                frameContadorBola = 0;
                spriteActualBola = (spriteActualBola + 1) % 2; // Solo alterna entre 0 y 1
                
                // Rotar el sprite de bola rodando (MobSnow3/MobSnow4)
                if (enemigo.getSpritesBolaRodando() != null) {
                    enemigo.getSpritesBolaRodando().set_estado_actual(spriteActualBola);
                }
                
                enemigo.notificar();
            }
        } else {
            if (enemigo.getSpritesBolaRodando() != null) {
                enemigo.getSpritesBolaRodando().set_estado_actual(0);
            }
            enemigo.notificar();
        }
    }

    @Override
    public boolean esVulnerable() {
        return false; // No vulnerable mientras es bola rodante
    }

    @Override
    public boolean afectarJugador(Logica.Jugador jugador) {
        boolean empujarDerecha = jugador.getPosX() < enemigo.getPosX();    
        enemigo.aplicarImpulso(8f, empujarDerecha);
            
        return true; 
    }

    @Override
    public boolean afectarEnemigo(Enemigo otroEnemigo) {
        if (otroEnemigo.esBolaDeNieve()) {
            float tempVelX = enemigo.velocidadRealX;
            enemigo.velocidadRealX = otroEnemigo.velocidadRealX;
            otroEnemigo.velocidadRealX = tempVelX;

            enemigo.moviendoDerecha = enemigo.velocidadRealX > 0;
            otroEnemigo.moviendoDerecha = otroEnemigo.velocidadRealX > 0;
            return true;
        }
        else if (Math.abs(enemigo.velocidadRealX) > 2f && !otroEnemigo.esJefe()) {
            otroEnemigo.morirPorImpacto();
            return true;
        }
        return false;
    }

    @Override
    public boolean esBolaDeNieve() {
        return true;
    }

    @Override
    public boolean bolaEstaQuieta() {
        return Math.abs(enemigo.velocidadRealX) < 0.5f;
    }

    @Override
    public void convertirEnBolaDeNieve(float velocidadInicial, boolean haciaLaDerecha, Logica.Jugador jugador) {
        inicializar(velocidadInicial, haciaLaDerecha, jugador);
    }

    @Override
    public void aplicarImpulso(float velocidad, boolean haciaLaDerecha) {
        enemigo.velocidadRealX = haciaLaDerecha ? velocidad : -velocidad;
        enemigo.moviendoDerecha = haciaLaDerecha;
    }

    @Override
    public void limpiarModoBola() {
        EstadoCongeladoEstatico estadoEstatico = new EstadoCongeladoEstatico(enemigo);
        enemigo.setEstadoEnemigo(estadoEstatico);
        enemigo.velocidadRealX = enemigo.moviendoDerecha ? Enemigo.VELOCIDAD_ENEMIGO : -Enemigo.VELOCIDAD_ENEMIGO;
    }

    @Override
    public Logica.Jugador getEmpujadoPor() {
        if (enemigo.getNivel() != null && enemigo.getNivel().getJuego() != null) {
            return enemigo.getNivel().getJuego().getJugador();
        }
        return null;
    }

    private void aplicarFisicaBola() {
        int posAnteriorX = enemigo.getPosX();
        int posAnteriorY = enemigo.getPosY();
        int posXAntes = enemigo.getPosX();
        
        if (enemigo.moviendoDerecha)
            enemigo.setPosX(enemigo.getPosX() + (int) enemigo.velocidadRealX + 2);
        else
            enemigo.setPosX(enemigo.getPosX() + (int) enemigo.velocidadRealX - 2);

        boolean seMovioHorizontalmente = (posXAntes != enemigo.getPosX());

        if (Math.abs(enemigo.velocidadRealX) < 0.5f) {
            enemigo.velocidadRealX = 0;
        }

        enemigo.getRectangulo().setLocation(enemigo.getPosX(), enemigo.getPosY());
        
        if (seMovioHorizontalmente && !enemigo.enElAire) {
            enemigo.verificarSoporteDebajo();
        }

        if (enemigo.enElAire) {
            enemigo.velocidadRealY += Enemigo.GRAVEDAD;
            if (enemigo.velocidadRealY < -15f) {
                enemigo.velocidadRealY = -15f;
            }
        } else {
            enemigo.velocidadRealY = 0;
        }

        enemigo.setPosY(enemigo.getPosY() + (int) enemigo.velocidadRealY);

        if (enemigo.getPosY() < Enemigo.LIMITE_PISO) {
            enemigo.setPosY(Enemigo.LIMITE_PISO);
            enemigo.velocidadRealY = 0;
            enemigo.enElAire = false;
        }

        if (enemigo.getPosY() > Enemigo.LIMITE_TECHO) {
            enemigo.setPosY(Enemigo.LIMITE_TECHO);
            enemigo.velocidadRealY = 0;
        }

        enemigo.getRectangulo().setLocation(enemigo.getPosX(), enemigo.getPosY());

        if (posAnteriorX != enemigo.getPosX() || posAnteriorY != enemigo.getPosY()) {
            enemigo.notificar();
        }
    }
    
    @Override
    protected void afectarPorParedDestructible(ParedDestructible p){
         SoundManager.getInstance().playSound("rock_break");
        if(p.getRectangulo().y>0)
        p.destruir();
        if (enemigo.yaMuerto) {
            return;
        }
        enemigo.morirPorImpacto();
    }
}
