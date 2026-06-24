package Logica;

import java.util.ArrayList;
import java.util.List;

import Fabricas.CreadorEntidades;
import Fabricas.Dominio;
import Grafica.ControladorGrafica;
import Grafica.Observer;
import ModosDeJuego.ModoDeJuego;
import ModosDeJuego.ModoTipo;
import ModosDeJuego.Clasico;
import ModosDeJuego.Contrarreloj;
import ModosDeJuego.Supervivencia;
import ParserArchivo.GeneradorNiveles;

public class Juego implements ControladorJuego {

    protected GeneradorNiveles generadorNiveles;
    protected Jugador jugador;
    protected CreadorEntidades fabricaEntidades;
    protected ModoDeJuego modoDeJuego;
    protected Nivel niveles[];
    protected Nivel nivelActual;
    private ControladorGrafica controladorGrafica;
    private GameLoop gameLoop;
    private ModoTipo modoTipoSeleccionado = ModoTipo.CLASICO;
    private long ultimoIncrementoTiempo = 0;

    public Juego(ControladorGrafica controladorASetear) {
        controladorGrafica = controladorASetear;
        ultimoIncrementoTiempo = System.currentTimeMillis();
    }

    public void configurar() {
        inicializarSonidos();

        controladorGrafica.mostrarPantallaInicial();
    }
    
    private void inicializarSonidos() {
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.loadSound("jump", "/sounds/jump.wav");
        soundManager.loadSound("shoot", "/sounds/snowShot.wav");
        soundManager.loadSound("hit", "/sounds/hit.wav");
        soundManager.loadSound("enemy_hit", "/sounds/mobDie.wav");
        soundManager.loadSound("player_hit", "/sounds/Player Death.wav");
        soundManager.loadSound("level_complete", "/sounds/nextLevel.wav");
        soundManager.loadSound("game_over", "/sounds/gameOver.wav");
        soundManager.loadSound("extra_life", "/sounds/extraLife.wav");
        soundManager.loadSound("level_change", "/sounds/cambionv.wav");
        soundManager.loadSound("victory", "/sounds/winGame.wav");
        soundManager.loadSound("potion_green", "/sounds/pocionVerde.wav");
        soundManager.loadSound("rock_break", "/sounds/rock_break_converted.wav");
        soundManager.playBackgroundMusic("/sounds/mainMusic.wav");
    }

    @Override
    public void seleccionarDominio(Dominio dominioAUtilizar) {
        fabricaEntidades = new CreadorEntidades(dominioAUtilizar);
        jugador = new Jugador(dominioAUtilizar.getSpritesBros(), fabricaEntidades);
    }

    public void actualizarEstado() {
        actualizarInvulnerabilidadJugador();
        actualizarEfectosJugador();
        modoDeJuegoTick();
        actualizarTiempoDeNivel();
        verificacionReanudacionEnemigos();

        List<Movible> moviblesCopia = new ArrayList<>(nivelActual.getMovibles());
        for (Movible mov : moviblesCopia) {
            mov.mover();
        }
        nivelActual.verificarColisiones();
        actualizarTicksPowerups();
        verificarCompletarNivel();

        if (modoDeJuego != null) {
            modoDeJuego.procesarCambiosNivelPendientes();
        }

    }

    private void actualizarTicksPowerups() {
        final int FRAME_MS = 16;
        List<Powerups.PowerUp> powerupsCopia = new ArrayList<>(nivelActual.getPowerups());
        for (Powerups.PowerUp pw : powerupsCopia) {
            pw.tick(FRAME_MS);
        }
    }

    private void verificacionReanudacionEnemigos() {
        if (nivelActual.getTiempoFinDetencion() > 0
                && System.currentTimeMillis() >= nivelActual.getTiempoFinDetencion()) {
            nivelActual.reanudarEnemigos();
        }
    }

    private void actualizarTiempoDeNivel() {
        long tiempoActual = System.currentTimeMillis();
        if (tiempoActual - ultimoIncrementoTiempo >= 1000) { 
            if (nivelActual != null) {
                nivelActual.incrementarTiempo();
            }
            ultimoIncrementoTiempo = tiempoActual;
            if (modoDeJuego != null) {
                modoDeJuego.verificarFinal();
            }
            if (modoDeJuego != null && controladorGrafica != null) {
                int restante = modoDeJuego.getTiempoRestante();
                if (restante >= 0) {
                    controladorGrafica.actualizarTiempoRestante(Math.max(0, restante));
                }
            }
        }
    }

