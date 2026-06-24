package Logica;

public class GameLoop implements Runnable {

    // Constantes para el control de tiempo del juego
    private static final double FPS = 60.0; 
    private static final double TIEMPO_POR_FRAME = 1000000000.0 / FPS; // Ojo, son 9 ceros para nanosegundos

    private final Juego juego;

    private volatile boolean enPartida = false;

    public GameLoop(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void run() {
        long tiempoAnterior = System.nanoTime();
        double delta = 0;

        while (enPartida) {
            long tiempoActual = System.nanoTime();
            delta += (tiempoActual - tiempoAnterior) / TIEMPO_POR_FRAME;
            tiempoAnterior = tiempoActual;

            
            if (delta >= 1) { 
                juego.actualizarEstado(); 
                delta--;
            }
        }
    }

    public void iniciar() {
        this.enPartida = true;
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public void detener() {
        this.enPartida = false;
    }
}
