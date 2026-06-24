package ModosDeJuego;

import Logica.Jugador;
import Logica.Nivel;
import Logica.Juego;
import Fabricas.CreadorEntidades;

public class Supervivencia extends ModoDeJuego {
    private static final int PUNTAJE_OBJETIVO = 20000;
    private static final int PUNTAJE_CAMBIO_NIVEL = 10000;

    private int oleadaActual;
    private int nivelSupervivenciaActual;
    private int dificultadActual;
    private int enemigosGeneradosEnOleada;
    private int framesDesdeUltimoSpawn;
    private boolean esperandoNuevaOleada;
    private int tiempoEsperaEntreOleadas;
    private boolean cambioDeNivelRealizado;
    private boolean pendienteCambioNivel;

    public Supervivencia(Nivel n, Jugador j) {
        super(n, j);
        this.ranking = new Ranking("supervivencia");
        this.oleadaActual = 1;
        this.nivelSupervivenciaActual = 1;
        this.dificultadActual = 1;
        this.enemigosGeneradosEnOleada = 0;
        this.framesDesdeUltimoSpawn = 0;
        this.esperandoNuevaOleada = false;
        this.tiempoEsperaEntreOleadas = 180; 
        this.cambioDeNivelRealizado = false;
        this.pendienteCambioNivel = false;
    }

    @Override
    public void verificarFinal() {
        int puntajeActual = jugador.getInformacion().getPuntaje();
        if (nivelSupervivenciaActual == 1 && puntajeActual >= PUNTAJE_CAMBIO_NIVEL && !cambioDeNivelRealizado) {
            cambioDeNivelRealizado = true;
            pendienteCambioNivel = true;
        }
        if (nivelSupervivenciaActual == 2 && puntajeActual >= PUNTAJE_OBJETIVO) {
            ranking.actualizarTop5(jugador.getInformacion());
            if (juego != null) {
                juego.terminarPartida(true);
            }
        }
        if (jugador.getInformacion().getVidas() <= 0) {
            ranking.actualizarTop5(jugador.getInformacion());
            if (juego != null) {
                juego.terminarPartida(false);
            }
        }
    }

    @Override
    public int getTiempoRestante() {
        if (nivel == null)
            return -1;
        return nivel.getTiempoTranscurrido();
    }

    @Override
    public void tick() {
        if (esperandoNuevaOleada) {
            tiempoEsperaEntreOleadas--;
            if (tiempoEsperaEntreOleadas <= 0) {
                iniciarNuevaOleada();
            }
            return;
        }
        if (nivel != null && nivel.getContadorEnemigos() == 0 && enemigosGeneradosEnOleada > 0) {
            if (enemigosGeneradosEnOleada >= calcularEnemigosPorOleada()) {
                iniciarNuevaOleada();
                return;
            }
        }
        framesDesdeUltimoSpawn++;
        if (framesDesdeUltimoSpawn >= calcularIntervaloSpawn()) {
            if (nivel.getContadorEnemigos() <= 7) {
                generarGrupoEnemigos();
                framesDesdeUltimoSpawn = 0;
                enemigosGeneradosEnOleada += 3;
            }
        }
    }

    private void iniciarNuevaOleada() {
        oleadaActual++;
        dificultadActual = Math.min(10, oleadaActual / 2 + 1);
        enemigosGeneradosEnOleada = 0;
        framesDesdeUltimoSpawn = 0;
        esperandoNuevaOleada = false;
    }

    private int calcularEnemigosPorOleada() {
        return 6 + (dificultadActual - 1) * 3; 
    }

    private int calcularIntervaloSpawn() {
        return Math.max(150, 300 - (dificultadActual - 1) * 18);
    }