    private void modoDeJuegoTick() {
        if (modoDeJuego != null) {
            modoDeJuego.tick();
        }
    }

    private void actualizarEfectosJugador() {
        if (jugador != null && jugador.getEfectos() != null) {
            jugador.getEfectos().actualizarEfectos();
        }
    }

    private void actualizarInvulnerabilidadJugador() {
        if (jugador != null && jugador.getInformacion() != null) {
            jugador.getInformacion().actualizarInvulnerabilidad();
        }
    }

    @Override
    public void iniciar() {
        if (fabricaEntidades == null || jugador == null) {
            seleccionarDominio(new Fabricas.DominioClasico());
        }
        generadorNiveles = new GeneradorNiveles(fabricaEntidades, modoTipoSeleccionado);
        nivelActual = generadorNiveles.generarNivel(0);
        nivelActual.agregarEntidad(jugador);
        nivelActual.agregarMovible(jugador);
        jugador.setNivelCorrespondiente(nivelActual);

        generarPowerupsInicialesNivel();

        controladorGrafica.registrarInfoJugador(jugador.getInformacion());

        switch (modoTipoSeleccionado) {
            case CLASICO:
                seleccionarModoDeJuego(new Clasico(nivelActual, jugador));
                break;
            case CONTRARRELOJ:
                seleccionarModoDeJuego(new Contrarreloj(nivelActual, jugador));
                break;
            case SUPERVIVENCIA:
                seleccionarModoDeJuego(new Supervivencia(nivelActual, jugador));
                break;
        }

        if (modoDeJuego != null) {
            modoDeJuego.setJuego(this);
        }

        gameLoop = new GameLoop(this);
        gameLoop.iniciar();
        registrarObservers();
        controladorGrafica.mostrarPantallaPartida();
        nivelActual.setJuego(this);

        SoundManager.getInstance().playBackgroundMusic("/sounds/mainMusic.wav");

        if (modoDeJuego != null && controladorGrafica != null) {
            int restanteInicial = modoDeJuego.getTiempoRestante();
            if (restanteInicial >= 0) {
                controladorGrafica.actualizarTiempoRestante(Math.max(0, restanteInicial));
            } else {
                controladorGrafica.actualizarTiempoRestante(-1);
            }
        }
        if (controladorGrafica != null) {
            int[] par = obtenerNivelYPisoActual();
            controladorGrafica.actualizarNivel(par[0], par[1]);
            SoundManager.getInstance().playSound("level_change");
            controladorGrafica.mostrarOverlayNivel(par[0], par[1], 3000);
        }
    }

    @Override
    public void seleccionarModoDeJuego(ModoDeJuego modoSeleccionado) {
        modoDeJuego = modoSeleccionado;
    }

    public void moverJugador() {
        jugador.mover();
    }

    protected void registrarObservers() {
        registrarObserverJugador();
        registrarObserverEntidad(nivelActual.getEntidades());
    }

    private void registrarObserverJugador() {
        Observer obJugador = controladorGrafica.registrarEntidad(jugador);
        jugador.agregarObserver(obJugador);
        jugador.setNivelCorrespondiente(nivelActual);
    }

    private void registrarObserverEntidad(List<? extends Entidad> entidades) {
        List<Entidad> entidadesCopia = new ArrayList<>(entidades);
        for (Entidad entidad : entidadesCopia) {
            if (entidad.esObservable()) {
                Observer observer = controladorGrafica.registrarEntidad(entidad);
                entidad.setNivelCorrespondiente(nivelActual);
                entidad.agregarObserver(observer);
            }
        }
    }

    public void teclaApretada(int p) {
        switch (p) {
            case ConstantesDireccion.DERECHA:
                jugador.setMoviendoDerecha(true);
                break;

            case ConstantesDireccion.IZQUIERDA:
                jugador.setMoviendoIzquierda(true);
                break;

            case ConstantesDireccion.ARRIBA:
                jugador.setMoviendoArriba(true);
                break;

            case ConstantesDireccion.ABAJO:
                jugador.setMoviendoAbajo(true);
                break;

            case ConstantesDireccion.ESPACIO:
                jugador.disparar();
                break;
        }
    }

