package Logica;

import java.util.ArrayList;
import java.util.List;

import Enemigos.Calabaza;
import Enemigos.Enemigo;
import Enemigos.Fantasma;
import Grafica.Observer;
import Fabricas.CreadorEntidades;

public class Nivel implements Sujeto {

    protected int contadorEnemigos;
    protected List<Movible> movibles;
    protected List<Entidad> entidades;
    protected List<Enemigo> enemigos;
    protected List<Powerups.PowerUp> powerups;
    protected List<Entidad> superficiesSolidas;
    protected List<Obstaculos.Escalera> escaleras;
    protected int tiempoTranscurrido;
    protected List<Observer> observers;
    private Juego juego;
    private long tiempoFinDetencion;
    private CreadorEntidades fabricaEntidades;
    private boolean calabazaSpawneada = false;

    private Calabaza calabazaActual;
    private static final int FRAMES_ENTRE_FANTASMAS_MIN = 10;
    private static final int FRAMES_ENTRE_FANTASMAS_MAX = 20;
    private long proximaGeneracionFantasma = 0;

    public Nivel() {

        this.movibles = new ArrayList<>();
        this.entidades = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.powerups = new ArrayList<>();
        this.superficiesSolidas = new ArrayList<>();
        this.escaleras = new ArrayList<>();
        this.contadorEnemigos = 0;
        this.tiempoTranscurrido = 0;
        this.observers = new ArrayList<>();
    }

    public int getContadorEnemigos() {
        return contadorEnemigos;
    }

    public List<Movible> getMovibles() {
        return movibles;
    }

    public List<Entidad> getEntidades() {
        return entidades;
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public List<Powerups.PowerUp> getPowerups() {
        return powerups;
    }

    public List<Entidad> getSuperficiesSolidas() {
        return superficiesSolidas;
    }

    public List<Obstaculos.Escalera> getEscaleras() {
        return escaleras;
    }

    public synchronized void agregarEscalera(Obstaculos.Escalera escalera) {
        this.escaleras.add(escalera);
    }

    public int getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }

    public void setTiempoTranscurrido(int segundos) {
        this.tiempoTranscurrido = segundos;
    }

    public void incrementarTiempo() {
        tiempoTranscurrido++;

        if (tiempoTranscurrido > 60 && !calabazaSpawneada) {
            spawnearCalabaza();
            calabazaSpawneada = true;
        }
        if (calabazaActual != null && tiempoTranscurrido >= proximaGeneracionFantasma) {
            generarFantasmaDesdeCalabaza();
            programarProximaGeneracionFantasma(tiempoTranscurrido);
        }
        if (tiempoTranscurrido % 5 == 0 && Math.random() < 0.2) {
            generarVidaExtraAleatoria();
        }
    }

    public synchronized void agregarMovible(Movible movible) {
        this.movibles.add(movible);
    }

    public synchronized void agregarEntidad(Entidad entidad) {
        this.entidades.add(entidad);
    }

    public synchronized void agregarSuperficieSolida(Entidad entidad) {
        this.superficiesSolidas.add(entidad);
    }

    public synchronized void eliminarSuperficieSolida(Entidad entidad) {
        this.superficiesSolidas.remove(entidad);
    }

    public synchronized void agregarPowerUp(Powerups.PowerUp powerup) {
        this.entidades.add(powerup);
        this.powerups.add(powerup);
    }

    public synchronized void agregarEnemigo(Enemigo enemigo) {
        this.enemigos.add(enemigo);
        this.contadorEnemigos++;
    }

    public synchronized void eliminarEnemigo(Enemigo enemigo) {
        if (this.enemigos.remove(enemigo)) {
            this.contadorEnemigos--;
        } else {
        }
    }

    public synchronized void eliminarPowerUp(Powerups.PowerUp powerup) {
        this.entidades.remove(powerup);
        this.powerups.remove(powerup);
        ArrayList<Observer> observersGraficos = powerup.getObserversGraficos();
        for (Observer o : observersGraficos) {
            o.eliminarDePantalla();
        }
    }

    public synchronized void eliminarEntidad(Entidad entidad) {
        this.entidades.remove(entidad);

        ArrayList<Observer> observersGraficos = entidad.getObserversGraficos();
        for (Observer o : observersGraficos) {
            o.eliminarDePantalla();
        }
    }