    private void generarGrupoEnemigos() {
        if (juego == null || nivel == null)
            return;

        CreadorEntidades fabrica = nivel.getFabricaEntidades();
        if (fabrica == null)
            return;
        if (dificultadActual >= 3 && Math.random() < 0.05) {
            generarJefe(fabrica);
            return;
        }

        String[] tiposEnemigos;
        if (dificultadActual <= 2) {
            tiposEnemigos = new String[] { "DemonioRojo", "DemonioRojo", "RanaDeFuego" };
        } else if (dificultadActual <= 4) {
            tiposEnemigos = new String[] { "DemonioRojo", "RanaDeFuego", "RanaDeFuego" };
        } else if (dificultadActual <= 6) {
            tiposEnemigos = new String[] { "TrollAmarillo", "RanaDeFuego", "DemonioRojo" };
        } else {
            tiposEnemigos = new String[] { "TrollAmarillo", "RanaDeFuego", "TrollAmarillo", "DemonioRojo" };
        }

        int posXBase = 200 + (int) (Math.random() * 600);
        int[] alturasDisponibles = { 100, 230, 400 };

        for (int i = 0; i < 3; i++) {
            String tipo = tiposEnemigos[(int) (Math.random() * tiposEnemigos.length)];
            int offsetX = (i == 0) ? 0 : ((i == 1) ? -100 : 100);
            int posX = posXBase + offsetX;
            int posY = alturasDisponibles[(int) (Math.random() * alturasDisponibles.length)];

            posX = Math.max(100, Math.min(900, posX));

            try {
                Enemigos.Enemigo enemigo = null;
                switch (tipo) {
                    case "TrollAmarillo":
                        enemigo = fabrica.getTrollAmarillo(posX, posY);
                        break;
                    case "RanaDeFuego":
                        enemigo = fabrica.getRanaDeFuego(posX, posY);
                        break;
                    case "DemonioRojo":
                        enemigo = fabrica.getDemonioRojo(posX, posY);
                        break;
                }

                if (enemigo != null) {
                    nivel.agregarEntidad(enemigo);
                    nivel.agregarMovible(enemigo);
                    nivel.agregarEnemigo(enemigo);
                    enemigo.setNivelCorrespondiente(nivel);
                    nivel.registrarNuevaEntidad(enemigo);
                }
            } catch (Exception e) {
                System.err.println("Error creando enemigo " + tipo + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void generarJefe(CreadorEntidades fabrica) {
        try {
            Enemigos.Enemigo jefe = null;
            if (Math.random() < 0.5) {
                int posX = 512;
                int posY = 300;
                jefe = fabrica.getKamakichi(posX, posY);
            } else {
                int posX = 200 + (int) (Math.random() * 600);
                int posY = 50;
                jefe = fabrica.getMoghera(posX, posY);
            }

            if (jefe != null) {
                nivel.agregarEntidad(jefe);
                nivel.agregarMovible(jefe);
                nivel.agregarEnemigo(jefe);
                jefe.setNivelCorrespondiente(nivel);
                nivel.registrarNuevaEntidad(jefe);
            }
        } catch (Exception e) {
            System.err.println("Error creando jefe: " + e.getMessage());
        }
    }

    public void reiniciarOleadas() {
        oleadaActual = 1;
        dificultadActual = 1;
        enemigosGeneradosEnOleada = 0;
        framesDesdeUltimoSpawn = 0;
        esperandoNuevaOleada = false;
        nivelSupervivenciaActual = 2;
    }

    @Override
    public int[] obtenerInfoNivelPiso() {
        return new int[] { nivelSupervivenciaActual, oleadaActual };
    }

    @Override
    public boolean prevenirAvanceAutomatico() {
        return true;
    }

    @Override
    public void nivelAvanzado(Nivel nuevoNivel, Juego juego) {
        if (this.juego == null) {
            setJuego(juego);
        }
        setNivel(nuevoNivel);
        reiniciarOleadas();
    }

    @Override
    public void procesarCambiosNivelPendientes() {
        if (pendienteCambioNivel && juego != null) {
            juego.cambiarANivel2Supervivencia();
            pendienteCambioNivel = false;
        }
    }

}