    public void teclaSoltada(int p) {
        switch (p) {
            case ConstantesDireccion.DERECHA:
                jugador.setMoviendoDerecha(false);
                break;

            case ConstantesDireccion.IZQUIERDA:
                jugador.setMoviendoIzquierda(false);
                break;

            case ConstantesDireccion.ARRIBA:
                jugador.setMoviendoArriba(false);
                break;

            case ConstantesDireccion.ABAJO:
                jugador.setMoviendoAbajo(false);
                break;
        }
    }

    public ModoDeJuego obtenerModoDeJuego() {
        return modoDeJuego;
    }

    @Override
    public void seleccionarModo(ModoTipo modo) {
        this.modoTipoSeleccionado = modo;
    }

    public Observer registrarEntidadDinamica(Entidad entidad) {
        Observer observer = controladorGrafica.registrarEntidad(entidad);
        entidad.setNivelCorrespondiente(nivelActual);
        return observer;
    }

    public InfoJugador getInfoJugador() {
        if (jugador != null) {
            return jugador.getInformacion();
        }
        return null;
    }

    private int[] obtenerNivelYPisoActual() {
        if (modoDeJuego != null) {
            int[] infoModo = modoDeJuego.obtenerInfoNivelPiso();
            if (infoModo != null) {
                return infoModo;
            }
        }
        if (generadorNiveles == null) {
            return new int[] { 1, 1 };
        }
        int numeroRuta = generadorNiveles.getNumeroNivelActual() - 1;
        int nivel = (numeroRuta / 3) + 1;
        int piso = (numeroRuta % 3) + 1;
        return new int[] { nivel, piso };
    }

    private void verificarCompletarNivel() {
        if (modoDeJuego != null && modoDeJuego.prevenirAvanceAutomatico()) {
            return; 
        }

        int enemigosRestantes = nivelActual.getContadorEnemigos();
        if (enemigosRestantes == 0) {
            cargarSiguienteNivel();
        }
    }

    private void cargarSiguienteNivel() {
        if (generadorNiveles.hayMasNiveles()) {
            int rutaPrev = generadorNiveles.getNumeroNivelActual() - 1;
            int tiempoPrev = 0;
            if (nivelActual != null) {
                tiempoPrev = nivelActual.getTiempoTranscurrido();
            }

            Nivel nuevoNivel = generadorNiveles.avanzarSiguienteNivel();
            int rutaNuevo = generadorNiveles.getNumeroNivelActual() - 1; 

            if (nuevoNivel != null) {
                nivelActual = nuevoNivel;
                nivelActual.setJuego(this);
                nivelActual.agregarEntidad(jugador);
                nivelActual.agregarMovible(jugador);
                jugador.setNivelCorrespondiente(nivelActual);
                jugador.respawn();
                generarPowerupsInicialesNivel();

                if (controladorGrafica != null) {
                    controladorGrafica.limpiarLienzo();
                }

                registrarObservers();
                if (modoDeJuego != null) {
                    modoDeJuego.setNivel(nuevoNivel);
                }

                gestionarTiempoTranscurrido(rutaPrev, tiempoPrev, nuevoNivel, rutaNuevo);

                if (controladorGrafica != null) {
                    int[] par = obtenerNivelYPisoActual();
                    controladorGrafica.actualizarNivel(par[0], par[1]);
                    SoundManager.getInstance().playSound("level_change");
                    controladorGrafica.mostrarOverlayNivel(par[0], par[1], 3000);
                }
            }
        } else {
            SoundManager.getInstance().playSound("victory");
            if (modoDeJuego != null && jugador != null && jugador.getInformacion() != null) {
                modoDeJuego.getRanking().actualizarTop5(jugador.getInformacion());
            }
            terminarPartida(true); 
        }
    }

