package Enemigos;

import Obstaculos.ParedDestructible;
import Proyectiles.BolaDeNieve;
import Logica.SoundManager;

public abstract class EstadoEnemigo {
    protected Enemigo enemigo;

    public EstadoEnemigo(Enemigo enemigo) {
        this.enemigo = enemigo;
    }

    public void actualizar() {
        // No-op por defecto. 
    }

    public void mover(Enemigo enemigo) {
        if (puedeMoverse()) {
            moverEspecifico();
        }
    }

    protected abstract void moverEspecifico();

    protected abstract boolean puedeMoverse();

    public abstract void recibirDisparo();

    public abstract void aplicarFisica();

    public abstract void actualizarSprite();
    
    public void actualizarSpriteConAnimacion(int contadorAnimacion, boolean alternar) {
        actualizarSprite();
    }

    public abstract boolean esVulnerable();

    public abstract boolean puedeCambiarDireccion();

    public boolean afectarJugador(Logica.Jugador jugador) {
        return false; // Por defecto, no maneja la interacción
    }

    public boolean afectarEnemigo(Enemigo otroEnemigo) {
        return false; // Por defecto, no maneja la interacción
    }

    public void recibirDisparoConPotencia(float potencia) {
        enemigo.disparosRecibidos += (int) potencia;

        if (enemigo.disparosRecibidos >= enemigo.disparosHastaCongelar) {
            enemigo.cambiarEstado(enemigo.crearEstadoCongelado());
        } else if (enemigo.disparosRecibidos >= enemigo.disparosHastaCongelar / 2) {
            enemigo.cambiarEstado(enemigo.estadoSemiCongelado);
        }
    }

    public void recibirDisparoConBola(Proyectiles.BolaDeNieve bola) {
        boolean estabaTotalmenteCongelado = enemigo.estaCompletamenteCongelado();

        enemigo.disparosRecibidos += (int) bola.getPotencia();

        if (enemigo.disparosRecibidos >= enemigo.disparosHastaCongelar) {
            if (!estabaTotalmenteCongelado && bola.getDuenio() != null) {
                bola.getDuenio().sumarPuntos(enemigo.puntosAlCongelar);
            }
            enemigo.cambiarEstado(enemigo.crearEstadoCongelado());
        } else if (enemigo.disparosRecibidos >= enemigo.disparosHastaCongelar / 2) {
            enemigo.cambiarEstado(enemigo.estadoSemiCongelado);
        }
    }

    public void morirPorImpacto() {
        if (enemigo.yaMuerto) {
            return;
        }
        enemigo.yaMuerto = true;

        SoundManager.getInstance().playSound("enemy_hit");

        Logica.Jugador empujadoPor = getEmpujadoPor();

        if (empujadoPor != null) {
            empujadoPor.sumarPuntos(enemigo.puntosAlMorir);
        } else if (enemigo.getNivel() != null) {
            Logica.InfoJugador infoJugador = enemigo.getNivel().getInfoJugador();
            if (infoJugador != null) {
                infoJugador.incrementarPuntaje(enemigo.puntosAlMorir);
            }
        }

        if (enemigo.getNivel() != null && enemigo.powerUpAlMorir != null) {
            enemigo.powerUpAlMorir.posicionar(enemigo.getPosX(), enemigo.getPosY());
            enemigo.powerUpAlMorir.setNivelCorrespondiente(enemigo.getNivel());
            enemigo.getNivel().agregarPowerUp(enemigo.powerUpAlMorir);
            enemigo.getNivel().registrarNuevaEntidad(enemigo.powerUpAlMorir);
        }

        if (enemigo.getNivel() != null) {
            enemigo.getNivel().eliminarEntidad(enemigo);
            enemigo.getNivel().EliminarMovible(enemigo);
            enemigo.getNivel().eliminarEnemigo(enemigo);
        }
    }
    public Logica.Jugador getEmpujadoPor() {
        return null;
    }

    public void aplicarImpulso(float velocidad, boolean haciaLaDerecha) {
        // Por defecto no hace nada - solo EstadoCongelado lo sobreescribe
    }

    public void limpiarModoBola() {
        // Por defecto no hace nada - solo EstadoCongelado lo implementa
    }

    public void convertirEnBolaDeNieve(float velocidadInicial, boolean haciaLaDerecha, Logica.Jugador jugador) {
        // Por defecto no hace nada - solo EstadoCongelado lo implementa
    }

    public boolean esBolaDeNieve() {
        return false;
    }

    public boolean bolaEstaQuieta() {
        return false;
    }

    public boolean esEstadoCongelado() {
        return false;
    }

    public boolean esEstadoCompletamenteCongelado() {
        return false;
    }

    protected void afectarPorParedDestructible(ParedDestructible p){
        // Solo EstadoBolaDeNieve sobrescribe este método para destruir la pared
    }

    protected void afectarPorBolaDeNieve(BolaDeNieve b) {
        if (b.yaColisiono()) {
            return;
        }

        b.setYaColisiono(true);

        SoundManager.getInstance().playSound("hit");

        enemigo.recibirDisparoConBola(b);
        
        b.setVelocidadRealX(0);
        b.setMoviendoDerecha(false);
        b.setMoviendoIzquierda(false);
        
        enemigo.eliminarBolaDeNieve(b);
    }
}