    public synchronized void EliminarMovible(Movible movible) {
        this.movibles.remove(movible);
    }

    public void notificar() {
        for (Observer observer : observers) {
            observer.actualizar();
        }
    }

    @Override
    public void agregarObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void quitarObserver(Observer o) {
        observers.remove(o);
    }

    public void detenerEnemigos() {
        tiempoFinDetencion = System.currentTimeMillis() + 5000; 
        for (Enemigo e : enemigos) {
            e.setDetenido(true);
        }
    }

    public void reanudarEnemigos() {
        tiempoFinDetencion = 0;
        for (Enemigo e : enemigos) {
            e.setDetenido(false);
        }
    }

    public long getTiempoFinDetencion() {
        return tiempoFinDetencion;
    }

    public void setJuego(Juego j) {
        this.juego = j;
    }

    public Juego getJuego() {
        return juego;
    }

    public InfoJugador getInfoJugador() {
        if (juego != null) {
            return juego.getInfoJugador();
        }
        return null;
    }

    public void registrarNuevaEntidad(Entidad entidad) {
        if (juego != null) {
            Observer observer = juego.registrarEntidadDinamica(entidad);
            if (observer != null) {
                entidad.agregarObserver(observer);
            }
        } else {
        }
    }

    public synchronized void verificarColisiones() {

        List<Entidad> entidadesCopia = new ArrayList<>(this.entidades);
        List<Movible> moviblesCopia = new ArrayList<>(this.movibles);

        for (int i = 0; i < moviblesCopia.size(); i++) {
            for (int j = 0; j < entidadesCopia.size(); j++) {
                Movible e1 = moviblesCopia.get(i);
                Entidad e2 = entidadesCopia.get(j);
                if (e1 == e2) {
                    continue;
                }
                if (e1.getRectangulo().intersects(e2.getRectangulo())) {
                    e1.chocar(e2);
                    e2.chocar(e1);
                }
            }
        }
    }

    public void incrementarPuntajeJugador(int puntos) {

    }

    public CreadorEntidades getFabricaEntidades() {
        return fabricaEntidades;
    }

    public void setFabricaEntidades(CreadorEntidades ce) {
        this.fabricaEntidades = ce;
    }

    private void spawnearCalabaza() {
        int x = (Math.random() > 0.5) ? 200 : 920; 
        int y = 400; 
        if (fabricaEntidades != null) {
            Enemigos.Calabaza calabaza = fabricaEntidades.getCalabaza(x, y);
            if (calabaza != null) {
                calabaza.setNivelCorrespondiente(this);
                agregarEntidad(calabaza);
                agregarMovible(calabaza);
                registrarNuevaEntidad(calabaza);
                calabazaActual = calabaza;
                programarProximaGeneracionFantasma(tiempoTranscurrido);
            } else {
            }
        } else {
        }
    }

    private void generarFantasmaDesdeCalabaza() {
        if (calabazaActual == null) {
            return;
        }

        if (calabazaActual.estaAturdido()) {
            return;
        }

        if (fabricaEntidades != null) {
            int posX = calabazaActual.getPosX();
            int posY = calabazaActual.getPosY();

            Fantasma fantasma = fabricaEntidades.getFantasma(posX, posY);
            fantasma.setNivelCorrespondiente(this);

            agregarEntidad(fantasma);
            agregarMovible(fantasma);
            registrarNuevaEntidad(fantasma);
        }
    }

    private void programarProximaGeneracionFantasma(long tiempoActual) {
        long intervalo = FRAMES_ENTRE_FANTASMAS_MIN +
                (long) (Math.random() * (FRAMES_ENTRE_FANTASMAS_MAX - FRAMES_ENTRE_FANTASMAS_MIN));
        proximaGeneracionFantasma = tiempoActual + intervalo;

    }

    private void generarVidaExtraAleatoria() {
        if (fabricaEntidades != null && juego != null) {
            int posX = 200 + (int) (Math.random() * 700);
            int posY = 300;
            Powerups.PowerUp vidaExtra = fabricaEntidades.getVidaExtra(posX, posY);
            agregarPowerUp(vidaExtra);
            registrarNuevaEntidad(vidaExtra);
        }
    }

}