    private void gestionarTiempoTranscurrido(int rutaPrev, int tiempoPrev, Nivel nuevoNivel, int rutaNuevo) {
        if (modoTipoSeleccionado == ModoTipo.CLASICO) {
            if (!(rutaPrev == 2 && rutaNuevo == 3)) {
                nuevoNivel.setTiempoTranscurrido(tiempoPrev);
            } else {
                nuevoNivel.setTiempoTranscurrido(0);
            }
        } else if (modoTipoSeleccionado == ModoTipo.CONTRARRELOJ) {
            if (rutaPrev == 2 && rutaNuevo == 3) {
                nuevoNivel.setTiempoTranscurrido(0);
            } else {
                nuevoNivel.setTiempoTranscurrido(tiempoPrev);
            }
        }
    }

    public Jugador getJugador() {
        return jugador;
    }
    public void cambiarANivel2Supervivencia() {
        if (generadorNiveles == null)
            return;

        if (generadorNiveles.hayMasNiveles()) {
            Nivel nuevoNivel = generadorNiveles.avanzarSiguienteNivel();

            if (nuevoNivel != null) {
                nivelActual = nuevoNivel;
                nivelActual.setJuego(this);

                nivelActual.agregarEntidad(jugador);
                nivelActual.agregarMovible(jugador);
                jugador.setNivelCorrespondiente(nivelActual);
                jugador.respawn();

                if (controladorGrafica != null) {
                    controladorGrafica.limpiarLienzo();
                }

                registrarObservers();

                if (modoDeJuego != null) {
                    modoDeJuego.nivelAvanzado(nuevoNivel, this);
                }

                if (controladorGrafica != null) {
                    int[] par = obtenerNivelYPisoActual();
                    controladorGrafica.actualizarNivel(par[0], par[1]);
                    SoundManager.getInstance().playSound("level_change");
                    controladorGrafica.mostrarOverlayNivel(par[0], par[1], 3000);
                }
            }
        }
    }

    public void terminarPartida(boolean esVictoria) {
        if (esVictoria) {
            SoundManager.getInstance().playSound("victory");
        } else {
            SoundManager.getInstance().playSound("game_over");
        }

        SoundManager.getInstance().stopBackgroundMusic();

        final ModosDeJuego.Ranking ranking = (modoDeJuego != null) ? modoDeJuego.getRanking() : null;
        final int puntajeFinal = (jugador != null && jugador.getInformacion() != null)
                ? jugador.getInformacion().getPuntaje()
                : 0;
        final String nombreActual = (jugador != null && jugador.getInformacion() != null)
                ? jugador.getInformacion().getNombre()
                : "Jugador";
        cortarEfectoSueloResbaladizo();

        if (gameLoop != null) {
            gameLoop.detener();
            gameLoop = null;
        }

        nivelActual = null;
        niveles = null;
        generadorNiveles = null;
        jugador = null;
        fabricaEntidades = null;
        controladorGrafica.limpiarLienzo();

        if (controladorGrafica != null) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (ranking != null) {
                    controladorGrafica.mostrarGameOverConRanking(ranking, puntajeFinal, nombreActual);
                } else {
                    controladorGrafica.mostrarPantallaGameOver();
                }
            });
        }
    }

    private void generarPowerupsInicialesNivel() {
        if (nivelActual == null || fabricaEntidades == null) {
            return;
        }
        int[] posicionesX = { 200, 400, 600, 800 };
        int posY = 300;
        int indicePos = 0;
        if (Math.random() < 0.5 && indicePos < posicionesX.length) {
            Powerups.PowerUp pocionRoja = fabricaEntidades.getPocionRoja(posicionesX[indicePos], posY);
            nivelActual.agregarPowerUp(pocionRoja);
            indicePos++;
        }
        if (Math.random() < 0.5 && indicePos < posicionesX.length) {
            Powerups.PowerUp pocionAzul = fabricaEntidades.getPocionAzul(posicionesX[indicePos], posY);
            nivelActual.agregarPowerUp(pocionAzul);
            indicePos++;
        }
        if (Math.random() < 0.5 && indicePos < posicionesX.length) {
            Powerups.PowerUp pocionVerde = fabricaEntidades.getPocionVerde(posicionesX[indicePos], posY);
            nivelActual.agregarPowerUp(pocionVerde);
            indicePos++;
        }
    }

    private void cortarEfectoSueloResbaladizo() {
        jugador.desaplicarEfectos();
    }
}
